package org.example;

import org.example.entity.Pair;
import org.example.entity.User;
import org.example.repository.PairRepositoryImpl;
import org.example.repository.UserRepositoryImpl;
import org.example.service.PairService;
import org.example.service.PairServiceImpl;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {
    private final UserService userService;
    private final PairService pairService;

    public Main(UserService userService, PairService pairService) {
        this.userService = userService;
        this.pairService = pairService;
    }

    public List<User> createListWithoutPicked() {
        return userService.getUsersWhereIsChosenFalse();
    }

    public List<User> checkForEmpty(List<User> users) {
        if (users.isEmpty()) {
            userService.changeStatusForAllUsers();
            users = userService.getUsersWhereIsChosenFalse();
        }
        return users;
    }

    public List<User> checkForOdd(List<User> users) {
        if (users.size() == 1) {
            System.out.println(users.get(0).getName() + " is lucky!");
            userService.updateStatusChosenUser(users.get(0).getId());
            System.exit(0);
        }
        return users;
    }

    public List<User> deleteTeammatesFromList(User user, List<User> users) {
        List<User> suitableOpponentsUsers = new ArrayList<>(users);
        suitableOpponentsUsers.removeIf(u -> u.getTeamId() == user.getTeamId());
        return suitableOpponentsUsers;
    }

    public List<User> deleteOpponentsBeforeFromList(User user, List<User> users) {
        List<User> opponentUsers = userService.getUsersWhichWereOpponentsBefore(user.getId());
        users.removeAll(opponentUsers);
        return users;
    }

    public List<User> checkCountOfOpponents(User user, List<User> usersOpponents, List<User> usersHaventPicked) {
        if (usersOpponents.isEmpty()) {
            pairService.deleteUserOpponent(user.getId());

            for (User u : usersHaventPicked) {
                if (u.getTeamId() != user.getTeamId()) {
                    usersOpponents.add(u);
                }
            }
        }
        return usersOpponents;
    }

    public Pair createPairOfOpponents(User chosenUser, User opponentForUser) {
        userService.updateStatusChosenUser(chosenUser.getId());
        userService.updateStatusChosenUser(opponentForUser.getId());

        pairService.createPairInBattleTable(chosenUser.getId(), opponentForUser.getId());
        return new Pair(chosenUser.getId(), opponentForUser.getId());
    }

    public static User getRandomElement(List<User> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }


    public static void main(String[] args) {
        Main main = new Main(new UserServiceImpl(new UserRepositoryImpl()), new PairServiceImpl(new PairRepositoryImpl()));

        List<User> originalUsersNotPicked = main.createListWithoutPicked();
        originalUsersNotPicked = main.checkForEmpty(originalUsersNotPicked);
        User userChosen = getRandomElement(originalUsersNotPicked);
        originalUsersNotPicked = main.checkForOdd(originalUsersNotPicked);

        List<User> suitableOpponentsUsers = main.deleteTeammatesFromList(userChosen, originalUsersNotPicked);
        suitableOpponentsUsers = main.deleteOpponentsBeforeFromList(userChosen, suitableOpponentsUsers);
        suitableOpponentsUsers = main.checkCountOfOpponents(userChosen, suitableOpponentsUsers, originalUsersNotPicked);
        User opponentUser = getRandomElement(suitableOpponentsUsers);

        Pair pair = main.createPairOfOpponents(userChosen, opponentUser);

        System.out.println("Our pair: " + pair.getChosenUserId() + " and " + pair.getOpponentUserId());
    }
}
