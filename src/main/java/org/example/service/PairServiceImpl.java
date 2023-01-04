package org.example.service;

import org.example.repository.PairRepository;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class PairServiceImpl implements PairService {
    private static final Logger log = LoggerFactory.getLogger(PairServiceImpl.class);

    private final PairRepository pairRepository;

    public PairServiceImpl(PairRepository pairRepository) {
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
}
