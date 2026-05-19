package lk.sliit.it25.bakeryweb.controller;

import jakarta.servlet.http.HttpSession;
import lk.sliit.it25.bakeryweb.model.Customer;
import lk.sliit.it25.bakeryweb.repository.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class CustomerController {

    private final CustomerRepository repository;
    private static final String UPLOAD_DIR = "src/main/webapp/uploads/";

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "customer/register";
    }

    @PostMapping({"/registerCustomer", "/register"})
    public String registerCustomer(
            @RequestParam("customerName") String name,
            @RequestParam("customerEmail") String email,
            @RequestParam("customerPhone") String phone,
            @RequestParam("customerAddress") String address,
            @RequestParam("customerPassword") String password,
            @RequestParam(value = "customerPhotoUrl", required = false) String photoUrl) {

        Customer newCustomer = new Customer(name, email, phone, address, password, photoUrl);
        repository.save(newCustomer);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/customer/editProfile")
    public String showEditProfilePage(Model model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "customer/editProfile";
    }

    @PostMapping("/customer/editProfile")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("profilePicture") MultipartFile profilePicture,
            HttpSession session) {

        Customer user = (Customer) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);

        if (!profilePicture.isEmpty()) {
            try {
                // Create the upload directory if it doesn't exist
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Save the file
                String originalName = profilePicture.getOriginalFilename() == null ? "profile.png" : profilePicture.getOriginalFilename();
                String fileName = user.getEmail() + "_" + originalName;
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, profilePicture.getBytes());
                user.setPhotoUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file upload error
            }
        }

        repository.update(user);
        session.setAttribute("user", user);

        return "redirect:/profile";
    }
}
