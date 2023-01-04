package org.example.service;

public interface PairService {
    void createPairInBattleTable(long userId, long opponentId);

    void deleteUserOpponent(long userId);
}
