package com.dto.responsdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aspectj.bridge.Message;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class DataResDTO {
    private String name;
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private String message;
    private String status;
}
