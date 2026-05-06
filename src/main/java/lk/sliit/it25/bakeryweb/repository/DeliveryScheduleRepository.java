package lk.sliit.it25.bakeryweb.repository;

import lk.sliit.it25.bakeryweb.model.DeliverySlot;

import java.util.List;

public interface DeliveryScheduleRepository {
    List<DeliverySlot> findAll();

    void save(DeliverySlot slot);

    boolean deleteById(String id);
}
