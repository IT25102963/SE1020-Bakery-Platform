package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.Cake;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

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
                    int id = Integer.parseInt(values[0]);
                    String name = values[1];
                    double price = Double.parseDouble(values[2]);
                    String category = values[3];
                    String description = values[4];
                    String imageUrl = values.length == 6 ? values[5] : null;
                    cakes.add(new Cake(id, name, price, category, description, imageUrl));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cakes;
    }

    public synchronized void addCake(Cake cake) {
        List<Cake> cakes = findAll();
        OptionalInt maxId = cakes.stream().mapToInt(Cake::getId).max();
        cake.setId(maxId.orElse(0) + 1);
        cakes.add(cake);
        saveAll(cakes);
    }

    public synchronized void deleteCake(int cakeId) {
        List<Cake> cakes = findAll();
        cakes.removeIf(cake -> cake.getId() == cakeId);
        saveAll(cakes);
    }

    private void saveAll(List<Cake> cakes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAKES_FILE_PATH, false))) {
            for (Cake cake : cakes) {
                writer.write(cake.getId()
                        + "," + sanitize(cake.getName())
                        + "," + cake.getPrice()
                        + "," + sanitize(cake.getCategory())
                        + "," + sanitize(cake.getDescription())
                        + "," + sanitize(cake.getImageUrl()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", " ").trim();
    }
}
