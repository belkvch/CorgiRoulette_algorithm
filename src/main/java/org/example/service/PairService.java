package org.example.service;

import org.example.entity.Opponents;

public interface PairService {
    void createPairInBattleTable(long userId, long opponentId);

    void deleteUserOpponent(long userId);

    Opponents createOpponents();
}
