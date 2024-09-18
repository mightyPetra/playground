package org.spend.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.spend.model.CartItem;

@Slf4j
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CartManager {

    private Map<String, Map<CartItem, Integer>> userCarts;

    private CartManager() {
        this.userCarts = new HashMap<>();
    }

    public Map<CartItem, Integer> getUserCart(String email) {
        if (!userCarts.containsKey(email)) {
            log.info("Creating user cart for user {}", email);
            userCarts.put(email, new HashMap<>());
        }
        return userCarts.get(email);
    }

    public void addToCart(String email, CartItem cartItem) {
        Map<CartItem, Integer> userCart = getUserCart(email);
        if (userCart.containsKey(cartItem)) {
            log.info("Item already exists in cart {} of user {}", cartItem.getName(), email);
            BiFunction<CartItem, Integer, Integer> increment = (key, value) -> value == null ? 1 : value + 1;
            userCart.compute(cartItem, increment);
            log.info("Item {} quantity updated to {}", cartItem.getName(), userCart.get(cartItem));
        } else {
            userCart.put(cartItem, 1);
            log.info("Item {} added to cart of user {}", cartItem.getName(), email);
        }
    }

}
