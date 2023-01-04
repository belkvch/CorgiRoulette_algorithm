package org.example.repository;

import org.example.connection.DataSource;
import org.example.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private static final String SELECT_USERS_WHERE_IS_CHOSEN_FALSE = "SELECT * FROM users WHERE isChosen = false";
    private static final String SELECT_USERS_OPPONENTS_BEFORE = "SELECT DISTINCT user_id, user_id, username, surname, team_id, ischosen FROM users JOIN users_opponents uo ON user_id = chosen_user_id OR user_id = opponent_user_id WHERE opponent_user_id = ? OR chosen_user_id = ?";
    private static final String CHANGE_STATUS_FOR_USERS_OPPONENTS = "UPDATE users SET ischosen = true WHERE user_id = ?";
    private static final String CHANGE_STATUS_FOR_ALL_USERS = "UPDATE users SET ischosen = false";


    @Override
    public List<User> getUsersWhereIsChosenFalse() {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USERS_WHERE_IS_CHOSEN_FALSE)) {
            ResultSet res = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (res.next()) {
                users.add(mapRowToUser(res));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUsersWhichWereOpponentsBefore(long userId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USERS_OPPONENTS_BEFORE)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            ResultSet res = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (res.next()) {
                users.add(mapRowToUser(res));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatusChosenUser(long userId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHANGE_STATUS_FOR_USERS_OPPONENTS)) {
            statement.setLong(1, userId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeStatusForAllUsers() {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHANGE_STATUS_FOR_ALL_USERS)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User mapRowToUser(ResultSet res) throws SQLException {
        int id = res.getInt("user_id");
        String name = res.getString("username");
        String surname = res.getString("surname");
        int teamId = res.getInt("team_id");
        boolean isChosen = res.getBoolean("isChosen");
        return new User(id, name, surname, teamId, isChosen);
    }
}
