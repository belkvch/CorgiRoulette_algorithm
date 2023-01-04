package org.example.entity;

import java.util.Objects;

public class Pair {
    private long chosenUserId;
    private long opponentUserId;

    public Pair() {
    }

    public Pair(long chosenUserId, long opponentUserId) {
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
        Pair pairUsers = (Pair) o;
        return chosenUserId == pairUsers.chosenUserId && opponentUserId == pairUsers.opponentUserId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chosenUserId, opponentUserId);
    }
}
