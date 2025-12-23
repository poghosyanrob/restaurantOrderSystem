package service;

import db.BDConnectionProvider;
import util.Status;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private Connection connection = BDConnectionProvider.getInstace().getConnection();
    private CustomerService customerService = new CustomerService();

    public void createOrder(Order order) {
        String sql = "INSERT INTO `order`  (customer_id,order_date,total_price,status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setInt(1, order.getCustomer().getId());
            statement.setString(2, order.getOrderDate());
            statement.setDouble(3, order.getTotalPrice());
            statement.setString(4, String.valueOf(order.getStatus()));
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM  `order` ";
        List<Order> orderList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                orderList.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<Order> getCustomerOrders(int id) {
        String sql = "SELECT * FROM  `order`  WHERE customer_id = ?";
        List<Order> orderList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orderList.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }


    public Order getOrderById(int id) {
        String sql = "SELECT * FROM  `order`  WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getOrderFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void changeOrderStatus(Order order) {
        String sql = "UPDATE `order` SET status = ?  WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(order.getStatus()));
            statement.setInt(2, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setCustomer(customerService.getCustomerById(resultSet.getInt("customer_id")));
        order.setOrderDate(resultSet.getString("order_date"));
        order.setTotalPrice(resultSet.getDouble("total_price"));
        order.setStatus(Status.valueOf(resultSet.getString("status")));
        return order;
    }


    public void updateOrder(Order order) {
        String sql = "UPDATE `order` SET total_price = ?  WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, order.getTotalPrice());
            statement.setInt(2, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
