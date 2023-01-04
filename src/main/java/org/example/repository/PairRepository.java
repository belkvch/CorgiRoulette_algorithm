package org.example.repository;

public interface PairRepository {
    void createPairInBattleTable(long userId, long opponentId);

    void deleteUserOpponent(long userId);
}
