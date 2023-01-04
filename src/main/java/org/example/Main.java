package org.example;

import org.example.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {
    private final static String url = "jdbc:postgresql://localhost/users";
    private final static String user = "belkvch";
    private final static String password = "12345";
    private static final String SELECT_USERS_WHERE_IS_CHOSEN_FALSE = "SELECT * FROM users WHERE isChosen = false";
    private static final String SELECT_USERS_OPPONENTS_BEFORE = "SELECT DISTINCT user_id, user_id, username, surname, team_id, ischosen FROM users JOIN users_opponents uo ON user_id = chosen_user_id OR user_id = opponent_user_id WHERE opponent_user_id = ? OR chosen_user_id = ?";
    private static final String DELETE_USERS_OPPONENTS = "DELETE FROM users_opponents WHERE chosen_user_id = ? OR opponent_user_id = ?";
    private static final String CHANGE_STATUS_FOR_USERS_OPPONENTS = "UPDATE users SET ischosen = true WHERE user_id = ?";
    private static final String ADD_PAIR_OPPONENTS = "INSERT INTO users_opponents VALUES (?,?)";
    private static final String CHANGE_STATUS_FOR_ALL_USERS = "UPDATE users SET ischosen = false";


    public static void main(String[] args) {
        Main example = new Main();

        //create List<User> for finding first user which hasn't chosen
        List<User> originalUsers = example.getUsersWhereIsChosenFalse();

        //if all users have chosen, change their status
        if (originalUsers.isEmpty()) {
            example.changeStatusForAllUsers();
            originalUsers = example.getUsersWhereIsChosenFalse();
        }

        //find random user in originalUsers
        User chosenUser = example.getRandomElement(originalUsers);
        System.out.println("The first user is " + chosenUser.getName());

        //for an odd number of users, if we have only one hasn't chosen user
        if (originalUsers.size() == 1) {
            System.out.println(chosenUser.getName() + " is lucky!");
            example.updateStatusChosenUser(chosenUser.getId());
            System.exit(0);
        }

        //create List<User> suitableOpponentsUsers for finding second user which hasn't chosen
        // It'll be opponent for first user
        List<User> suitableOpponentsUsers = new ArrayList<>(originalUsers);
        //create List<User> where users which are teammates for first user
        suitableOpponentsUsers.removeIf(u -> u.getTeamId() == chosenUser.getTeamId());
        System.out.println("Without teammates:");
        for (User u : suitableOpponentsUsers) {
            System.out.println(u.getName());
        }
        //create List<User> where users which were opponents before for first user and the user
        List<User> opponentUsers = example.getUsersWhichWereOpponentsBefore(chosenUser.getId());
        suitableOpponentsUsers.removeAll(opponentUsers);
        System.out.println("Final list for potential opponents:");
        for (User u : suitableOpponentsUsers) {
            System.out.println(u.getName());
        }

        //condition for check for the number of opponents
        if (suitableOpponentsUsers.isEmpty()) {
            System.out.println("There are no candidates for battle");
            //delete all pair with this user in battle table
            example.deleteUserOpponent(chosenUser.getId());
            //add all users without first user and teammates in suitableOpponentsUsers
            System.out.println("Add candidates for battle from other teams");
            for (User u : originalUsers) {
                if (u.getTeamId() != chosenUser.getTeamId()) {
                    suitableOpponentsUsers.add(u);
                    System.out.println(u.getName());
                }
            }
        }

        //find random user/opponent in opponentUsers
        User opponentForUser = example.getRandomElement(suitableOpponentsUsers);
        System.out.println("The second user is " + opponentForUser.getName());

        //change isChosen for users
        example.updateStatusChosenUser(chosenUser.getId());
        example.updateStatusChosenUser(opponentForUser.getId());

        //add pair in battle table
        example.createPairInBattleTable(chosenUser.getId(), opponentForUser.getId());

        //output for pair
        System.out.println("Our pair is " + chosenUser.getName() + " and " + opponentForUser.getName());
    }


    public List<User> getUsersWhereIsChosenFalse() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
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

    public List<User> getUsersWhichWereOpponentsBefore(long userId) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
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

    public void deleteUserOpponent(long userId) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(DELETE_USERS_OPPONENTS)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatusChosenUser(long userId) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(CHANGE_STATUS_FOR_USERS_OPPONENTS)) {
            statement.setLong(1, userId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPairInBattleTable(long userId, long opponentId) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(ADD_PAIR_OPPONENTS)) {
            statement.setLong(1, userId);
            statement.setLong(2, opponentId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeStatusForAllUsers() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(CHANGE_STATUS_FOR_ALL_USERS)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getRandomElement(List<User> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
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
