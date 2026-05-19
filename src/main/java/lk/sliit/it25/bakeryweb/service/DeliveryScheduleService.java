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
        return repository.findById(id);
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

    public boolean upsertSlot(String id, String customerName, String address, String deliveryDate, String deliveryTime) {
        String normalizedId = id == null ? "" : id.trim().toUpperCase();
        if (normalizedId.isBlank()) {
            return false;
        }

        if (repository.findById(normalizedId).isPresent()) {
            return updateSlot(normalizedId, customerName, address, deliveryDate, deliveryTime);
        }

        DeliverySlot slot = new DeliverySlot(
                normalizedId,
                customerName.trim(),
                address.trim(),
                deliveryDate,
                deliveryTime
        );
        repository.save(slot);
        orderBookingDateSyncRepository.updateDeliveryDateByBookingId(normalizedId, deliveryDate);
        return true;
    }

    public boolean cancelSlot(String id) {
        return repository.deleteById(id);
    }

}
