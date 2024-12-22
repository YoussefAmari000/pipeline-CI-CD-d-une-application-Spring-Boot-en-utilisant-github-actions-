package com.immobiliere.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.immobiliere.model.Comment;
import com.immobiliere.model.House;
import com.immobiliere.model.Message;
import com.immobiliere.model.User;
import com.immobiliere.repository.CommentRepository;
import com.immobiliere.repository.HouseRepository;
import com.immobiliere.repository.MessageRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/add")
    public String showAddHouseForm(Model model) {
        model.addAttribute("house", new House());
        return "add-house";
    }

    @PostMapping("/add")
    public String addHouse(@ModelAttribute House house, HttpSession session) {
        User owner = (User) session.getAttribute("user");
        if (owner == null) {
            return "redirect:/login";
        }
        
        house.setOwner(owner);
        house.setAvailable(true);
        houseRepository.save(house);
        return "redirect:/house/list";
    }

    @GetMapping("/details")
    public String houseDetails(@RequestParam("houseId") Long houseId, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        Optional<House> house = houseRepository.findById(houseId);
        if (house.isPresent()) {
            model.addAttribute("house", house.get());
            model.addAttribute("comments", commentRepository.findByHouseId(houseId));
            return "houseDetails";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/list")
    public String listHouses(Model model) {
        model.addAttribute("houses", houseRepository.findByIsAvailableTrue());
        return "house-list";
    }

    @PostMapping("/{id}/comment")
    public String leaveComment(@PathVariable Long id, @RequestParam String comment, @RequestParam int rating, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        House house = houseRepository.findById(id).orElse(null);
        if (house != null) {
            Comment newComment = new Comment(comment, rating, house, user);
            commentRepository.save(newComment);
        }
        return "redirect:/house/details?houseId=" + id;
    }

    @PostMapping("/{id}/contact")
    public String contactOwner(@PathVariable Long id, @RequestParam String message, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        House house = houseRepository.findById(id).orElse(null);
        if (house != null) {
            Message newMessage = new Message(message, house, user, house.getOwner());
            messageRepository.save(newMessage);
        }
        return "redirect:/house/details?houseId=" + id;
    }
    
    
}
