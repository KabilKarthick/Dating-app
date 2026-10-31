package com.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "APP_Users")
public class AppUser {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appUser_seq"
    )
    @SequenceGenerator(
            allocationSize = 1,
            sequenceName = "APPUSER_SEQ",
            name = "appUser_seq"
    )
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Partner_Id")
    private Integer partnerId;
}