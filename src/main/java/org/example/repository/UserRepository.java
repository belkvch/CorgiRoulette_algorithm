package org.example.repository;

import org.example.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsersWhereIsChosenFalse();

    List<User> getUsersWhichWereOpponentsBefore(long userId);

    void updateStatusChosenUser(long userId);

    void changeStatusForAllUsers();
}
