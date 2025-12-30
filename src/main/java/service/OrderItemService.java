package service;

import db.BDConnectionProvider;
import model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderItemService {
    private Connection connection = BDConnectionProvider.getInstace().getConnection();
    private OrderService orderService = new OrderService();
    private DishService dishService = new DishService();

    public void createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_item (order_id,dish_id,quantity,price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setInt(1, orderItem.getOrder().getId());
            statement.setInt(2, orderItem.getDish().getId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getPrice());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                orderItem.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrderItem getOrderItem(int id) {
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        OrderItem orderItem = new OrderItem();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                orderItem.setId(resultSet.getInt("id"));
                orderItem.setOrder(orderService.getOrderById(resultSet.getInt("order_id")));
                orderItem.setDish(dishService.getDishById(resultSet.getInt("dish_id")));
                orderItem.setQuantity(resultSet.getInt("quantity"));
                orderItem.setPrice(resultSet.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItem;
    }

}
