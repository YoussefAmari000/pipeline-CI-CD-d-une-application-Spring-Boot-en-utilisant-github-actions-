package com.immobiliere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.immobiliere.repository.HouseRepository;
import com.immobiliere.model.House;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final HouseRepository houseRepository;

    public HomeController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
                       @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                       @RequestParam(value = "bedrooms", required = false) Integer bedrooms,
                       @RequestParam(value = "minSurface", required = false) Integer minSurface,
                       @RequestParam(value = "maxSurface", required = false) Integer maxSurface,
                       @RequestParam(value = "city", required = false) String city) {

        List<House> houses = houseRepository.findAllByIsAvailable(true);

        if (minPrice != null) {
            houses = houses.stream().filter(h -> h.getPrice().compareTo(minPrice) >= 0).collect(Collectors.toList());
        }

        if (maxPrice != null) {
            houses = houses.stream().filter(h -> h.getPrice().compareTo(maxPrice) <= 0).collect(Collectors.toList());
        }

        if (bedrooms != null) {
            houses = houses.stream().filter(h -> h.getBedrooms() == bedrooms).collect(Collectors.toList());
        }

        if (minSurface != null) {
            houses = houses.stream().filter(h -> h.getSurface() >= minSurface).collect(Collectors.toList());
        }

        if (maxSurface != null) {
            houses = houses.stream().filter(h -> h.getSurface() <= maxSurface).collect(Collectors.toList());
        }

        if (city != null && !city.isEmpty()) {
            houses = houses.stream().filter(h -> h.getCity().equalsIgnoreCase(city)).collect(Collectors.toList());
        }

        model.addAttribute("houses", houses);
        return "home";
    }
}

