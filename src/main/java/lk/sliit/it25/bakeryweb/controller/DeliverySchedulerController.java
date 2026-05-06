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

    @PostMapping("/schedule")
    public String scheduleTime(@RequestParam String customerName,
                               @RequestParam String address,
                               @RequestParam String deliveryDate,
                               @RequestParam String deliveryTime,
                               RedirectAttributes redirectAttributes) {
        if (customerName.isBlank() || address.isBlank() || deliveryDate.isBlank() || deliveryTime.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "All fields are required.");
            return "redirect:/delivery";
        }

        deliveryScheduleService.scheduleTime(customerName, address, deliveryDate, deliveryTime);
        redirectAttributes.addFlashAttribute("message", "Delivery slot scheduled.");
        return "redirect:/delivery";
    }

    @PostMapping("/cancel")
    public String cancelSlot(@RequestParam String slotId, RedirectAttributes redirectAttributes) {
        boolean removed = deliveryScheduleService.cancelSlot(slotId);
        redirectAttributes.addFlashAttribute("message", removed ? "Delivery slot cancelled." : "Slot not found.");
        return "redirect:/delivery";
    }
}
