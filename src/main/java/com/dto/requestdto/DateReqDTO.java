package com.dto.requestdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class DateReqDTO {
    @NotBlank(message = "Enter your Name")
    private String name;
    private String answer;
    @NotBlank(message ="Enter your mobile number")
    private String mobile;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Enter your date")
    private LocalDate date;
    @NotNull(message = "Enter your time")
    private LocalTime time;
    @NotBlank(message = "Enter your your message")
    private String message;

}
