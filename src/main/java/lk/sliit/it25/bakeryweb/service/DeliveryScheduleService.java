package lk.sliit.it25.bakeryweb.service;

import lk.sliit.it25.bakeryweb.model.DeliverySlot;
import lk.sliit.it25.bakeryweb.repository.DeliveryScheduleRepository;
import lk.sliit.it25.bakeryweb.repository.OrderBookingDateSyncRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryScheduleService {
    private static final String BOOKING_ID_PATTERN = "^B\\d{3,}$";
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
        if (!normalizedBookingId.matches(BOOKING_ID_PATTERN)) {
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
}
