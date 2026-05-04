package com.cakecraft.orderbookings.util;

import com.cakecraft.orderbookings.model.Booking;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.OptionalInt;

@Component
public class BookingIdGenerator {

    public String generateNextId(List<Booking> bookings) {
        OptionalInt maxId = bookings.stream()
                .map(Booking::getBookingId)
                .filter(id -> id != null && id.matches("B\\d{3}"))
                .mapToInt(id -> Integer.parseInt(id.substring(1)))
                .max();

        int next = maxId.orElse(0) + 1;
        return String.format("B%03d", next);
    }
}
