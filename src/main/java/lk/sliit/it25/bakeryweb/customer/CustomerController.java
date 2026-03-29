package lk.sliit.it25.bakeryweb.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Controller
public class CustomerController {

    // 1. Shows the web page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "customer/register";
    }

    // 2. Catches the form submission
    @PostMapping("/registerCustomer")
    public String saveCustomer(
            @RequestParam("customerName") String customerName,
            @RequestParam("customerEmail") String customerEmail,
            @RequestParam("customerPhone") String customerPhone,
            @RequestParam("customerAddress") String customerAddress,
            @RequestParam("customerPassword") String customerPassword) {

        // 3. File Handling (true = append mode)
        try (FileWriter fw = new FileWriter("data/customers.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            // Save as a single line separated by commas
            String userData = customerName + "," + customerEmail + "," + customerPhone + "," + customerAddress + "," + customerPassword;
            bw.write(userData);
            bw.newLine();

            System.out.println("Successfully saved: " + customerName);

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }

        // 4. Redirect them back to the register page for now
        return "redirect:/register";
    }
}
