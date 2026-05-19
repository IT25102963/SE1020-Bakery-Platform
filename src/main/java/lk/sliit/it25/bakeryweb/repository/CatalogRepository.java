package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.Cake;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class CatalogRepository {

    private static final String CAKES_FILE_PATH = "data/cakes.txt";

    public List<Cake> findAll() {
        List<Cake> cakes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CAKES_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", 6);
                if (values.length >= 5) {
                    String id = values[0].trim();
                    String name = values[1].trim();
                    double price = Double.parseDouble(values[2].trim());
                    String category = values[3].trim();
                    String description = values[4].trim();
                    String imageUrl = values.length >= 6 ? values[5].trim() : "";

                    Cake cake = new Cake(id, name, description, price, category, imageUrl);
                    cakes.add(cake);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cakes.sort(Comparator.comparing(Cake::getName, String.CASE_INSENSITIVE_ORDER));
        return cakes;
    }

    public synchronized void save(Cake cake) {
        try (FileWriter fw = new FileWriter(CAKES_FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(toCsv(cake));
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error saving cake: " + e.getMessage(), e);
        }
    }

    public synchronized boolean deleteById(String id) {
        List<Cake> cakes = findAll();
        boolean removed = cakes.removeIf(cake -> cake.getId().equals(id));
        if (!removed) {
            return false;
        }
        writeAll(cakes);
        return true;
    }

    public synchronized Optional<Cake> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return findAll().stream().filter(cake -> id.trim().equals(cake.getId())).findFirst();
    }

    public synchronized boolean update(Cake updatedCake) {
        if (updatedCake == null || updatedCake.getId() == null || updatedCake.getId().isBlank()) {
            return false;
        }

        List<Cake> cakes = findAll();
        boolean updated = false;
        for (int i = 0; i < cakes.size(); i++) {
            Cake current = cakes.get(i);
            if (current.getId().equals(updatedCake.getId())) {
                cakes.set(i, updatedCake);
                updated = true;
                break;
            }
        }

        if (!updated) {
            return false;
        }

        writeAll(cakes);
        return true;
    }

    public synchronized String nextId() {
        int maxId = 0;
        for (Cake cake : findAll()) {
            try {
                int parsed = Integer.parseInt(cake.getId());
                if (parsed > maxId) {
                    maxId = parsed;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return String.valueOf(maxId + 1);
    }

    private void writeAll(List<Cake> cakes) {
        try (FileWriter fw = new FileWriter(CAKES_FILE_PATH, false);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Cake cake : cakes) {
                bw.write(toCsv(cake));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing cakes: " + e.getMessage(), e);
        }
    }

    private String toCsv(Cake cake) {
        return String.join(",",
                sanitize(cake.getId()),
                sanitize(cake.getName()),
                String.valueOf(cake.getPrice()),
                sanitize(cake.getCategory()),
                sanitize(cake.getDescription()),
                sanitize(cake.getImageUrl())
        );
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", " ").replace("\n", " ").replace("\r", " ").trim();
    }
}
