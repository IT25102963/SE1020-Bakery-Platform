package com.cakecraft.orderbookings.repository;

import com.cakecraft.orderbookings.model.Booking;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookingFileRepository {

    private final Path filePath;

    public BookingFileRepository(@Value("${booking.file.path}") String bookingFilePath) {
        this.filePath = Paths.get(bookingFilePath);
    }

    @PostConstruct
    public void init() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize booking data file", e);
        }
    }

    public List<Booking> findAll() {
        try {
            if (Files.notExists(filePath)) {
                return new ArrayList<>();
            }

            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<Booking> bookings = new ArrayList<>();

            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                bookings.add(fromCsv(line));
            }

            return bookings;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read bookings", e);
        }
    }

    public Optional<Booking> findById(String bookingId) {
        return findAll().stream()
                .filter(booking -> booking.getBookingId().equalsIgnoreCase(bookingId))
                .findFirst();
    }

    public Booking save(Booking booking) {
        List<Booking> bookings = findAll();
        bookings.add(booking);
        writeAll(bookings);
        return booking;
    }

    public boolean update(Booking updatedBooking) {
        List<Booking> bookings = findAll();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId().equalsIgnoreCase(updatedBooking.getBookingId())) {
                bookings.set(i, updatedBooking);
                writeAll(bookings);
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(String bookingId) {
        List<Booking> bookings = findAll();
        boolean removed = bookings.removeIf(booking -> booking.getBookingId().equalsIgnoreCase(bookingId));
        if (removed) {
            writeAll(bookings);
        }
        return removed;
    }

    private void writeAll(List<Booking> bookings) {
        List<String> lines = bookings.stream()
                .map(this::toCsv)
                .toList();

        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write bookings", e);
        }
    }

    private String toCsv(Booking booking) {
        return String.join(",",
                sanitize(booking.getBookingId()),
                sanitize(booking.getCustomerName()),
                sanitize(booking.getPhone()),
                sanitize(booking.getCakeName()),
                sanitize(booking.getOrderDetails()),
                sanitize(booking.getOrderType()),
                String.valueOf(booking.getQuantity()),
                String.valueOf(booking.getBookingDate()),
                String.valueOf(booking.getDeliveryDate()),
                booking.getTotalPrice().toPlainString(),
                sanitize(booking.getStatus())
        );
    }

    private Booking fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length != 10 && parts.length != 11) {
            throw new IllegalStateException("Invalid booking line in file: " + csvLine);
        }

        if (parts.length == 11) {
            return new Booking(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim(),
                    Integer.parseInt(parts[6].trim()),
                    LocalDate.parse(parts[7].trim()),
                    LocalDate.parse(parts[8].trim()),
                    new BigDecimal(parts[9].trim()),
                    parts[10].trim()
            );
        }

        return new Booking(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                "",
                parts[4].trim(),
                Integer.parseInt(parts[5].trim()),
                LocalDate.parse(parts[6].trim()),
                LocalDate.parse(parts[7].trim()),
                new BigDecimal(parts[8].trim()),
                parts[9].trim()
        );
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replace(",", " ").trim();
    }
}
