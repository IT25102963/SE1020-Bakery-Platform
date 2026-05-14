package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.model.Booking;
import lk.sliit.it25.bakeryweb.model.CartItem;
import lk.sliit.it25.bakeryweb.service.BookingService;
import lk.sliit.it25.bakeryweb.service.CartService;
import jakarta.servlet.http.HttpSession;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CartService cartService;

    public BookingController(BookingService bookingService, CartService cartService) {
        this.bookingService = bookingService;
        this.cartService = cartService;
    }

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        return cartService.getCartCount(session);
    }

    @ModelAttribute("cartTotal")
    public BigDecimal cartTotal(HttpSession session) {
        return cartService.getCartTotal(session);
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
        model.addAttribute("readyCount", bookingService.getReadyForDeliveryCount());
        model.addAttribute("pendingCount", bookingService.getPendingCount());
        return "booking-list";
    }

    @GetMapping("/new")
    public String showCreateForm(
            @RequestParam(value = "cakeName", required = false) String cakeName,
            Model model) {
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

    @PostMapping("/place-order")
    public String saveBooking(
            @Valid @ModelAttribute("booking") Booking booking,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (bindingResult.hasErrors()) {
            booking.setBookingDate(LocalDate.now());
            booking.setDeliveryDate(LocalDate.now().plusDays(1));
            model.addAttribute("booking", booking);
            model.addAttribute("cartItems", cartService.getCart(session));
            return "cart";
        }

        // Process Checkout from Cart if it was a cart-based order
        List<CartItem> cart = cartService.getCart(session);
        if (!cart.isEmpty()) {
            for (CartItem item : cart) {
                Booking newBooking = new Booking();
                newBooking.setCustomerName(booking.getCustomerName());
                newBooking.setPhone(booking.getPhone());
                newBooking.setOrderDetails(booking.getOrderDetails());
                newBooking.setOrderType(booking.getOrderType());
                // inherit booking/delivery dates and status from the checkout form (use defaults when missing)
                newBooking.setBookingDate(booking.getBookingDate() != null ? booking.getBookingDate() : LocalDate.now());
                newBooking.setDeliveryDate(booking.getDeliveryDate() != null ? booking.getDeliveryDate() : LocalDate.now().plusDays(1));
                newBooking.setStatus(booking.getStatus() == null || booking.getStatus().isBlank() ? "Pending" : booking.getStatus());

                newBooking.setCakeName(item.getCakeName());
                newBooking.setQuantity(item.getQuantity());
                newBooking.setTotalPrice(item.getSubTotal());

                bookingService.createBooking(newBooking);
            }
            cartService.clearCart(session);
            redirectAttributes.addFlashAttribute("successMessage",
                    "All bookings from your cart have been placed successfully!");
        } else {
            // Fallback for single item booking - validate cake selection
            if (booking.getCakeName() == null || booking.getCakeName().isBlank()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No items to place order. Please select a product or add items to your cart.");
                return "redirect:/bookings/products";
            }
            // Ensure dates and total price are populated
            if (booking.getBookingDate() == null) booking.setBookingDate(LocalDate.now());
            if (booking.getDeliveryDate() == null) booking.setDeliveryDate(LocalDate.now().plusDays(1));
            if (booking.getTotalPrice() == null || booking.getTotalPrice().compareTo(BigDecimal.ZERO) == 0) {
                booking.setTotalPrice(bookingService.calculateTotalPrice(booking.getCakeName(), booking.getQuantity() == null ? 1 : booking.getQuantity()));
            }
            Booking saved = bookingService.createBooking(booking);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Booking created successfully with ID " + saved.getBookingId());
        }

        return "redirect:/bookings/my-orders";
    }

    @GetMapping("/place-order")
    public String redirectToCatalog() {
        return "redirect:/bookings/products";
    }

    // --- Cart Endpoints ---

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = cartService.getCart(session);
        model.addAttribute("cartItems", cart);
        
        Booking checkoutInfo = new Booking();
        checkoutInfo.setBookingDate(LocalDate.now());
        checkoutInfo.setDeliveryDate(LocalDate.now().plusDays(1));
        checkoutInfo.setOrderType("Standard");
        model.addAttribute("booking", checkoutInfo);
        
        return "cart";
    }

    @PostMapping("/cart/update")
    public String updateCartQuantity(
            @RequestParam("cakeName") String cakeName,
            @RequestParam("quantity") Integer quantity,
            HttpSession session
    ) {
        cartService.updateQuantity(session, cakeName, quantity);
        return "redirect:/bookings/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam("cakeName") String cakeName,
            @RequestParam("quantity") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        BigDecimal unitPrice = bookingService.getCakePrices().getOrDefault(cakeName, BigDecimal.ZERO);
        CartItem item = new CartItem(cakeName, quantity, unitPrice);
        cartService.addToCart(session, item);
        redirectAttributes.addFlashAttribute("successMessage", cakeName + " added to your bag!");
        return "redirect:/bookings/cart";
    }

    @GetMapping("/cart/remove/{cakeName}")
    public String removeFromCart(@PathVariable("cakeName") String cakeName, HttpSession session) {
        cartService.removeFromCart(session, cakeName);
        return "redirect:/bookings/cart";
    }

    @GetMapping("/receipt/{id}")
    public String showReceipt(@PathVariable("id") String bookingId, Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Booking> bookingOptional = bookingService.getBookingById(bookingId);
        if (bookingOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings/my-orders";
        }
        model.addAttribute("booking", bookingOptional.get());
        return "booking-receipt";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String bookingId, Model model,
            RedirectAttributes redirectAttributes) {
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
            RedirectAttributes redirectAttributes) {
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
    public String showStatusForm(@PathVariable("id") String bookingId, Model model,
            RedirectAttributes redirectAttributes) {
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
            RedirectAttributes redirectAttributes) {
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
