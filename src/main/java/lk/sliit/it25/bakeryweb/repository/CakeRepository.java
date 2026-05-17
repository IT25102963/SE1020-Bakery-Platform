


package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.Cake;
import org.springframework.stereotype.Repository;

import java.io.*;
        import java.util.ArrayList;
import java.util.List;

@Repository
public class CakeRepository {

    private static final String FILE_PATH = "data/standard_cakes.txt";

    public CakeRepository() {
        // Ensure the data directory and file exist on startup
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(FILE_PATH);
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Cake> findAll() {
        List<Cake> cakes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 5) {
                        cakes.add(new Cake(
                                parts[0], parts[1], parts[2],
                                Double.parseDouble(parts[3]), parts[4]
                        ));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cakes;
    }

    public void save(Cake cake) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(cake.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteById(String id) {
        List<Cake> cakes = findAll();
        boolean removed = cakes.removeIf(c -> c.getId().equals(id));
        if (removed) {
            rewriteFile(cakes);
        }
        return removed;
    }

    public boolean existsById(String id) {
        return findAll().stream().anyMatch(c -> c.getId().equals(id));
    }

    private void rewriteFile(List<Cake> cakes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Cake c : cakes) {
                writer.write(c.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}