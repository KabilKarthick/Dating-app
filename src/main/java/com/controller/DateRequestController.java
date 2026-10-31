package com.controller;

import com.dto.requestdto.AppUserReqDTO;
import com.dto.requestdto.DateReqDTO;
import com.dto.requestdto.LoginReqDTO;
import com.dto.responsdto.DataResDTO;
import com.dto.responsdto.UserResDTO;
import com.entity.AppUser;
import com.entity.Invitation;
import com.exception.AuthException;
import com.service.AppUserService;
import com.service.DateRequestService;
import com.service.EmailService;
import com.service.InvitationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class DateRequestController {

    private final DateRequestService dateRequestService;
    private final AppUserService appUserService;
    private final InvitationService invitationService;
    private final EmailService emailService;

    public DateRequestController(DateRequestService dateRequestService, AppUserService appUserService, InvitationService invitationService, EmailService emailService) {
        this.dateRequestService = dateRequestService;
        this.appUserService = appUserService;
        this.invitationService = invitationService;
        this.emailService = emailService;
    }

    // =====================================================
    // 1. REGISTER USER
    // =====================================================

    @PostMapping("/save/user")
    @ResponseBody
    public AppUserReqDTO saveUser(@Valid @RequestBody AppUserReqDTO appUserReqDTO) {
        return appUserService.saveUser(appUserReqDTO);
    }


    // =====================================================
    // 1.1 LOGIN USER
    // =====================================================

    @PostMapping("/login")
    @ResponseBody
    public UserResDTO loginUser(@Valid @RequestBody LoginReqDTO loginReqDTO, HttpSession session) {
        AppUser user = appUserService.loginUser(loginReqDTO);
        session.setAttribute("email", user.getEmail());
        return new UserResDTO(user.getName(), user.getEmail());
    }


    // =====================================================
    // 2. SAVE DATE SCHEDULE
    // =====================================================

    @PostMapping("/save/schedule")
    @ResponseBody
    public DataResDTO saveSchedule(@Valid @RequestBody DateReqDTO dateReqDTO, HttpSession session) {

        String myEmail = (String) session.getAttribute("email");
        if (myEmail == null) {
            throw new AuthException("Please login first");
        }

        AppUser me = appUserService.getUserByEmail(myEmail);

        DataResDTO result = dateRequestService.saveData(dateReqDTO, me);

        if (me.getPartnerId() == null) {
            result.setStatus("Date saved. No partner is linked to your account yet.");
            return result;
        }

        AppUser partner = appUserService.getUserById(me.getPartnerId());

        try {
            emailService.sendDateConfirmationEmail(partner.getEmail(), me.getName(), dateReqDTO.getDate(), dateReqDTO.getTime(), dateReqDTO.getMessage() );
            result.setStatus("Date saved ❤️ Confirmation sent to " + partner.getEmail());
        } catch (Exception e) {
            result.setStatus("Date saved, but the confirmation email could not be sent ❌");
        }

        return result;
    }


    // =====================================================
    // 3. CREATE INVITATION
    // =====================================================

    @PostMapping("/invitation")
    @ResponseBody
    public Map<String, String> createInvitation(@RequestParam String invitedEmail, HttpSession session) {
        String myEmail = (String) session.getAttribute("email");
        if (myEmail == null) {
            throw new AuthException("Please login first");
        }

        AppUser inviter = appUserService.getUserByEmail(myEmail);

        if (inviter.getEmail().equalsIgnoreCase(invitedEmail)) {
            throw new AuthException("You cannot invite your own email");
        }

        Invitation invitation = invitationService.createInvitation(inviter, invitedEmail);

        String invitationLink = "http://localhost:8080/invite/" + invitation.getToken();

        try {
            emailService.sendInvitationEmail(invitedEmail, invitationLink);
        } catch (Exception e) {
            return Map.of(
                    "message", "Invitation created but email could not be sent. Share this link: " + invitationLink,
                    "link", invitationLink
            );
        }

        return Map.of(
                "message", "Invitation sent to " + invitedEmail,
                "link", invitationLink
        );
    }


    // =====================================================
    // 4. USER B CLICKS EMAIL LINK
    // =====================================================

    @GetMapping("/invite/{token}")
    public String acceptInvitation(@PathVariable String token, HttpSession session) {
        // Find invitation using token
        Invitation invitation = invitationService.getInvitationByToken(token);
        // Invalid token
        if (invitation == null) {
            return "redirect:/index.html?error=invalid-invitation";
        }

        // Get User B email from invitation
        String partnerEmail = invitation.getInvitedEmail();

        // Find User B
        AppUser partner = appUserService.findUserByEmail(partnerEmail).orElse(null);

        // User B not registered yet -> send them to register first
        if (partner == null) {
            return "redirect:/register.html?error=please-register";
        }

        // Get User A
        AppUser inviter = invitation.getUser();

        // Connect User A and User B
        appUserService.connectUsers(inviter.getId(), partner.getId());
        // Change invitation status to ACCEPTED
        invitationService.acceptInvitation(token);
        // Auto login the invited partner (the link proves email ownership)
        session.setAttribute("email", partner.getEmail());
        // Automatically open scheduler
        return "redirect:/schedule?token=" + token;
    }


    // =====================================================
    // 5. OPEN SCHEDULER PAGE
    // =====================================================

    @GetMapping("/schedule")
    public String openSchedule(@RequestParam String token) {
        // Verify invitation
        Invitation invitation = invitationService.getInvitationByToken(token);
        if (invitation == null) {
            return "redirect:/index.html?error=invalid-invitation";
        }
        // Check invitation is accepted
        // Adjust this condition according to your Invitation entity.
        if (!invitation.isAccepted()) {
            return "redirect:/index.html?error=invitation-not-accepted";
        }
        // Open scheduler page
        return "redirect:/Index.html";
    }
}