package lk.sliit.it25.bakeryweb.service;

import lk.sliit.it25.bakeryweb.model.DeliverySlot;
import lk.sliit.it25.bakeryweb.repository.DeliveryScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DeliveryScheduleService {
    private final DeliveryScheduleRepository repository;

    public DeliveryScheduleService(DeliveryScheduleRepository repository) {
        this.repository = repository;
    }

    public List<DeliverySlot> getRoster() {
        return repository.findAll();
    }

    public void scheduleTime(String customerName, String address, String deliveryDate, String deliveryTime) {
        DeliverySlot slot = new DeliverySlot(
                generateId(),
                customerName.trim(),
                address.trim(),
                deliveryDate,
                deliveryTime
        );
        repository.save(slot);
    }

    public boolean cancelSlot(String id) {
        return repository.deleteById(id);
    }

    private String generateId() {
        return "DS-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
