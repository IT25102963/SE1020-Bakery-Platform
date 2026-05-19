package lk.sliit.it25.bakeryweb.controller;

import jakarta.servlet.http.HttpSession;
import lk.sliit.it25.bakeryweb.model.Cake;
import lk.sliit.it25.bakeryweb.repository.CatalogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class CatalogController {

    private final CatalogRepository catalogRepository;
    private static final Path UPLOAD_DIR = Paths.get("src", "main", "resources", "static", "uploads", "cakes");

    public CatalogController(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @GetMapping({"/", "/catalog"})
    public String showCatalog(Model model, HttpSession session) {
        List<Cake> cakes = catalogRepository.findAll();
        model.addAttribute("cakes", cakes);
        return "catalog/catalog";
    }

    @PostMapping("/catalog/admin/add")
    public String addCake(
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            HttpSession session
    ) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        Cake cake = new Cake(0, name, price, category, description);
        String imageUrl = storeCakeImage(imageFile);
        if (imageUrl != null) {
            cake.setImageUrl(imageUrl);
        }

        catalogRepository.addCake(cake);
        return "redirect:/catalog?added=true";
    }

    @PostMapping("/catalog/admin/delete")
    public String deleteCake(@RequestParam int cakeId, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        catalogRepository.deleteCake(cakeId);
        return "redirect:/catalog?cakeDeleted=true";
    }

    private String storeCakeImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return null;
        }

        String original = imageFile.getOriginalFilename();
        String safeName = original == null ? "cake.jpg" : original.replaceAll("[^a-zA-Z0-9._-]", "_");
        String filename = UUID.randomUUID() + "_" + safeName;

        try {
            Files.createDirectories(UPLOAD_DIR);
            Path target = UPLOAD_DIR.resolve(filename);
            Files.copy(imageFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/cakes/" + filename;
        } catch (IOException e) {
            return null;
        }
    }
}
