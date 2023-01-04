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

        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS user (
                      `id` INT NOT NULL AUTO_INCREMENT,
                      `name` VARCHAR(45) NOT NULL,
                      `lastname` VARCHAR(45) NOT NULL,
                      `age` INT NULL,
                      PRIMARY KEY (`id`));""");
        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "Произошла ошибка создания таблицы...");
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS user");
        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "Не удалось удалить таблицу user");
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement pStatement = getConnection().prepareStatement(
                "INSERT INTO user (name, lastname, age) VALUES(?, ?, ?)")) {

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
        try (PreparedStatement pStatement = getConnection().prepareStatement(
                "DELETE FROM user WHERE id = ?")) {
            pStatement.setLong(1, id);
            pStatement.executeUpdate();
        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "При удалении пользователя произошла ошибка");
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT name, lastname, age FROM user");

            while (resultSet.next()) {
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
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate("DELETE FROM user");
        } catch (SQLException e) {
            Util.getLOGGER().log(Level.INFO, "При очистке произошла ошибка");
            e.printStackTrace();
        }
    }
}
