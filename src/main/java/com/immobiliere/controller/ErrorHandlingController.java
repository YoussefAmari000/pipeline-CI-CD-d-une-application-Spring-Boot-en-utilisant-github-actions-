package com.immobiliere.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleException(Exception e){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setViewName("error");  // assuming you have an error.html
        return modelAndView;
    }
}
