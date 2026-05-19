package lk.sliit.it25.bakeryweb.service;

import lk.sliit.it25.bakeryweb.model.DeliverySlot;
import lk.sliit.it25.bakeryweb.repository.DeliveryScheduleRepository;
import lk.sliit.it25.bakeryweb.repository.OrderBookingDateSyncRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryScheduleService {
    private static final String SCHEDULE_ID_PATTERN = "^[BC]\\d{3,}$";
    private final DeliveryScheduleRepository repository;
    private final OrderBookingDateSyncRepository orderBookingDateSyncRepository;

    public DeliveryScheduleService(DeliveryScheduleRepository repository,
                                   OrderBookingDateSyncRepository orderBookingDateSyncRepository) {
        this.repository = repository;
        this.orderBookingDateSyncRepository = orderBookingDateSyncRepository;
    }

    public List<DeliverySlot> getRoster() {
        return repository.findAll();
    }

    public Optional<DeliverySlot> getSlotById(String id) {
        Optional<DeliverySlot> stored = repository.findById(id);
        if (stored.isPresent()) {
            return stored;
        }
        return getDemoSlotById(id);
    }

    public boolean scheduleTime(String bookingId, String customerName, String address, String deliveryDate, String deliveryTime) {
        String normalizedBookingId = bookingId.trim().toUpperCase();
        if (!normalizedBookingId.matches(SCHEDULE_ID_PATTERN)) {
            return false;
        }
        if (repository.findById(normalizedBookingId).isPresent()) {
            return false;
        }
        DeliverySlot slot = new DeliverySlot(
                normalizedBookingId,
                customerName.trim(),
                address.trim(),
                deliveryDate,
                deliveryTime
        );
        repository.save(slot);
        return true;
    }

    public boolean updateSlot(String id, String customerName, String address, String deliveryDate, String deliveryTime) {
        DeliverySlot slot = new DeliverySlot(
                id,
                customerName.trim(),
                address.trim(),
                deliveryDate,
                deliveryTime
        );
        boolean updated = repository.update(slot);
        if (updated) {
            orderBookingDateSyncRepository.updateDeliveryDateByBookingId(id, deliveryDate);
        }
        return updated;
    }

    public boolean cancelSlot(String id) {
        return repository.deleteById(id);
    }

    private Optional<DeliverySlot> getDemoSlotById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        String normalized = id.trim().toUpperCase();
        return switch (normalized) {
            case "B104" -> Optional.of(new DeliverySlot("B104", "Nimal Perera", "12 Temple Road, Colombo 05", "2026-06-18", "10:30"));
            case "B105" -> Optional.of(new DeliverySlot("B105", "Shehan Silva", "44 Lake Drive, Kandy", "2026-06-19", "14:00"));
            case "B106" -> Optional.of(new DeliverySlot("B106", "Rashmi Fernando", "8 Beach Lane, Galle", "2026-06-20", "16:45"));
            case "C201" -> Optional.of(new DeliverySlot("C201", "Dinithi Jayasuriya", "21 Rose Avenue, Nugegoda", "2026-06-21", "11:15"));
            case "C202" -> Optional.of(new DeliverySlot("C202", "Kavindu Wijesinghe", "67 Palm Street, Matara", "2026-06-22", "13:30"));
            case "C203" -> Optional.of(new DeliverySlot("C203", "Hashini Dissanayake", "5 Station Road, Kurunegala", "2026-06-23", "15:00"));
            default -> Optional.empty();
        };
    }
}
