package org.example.service;

import org.example.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsersWhereIsChosenFalse();

    List<User> getUsersWhichWereOpponentsBefore(long userId);

    void updateStatusChosenUser(long userId);

    void changeStatusForAllUsers();
}
