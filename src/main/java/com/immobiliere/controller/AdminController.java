package com.immobiliere.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.immobiliere.model.House;
import com.immobiliere.model.User;
import com.immobiliere.repository.HouseRepository;
import com.immobiliere.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HouseRepository houseRepository;
    
    @GetMapping("/profile")
    public String adminProfile(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("user");
        if (admin == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", admin);
        return "admin-profile";
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin-users";
    }

    @GetMapping("/edit-user/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).orElse(null));
        return "edit-user";
    }

    @PostMapping("/update-user/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            userRepository.save(existingUser);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        // Supprimer les maisons associées à cet utilisateur
        List<House> houses = houseRepository.findAllByOwner_Id(id);
        for (House house : houses) {
            houseRepository.delete(house);
        }

        // Supprimer l'utilisateur
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
