package lk.sliit.it25.bakeryweb.controller;

import lk.sliit.it25.bakeryweb.service.DeliveryScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/delivery")
public class DeliverySchedulerController {
    private final DeliveryScheduleService deliveryScheduleService;

    public DeliverySchedulerController(DeliveryScheduleService deliveryScheduleService) {
        this.deliveryScheduleService = deliveryScheduleService;
    }

    @GetMapping
    public String showScheduler(Model model) {
        model.addAttribute("slots", deliveryScheduleService.getRoster());
        return "delivery/scheduler";
    }

    @GetMapping("/details")
    public String showDetails(@RequestParam String slotId, Model model, RedirectAttributes redirectAttributes) {
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
    public String showEditPage(@RequestParam String slotId, Model model, RedirectAttributes redirectAttributes) {
        return deliveryScheduleService.getSlotById(slotId)
                .map(slot -> {
                    model.addAttribute("slot", slot);
                    return "delivery/updateSlot";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("message", "Slot not found.");
                    return "redirect:/delivery";
                });
    }

    @PostMapping("/schedule")
    public String scheduleTime(@RequestParam String bookingId,
                               @RequestParam String customerName,
                               @RequestParam String address,
                               @RequestParam String deliveryDate,
                               @RequestParam String deliveryTime,
                               RedirectAttributes redirectAttributes) {
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
                             @RequestParam String address,
                             @RequestParam String deliveryDate,
                             @RequestParam String deliveryTime,
                             RedirectAttributes redirectAttributes) {
        if (slotId.isBlank() || customerName.isBlank() || address.isBlank() || deliveryDate.isBlank() || deliveryTime.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "All fields are required.");
            return "redirect:/delivery";
        }

        boolean updated = deliveryScheduleService.updateSlot(slotId, customerName, address, deliveryDate, deliveryTime);
        redirectAttributes.addFlashAttribute("message", updated ? "Delivery slot updated." : "Slot not found.");
        return "redirect:/delivery";
    }

    @PostMapping("/cancel")
    public String cancelSlot(@RequestParam String slotId, RedirectAttributes redirectAttributes) {
        boolean removed = deliveryScheduleService.cancelSlot(slotId);
        redirectAttributes.addFlashAttribute("message", removed ? "Delivery slot cancelled." : "Slot not found.");
        return "redirect:/delivery";
    }
}
