package com.immobiliere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import com.immobiliere.model.House;
import com.immobiliere.model.Message;
import com.immobiliere.model.User;
import com.immobiliere.repository.HouseRepository;
import com.immobiliere.repository.MessageRepository;
import com.immobiliere.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    private final String uploadDir = "C:\\Users\\HP\\eclipse-workspace\\bestImmobiliere\\public\\images";

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/houses")
    public String listHouses(HttpSession session, Model model) {
        User owner = (User) session.getAttribute("user");
        if (owner == null) {
            return "redirect:/login";
        }
        List<House> houses = houseRepository.findAllByOwner(owner);
        List<Message> messages = messageRepository.findByRecipientId(owner.getId());
        model.addAttribute("houses", houses);
        model.addAttribute("messages", messages);
        model.addAttribute("owner", owner);
        return "owner-houses";
    }

    @GetMapping("/add-house")
    public String showAddHouseForm(Model model) {
        model.addAttribute("house", new House());
        return "add-house";
    }

    @PostMapping("/add-house")
    public String addHouse(@ModelAttribute House house, @RequestParam("imageFile") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) {
        User owner = (User) session.getAttribute("user");
        if (owner == null) {
            return "redirect:/login";
        }

        house.setOwner(owner);
        house.setAvailable(true);

        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                saveFile(uploadDir, fileName, file);
                house.setImage(fileName);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "L'image n'a pas pu être enregistrée: " + e.getMessage());
                return "redirect:/owner/add-house";
            }
        }

        houseRepository.save(house);
        return "redirect:/owner/houses";
    }

    private void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        multipartFile.transferTo(filePath);
    }

    @GetMapping("/edit-house/{id}")
    public String showEditHouseForm(@PathVariable Long id, Model model) {
        House house = houseRepository.findById(id).orElseThrow(() -> new RuntimeException("Maison non trouvée"));
        model.addAttribute("house", house);
        return "edit-house";
    }

    @PostMapping("/update-house/{id}")
    public String updateHouse(@PathVariable Long id, @ModelAttribute House house, HttpSession session) {
        User owner = (User) session.getAttribute("user");
        if (owner == null) {
            return "redirect:/login";
        }

        house.setId(id);
        house.setOwner(owner);
        houseRepository.save(house);
        return "redirect:/owner/houses";
    }

    @GetMapping("/delete-house/{id}")
    public String deleteHouse(@PathVariable Long id) {
        houseRepository.deleteById(id);
        return "redirect:/owner/houses";
    }

    @GetMapping("/details")
    public String getOwnerDetails(@RequestParam("ownerId") Long ownerId, Model model) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
            return "owner-profile";
        } else {
            return "redirect:/error"; // redirige vers une page d'erreur si l'utilisateur n'existe pas
        }
    }

    @PostMapping("/update-profile-picture")
    public String updateProfilePicture(@RequestParam("profilePicture") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) {
        User owner = (User) session.getAttribute("user");
        if (owner == null) {
            return "redirect:/login";
        }

        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                saveFile(uploadDir, fileName, file);
                owner.setProfilePicture(fileName);
                userRepository.save(owner);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "La photo de profil n'a pas pu être enregistrée: " + e.getMessage());
                return "redirect:/owner/houses";
            }
        }

        return "redirect:/owner/houses";
    }
    
    @PostMapping("/reply-message")
    public String replyMessage(@RequestParam Long messageId, @RequestParam String reply, HttpSession session) {
        User owner = (User) session.getAttribute("user");
        if (owner == null) {
            return "redirect:/login";
        }
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setReply(reply);
            messageRepository.save(message);
        }
        return "redirect:/owner/houses";
    }
    
    @PostMapping("/update-availability")
    public String updateAvailability(@RequestParam Long houseId, @RequestParam boolean isAvailable, HttpSession session) {
        User owner = (User) session.getAttribute("user");
        if (owner == null) {
            return "redirect:/login";
        }

        Optional<House> houseOpt = houseRepository.findById(houseId);
        if (houseOpt.isPresent() && houseOpt.get().getOwner().equals(owner)) {
            House house = houseOpt.get();
            house.setAvailable(isAvailable);
            houseRepository.save(house);
        }

        return "redirect:/owner/houses";
    }

}
