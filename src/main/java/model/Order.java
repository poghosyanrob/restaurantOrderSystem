package model;


import util.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    private int id;
    private Customer customer;
    private String orderDate;
    private double totalPrice;
    private Status status;

    public Order(Customer customer) {
        this.customer = customer;
        this.orderDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.status = Status.PENDING;
    }


}
