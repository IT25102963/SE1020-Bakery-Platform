package lk.sliit.it25.bakeryweb.service;

import lk.sliit.it25.bakeryweb.model.StandardCake;
import lk.sliit.it25.bakeryweb.repository.StandardCakeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class StandardCakeCatalogService {
    private final StandardCakeRepository repository;

    public StandardCakeCatalogService(StandardCakeRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public List<StandardCake> listCatalog() {
        return repository.findAll();
    }

    public void addCake(String name, String flavor, String size, BigDecimal price, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Cake name is required.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }

        StandardCake cake = new StandardCake(
                UUID.randomUUID().toString(),
                name.trim(),
                flavor == null ? "" : flavor.trim(),
                size == null ? "" : size.trim(),
                price,
                description == null ? "" : description.trim()
        );
        repository.add(cake);
    }

    public boolean deleteCake(String id) {
        return repository.deleteById(id);
    }
}

