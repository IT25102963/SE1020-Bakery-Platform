package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.StandardCake;
import lk.sliit.it25.bakeryweb.util.DelimitedTextCodec;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class StandardCakeRepository {
    private static final String FILE_NAME = "standard_cakes.txt";

    private final Path dataFile;

    public StandardCakeRepository() {
        // Keep storage at project root /data to match the assignment's "Text Files" requirement.
        this(Path.of("data").resolve(FILE_NAME));
    }

    // Package-private for tests.
    StandardCakeRepository(Path dataFile) {
        this.dataFile = Objects.requireNonNull(dataFile, "dataFile");
    }

    public synchronized List<StandardCake> findAll() {
        ensureFileExists();
        List<String> lines;
        try {
            lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read standard cakes from " + dataFile.toAbsolutePath(), e);
        }

        List<StandardCake> cakes = new ArrayList<>();
        for (String line : lines) {
            String trimmed = line == null ? "" : line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

            List<String> fields = DelimitedTextCodec.decodePipeDelimited(trimmed);
            if (fields.size() < 6) continue; // tolerate old/bad rows

            String id = fields.get(0);
            String name = fields.get(1);
            String flavor = fields.get(2);
            String size = fields.get(3);
            BigDecimal price;
            try {
                price = new BigDecimal(fields.get(4));
            } catch (Exception ex) {
                continue;
            }
            String description = fields.get(5);

            cakes.add(new StandardCake(id, name, flavor, size, price, description));
        }

        cakes.sort(Comparator.comparing(StandardCake::getName, String.CASE_INSENSITIVE_ORDER));
        return cakes;
    }

    public synchronized Optional<StandardCake> findById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        return findAll().stream().filter(c -> id.equals(c.getId())).findFirst();
    }

    public synchronized void add(StandardCake cake) {
        Objects.requireNonNull(cake, "cake");
        ensureFileExists();

        String row = DelimitedTextCodec.encodePipeDelimited(List.of(
                cake.getId(),
                cake.getName(),
                cake.getFlavor(),
                cake.getSize(),
                cake.getPrice().toPlainString(),
                cake.getDescription()
        ));

        try {
            Files.writeString(
                    dataFile,
                    row + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to write standard cake to " + dataFile.toAbsolutePath(), e);
        }
    }

    public synchronized boolean deleteById(String id) {
        if (id == null || id.isBlank()) return false;

        List<StandardCake> all = findAll();
        boolean removed = all.removeIf(c -> id.equals(c.getId()));
        if (!removed) return false;

        List<String> rows = new ArrayList<>();
        rows.add("# id|name|flavor|size|price|description");
        for (StandardCake c : all) {
            rows.add(DelimitedTextCodec.encodePipeDelimited(List.of(
                    c.getId(),
                    c.getName(),
                    c.getFlavor(),
                    c.getSize(),
                    c.getPrice().toPlainString(),
                    c.getDescription()
            )));
        }

        try {
            Files.write(dataFile, rows, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to rewrite standard cakes file " + dataFile.toAbsolutePath(), e);
        }
        return true;
    }

    private void ensureFileExists() {
        try {
            Files.createDirectories(dataFile.getParent());
            if (!Files.exists(dataFile)) {
                Files.write(
                        dataFile,
                        List.of("# id|name|flavor|size|price|description"),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE_NEW
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data file at " + dataFile.toAbsolutePath(), e);
        }
    }
}

