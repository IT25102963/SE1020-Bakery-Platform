package lk.sliit.it25.bakeryweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/bookings/products";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }
}
