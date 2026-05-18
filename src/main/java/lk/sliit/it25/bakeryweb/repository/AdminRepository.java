package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.Admin;
import org.springframework.stereotype.Repository;
import java.io.*;

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
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && data[1].equals(email) && data[2].equals(password)) {
                    return new Admin(data[0], data[1], data[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading admin file: " + e.getMessage());
        }
        return null;
    }
}