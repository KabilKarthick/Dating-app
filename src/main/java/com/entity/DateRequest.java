package com.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="Data_Request")
@Data
public class DateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "date_request_seq")
    @SequenceGenerator(name = "date_request_seq" ,sequenceName ="Data_Request_SEQ",allocationSize = 1)
    private Integer id ;
    @Column(name ="Name")
    private String name;
    @Column(name ="Answer")
    private String answer;
    @Column(name ="Date_value")
    private LocalDate date;
    @Column(name ="Time_Value")
    private LocalTime time;
    @Column(name ="Message")
    private String message;
    @Column(name = "Mobile_Num")
    private String mobile;
    @Column(name="Email")
    private String email;
    @Column(name="Notification")
    private boolean notification=false;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private AppUser appUser;

}
