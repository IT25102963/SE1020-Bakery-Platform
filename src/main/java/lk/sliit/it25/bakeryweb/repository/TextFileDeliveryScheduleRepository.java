package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.DeliverySlot;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TextFileDeliveryScheduleRepository implements DeliveryScheduleRepository {
    private static final String FILE_PATH = "data/schedules.txt";
    private static final String DELIMITER = "\\|";

    @Override
    public synchronized List<DeliverySlot> findAll() {
        Path path = getPath();
        ensureFile(path);
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<DeliverySlot> slots = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length != 5) {
                    continue;
                }
                slots.add(new DeliverySlot(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
            return slots;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read schedules.txt", e);
        }
    }

    @Override
    public synchronized Optional<DeliverySlot> findById(String id) {
        return findAll().stream().filter(slot -> slot.getId().equals(id)).findFirst();
    }

    @Override
    public synchronized void save(DeliverySlot slot) {
        Path path = getPath();
        ensureFile(path);
        String line = toLine(slot);
        try {
            Files.writeString(path, line + System.lineSeparator(), StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write schedules.txt", e);
        }
    }

    @Override
    public synchronized boolean update(DeliverySlot slot) {
        List<DeliverySlot> slots = findAll();
        boolean updated = false;

        for (int i = 0; i < slots.size(); i++) {
            DeliverySlot existing = slots.get(i);
            if (existing.getId().equals(slot.getId())) {
                slots.set(i, slot);
                updated = true;
                break;
            }
        }

        if (!updated) {
            return false;
        }

        Path path = getPath();
        ensureFile(path);
        List<String> lines = slots.stream().map(this::toLine).toList();
        try {
            Files.write(path, lines, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to update schedules.txt", e);
        }
    }

    @Override
    public synchronized boolean deleteById(String id) {
        List<DeliverySlot> slots = findAll();
        boolean removed = slots.removeIf(slot -> slot.getId().equals(id));
        if (!removed) {
            return false;
        }
        Path path = getPath();
        ensureFile(path);
        List<String> lines = slots.stream().map(this::toLine).toList();
        try {
            Files.write(path, lines, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to update schedules.txt", e);
        }
    }

    private String toLine(DeliverySlot slot) {
        return String.join("|",
                sanitize(slot.getId()),
                sanitize(slot.getCustomerName()),
                sanitize(slot.getAddress()),
                sanitize(slot.getDeliveryDate()),
                sanitize(slot.getDeliveryTime()));
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replace("|", "/");
    }

    private Path getPath() {
        return Paths.get(FILE_PATH);
    }

    private void ensureFile(Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize schedules.txt", e);
        }
    }
}
