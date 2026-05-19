package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.model.Admin;
import lk.sliit.it25.bakeryweb.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin") // This adds "/admin" to the start of all URLs in this file!
public class AdminController {

    private final AdminRepository repository;

    public AdminController(AdminRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "admin/register";
    }

    @PostMapping({"/processRegister", "/register"})
    public String registerAdmin(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        repository.save(new Admin(name, email, password));
        return "redirect:/admin/login?registered=true";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "admin/login";
    }

    @PostMapping({"/processLogin", "/login"})
    public String processLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        Admin loggedInAdmin = repository.authenticate(email, password);

        if (loggedInAdmin != null) {
            session.setAttribute("admin", loggedInAdmin); // Saving as "admin"!
            session.removeAttribute("user");
            return "redirect:/standard-catalog";
        } else {
            model.addAttribute("error", "Invalid Admin Credentials!");
            return "admin/login";
        }
    }

    @GetMapping("/profile")
    public String showAdminProfile(HttpSession session, Model model) {
        Admin loggedInAdmin = (Admin) session.getAttribute("admin");
        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", loggedInAdmin);
        return "admin/profile";
    }

    @GetMapping("/editProfile")
    public String showEditAdminProfile(HttpSession session, Model model) {
        Admin loggedInAdmin = (Admin) session.getAttribute("admin");
        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", loggedInAdmin);
        return "admin/editProfile";
    }

    @PostMapping("/editProfile")
    public String updateAdminProfile(
            @RequestParam("name") String name,
            @RequestParam(value = "password", required = false) String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Admin loggedInAdmin = (Admin) session.getAttribute("admin");
        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        String updatedName = name == null ? "" : name.trim();
        if (updatedName.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Name cannot be empty.");
            return "redirect:/admin/editProfile";
        }

        String updatedPassword = (password == null || password.trim().isEmpty())
                ? loggedInAdmin.getPassword()
                : password.trim();

        Admin updatedAdmin = new Admin(updatedName, loggedInAdmin.getEmail(), updatedPassword);
        boolean updated = repository.update(updatedAdmin);
        if (!updated) {
            redirectAttributes.addFlashAttribute("error", "Admin profile update failed.");
            return "redirect:/admin/editProfile";
        }

        session.setAttribute("admin", updatedAdmin);
        redirectAttributes.addFlashAttribute("success", "Admin profile updated successfully.");
        return "redirect:/admin/profile";
    }
}
