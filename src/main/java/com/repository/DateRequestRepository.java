package com.repository;

import com.entity.DateRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DateRequestRepository extends JpaRepository<DateRequest,Integer> {
    List<DateRequest> findByNotificationFalseAndDateLessThanEqual(LocalDate date);
}
