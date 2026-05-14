package lk.sliit.it25.bakeryweb.service;

import lk.sliit.it25.bakeryweb.model.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        cart.add(item);
    }

    public void removeFromCart(HttpSession session, String cakeName) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(i -> i.getCakeName().equals(cakeName));
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public int getCartCount(HttpSession session) {
        return getCart(session).size();
    }

    public BigDecimal getCartTotal(HttpSession session) {
        return getCart(session).stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateQuantity(HttpSession session, String cakeName, Integer quantity) {
        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (item.getCakeName().equals(cakeName)) {
                item.setQuantity(quantity);
                break;
            }
        }
    }
}
