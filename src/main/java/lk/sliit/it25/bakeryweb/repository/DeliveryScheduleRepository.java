package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.DeliverySlot;

import java.util.List;
import java.util.Optional;

public interface DeliveryScheduleRepository {
    List<DeliverySlot> findAll();

    Optional<DeliverySlot> findById(String id);

    void save(DeliverySlot slot);

    boolean update(DeliverySlot slot);

    boolean deleteById(String id);
}
