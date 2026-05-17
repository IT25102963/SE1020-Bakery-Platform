
package lk.sliit.it25.bakeryweb.service;

import lk.sliit.it25.bakeryweb.model.Cake;
import lk.sliit.it25.bakeryweb.repository.CakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CakeService {

    @Autowired
    private CakeRepository cakeRepository;

    public List<Cake> getAllCakes() {
        return cakeRepository.findAll();
    }

    public Cake addCake(String name, String description, double price, String category) {
        String id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Cake cake = new Cake(id, name, description, price, category);
        cakeRepository.save(cake);
        return cake;
    }

    public boolean deleteCake(String id) {
        return cakeRepository.deleteById(id);
    }
}
