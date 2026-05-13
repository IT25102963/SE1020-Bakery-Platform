package com.cakecraft.orderbookings.service;

import com.cakecraft.orderbookings.model.Booking;
import com.cakecraft.orderbookings.repository.BookingFileRepository;
import com.cakecraft.orderbookings.util.BookingIdGenerator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {

    private static final int DELIVERY_OFFSET_DAYS = 1;

    private static final List<String> STATUSES = Arrays.asList(
            "Pending", "Confirmed", "In Progress", "Ready", "Delivered", "Cancelled"
    );

    private static final List<String> ORDER_TYPES = Arrays.asList("Standard", "Custom");

    private static final Map<String, BigDecimal> CAKE_PRICES = new LinkedHashMap<>();

    static {
        CAKE_PRICES.put("Creamy Strawberry Cake", new BigDecimal("2200.00"));
        CAKE_PRICES.put("Classic Tiramisu Slice", new BigDecimal("1800.00"));
        CAKE_PRICES.put("Fruit Fiesta Tart", new BigDecimal("2500.00"));
        CAKE_PRICES.put("Raspberry Choco Tart", new BigDecimal("2700.00"));
    }

    private final BookingFileRepository bookingFileRepository;
    private final BookingIdGenerator bookingIdGenerator;

    public BookingService(BookingFileRepository bookingFileRepository, BookingIdGenerator bookingIdGenerator) {
        this.bookingFileRepository = bookingFileRepository;
        this.bookingIdGenerator = bookingIdGenerator;
    }

    public List<Booking> getAllBookings() {
        return bookingFileRepository.findAll();
    }

    public Optional<Booking> getBookingById(String bookingId) {
        return bookingFileRepository.findById(bookingId);
    }

    public Booking createBooking(Booking booking) {
        booking.setBookingId(bookingIdGenerator.generateNextId(bookingFileRepository.findAll()));
        booking.setBookingDate(LocalDate.now());
        booking.setDeliveryDate(booking.getBookingDate().plusDays(DELIVERY_OFFSET_DAYS));
        booking.setStatus("Pending");
        booking.setTotalPrice(calculateTotalPrice(booking.getCakeName(), booking.getQuantity()));
        return bookingFileRepository.save(booking);
    }

    public boolean updateBooking(String bookingId, Booking updatedBooking) {
        Optional<Booking> existingBooking = bookingFileRepository.findById(bookingId);
        if (existingBooking.isEmpty()) {
            return false;
        }

        Booking existing = existingBooking.get();
        updatedBooking.setBookingId(bookingId);
        updatedBooking.setBookingDate(existing.getBookingDate());
        updatedBooking.setDeliveryDate(existing.getBookingDate().plusDays(DELIVERY_OFFSET_DAYS));
        updatedBooking.setStatus(existing.getStatus());
        updatedBooking.setTotalPrice(calculateTotalPrice(updatedBooking.getCakeName(), updatedBooking.getQuantity()));
        return bookingFileRepository.update(updatedBooking);
    }

    public boolean updateStatus(String bookingId, String status) {
        if (!STATUSES.contains(status)) {
            return false;
        }

        Optional<Booking> existing = bookingFileRepository.findById(bookingId);
        if (existing.isEmpty()) {
            return false;
        }

        Booking booking = existing.get();
        booking.setStatus(status);
        return bookingFileRepository.update(booking);
    }

    public boolean cancelBooking(String bookingId) {
        return updateStatus(bookingId, "Cancelled");
    }

    public boolean deleteBooking(String bookingId) {
        return bookingFileRepository.deleteById(bookingId);
    }

    public List<String> getStatuses() {
        return STATUSES;
    }

    public List<String> getOrderTypes() {
        return ORDER_TYPES;
    }

    public Map<String, BigDecimal> getCakePrices() {
        return CAKE_PRICES;
    }

    public BigDecimal calculateTotalPrice(String cakeName, Integer quantity) {
        int safeQuantity = quantity == null ? 0 : Math.max(quantity, 0);
        BigDecimal unitPrice = CAKE_PRICES.getOrDefault(cakeName, BigDecimal.ZERO);
        return unitPrice.multiply(BigDecimal.valueOf(safeQuantity));
    }
}
