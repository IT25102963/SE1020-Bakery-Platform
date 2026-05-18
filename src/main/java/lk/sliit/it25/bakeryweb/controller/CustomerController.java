package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.model.Customer;
import lk.sliit.it25.bakeryweb.repository.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerController {

    // Spring Boot automatically connects our Repository here
    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "customer/register"; // Looks inside your WEB-INF/jsp/customer folder
    }

    @PostMapping({"/registerCustomer", "/register"})
    public String registerCustomer(
            @RequestParam("customerName") String name,
            @RequestParam("customerEmail") String email,
            @RequestParam("customerPhone") String phone,
            @RequestParam("customerAddress") String address,
            @RequestParam("customerPassword") String password,
            @RequestParam(value = "customerPhotoUrl", required = false) String photoUrl) {

        // 1. Create the Object
        Customer newCustomer = new Customer(name, email, phone, address, password, photoUrl);

        // 2. Save it using the Repository
        repository.save(newCustomer);

        // 3. Send them to the login page after registering!
        return "redirect:/login?registered=true";
    }
}
