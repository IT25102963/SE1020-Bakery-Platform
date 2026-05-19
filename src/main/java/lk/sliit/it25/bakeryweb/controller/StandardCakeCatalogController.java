package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.service.StandardCakeCatalogService;
import jakarta.servlet.http.HttpSession;
import lk.sliit.it25.bakeryweb.model.Cake;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/standard-catalog")
public class StandardCakeCatalogController {
    private final StandardCakeCatalogService service;
    private static final String CAKE_IMAGE_UPLOAD_DIR = "data/uploads/cakes/";

    public StandardCakeCatalogController(StandardCakeCatalogService service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    @GetMapping
    public String viewCatalog(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("cakes", service.listCatalog());
        return "catalog/standardCatalog";
    }

    @PostMapping("/add")
    public String addCake(
            @RequestParam String name,
            @RequestParam(required = false) String category,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) String description,
            @RequestParam(value = "cakeImage", required = false) MultipartFile cakeImage,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("admin") == null) {
            redirectAttributes.addFlashAttribute("error", "Admin login required.");
            return "redirect:/admin/login";
        }
        try {
            String imageUrl = saveCakeImage(cakeImage);
            service.addCake(name, category, price, description, imageUrl);
            redirectAttributes.addFlashAttribute("success", "Cake added to catalog.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Image upload failed. Please try again.");
        }
        return "redirect:/standard-catalog";
    }

    @PostMapping("/delete")
    public String deleteCake(@RequestParam String id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            redirectAttributes.addFlashAttribute("error", "Admin login required.");
            return "redirect:/admin/login";
        }
        if (service.deleteCake(id)) {
            redirectAttributes.addFlashAttribute("success", "Cake deleted.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cake not found.");
        }
        return "redirect:/standard-catalog";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            redirectAttributes.addFlashAttribute("error", "Admin login required.");
            return "redirect:/admin/login";
        }

        Optional<Cake> cake = service.findById(id);
        if (cake.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cake not found.");
            return "redirect:/standard-catalog";
        }

        model.addAttribute("cake", cake.get());
        return "catalog/editCake";
    }

    @PostMapping("/edit")
    public String editCake(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam(required = false) String category,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) String description,
            @RequestParam(value = "cakeImage", required = false) MultipartFile cakeImage,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("admin") == null) {
            redirectAttributes.addFlashAttribute("error", "Admin login required.");
            return "redirect:/admin/login";
        }

        Optional<Cake> existingCake = service.findById(id);
        if (existingCake.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cake not found.");
            return "redirect:/standard-catalog";
        }

        try {
            String imageUrl = existingCake.get().getImageUrl();
            if (cakeImage != null && !cakeImage.isEmpty()) {
                imageUrl = saveCakeImage(cakeImage);
            }

            boolean updated = service.updateCake(id, name, category, price, description, imageUrl);
            if (updated) {
                redirectAttributes.addFlashAttribute("success", "Cake updated.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Cake not found.");
            }
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Image upload failed. Please try again.");
        }
        return "redirect:/standard-catalog";
    }

    private String saveCakeImage(MultipartFile cakeImage) throws IOException {
        if (cakeImage == null || cakeImage.isEmpty()) {
            return "";
        }

        String originalName = cakeImage.getOriginalFilename();
        String extension = getFileExtension(originalName);
        String fileName = UUID.randomUUID() + extension;

        Path uploadDir = Paths.get(CAKE_IMAGE_UPLOAD_DIR);
        Files.createDirectories(uploadDir);

        Path targetPath = uploadDir.resolve(fileName);
        cakeImage.transferTo(targetPath);

        return "/uploads/cakes/" + fileName;
    }

    private String getFileExtension(String originalName) {
        if (originalName == null || originalName.isBlank()) {
            return ".jpg";
        }

        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == originalName.length() - 1) {
            return ".jpg";
        }

        String extension = originalName.substring(dotIndex).toLowerCase();
        if (extension.length() > 10) {
            return ".jpg";
        }
        return extension;
    }
}
