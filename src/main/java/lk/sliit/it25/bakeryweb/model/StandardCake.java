package lk.sliit.it25.bakeryweb.model;

import java.math.BigDecimal;
import java.util.Objects;

public class StandardCake {
    private final String id;
    private final String name;
    private final String flavor;
    private final String size;
    private final BigDecimal price;
    private final String description;

    public StandardCake(String id, String name, String flavor, String size, BigDecimal price, String description) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.flavor = flavor == null ? "" : flavor;
        this.size = size == null ? "" : size;
        this.price = Objects.requireNonNull(price, "price");
        this.description = description == null ? "" : description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFlavor() {
        return flavor;
    }

    public String getSize() {
        return size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}

