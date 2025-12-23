package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItem {
    private int id;
    private Order order;
    private Dish dish;
    private int quantity;
    private double price;

    public OrderItem(Order order, Dish dish, int quantity) {
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
        this.price = dish.getPrice();
    }
}
