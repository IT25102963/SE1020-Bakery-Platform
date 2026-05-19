package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.model.Booking;
import lk.sliit.it25.bakeryweb.model.CartItem;
import lk.sliit.it25.bakeryweb.model.Customer;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
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

    @ModelAttribute("orderTypeOptions")
    public java.util.List<String> orderTypeOptions() {
        return bookingService.getOrderTypes();
    }

    @ModelAttribute("statusOptions")
    public java.util.List<String> statusOptions() {
        return bookingService.getStatuses();
    }

    @ModelAttribute("cakePrices")
    public Map<String, BigDecimal> cakePrices() {
        return bookingService.getCakePrices();
    }

    @GetMapping
    public String bookingsHome(HttpSession session, RedirectAttributes redirectAttributes) {
        if (isAdminSession(session)) {
            return "redirect:/bookings/my-orders";
        }
        if (getLoggedInCustomer(session) == null) {
            return "redirect:/login";
        }
        return "redirect:/catalog";
    }

    @GetMapping("/products")
    public String productList(HttpSession session, RedirectAttributes redirectAttributes) {
        if (isAdminSession(session)) {
            return "redirect:/bookings/my-orders";
        }
        if (getLoggedInCustomer(session) == null) {
            return "redirect:/login";
        }
        return "redirect:/catalog";
    }

    @GetMapping("/new")
    public String newBooking(HttpSession session, RedirectAttributes redirectAttributes) {
        if (isAdminSession(session)) {
            return "redirect:/bookings/my-orders";
        }
        if (getLoggedInCustomer(session) == null) {
            return "redirect:/login";
        }
        return "redirect:/catalog";
    }

    @GetMapping("/my-orders")
    public String listBookings(Model model, HttpSession session) {
        List<Booking> bookings = resolveVisibleBookings(session);
        if (bookings == null) {
            return "redirect:/login";
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("readyCount", countReadyForDelivery(bookings));
        model.addAttribute("pendingCount", countPending(bookings));
        model.addAttribute("ordersVersion", buildOrdersVersion(bookings));
        return "booking-list";
    }

    @GetMapping("/my-orders/version")
    @ResponseBody
    public Map<String, Object> getMyOrdersVersion(HttpSession session) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Booking> bookings = resolveVisibleBookings(session);
        if (bookings == null) {
            response.put("authenticated", false);
            return response;
        }

        response.put("authenticated", true);
        response.put("version", buildOrdersVersion(bookings));
        response.put("count", bookings.size());
        return response;
    }

    @PostMapping("/place-order")
    public String saveBooking(
            @Valid @ModelAttribute("booking") Booking booking,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        String redirect = requireCustomerSession(session, redirectAttributes);
        if (redirect != null) {
            return redirect;
        }

        Customer loggedInUser = getLoggedInCustomer(session);
        if (loggedInUser != null) {
            booking.setCustomerName(firstNonBlank(booking.getCustomerName(), loggedInUser.getName()));
            booking.setPhone(firstNonBlank(booking.getPhone(), loggedInUser.getPhone()));
        }

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
                return "redirect:/catalog";
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
    public String redirectToCatalog(HttpSession session, RedirectAttributes redirectAttributes) {
        if (isAdminSession(session)) {
            return "redirect:/bookings/my-orders";
        }
        if (getLoggedInCustomer(session) == null) {
            return "redirect:/login";
        }
        return "redirect:/catalog";
    }

    // --- Cart Endpoints ---

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String redirect = requireCustomerSession(session, redirectAttributes);
        if (redirect != null) {
            return redirect;
        }

        List<CartItem> cart = cartService.getCart(session);
        model.addAttribute("cartItems", cart);

        Booking checkoutInfo = new Booking();
        Customer loggedInUser = getLoggedInCustomer(session);
        if (loggedInUser != null) {
            checkoutInfo.setCustomerName(loggedInUser.getName());
            checkoutInfo.setPhone(loggedInUser.getPhone());
        }
        checkoutInfo.setBookingDate(LocalDate.now());
        checkoutInfo.setDeliveryDate(LocalDate.now().plusDays(1));
        checkoutInfo.setOrderType("Standard");
        model.addAttribute("booking", checkoutInfo);

        return "cart";
    }

    @GetMapping("/checkout")
    public String viewCheckout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        return viewCart(session, model, redirectAttributes);
    }

    @PostMapping("/cart/update")
    public String updateCartQuantity(
            @RequestParam("cakeName") String cakeName,
            @RequestParam("quantity") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        String redirect = requireCustomerSession(session, redirectAttributes);
        if (redirect != null) {
            return redirect;
        }
        cartService.updateQuantity(session, cakeName, quantity);
        return "redirect:/bookings/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam("cakeName") String cakeName,
            @RequestParam("quantity") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String redirect = requireCustomerSession(session, redirectAttributes);
        if (redirect != null) {
            return redirect;
        }
        BigDecimal unitPrice = bookingService.findCakePrice(cakeName);
        CartItem item = new CartItem(cakeName, quantity, unitPrice);
        cartService.addToCart(session, item);
        redirectAttributes.addFlashAttribute("successMessage", cakeName + " added to your bag!");
        return "redirect:/catalog";
    }

    @PostMapping("/cart/add-from-catalog")
    public String addToCartFromCatalog(
            @RequestParam("cakeName") String cakeName,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            @RequestParam(value = "unitPrice", required = false) BigDecimal unitPrice,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String redirect = requireCustomerSession(session, redirectAttributes);
        if (redirect != null) {
            return redirect;
        }

        CartItem item = new CartItem(cakeName, quantity, resolveUnitPrice(cakeName, unitPrice));
        cartService.addToCart(session, item);
        redirectAttributes.addFlashAttribute("successMessage", cakeName + " added to your bag!");
        return "redirect:/catalog";
    }

    @PostMapping("/cart/buy-now")
    public String buyNowFromCatalog(
            @RequestParam("cakeName") String cakeName,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            @RequestParam(value = "unitPrice", required = false) BigDecimal unitPrice,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String redirect = requireCustomerSession(session, redirectAttributes);
        if (redirect != null) {
            return redirect;
        }

        CartItem item = new CartItem(cakeName, quantity, resolveUnitPrice(cakeName, unitPrice));
        cartService.addToCart(session, item);
        return "redirect:/bookings/checkout";
    }

    @PostMapping("/cart/add-ajax")
    @ResponseBody
    public Map<String, Object> addToCartAjax(
            @RequestParam("cakeName") String cakeName,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            @RequestParam(value = "unitPrice", required = false) BigDecimal unitPrice,
            HttpSession session) {
        Map<String, Object> response = new LinkedHashMap<>();

        if (isAdminSession(session)) {
            response.put("success", false);
            response.put("message", "Admin accounts cannot use customer cart.");
            response.put("redirectUrl", "/bookings/my-orders");
            return response;
        }

        if (getLoggedInCustomer(session) == null) {
            response.put("success", false);
            response.put("message", "Please login first.");
            response.put("redirectUrl", "/login");
            return response;
        }

        CartItem item = new CartItem(cakeName, quantity, resolveUnitPrice(cakeName, unitPrice));
        cartService.addToCart(session, item);

        response.put("success", true);
        response.put("cartCount", cartService.getCartCount(session));
        response.put("message", cakeName + " added to your bag!");
        return response;
    }

    @GetMapping("/cart/remove/{cakeName}")
    public String removeFromCart(@PathVariable("cakeName") String cakeName, HttpSession session,
            RedirectAttributes redirectAttributes) {
        String redirect = requireCustomerSession(session, redirectAttributes);
        if (redirect != null) {
            return redirect;
        }
        cartService.removeFromCart(session, cakeName);
        return "redirect:/bookings/cart";
    }

    @GetMapping("/receipt/{id}")
    public String showReceipt(@PathVariable("id") String bookingId, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Optional<Booking> bookingOptional = bookingService.getBookingById(bookingId);
        if (bookingOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings/my-orders";
        }
        Booking booking = bookingOptional.get();
        if (!canAccessBooking(booking, session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot access this booking.");
            return "redirect:/bookings/my-orders";
        }
        model.addAttribute("booking", booking);
        return "booking-receipt";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String bookingId, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Optional<Booking> bookingOptional = bookingService.getBookingById(bookingId);
        if (bookingOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings/my-orders";
        }
        if (!canAccessBooking(bookingOptional.get(), session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot edit this booking.");
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
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Optional<Booking> existingBookingOptional = bookingService.getBookingById(bookingId);
        if (existingBookingOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings/my-orders";
        }

        Booking existingBooking = existingBookingOptional.get();
        if (!canAccessBooking(existingBooking, session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot update this booking.");
            return "redirect:/bookings/my-orders";
        }

        if (bindingResult.hasErrors()) {
            booking.setBookingId(bookingId);
            booking.setBookingDate(existingBooking.getBookingDate());
            booking.setDeliveryDate(existingBooking.getDeliveryDate());
            booking.setStatus(existingBooking.getStatus());
            booking.setCustomerName(existingBooking.getCustomerName());
            booking.setPhone(existingBooking.getPhone());
            booking.setTotalPrice(bookingService.calculateTotalPrice(booking.getCakeName(), booking.getQuantity()));
            model.addAttribute("booking", booking);
            return "booking-edit";
        }

        booking.setCustomerName(existingBooking.getCustomerName());
        booking.setPhone(existingBooking.getPhone());
        boolean updated = bookingService.updateBooking(bookingId, booking);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
        }
        return "redirect:/bookings/my-orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") String bookingId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Optional<Booking> bookingOptional = bookingService.getBookingById(bookingId);
        if (bookingOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings/my-orders";
        }
        Booking booking = bookingOptional.get();

        if (isAdminSession(session)) {
            boolean deleted = bookingService.deleteBooking(bookingId);
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Booking deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            }
            return "redirect:/bookings/my-orders";
        }

        Customer user = getLoggedInCustomer(session);
        if (user == null || !isBookingOwner(booking, user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete this booking.");
            return "redirect:/bookings/my-orders";
        }

        String status = safeTrim(booking.getStatus()).toLowerCase();
        if ("pending".equals(status)) {
            boolean deleted = bookingService.deleteBooking(bookingId);
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Pending booking deleted successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            }
            return "redirect:/bookings/my-orders";
        }

        boolean requested = bookingService.updateStatus(bookingId, "Delete Requested");
        if (requested) {
            redirectAttributes.addFlashAttribute("successMessage", "Delete request sent to admin.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to send delete request.");
        }
        return "redirect:/bookings/my-orders";
    }

    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable("id") String bookingId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (!isAdminSession(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only admin can change order status.");
            return "redirect:/bookings/my-orders";
        }

        Optional<Booking> bookingOptional = bookingService.getBookingById(bookingId);
        if (bookingOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings/my-orders";
        }

        boolean cancelled = bookingService.cancelBooking(bookingId);
        if (cancelled) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found");
        }
        return "redirect:/bookings/my-orders";
    }

    @GetMapping("/status/{id}")
    public String showStatusForm(@PathVariable("id") String bookingId, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (!isAdminSession(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only admin can update order status.");
            return "redirect:/bookings/my-orders";
        }

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
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (!isAdminSession(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only admin can update order status.");
            return "redirect:/bookings/my-orders";
        }

        boolean updated = bookingService.updateStatus(bookingId, status);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid status or booking not found.");
        }
        return "redirect:/bookings/my-orders";
    }

    private String requireCustomerSession(HttpSession session, RedirectAttributes redirectAttributes) {
        if (getLoggedInCustomer(session) != null) {
            return null;
        }

        if (isAdminSession(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please use the orders dashboard for admin actions.");
            return "redirect:/bookings/my-orders";
        }

        return "redirect:/login";
    }

    private Customer getLoggedInCustomer(HttpSession session) {
        Object user = session.getAttribute("user");
        return user instanceof Customer ? (Customer) user : null;
    }

    private boolean isAdminSession(HttpSession session) {
        return session.getAttribute("admin") != null;
    }

    private boolean canAccessBooking(Booking booking, HttpSession session) {
        if (isAdminSession(session)) {
            return true;
        }

        Customer user = getLoggedInCustomer(session);
        return user != null && isBookingOwner(booking, user);
    }

    private boolean isBookingOwner(Booking booking, Customer user) {
        String userPhone = safeTrim(user.getPhone());
        String bookingPhone = safeTrim(booking.getPhone());
        if (!userPhone.isEmpty() && !bookingPhone.isEmpty()) {
            return userPhone.equals(bookingPhone);
        }

        String userName = safeTrim(user.getName());
        String bookingName = safeTrim(booking.getCustomerName());
        return !userName.isEmpty() && !bookingName.isEmpty() && userName.equalsIgnoreCase(bookingName);
    }

    private List<Booking> filterBookingsForCustomer(List<Booking> allBookings, Customer user) {
        return allBookings.stream()
                .filter(booking -> isBookingOwner(booking, user))
                .toList();
    }

    private List<Booking> resolveVisibleBookings(HttpSession session) {
        if (isAdminSession(session)) {
            return bookingService.getAllBookings();
        }

        Customer loggedInUser = getLoggedInCustomer(session);
        if (loggedInUser == null) {
            return null;
        }
        return filterBookingsForCustomer(bookingService.getAllBookings(), loggedInUser);
    }

    private String buildOrdersVersion(List<Booking> bookings) {
        StringBuilder key = new StringBuilder();
        for (Booking booking : bookings) {
            key.append(safeTrim(booking.getBookingId())).append('|')
                    .append(safeTrim(booking.getStatus())).append('|')
                    .append(booking.getQuantity() == null ? 0 : booking.getQuantity()).append('|')
                    .append(booking.getTotalPrice() == null ? BigDecimal.ZERO : booking.getTotalPrice()).append(';');
        }
        return Integer.toHexString(key.toString().hashCode());
    }

    private long countPending(List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> {
                    String status = safeTrim(booking.getStatus());
                    return status.equalsIgnoreCase("Pending");
                })
                .count();
    }

    private long countReadyForDelivery(List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> {
                    String status = safeTrim(booking.getStatus()).toLowerCase();
                    return status.equals("ready for delivery") || status.equals("ready") || status.equals("confirmed");
                })
                .count();
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private String firstNonBlank(String primary, String fallback) {
        String normalizedPrimary = safeTrim(primary);
        return normalizedPrimary.isEmpty() ? safeTrim(fallback) : normalizedPrimary;
    }

    private BigDecimal resolveUnitPrice(String cakeName, BigDecimal fallbackUnitPrice) {
        BigDecimal resolvedPrice = bookingService.findCakePrice(cakeName);
        if (resolvedPrice.compareTo(BigDecimal.ZERO) > 0) {
            return resolvedPrice;
        }
        if (fallbackUnitPrice != null && fallbackUnitPrice.compareTo(BigDecimal.ZERO) > 0) {
            return fallbackUnitPrice;
        }
        return BigDecimal.ZERO;
    }
}
