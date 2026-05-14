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
                if (l == null || l.isBlank()) continue;
                try {
                    Booking b = fromCsv(l);
                    if (b == null) continue;
                    // skip malformed or missing-id entries
                    if (b.getBookingId() == null || b.getBookingId().isBlank()) continue;
                    out.add(b);
                } catch (Exception ex) {
                    // skip invalid lines instead of failing the entire read
                    continue;
                }
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
                safe(b.getBookingId()),
                safe(b.getCustomerName()),
                safe(b.getPhone()),
                safe(b.getCakeName()),
                safe(b.getOrderDetails()),
                safe(b.getOrderType()),
                String.valueOf(b.getQuantity() == null ? 0 : b.getQuantity()),
                b.getBookingDate() == null ? "" : b.getBookingDate().toString(),
                b.getDeliveryDate() == null ? "" : b.getDeliveryDate().toString(),
                b.getTotalPrice() == null ? "0" : b.getTotalPrice().toString(),
                safe(b.getStatus()));
    }

    private String safe(String s) {
        if (s == null) return "";
        String t = s.trim();
        return "null".equalsIgnoreCase(t) ? "" : t;
    }

    private Booking fromCsv(String line) {
        String[] parts = line.split(",", -1);
        Booking b = new Booking();
        b.setBookingId(parts.length > 0 ? parts[0].trim() : "");
        b.setCustomerName(parts.length > 1 ? parts[1].trim() : "");
        b.setPhone(parts.length > 2 ? parts[2].trim() : "");
        b.setCakeName(parts.length > 3 ? parts[3].trim() : "");
        b.setOrderDetails(parts.length > 4 ? parts[4].trim() : "");
        b.setOrderType(parts.length > 5 ? parts[5].trim() : "");
        try {
            b.setQuantity(parts.length > 6 && !parts[6].isBlank() ? Integer.parseInt(parts[6].trim()) : 0);
        } catch (NumberFormatException nfe) {
            b.setQuantity(0);
        }
        try {
            b.setBookingDate(parts.length > 7 && !parts[7].isBlank() ? LocalDate.parse(parts[7].trim()) : null);
        } catch (Exception e) {
            b.setBookingDate(null);
        }
        try {
            b.setDeliveryDate(parts.length > 8 && !parts[8].isBlank() ? LocalDate.parse(parts[8].trim()) : null);
        } catch (Exception e) {
            b.setDeliveryDate(null);
        }
        try {
            b.setTotalPrice(parts.length > 9 && !parts[9].isBlank() ? new BigDecimal(parts[9].trim()) : BigDecimal.ZERO);
        } catch (Exception e) {
            b.setTotalPrice(BigDecimal.ZERO);
        }
        b.setStatus(parts.length > 10 ? parts[10].trim() : "");
        return b;
    }
}
