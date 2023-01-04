package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsersWhereIsChosenFalse() {
        List<User> users = userRepository.getUsersWhereIsChosenFalse();
        log.info("Successfully found all users that haven't picked before");
        return users;
    }

    @Override
    public List<User> getUsersWhichWereOpponentsBefore(long userId) {
        List<User> users = userRepository.getUsersWhichWereOpponentsBefore(userId);
        log.info("Successfully found all users that haven't picked before as pair with user with id {}", userId);
        return users;
    }

    @Override
    public void updateStatusChosenUser(long userId) {
        userRepository.updateStatusChosenUser(userId);
        log.info("Successfully update status for user with id {}", userId);
    }

    @Override
    public void changeStatusForAllUsers() {
        userRepository.changeStatusForAllUsers();
        log.info("Successfully update status for all users");
    }
}
