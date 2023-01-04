package org.example.repository;

import org.example.connection.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PairRepositoryImpl implements PairRepository {
    private static final String ADD_PAIR_OPPONENTS = "INSERT INTO users_opponents VALUES (?,?)";
    private static final String DELETE_USERS_OPPONENTS = "DELETE FROM users_opponents WHERE chosen_user_id = ? OR opponent_user_id = ?";

    @Override
    public void createPairInBattleTable(long userId, long opponentId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_PAIR_OPPONENTS)) {
            statement.setLong(1, userId);
            statement.setLong(2, opponentId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserOpponent(long userId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USERS_OPPONENTS)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
