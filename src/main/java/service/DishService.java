package service;

import db.BDConnectionProvider;
import util.Category;
import model.Dish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DishService {

    private Connection connection = BDConnectionProvider.getInstace().getConnection();

    public void addDish(Dish dish) {
        String sql = "INSERT INTO dish (name,category,price,available) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, dish.getName());
            statement.setString(2, String.valueOf(dish.getCategory()));
            statement.setDouble(3, dish.getPrice());
            statement.setBoolean(4, dish.isAvailable());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                dish.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteDish(int id) {
        String sql = "DELETE FROM dish WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeDish(Dish dish) {
        String sql = "UPDATE dish SET  name = ?, category = ?,price = ?,available = ?  WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dish.getName());
            statement.setString(2, String.valueOf(dish.getCategory()));
            statement.setDouble(3, dish.getPrice());
            statement.setBoolean(4, dish.isAvailable());
            statement.setInt(5, dish.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Dish getDishById(int id) {
        String sql = "SELECT * FROM dish WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Dish dish = getDish(resultSet);
                return dish;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Dish> getAllDishes() {
        String sql = "SELECT * FROM dish";
        List<Dish> dishList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                dishList.add(getDish(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishList;

    }

    public List<Dish> getDishByCategory(Category category) {
        String sql = "SELECT * FROM dish WHERE category = ?";
        List<Dish> dishList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(category));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                dishList.add(getDish(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishList;
    }

    private static Dish getDish(ResultSet resultSet) throws SQLException {
        Dish dish = new Dish();
        dish.setId(resultSet.getInt("id"));
        dish.setName(resultSet.getString("name"));
        dish.setCategory(Category.valueOf(resultSet.getString("category")));
        dish.setPrice(resultSet.getDouble("price"));
        dish.isAvailable();
        return dish;
    }
}
