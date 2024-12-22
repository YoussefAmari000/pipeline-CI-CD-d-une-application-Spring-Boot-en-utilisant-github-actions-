package com.immobiliere.controller;


import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.immobiliere.model.User;
import com.immobiliere.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;


@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

   

    @GetMapping("/login")
    public String login() {
        return "login";
    }

   
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            User user = userOptional.get();
            session.setAttribute("user", user);
            if ("OWNER".equals(user.getRole())) {
                return "redirect:/owner/houses";
            } else {
                return "redirect:/user/profile";
            }
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }


    
}
