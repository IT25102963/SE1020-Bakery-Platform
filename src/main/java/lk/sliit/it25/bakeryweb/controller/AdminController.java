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
            session.setAttribute("admin", loggedInAdmin);
            return "redirect:/delivery";
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
}
