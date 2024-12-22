package com.immobiliere.controller;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/custom-error")
    public String handleError(WebRequest webRequest, Model model) {
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        model.addAttribute("timestamp", errorAttributes.get("timestamp"));
        model.addAttribute("error", errorAttributes.get("error"));
        model.addAttribute("message", errorAttributes.get("message"));
        model.addAttribute("path", errorAttributes.get("path"));
        model.addAttribute("status", errorAttributes.get("status"));
        return "error";
    }
}
