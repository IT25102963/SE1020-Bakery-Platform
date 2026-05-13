package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.Booking;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Repository
public class BookingFileRepository {

    private final Path dataFile = Path.of("src/main/resources/data/bookings.txt");

    public void save(Booking booking) {
        try {
            Files.createDirectories(dataFile.getParent());
            String line = toCsv(booking);
            Files.writeString(dataFile, line + System.lineSeparator(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<Booking> findAll() {
        if (!Files.exists(dataFile)) return new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(dataFile);
            List<Booking> out = new ArrayList<>();
            for (String l : lines) {
                if (l.isBlank()) continue;
                out.add(fromCsv(l));
            }
            return out;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Optional<Booking> findById(String id) {
        return findAll().stream().filter(b -> id.equals(b.getBookingId())).findFirst();
    }

    public boolean update(String id, Booking updated) {
        List<Booking> all = findAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (id.equals(all.get(i).getBookingId())) {
                updated.setBookingId(id);
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAll(all);
        return found;
    }

    public boolean updateStatus(String id, String status) {
        List<Booking> all = findAll();
        for (Booking b : all) {
            if (id.equals(b.getBookingId())) {
                b.setStatus(status);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String id) {
        List<Booking> all = findAll();
        boolean removed = all.removeIf(b -> id.equals(b.getBookingId()));
        if (removed) writeAll(all);
        return removed;
    }

    public boolean cancel(String id) {
        return updateStatus(id, "Cancelled");
    }

    private void writeAll(List<Booking> all) {
        try {
            Files.createDirectories(dataFile.getParent());
            List<String> lines = new ArrayList<>();
            for (Booking b : all) lines.add(toCsv(b));
            Files.write(dataFile, lines);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String toCsv(Booking b) {
        return String.join(",",
                b.getBookingId(),
                b.getCustomerName(),
                b.getPhone(),
                b.getCakeName(),
                b.getOrderDetails(),
                b.getOrderType(),
                String.valueOf(b.getQuantity()),
                b.getBookingDate() == null ? "" : b.getBookingDate().toString(),
                b.getDeliveryDate() == null ? "" : b.getDeliveryDate().toString(),
                b.getTotalPrice() == null ? "0" : b.getTotalPrice().toString(),
                b.getStatus() == null ? "" : b.getStatus());
    }

    private Booking fromCsv(String line) {
        String[] parts = line.split(",");
        Booking b = new Booking();
        b.setBookingId(parts.length > 0 ? parts[0] : "");
        b.setCustomerName(parts.length > 1 ? parts[1] : "");
        b.setPhone(parts.length > 2 ? parts[2] : "");
        b.setCakeName(parts.length > 3 ? parts[3] : "");
        b.setOrderDetails(parts.length > 4 ? parts[4] : "");
        b.setOrderType(parts.length > 5 ? parts[5] : "");
        b.setQuantity(parts.length > 6 && !parts[6].isBlank() ? Integer.parseInt(parts[6]) : 0);
        b.setBookingDate(parts.length > 7 && !parts[7].isBlank() ? LocalDate.parse(parts[7]) : null);
        b.setDeliveryDate(parts.length > 8 && !parts[8].isBlank() ? LocalDate.parse(parts[8]) : null);
        b.setTotalPrice(parts.length > 9 && !parts[9].isBlank() ? new BigDecimal(parts[9]) : BigDecimal.ZERO);
        b.setStatus(parts.length > 10 ? parts[10] : "");
        return b;
    }
}
