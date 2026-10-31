package com.service;

import com.dto.requestdto.DateReqDTO;
import com.dto.responsdto.DataResDTO;
import com.entity.AppUser;
import com.entity.DateRequest;
import com.exception.DateRequestException;
import com.repository.DateRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DateRequestService {
    @Autowired
    private final DateRequestRepository dateRequestRepository;


    public DateRequestService(DateRequestRepository dateRequestRepository){
        this.dateRequestRepository=dateRequestRepository;
    }

    public DataResDTO saveData(DateReqDTO dateReqDTO, AppUser appUser){
        DateRequest dateRequest=new DateRequest();
        dateRequest.setName(dateReqDTO.getName());
        dateRequest.setAnswer(dateReqDTO.getAnswer());
        dateRequest.setMobile(dateReqDTO.getMobile());
        if(dateReqDTO.getDate().isBefore(LocalDate.now())){
            throw new DateRequestException("Date Cannot In Past");
        }
        dateRequest.setDate(dateReqDTO.getDate());
        dateRequest.setTime(dateReqDTO.getTime());
        dateRequest.setMessage(dateReqDTO.getMessage());
        dateRequest.setEmail(appUser.getEmail());
        dateRequest.setAppUser(appUser);

        DateRequest saveval =dateRequestRepository.save(dateRequest);
        return new DataResDTO(saveval.getName(),saveval.getId(),saveval.getDate(),saveval.getTime(),saveval.getMessage(),"");
    }

}
