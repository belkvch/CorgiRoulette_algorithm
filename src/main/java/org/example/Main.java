package org.example;

import org.example.entity.Opponents;
import org.example.repository.PairRepositoryImpl;
import org.example.repository.UserRepositoryImpl;
import org.example.service.PairService;
import org.example.service.PairServiceImpl;
import org.example.service.UserServiceImpl;


public class Main {

    private final PairService pairService;

    public Main(PairService pairService) {
        this.pairService = pairService;
    }

    public static void main(String[] args) {
        Main main = new Main(new PairServiceImpl(new UserServiceImpl(new UserRepositoryImpl()), new PairRepositoryImpl()));
        Opponents opponents = main.pairService.createOpponents();
        System.out.println("Opponents: " + opponents.getChosenUserId() + " and " + opponents.getOpponentUserId());
    }
}
