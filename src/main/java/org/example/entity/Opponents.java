package org.example.entity;

import java.util.Objects;

public class Opponents {
    private long chosenUserId;
    private long opponentUserId;

    public Opponents() {
    }

    public Opponents(long chosenUserId, long opponentUserId) {
        this.chosenUserId = chosenUserId;
        this.opponentUserId = opponentUserId;
    }

    public long getChosenUserId() {
        return chosenUserId;
    }

    public void setChosenUserId(long chosenUserId) {
        this.chosenUserId = chosenUserId;
    }

    public long getOpponentUserId() {
        return opponentUserId;
    }

    public void setOpponentUserId(long opponentUserId) {
        this.opponentUserId = opponentUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Opponents opponentsUsers = (Opponents) o;
        return chosenUserId == opponentsUsers.chosenUserId && opponentUserId == opponentsUsers.opponentUserId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chosenUserId, opponentUserId);
    }
}
