package lk.sliit.it25.bakeryweb.repository;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderBookingDateSyncRepository {
    private static final Path BOOKINGS_FILE = Path.of("src/main/resources/data/bookings.txt");

    public boolean updateDeliveryDateByBookingId(String bookingId, String deliveryDate) {
        if (bookingId == null || bookingId.isBlank() || deliveryDate == null || deliveryDate.isBlank()) {
            return false;
        }
        if (!Files.exists(BOOKINGS_FILE)) {
            return false;
        }

        try {
            List<String> lines = Files.readAllLines(BOOKINGS_FILE, StandardCharsets.UTF_8);
            List<String> updated = new ArrayList<>(lines.size());
            boolean found = false;

            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    updated.add(line);
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 11) {
                    updated.add(line);
                    continue;
                }

                String id = parts[0] == null ? "" : parts[0].trim();
                if (!id.equals(bookingId)) {
                    updated.add(line);
                    continue;
                }

                parts[8] = deliveryDate;
                updated.add(String.join(",", parts));
                found = true;
            }

            if (!found) {
                return false;
            }
            Files.write(BOOKINGS_FILE, updated, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
