package com.cakecraft.orderbookings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToBookings() {
        return "redirect:/bookings/products";
    }
}
