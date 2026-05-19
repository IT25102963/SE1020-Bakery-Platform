package lk.sliit.it25.bakeryweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/catalog";
    }

    @GetMapping("/about")
    public String about() {
        return "redirect:/catalog";
    }
}
