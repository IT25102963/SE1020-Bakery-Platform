package lk.sliit.it25.bakeryweb.service;

import lk.sliit.it25.bakeryweb.model.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    public List<CartItem> getCart(HttpSession session) {
        Object raw = session.getAttribute(CART_SESSION_KEY);
        if (raw instanceof List<?>) {
            List<?> rawList = (List<?>) raw;
            List<CartItem> cart = new ArrayList<>();
            for (Object o : rawList) {
                if (o instanceof CartItem) {
                    cart.add((CartItem) o);
                }
            }
            cart = mergeDuplicateItems(cart);
            // ensure the session holds a typed list
            session.setAttribute(CART_SESSION_KEY, cart);
            return cart;
        } else {
            List<CartItem> cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
            return cart;
        }
    }

    public void addToCart(HttpSession session, CartItem item) {
        List<CartItem> cart = getCart(session);
        if (item == null || item.getCakeName() == null || item.getCakeName().isBlank()) {
            return;
        }

        int quantityToAdd = sanitizeQuantity(item.getQuantity());
        String cakeName = item.getCakeName().trim();
        for (CartItem existing : cart) {
            if (sameCake(existing.getCakeName(), cakeName)) {
                existing.setQuantity(sanitizeQuantity(existing.getQuantity()) + quantityToAdd);
                if (existing.getUnitPrice() == null) {
                    existing.setUnitPrice(item.getUnitPrice());
                }
                return;
            }
        }

        item.setCakeName(cakeName);
        item.setQuantity(quantityToAdd);
        cart.add(item);
    }

    public void removeFromCart(HttpSession session, String cakeName) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(i -> sameCake(i.getCakeName(), cakeName));
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public int getCartCount(HttpSession session) {
        return getCart(session).stream()
                .mapToInt(item -> sanitizeQuantity(item.getQuantity()))
                .sum();
    }

    public BigDecimal getCartTotal(HttpSession session) {
        return getCart(session).stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateQuantity(HttpSession session, String cakeName, Integer quantity) {
        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (sameCake(item.getCakeName(), cakeName)) {
                item.setQuantity(sanitizeQuantity(quantity));
                break;
            }
        }
    }

    private List<CartItem> mergeDuplicateItems(List<CartItem> cart) {
        Map<String, CartItem> merged = new LinkedHashMap<>();
        for (CartItem item : cart) {
            if (item == null || item.getCakeName() == null || item.getCakeName().isBlank()) {
                continue;
            }

            String key = normalizeCakeName(item.getCakeName());
            CartItem existing = merged.get(key);
            if (existing == null) {
                item.setCakeName(item.getCakeName().trim());
                item.setQuantity(sanitizeQuantity(item.getQuantity()));
                merged.put(key, item);
            } else {
                existing.setQuantity(sanitizeQuantity(existing.getQuantity()) + sanitizeQuantity(item.getQuantity()));
                if (existing.getUnitPrice() == null) {
                    existing.setUnitPrice(item.getUnitPrice());
                }
            }
        }
        return new ArrayList<>(merged.values());
    }

    private boolean sameCake(String left, String right) {
        return normalizeCakeName(left).equals(normalizeCakeName(right));
    }

    private String normalizeCakeName(String cakeName) {
        return cakeName == null ? "" : cakeName.trim().toLowerCase();
    }

    private int sanitizeQuantity(Integer quantity) {
        return quantity == null || quantity < 1 ? 1 : quantity;
    }
}
