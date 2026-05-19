package lk.sliit.it25.bakeryweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalogPageController {

    @GetMapping("/legacy-catalog")
    public String showCatalogPage() {
        return "forward:/index.html";
    }

    @GetMapping("/legacy-catalog/add-cake")
    public String showAddCakePage() {
        return "forward:/add-cake.html";
    }
}
