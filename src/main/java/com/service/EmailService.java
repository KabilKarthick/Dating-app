package com.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendInvitationEmail(String toEmail, String invitationLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Date Invitation ❤️");

        message.setText(
                "Hello,\n\n" +
                        "You have received a date invitation.\n\n" +
                        "Click the link below to accept the invitation:\n" +
                        invitationLink +
                        "\n\n" +
                        "Thank you."
        );

        mailSender.send(message);
    }

    public void sendDateConfirmationEmail(String toEmail,
                                          String senderName,
                                          LocalDate date,
                                          LocalTime time,
                                          String dateMessage) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Date Scheduled ❤️ " + senderName + " replied");

        message.setText(
                "Hello,\n\n" +
                        senderName + " has scheduled a date with you.\n\n" +
                        "Name    : " + senderName + "\n" +
                        "Date    : " + date.format(DATE_FORMAT) + "\n" +
                        "Time    : " + time + "\n" +
                        "Message : " + (dateMessage == null || dateMessage.isBlank() ? "-" : dateMessage) + "\n\n" +
                        "Open the app: http://localhost:8080/Index.html\n\n" +
                        "Thank you."
        );

        mailSender.send(message);
    }
}
