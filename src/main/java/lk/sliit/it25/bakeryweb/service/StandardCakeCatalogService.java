package lk.sliit.it25.bakeryweb.service;

import lk.sliit.it25.bakeryweb.model.Cake;
import lk.sliit.it25.bakeryweb.repository.CatalogRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StandardCakeCatalogService {
    private final CatalogRepository repository;

    public StandardCakeCatalogService(CatalogRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public List<Cake> listCatalog() {
        return repository.findAll();
    }

    public Optional<Cake> findById(String id) {
        return repository.findById(id);
    }

    public void addCake(String name, String category, BigDecimal price, String description, String imageUrl) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Cake name is required.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }

        String normalizedCategory = (category == null || category.trim().isEmpty())
                ? "Standard Cakes"
                : category.trim();

        Cake cake = new Cake(
                repository.nextId(),
                name.trim(),
                description == null ? "" : description.trim(),
                price.doubleValue(),
                normalizedCategory,
                imageUrl == null ? "" : imageUrl.trim()
        );
        repository.save(cake);
    }

    public boolean deleteCake(String id) {
        return repository.deleteById(id);
    }

    public boolean updateCake(String id, String name, String category, BigDecimal price, String description, String imageUrl) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Cake id is required.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Cake name is required.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }

        String normalizedCategory = (category == null || category.trim().isEmpty())
                ? "Standard Cakes"
                : category.trim();

        Cake updatedCake = new Cake(
                id.trim(),
                name.trim(),
                description == null ? "" : description.trim(),
                price.doubleValue(),
                normalizedCategory,
                imageUrl == null ? "" : imageUrl.trim()
        );
        return repository.update(updatedCake);
    }
}
