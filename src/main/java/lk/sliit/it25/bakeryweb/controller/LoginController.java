package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.model.Customer;
import lk.sliit.it25.bakeryweb.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final CustomerRepository repository;

    public LoginController(CustomerRepository repository) {
        this.repository = repository;
    }

    // 1. Show the Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        // We changed this from "auth/login" to match your actual folder!
        return "customer/login";
    }

    // 2. Process the Form Submission
    @PostMapping({"/processLogin", "/login"})
    public String processLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        // Ask the repository to find this user
        Customer loggedInUser = repository.authenticate(email, password);

        if (loggedInUser != null) {
            // SUCCESS: Save user to the "Session" (browser memory) so they stay logged in
            session.setAttribute("user", loggedInUser);
            session.removeAttribute("admin");
            System.out.println("Login successful for: " + email);
            return "redirect:/catalog"; // Send them to the cake catalog!
        } else {
            // FAIL: Send an error message back to the login page
            model.addAttribute("error", "Invalid email or password!");
            return "customer/login";
        }
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Customer loggedInUser = (Customer) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedInUser);
        return "customer/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "photoUrl", required = false) String photoUrl,
            HttpSession session) {

        Customer loggedInUser = (Customer) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        String newPassword = (password == null || password.trim().isEmpty())
                ? loggedInUser.getPassword()
                : password;

        Customer updatedCustomer = new Customer(
                name,
                loggedInUser.getEmail(),
                phone,
                address,
                newPassword,
                photoUrl
        );

        boolean updated = repository.update(updatedCustomer);
        if (!updated) {
            return "redirect:/profile?error=update";
        }

        session.setAttribute("user", updatedCustomer);
        return "redirect:/profile?updated=true";
    }

    @PostMapping("/profile/delete")
    public String deleteProfile(HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        boolean deleted = repository.deleteByEmail(loggedInUser.getEmail());
        if (!deleted) {
            return "redirect:/profile?error=delete";
        }

        session.invalidate();
        return "redirect:/catalog?deleted=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/catalog";
    }
}
