package lk.sliit.it25.bakeryweb.controller;

import jakarta.servlet.http.HttpSession;
import lk.sliit.it25.bakeryweb.model.Cake;
import lk.sliit.it25.bakeryweb.repository.CatalogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CatalogController {

    private final CatalogRepository catalogRepository;

    public CatalogController(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @GetMapping({"/", "/catalog"})
    public String showCatalog(Model model, HttpSession session) {
        List<Cake> cakes = catalogRepository.findAll();
        model.addAttribute("cakes", cakes);
        return "catalog/catalog";
    }
}
