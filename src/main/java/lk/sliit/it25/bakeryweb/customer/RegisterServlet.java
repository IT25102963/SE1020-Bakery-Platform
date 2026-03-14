package lk.sliit.it25.bakeryweb.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// This annotation MUST match the 'action' in your HTML form!
@WebServlet("/customer/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. GRAB THE DATA FROM THE HTML FORM
        // The names inside the quotes MUST match the name="..." attributes in your JSP file
        String name = request.getParameter("customerName");
        String email = request.getParameter("customerEmail");
        String phone = request.getParameter("customerPhone");
        String address = request.getParameter("customerAddress");
        String password = request.getParameter("customerPassword");

        // 2. CREATE THE OOP OBJECT
        Customer newCustomer = new Customer(name, email, phone, address, password);

        // 3. SECURE THE FILE HANDLING MARKS
        // This finds the correct folder inside the running Tomcat server
        String dataFolderPath = getServletContext().getRealPath("/data/");
        File dataFolder = new File(dataFolderPath);
        if (!dataFolder.exists()) {
            dataFolder.mkdir(); // Create the folder if it doesn't exist
        }

        String filePath = dataFolderPath + "customers.txt";

        // FileWriter(filePath, true) -> The 'true' means APPEND. It won't delete old users!
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(newCustomer.toCSV());
            writer.newLine(); // Move to the next line for the next customer
            System.out.println("SUCCESS: Saved to " + filePath); // Prints to your IntelliJ terminal
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4. SEND THE USER TO THE NEXT PAGE
        // For now, let's just send them back to the form with a success message in the URL
        response.sendRedirect("register.jsp?status=success");
    }
}