package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.customrequests.CustomRequest;
import lk.sliit.it25.bakeryweb.model.Booking;
import lk.sliit.it25.bakeryweb.model.DeliverySlot;
import lk.sliit.it25.bakeryweb.service.DeliveryScheduleService;
import lk.sliit.it25.bakeryweb.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/delivery")
public class DeliverySchedulerController {
    private final DeliveryScheduleService deliveryScheduleService;
    private final BookingService bookingService;

    public DeliverySchedulerController(DeliveryScheduleService deliveryScheduleService,
                                       BookingService bookingService) {
        this.deliveryScheduleService = deliveryScheduleService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String showScheduler(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String adminRedirect = requireAdminSession(session, redirectAttributes);
        if (adminRedirect != null) {
            return adminRedirect;
        }

        List<DeliverySlot> slots = deliveryScheduleService.getRoster();
        model.addAttribute("slots", slots);
        List<Booking> catalogOrders = bookingService.getAllBookings();
        List<CustomRequest> customRequests = readCustomRequestsFromFile();

        Map<String, DeliverySlot> deliverySlotMap = new HashMap<>();
        for (DeliverySlot slot : slots) {
            if (slot != null && slot.getId() != null && !slot.getId().isBlank()) {
                deliverySlotMap.put(slot.getId().trim().toUpperCase(), slot);
            }
        }

        Map<String, String> customSlotIdMap = new HashMap<>();
        for (CustomRequest customRequest : customRequests) {
            customSlotIdMap.put(customRequest.getRequestId(), toCustomSlotId(customRequest.getRequestId()));
        }

        model.addAttribute("catalogOrders", catalogOrders);
        model.addAttribute("customRequests", customRequests);
        model.addAttribute("deliverySlotMap", deliverySlotMap);
        model.addAttribute("customSlotIdMap", customSlotIdMap);
        return "delivery/scheduler";
    }

    @GetMapping("/details")
    public String showDetails(@RequestParam String slotId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String adminRedirect = requireAdminSession(session, redirectAttributes);
        if (adminRedirect != null) {
            return adminRedirect;
        }

        return deliveryScheduleService.getSlotById(slotId)
                .map(slot -> {
                    model.addAttribute("slot", slot);
                    return "delivery/slotDetails";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("message", "Slot not found.");
                    return "redirect:/delivery";
                });
    }

    @GetMapping("/edit")
    public String showEditPage(@RequestParam String slotId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String adminRedirect = requireAdminSession(session, redirectAttributes);
        if (adminRedirect != null) {
            return adminRedirect;
        }

        String normalizedSlotId = slotId == null ? "" : slotId.trim().toUpperCase();
        if (normalizedSlotId.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "Slot ID is required.");
            return "redirect:/delivery";
        }

        Optional<DeliverySlot> existingSlot = deliveryScheduleService.getSlotById(normalizedSlotId);
        if (existingSlot.isPresent()) {
            model.addAttribute("slot", existingSlot.get());
            return "delivery/updateSlot";
        }

        Optional<DeliverySlot> defaultSlot = buildDefaultSlotForId(normalizedSlotId);
        if (defaultSlot.isPresent()) {
            model.addAttribute("slot", defaultSlot.get());
            return "delivery/updateSlot";
        }

        redirectAttributes.addFlashAttribute("message", "Slot not found.");
        return "redirect:/delivery";
    }

    @PostMapping("/schedule")
    public String scheduleTime(@RequestParam String bookingId,
                               @RequestParam String customerName,
                               @RequestParam String address,
                               @RequestParam String deliveryDate,
                               @RequestParam String deliveryTime,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        String adminRedirect = requireAdminSession(session, redirectAttributes);
        if (adminRedirect != null) {
            return adminRedirect;
        }

        if (bookingId.isBlank() || customerName.isBlank() || address.isBlank() || deliveryDate.isBlank() || deliveryTime.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "All fields are required.");
            return "redirect:/delivery";
        }

        String normalizedBookingId = bookingId.trim().toUpperCase();
        if (!normalizedBookingId.matches("^[BC]\\d{3,}$")) {
            redirectAttributes.addFlashAttribute("message", "Schedule ID must be like B001 (booking) or C001 (custom cake).");
            return "redirect:/delivery";
        }

        boolean created = deliveryScheduleService.scheduleTime(bookingId, customerName, address, deliveryDate, deliveryTime);
        redirectAttributes.addFlashAttribute("message", created
                ? "Delivery slot scheduled."
                : "Booking ID already exists in delivery schedule.");
        return "redirect:/delivery";
    }

    @PostMapping("/update")
    public String updateSlot(@RequestParam String slotId,
                             @RequestParam String customerName,
                             @RequestParam String deliveryDate,
                             @RequestParam String deliveryTime,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String adminRedirect = requireAdminSession(session, redirectAttributes);
        if (adminRedirect != null) {
            return adminRedirect;
        }

        if (slotId.isBlank() || customerName.isBlank() || deliveryDate.isBlank() || deliveryTime.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "All fields are required.");
            return "redirect:/delivery";
        }

        String trustedAddress = resolveTrustedAddress(slotId, customerName);
        if (trustedAddress.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "Customer address not found. Ask customer to update profile address.");
            return "redirect:/delivery";
        }

        boolean updated = deliveryScheduleService.upsertSlot(slotId, customerName, trustedAddress, deliveryDate, deliveryTime);
        redirectAttributes.addFlashAttribute("message", updated ? "Delivery schedule saved." : "Unable to save delivery schedule.");
        return "redirect:/delivery";
    }

    @PostMapping("/cancel")
    public String cancelSlot(@RequestParam String slotId, HttpSession session, RedirectAttributes redirectAttributes) {
        String adminRedirect = requireAdminSession(session, redirectAttributes);
        if (adminRedirect != null) {
            return adminRedirect;
        }

        boolean removed = deliveryScheduleService.cancelSlot(slotId);
        redirectAttributes.addFlashAttribute("message", removed ? "Delivery slot cancelled." : "Slot not found.");
        return "redirect:/delivery";
    }

    private List<CustomRequest> readCustomRequestsFromFile() {
        List<CustomRequest> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/custom_requests.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] data = line.split(",", -1);
                if (data.length < 5) {
                    continue;
                }
                requests.add(new CustomRequest(data[0], data[1], data[2], data[3], data[4]));
            }
        } catch (IOException ignored) {
            // If the file doesn't exist yet, we keep the list empty.
        }
        return requests;
    }

    private Optional<DeliverySlot> buildDefaultSlotForId(String slotId) {
        Optional<Booking> booking = bookingService.getBookingById(slotId);
        if (booking.isPresent()) {
            Booking order = booking.get();
            String address = findCustomerAddress(order.getCustomerName(), order.getPhone());
            String deliveryDate = order.getDeliveryDate() == null ? "" : order.getDeliveryDate().toString();
            return Optional.of(new DeliverySlot(
                    slotId,
                    safeValue(order.getCustomerName()),
                    address,
                    deliveryDate,
                    ""
            ));
        }

        Optional<CustomRequest> customRequest = findCustomRequestBySlotId(slotId);
        if (customRequest.isPresent()) {
            CustomRequest request = customRequest.get();
            return Optional.of(new DeliverySlot(
                    slotId,
                    safeValue(request.getCustomerName()),
                    "",
                    "",
                    ""
            ));
        }

        return Optional.empty();
    }

    private Optional<CustomRequest> findCustomRequestBySlotId(String slotId) {
        String normalizedSlotId = safeValue(slotId).toUpperCase();
        return readCustomRequestsFromFile().stream()
                .filter(request -> {
                    String requestId = safeValue(request.getRequestId());
                    return requestId.equalsIgnoreCase(normalizedSlotId)
                            || toCustomSlotId(requestId).equalsIgnoreCase(normalizedSlotId);
                })
                .findFirst();
    }

    private String toCustomSlotId(String requestId) {
        String normalizedRequestId = safeValue(requestId).toUpperCase();
        if (normalizedRequestId.matches("^C\\d{3,}$")) {
            return normalizedRequestId;
        }
        String digits = normalizedRequestId.replaceAll("[^0-9]", "");
        if (digits.isBlank()) {
            digits = "000";
        }
        while (digits.length() < 3) {
            digits = "0" + digits;
        }
        return "C" + digits;
    }

    private String findCustomerAddress(String customerName, String phone) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 4) {
                    continue;
                }
                String storedName = safeValue(parts[0]);
                String storedPhone = safeValue(parts[2]);
                boolean sameName = storedName.equalsIgnoreCase(safeValue(customerName));
                boolean samePhone = !storedPhone.isBlank() && storedPhone.equals(safeValue(phone));
                if (sameName || samePhone) {
                    return safeValue(parts[3]);
                }
            }
        } catch (IOException ignored) {
            // Best effort: keep address empty if customer data cannot be read.
        }
        return "";
    }

    private String findCustomerAddressByName(String customerName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 4) {
                    continue;
                }
                String storedName = safeValue(parts[0]);
                if (storedName.equalsIgnoreCase(safeValue(customerName))) {
                    return safeValue(parts[3]);
                }
            }
        } catch (IOException ignored) {
            // Best effort.
        }
        return "";
    }

    private String resolveTrustedAddress(String slotId, String customerName) {
        String normalizedSlotId = safeValue(slotId).toUpperCase();

        Optional<Booking> booking = bookingService.getBookingById(normalizedSlotId);
        if (booking.isPresent()) {
            Booking order = booking.get();
            String bookingAddress = findCustomerAddress(order.getCustomerName(), order.getPhone());
            if (!bookingAddress.isBlank()) {
                return bookingAddress;
            }
        }

        Optional<CustomRequest> customRequest = findCustomRequestBySlotId(normalizedSlotId);
        if (customRequest.isPresent()) {
            String customAddress = findCustomerAddressByName(customRequest.get().getCustomerName());
            if (!customAddress.isBlank()) {
                return customAddress;
            }
        }

        String fallbackAddress = findCustomerAddressByName(customerName);
        if (!fallbackAddress.isBlank()) {
            return fallbackAddress;
        }

        Optional<DeliverySlot> existingSlot = deliveryScheduleService.getSlotById(normalizedSlotId);
        if (existingSlot.isPresent()) {
            String storedAddress = safeValue(existingSlot.get().getAddress());
            if (!storedAddress.isBlank()) {
                return storedAddress;
            }
        }

        return "";
    }

    private String requireAdminSession(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") != null) {
            return null;
        }
        redirectAttributes.addFlashAttribute("message", "Only admin can access delivery management.");
        if (session.getAttribute("user") != null) {
            return "redirect:/bookings/my-orders";
        }
        return "redirect:/admin/login";
    }

    private String safeValue(String value) {
        return value == null ? "" : value.trim();
    }
}
