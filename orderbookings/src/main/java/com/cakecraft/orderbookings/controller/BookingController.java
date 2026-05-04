package com.cakecraft.orderbookings.controller;

import com.cakecraft.orderbookings.model.Booking;
import com.cakecraft.orderbookings.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ModelAttribute("statusOptions")
    public java.util.List<String> statusOptions() {
        return bookingService.getStatuses();
    }

    @ModelAttribute("orderTypeOptions")
    public java.util.List<String> orderTypeOptions() {
        return bookingService.getOrderTypes();
    }

    @ModelAttribute("cakePrices")
    public Map<String, BigDecimal> cakePrices() {
        return bookingService.getCakePrices();
    }

    @GetMapping
    public String bookingsHome() {
        return "redirect:/bookings/products";
    }

    @GetMapping("/products")
    public String productList() {
        return "booking-products";
    }

    @GetMapping("/my-orders")
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "booking-list";
    }

    @GetMapping("/new")
    public String showCreateForm(
            @RequestParam(value = "cakeName", required = false) String cakeName,
            Model model
    ) {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());
        booking.setDeliveryDate(LocalDate.now().plusDays(1));
        booking.setStatus("Pending");
        booking.setTotalPrice(BigDecimal.ZERO);
        booking.setQuantity(1);
        if (cakeName != null && !cakeName.isBlank()) {
            booking.setCakeName(cakeName.trim());
            booking.setTotalPrice(bookingService.calculateTotalPrice(booking.getCakeName(), booking.getQuantity()));
        }
        model.addAttribute("booking", booking);
        return "booking-form";
    }

    @PostMapping("/save")
    public String saveBooking(
            @Valid @ModelAttribute("booking") Booking booking,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            booking.setBookingDate(LocalDate.now());
            booking.setDeliveryDate(LocalDate.now().plusDays(1));
            booking.setTotalPrice(bookingService.calculateTotalPrice(booking.getCakeName(), booking.getQuantity()));
            model.addAttribute("booking", booking);
            return "booking-form";
        }

        Booking saved = bookingService.createBooking(booking);
        redirectAttributes.addFlashAttribute("successMessage",
                "Booking created successfully with ID " + saved.getBookingId());
        return "redirect:/bookings/my-orders";
    }

    @GetMapping("/receipt/{id}")
    public String showReceipt(@PathVariable("id") String bookingId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Booking> bookingOptional = bookingService.getBookingById(bookingId);
        if (bookingOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings/my-orders";
        }
        model.addAttribute("booking", bookingOptional.get());
        return "booking-receipt";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String bookingId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Booking> bookingOptional = bookingService.getBookingById(bookingId);
        if (bookingOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings/my-orders";
        }

        model.addAttribute("booking", bookingOptional.get());
        return "booking-edit";
    }

    @PostMapping("/update/{id}")
    public String updateBooking(
            @PathVariable("id") String bookingId,
            @Valid @ModelAttribute("booking") Booking booking,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            booking.setBookingId(bookingId);
            bookingService.getBookingById(bookingId).ifPresent(existing -> {
                booking.setBookingDate(existing.getBookingDate());
                booking.setDeliveryDate(existing.getDeliveryDate());
                booking.setStatus(existing.getStatus());
                booking.setTotalPrice(bookingService.calculateTotalPrice(booking.getCakeName(), booking.getQuantity()));
            });
            model.addAttribute("booking", booking);
            return "booking-edit";
        }

        boolean updated = bookingService.updateBooking(bookingId, booking);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
        }
        return "redirect:/bookings/my-orders";
    }

    @GetMapping("/status/{id}")
    public String showStatusForm(@PathVariable("id") String bookingId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Booking> bookingOptional = bookingService.getBookingById(bookingId);
        if (bookingOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings/my-orders";
        }

        model.addAttribute("booking", bookingOptional.get());
        return "booking-status";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(
            @PathVariable("id") String bookingId,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes
    ) {
        boolean updated = bookingService.updateStatus(bookingId, status);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking status updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update booking status");
        }
        return "redirect:/bookings/my-orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") String bookingId, RedirectAttributes redirectAttributes) {
        boolean deleted = bookingService.deleteBooking(bookingId);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
        }
        return "redirect:/bookings/my-orders";
    }

    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable("id") String bookingId, RedirectAttributes redirectAttributes) {
        boolean cancelled = bookingService.cancelBooking(bookingId);
        if (cancelled) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
        }
        return "redirect:/bookings/my-orders";
    }
}
