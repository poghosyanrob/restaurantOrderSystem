package model;

import util.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Dish {

    private int id;
    private String name;
    private Category category;
    private double price;
    private boolean available = true;

    public Dish(String name, Category category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

}
