package com.cakecraft.orderbookings.service;

import com.cakecraft.orderbookings.model.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private static final String CART_SESSION_KEY = "shopping_cart";

    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, CartItem item) {
        List<CartItem> cart = getCart(session);
        
        // Check if item already exists
        for (CartItem existingItem : cart) {
            if (existingItem.getCakeName().equals(item.getCakeName())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        
        cart.add(item);
    }

    public void removeFromCart(HttpSession session, String cakeName) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getCakeName().equals(cakeName));
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void updateQuantity(HttpSession session, String cakeName, int quantity) {
        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (item.getCakeName().equals(cakeName)) {
                if (quantity <= 0) {
                    cart.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                break;
            }
        }
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.setAttribute(CART_SESSION_KEY, new ArrayList<CartItem>());
    }

    public BigDecimal getCartTotal(HttpSession session) {
        return getCart(session).stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCartCount(HttpSession session) {
        return getCart(session).stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
