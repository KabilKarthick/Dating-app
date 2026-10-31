package com.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "INVITATION")
@Data
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invitation_seq")
    @SequenceGenerator(
            name = "invitation_seq",
            sequenceName = "INVITATION_SEQ",
            allocationSize = 1
    )
    private Integer id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private AppUser user;

    @Column(name = "INVITED_EMAIL", nullable = false)
    private String invitedEmail;

    @Column(nullable = false)
    private boolean accepted = false;

    private LocalDateTime createdAt;
}