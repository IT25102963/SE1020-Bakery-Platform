package lk.sliit.it25.bakeryweb.service;

import lk.sliit.it25.bakeryweb.model.Booking;
import lk.sliit.it25.bakeryweb.repository.BookingFileRepository;
import lk.sliit.it25.bakeryweb.util.BookingIdGenerator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BookingService {

    private final BookingFileRepository repository;
    private final BookingIdGenerator idGenerator;

    public BookingService(BookingFileRepository repository, BookingIdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    public Booking createBooking(Booking booking) {
        booking.setBookingId(idGenerator.generateId());
        repository.save(booking);
        return booking;
    }

    public List<Booking> getAllBookings() {
        return repository.findAll();
    }

    public Optional<Booking> getBookingById(String id) {
        return repository.findById(id);
    }

    public boolean updateBooking(String id, Booking updated) {
        return repository.update(id, updated);
    }

    public boolean updateStatus(String id, String status) {
        return repository.updateStatus(id, status);
    }

    public boolean deleteBooking(String id) {
        return repository.delete(id);
    }

    public boolean cancelBooking(String id) {
        return repository.cancel(id);
    }

    public List<String> getStatuses() {
        return Arrays.asList("Pending", "Confirmed", "Cancelled");
    }

    public List<String> getOrderTypes() {
        return Arrays.asList("Standard", "Custom");
    }

    public Map<String, BigDecimal> getCakePrices() {
        Map<String, BigDecimal> prices = new LinkedHashMap<>();
        prices.put("Chocolate Truffle", new BigDecimal("4500.00"));
        prices.put("Vanilla Dream", new BigDecimal("4000.00"));
        prices.put("Red Velvet", new BigDecimal("4800.00"));
        return prices;
    }

    public BigDecimal calculateTotalPrice(String cakeName, Integer quantity) {
        BigDecimal unit = getCakePrices().getOrDefault(cakeName, BigDecimal.ZERO);
        return unit.multiply(new BigDecimal(quantity));
    }
}
