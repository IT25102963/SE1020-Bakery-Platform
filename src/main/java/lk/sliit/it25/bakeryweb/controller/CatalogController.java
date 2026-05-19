package lk.sliit.it25.bakeryweb.controller;

import jakarta.servlet.http.HttpSession;
import lk.sliit.it25.bakeryweb.model.Cake;
import lk.sliit.it25.bakeryweb.repository.CatalogRepository;
import lk.sliit.it25.bakeryweb.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CatalogController {

    private final CatalogRepository catalogRepository;
    private final CartService cartService;

    public CatalogController(CatalogRepository catalogRepository, CartService cartService) {
        this.catalogRepository = catalogRepository;
        this.cartService = cartService;
    }

    @GetMapping({"/", "/catalog"})
    public String showCatalog(Model model, HttpSession session) {
        List<Cake> cakes = catalogRepository.findAll();
        model.addAttribute("cakes", cakes);
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "catalog/catalog";
    }
}
