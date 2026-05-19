package lk.sliit.it25.bakeryweb.util;

import lk.sliit.it25.bakeryweb.model.Booking;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingIdGenerator {

    private final java.util.concurrent.atomic.AtomicInteger counter = new java.util.concurrent.atomic.AtomicInteger(1);

    public String generateId() {
        int next = counter.getAndIncrement();
        return String.format("B%03d", next);
    }

    public String generateFromExisting(List<Booking> existing) {
        int max = existing.stream().map(Booking::getBookingId)
                .filter(id -> id != null && id.startsWith("B"))
                .mapToInt(id -> Integer.parseInt(id.substring(1)))
                .max().orElse(0);
        return String.format("B%03d", max + 1);
    }
}
