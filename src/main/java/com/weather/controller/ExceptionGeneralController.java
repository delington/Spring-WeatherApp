package com.weather.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
public class ExceptionGeneralController {
    
    @ExceptionHandler
    public String exception(Exception ex, Model model) {
        model.addAttribute("exception", ex);
        
        log.error(ex.toString(), ex);
        return "error";
    }
    
    @ExceptionHandler(HttpClientErrorException.class)
    public String notFoundExceptionHandle(Exception ex, Model model) {
        model.addAttribute("exception", ex);
        
        log.error(ex.toString());
        return "notFound";
    }
    
}
