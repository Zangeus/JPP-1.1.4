package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {

    public void createUsersTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS user (
                  `id` INT NOT NULL AUTO_INCREMENT,
                  `name` VARCHAR(45) NOT NULL,
                  `lastname` VARCHAR(45) NOT NULL,
                  `age` INT NULL,
                  PRIMARY KEY (`id`));""";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "Произошла ошибка создания таблицы...");
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS user";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "Не удалось удалить таблицу user");
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO user (name, lastname, age) VALUES(?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(query)) {

            pStatement.setString(1, name);
            pStatement.setString(2, lastName);
            pStatement.setByte(3, age);

            pStatement.executeUpdate();

        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "При сохранении пользователя произошла ошибка");
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM user WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(query)) {
            pStatement.setLong(1, id);
            pStatement.executeUpdate();
        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "При удалении пользователя произошла ошибка");
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT name, lastname, age FROM user";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                User user = new User();
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));

                userList.add(user);
            }
        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "При получении списка пользователей произошла ошибка");
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        String query = "DELETE FROM user";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "При очистке произошла ошибка");
            e.printStackTrace();
        }
    }
}
