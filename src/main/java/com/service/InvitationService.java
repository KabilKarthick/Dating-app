package com.service;

import com.entity.AppUser;
import com.entity.Invitation;
import com.repository.InvitationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public Invitation createInvitation(AppUser user, String invitedEmail) {
        Invitation invitation = new Invitation();
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setUser(user);
        invitation.setInvitedEmail(invitedEmail);
        invitation.setAccepted(false);
        invitation.setCreatedAt(LocalDateTime.now());
        return invitationRepository.save(invitation);
    }

    public Invitation getInvitationByToken(String token) {
        return invitationRepository.findByToken(token).orElse(null);
    }
    public Invitation acceptInvitation(String token) {
        Invitation invitation = invitationRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invitation not found"));
        invitation.setAccepted(true);
        return invitationRepository.save(invitation);
    }
}