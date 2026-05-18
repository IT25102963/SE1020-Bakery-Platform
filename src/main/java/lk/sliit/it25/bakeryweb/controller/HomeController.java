package lk.sliit.it25.bakeryweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToDeliveryScheduler() {
        return "redirect:/delivery";
    }
}
