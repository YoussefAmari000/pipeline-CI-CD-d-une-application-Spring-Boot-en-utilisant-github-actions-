package com.immobiliere.controller;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.immobiliere.model.House;
import com.immobiliere.model.Message;
import com.immobiliere.model.User;
import com.immobiliere.repository.HouseRepository;
import com.immobiliere.repository.MessageRepository;
import com.immobiliere.repository.UserRepository;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HouseRepository houseRepository;
    
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model, HttpSession session) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Username already exists. Please choose a different one.");
            return "register";
        }
        
        userRepository.save(user);
        session.setAttribute("user", user);
        
        switch (user.getRole()) {
            case "OWNER":
                return "redirect:/owner/houses";
            case "ADMIN":
                return "redirect:/admin/profile";
            default:
                return "redirect:/user/profile";
        }
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        logger.info("Fetching houses for user: {} (ID: {})", user.getUsername(), user.getId());
        List<House> houses = houseRepository.findAllByOwner(user);
        logger.info("Houses found: {}", houses.size());

        logger.info("Fetching messages received by user: {} (ID: {})", user.getUsername(), user.getId());
        List<Message> receivedMessages = messageRepository.findByRecipientId(user.getId());
        logger.info("Messages found: {}", receivedMessages.size());

        logger.info("Fetching messages sent by user: {} (ID: {})", user.getUsername(), user.getId());
        List<Message> sentMessages = messageRepository.findBySenderId(user.getId());
        logger.info("Messages found: {}", sentMessages.size());

        for (Message message : receivedMessages) {
            logger.info("Message from {}: {}", message.getSender().getUsername(), message.getContent());
            logger.info("House ID: {}", message.getHouse().getId());
            logger.info("Recipient ID: {}", message.getRecipient().getId());
            logger.info("Sender ID: {}", message.getSender().getId());
        }

        model.addAttribute("user", user);
        model.addAttribute("houses", houses);
        model.addAttribute("receivedMessages", receivedMessages);
        model.addAttribute("sentMessages", sentMessages);

        return "profile";
    }

    @PostMapping("/reply-owner")
    public String replyToOwner(@RequestParam("messageId") Long messageId, @RequestParam("reply") String reply, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null && message.getSender().getId().equals(user.getId())) {
            message.setReply(reply);
            messageRepository.save(message);
        }

        return "redirect:/user/profile";
    }

    
    @PostMapping("/house/{houseId}/contact")
    public String sendMessageToOwner(@PathVariable Long houseId, @RequestParam("message") String messageContent, HttpSession session) {
        User sender = (User) session.getAttribute("user");
        if (sender == null) {
            return "redirect:/login";
        }

        House house = houseRepository.findById(houseId).orElse(null);
        if (house != null) {
            Message message = new Message();
            message.setContent(messageContent);
            message.setHouse(house);
            message.setSender(sender);
            message.setRecipient(house.getOwner());
            messageRepository.save(message);
        }

        return "redirect:/house/" + houseId;
    }

    @GetMapping("/houses")
    public String listAllHouses(Model model) {
        List<House> houses = houseRepository.findAll();
        model.addAttribute("houses", houses);
        return "house-list";
    }

    @GetMapping("/filter-houses")
    public String filterHouses(@RequestParam(value = "price", required = false) BigDecimal price,
                               @RequestParam(value = "surface", required = false) Double surface,
                               @RequestParam(value = "city", required = false) String city,
                               Model model) {
        List<House> filteredHouses;

        if (price != null && surface != null && city != null) {
            filteredHouses = houseRepository.findByPriceLessThanEqualAndSurfaceGreaterThanEqualAndCityContaining(price, surface, city);
        } else if (price != null && surface != null) {
            filteredHouses = houseRepository.findByPriceLessThanEqualAndSurfaceGreaterThanEqual(price, surface);
        } else if (price != null && city != null) {
            filteredHouses = houseRepository.findByPriceLessThanEqualAndCityContaining(price, city);
        } else if (surface != null && city != null) {
            filteredHouses = houseRepository.findBySurfaceGreaterThanEqualAndCityContaining(surface, city);
        } else if (price != null) {
            filteredHouses = houseRepository.findByPriceLessThanEqual(price);
        } else if (surface != null) {
            filteredHouses = houseRepository.findBySurfaceGreaterThanEqual(surface);
        } else if (city != null) {
            filteredHouses = houseRepository.findByCityContaining(city);
        } else {
            filteredHouses = houseRepository.findByIsAvailableTrue();
        }

        model.addAttribute("houses", filteredHouses);
        return "house-list";
    }
}
