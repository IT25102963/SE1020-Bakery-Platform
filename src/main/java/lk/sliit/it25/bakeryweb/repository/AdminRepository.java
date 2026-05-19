package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.Admin;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminRepository {

    private final String FILE_PATH = "data/admins.txt";

    public void save(Admin admin) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(admin.toCSV());
            bw.newLine();
            System.out.println("Admin saved: " + admin.toCSV());

        } catch (IOException e) {
            System.out.println("Error saving admin: " + e.getMessage());
        }
    }

    public Admin authenticate(String email, String password) {
        for (Admin admin : findAll()) {
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }

    public Admin findByEmail(String email) {
        for (Admin admin : findAll()) {
            if (admin.getEmail().equals(email)) {
                return admin;
            }
        }
        return null;
    }

    public boolean update(Admin updatedAdmin) {
        List<Admin> admins = findAll();
        boolean found = false;

        for (int i = 0; i < admins.size(); i++) {
            Admin admin = admins.get(i);
            if (admin.getEmail().equals(updatedAdmin.getEmail())) {
                admins.set(i, updatedAdmin);
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        return writeAll(admins);
    }

    private List<Admin> findAll() {
        List<Admin> admins = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 3) {
                    admins.add(new Admin(data[0], data[1], data[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading admin file: " + e.getMessage());
        }
        return admins;
    }

    private boolean writeAll(List<Admin> admins) {
        List<String> lines = new ArrayList<>();
        for (Admin admin : admins) {
            lines.add(admin.toCSV());
        }

        try {
            Files.write(Paths.get(FILE_PATH), lines);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing admin file: " + e.getMessage());
            return false;
        }
    }
}
