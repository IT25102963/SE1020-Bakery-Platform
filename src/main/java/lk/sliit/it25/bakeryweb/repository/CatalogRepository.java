package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.Cake;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CatalogRepository {

    private static final String CAKES_FILE_PATH = "data/cakes.txt";

    public List<Cake> findAll() {
        List<Cake> cakes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CAKES_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 5) {
                    int id = Integer.parseInt(values[0]);
                    String name = values[1];
                    double price = Double.parseDouble(values[2]);
                    String category = values[3];
                    String description = values[4];
                    cakes.add(new Cake(id, name, price, category, description));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cakes;
    }
}
