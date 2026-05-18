package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.Customer;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository // This tells Spring Boot this class handles data!
public class CustomerRepository {

    private final String FILE_PATH = "data/customers.txt";

    public void save(Customer customer) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(customer.toCSV());
            bw.newLine();
            System.out.println("Saved to database: " + customer.toCSV());

        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }

    public Customer authenticate(String email, String password) {
        for (Customer customer : findAll()) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null; // Return null if no match is found
    }

    public Customer findByEmail(String email) {
        for (Customer customer : findAll()) {
            if (customer.getEmail().equals(email)) {
                return customer;
            }
        }
        return null;
    }

    public boolean update(Customer updatedCustomer) {
        List<Customer> customers = findAll();
        boolean found = false;

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer.getEmail().equals(updatedCustomer.getEmail())) {
                customers.set(i, updatedCustomer);
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        return writeAll(customers);
    }

    public boolean deleteByEmail(String email) {
        List<Customer> customers = findAll();
        int originalSize = customers.size();
        customers.removeIf(customer -> customer.getEmail().equals(email));

        if (customers.size() == originalSize) {
            return false;
        }

        return writeAll(customers);
    }

    private List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 5) {
                    String photoUrl = data.length >= 6 ? data[5] : "";
                    customers.add(new Customer(data[0], data[1], data[2], data[3], data[4], photoUrl));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return customers;
    }

    private boolean writeAll(List<Customer> customers) {
        List<String> lines = new ArrayList<>();
        for (Customer customer : customers) {
            lines.add(customer.toCSV());
        }

        try {
            Files.write(Paths.get(FILE_PATH), lines);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
            return false;
        }
    }
}
