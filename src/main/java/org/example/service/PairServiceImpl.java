package org.example.service;

import org.example.entity.Opponents;
import org.example.entity.User;
import org.example.repository.PairRepository;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PairServiceImpl implements PairService {
    private static final Logger log = LoggerFactory.getLogger(PairServiceImpl.class);
    private final UserService userService;
    private final PairRepository pairRepository;

    public PairServiceImpl(UserService userService, PairRepository pairRepository) {
        this.userService = userService;
        this.pairRepository = pairRepository;
    }

    @Override
    public void createPairInBattleTable(long userId, long opponentId) {
        pairRepository.createPairInBattleTable(userId, opponentId);
        log.info("Successfully created new pair with for user with id {}", userId);
        log.info("The opponent with id {}", opponentId);
    }

    @Override
    public void deleteUserOpponent(long userId) {
        pairRepository.deleteUserOpponent(userId);
        log.info("Successfully deleted pairs with id {}", userId);
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

    public List<User> deleteToChooseSuitableOpponents(User user, List<User> users) {
        List<User> suitableOpponentsUsers = new ArrayList<>(users);
        suitableOpponentsUsers.removeIf(newUser -> newUser.getTeamId() == user.getTeamId());

        List<User> opponentUsers = userService.getUsersWhichWereOpponentsBefore(user.getId());
        suitableOpponentsUsers.removeAll(opponentUsers);
        return suitableOpponentsUsers;
    }

    public List<User> checkCountOfOpponents(User user, List<User> usersOpponents, List<User> usersHaveNotPicked) {
        if (usersOpponents.isEmpty()) {
            deleteUserOpponent(user.getId());

            for (User newUser : usersHaveNotPicked) {
                if (newUser.getTeamId() != user.getTeamId()) {
                    usersOpponents.add(newUser);
                }
            }
        }
        return usersOpponents;
    }

    public Opponents createPairOfOpponents(User chosenUser, User opponentForUser) {
        userService.updateStatusChosenUser(chosenUser.getId());
        userService.updateStatusChosenUser(opponentForUser.getId());

        createPairInBattleTable(chosenUser.getId(), opponentForUser.getId());
        return new Opponents(chosenUser.getId(), opponentForUser.getId());
    }

    public static User getRandomElement(List<User> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    @Override
    public Opponents createOpponents() {
        List<User> originalUsersNotPicked = createListWithoutPicked();

        originalUsersNotPicked = checkForEmpty(originalUsersNotPicked);
        User userChosen = getRandomElement(originalUsersNotPicked);
        originalUsersNotPicked = checkForOdd(originalUsersNotPicked);

        List<User> suitableOpponentsUsers = deleteToChooseSuitableOpponents(userChosen, originalUsersNotPicked);
        suitableOpponentsUsers = checkCountOfOpponents(userChosen, suitableOpponentsUsers, originalUsersNotPicked);
        User opponentUser = getRandomElement(suitableOpponentsUsers);

        return createPairOfOpponents(userChosen, opponentUser);
    }
}
