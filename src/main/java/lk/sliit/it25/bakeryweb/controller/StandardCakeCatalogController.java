package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.service.StandardCakeCatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Objects;

@Controller
@RequestMapping("/standard-catalog")
public class StandardCakeCatalogController {
    private final StandardCakeCatalogService service;

    public StandardCakeCatalogController(StandardCakeCatalogService service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    @GetMapping
    public String viewCatalog(Model model) {
        model.addAttribute("cakes", service.listCatalog());
        return "catalog/standardCatalog";
    }

    @PostMapping("/add")
    public String addCake(
            @RequestParam String name,
            @RequestParam(required = false) String flavor,
            @RequestParam(required = false) String size,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes
    ) {
        try {
            service.addCake(name, flavor, size, price, description);
            redirectAttributes.addFlashAttribute("success", "Cake added to catalog.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/standard-catalog";
    }

    @PostMapping("/delete")
    public String deleteCake(@RequestParam String id, RedirectAttributes redirectAttributes) {
        if (service.deleteCake(id)) {
            redirectAttributes.addFlashAttribute("success", "Cake deleted.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cake not found.");
        }
        return "redirect:/standard-catalog";
    }
}
