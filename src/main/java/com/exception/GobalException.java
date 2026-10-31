package com.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GobalException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex){
        Map<String,String>errors=new HashMap<>();
        //----------------it give the validation one by one-----------------------------------------
//        ex.getBindingResult().getAllErrors().forEach(error -> errors.put(error.getCode(), error.getDefaultMessage()));
//        return  ResponseEntity.badRequest().body(errors);

//--------------------------------------------------------------------------------------------------
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, String>> handleAuth(AuthException ex){
        Map<String,String>error=new HashMap<>();
        error.put("error",ex.getMessage());
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(DateRequestException.class)
    public ResponseEntity<Map<String,String>>handelDateRequest(DateRequestException dateRequestException){
        Map<String,String>error=new HashMap<>();
        error.put("error",dateRequestException.getMessage());
        return  ResponseEntity.badRequest().body(error);
    }



}
