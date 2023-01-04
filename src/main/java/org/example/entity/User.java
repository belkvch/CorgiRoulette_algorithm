package org.example.entity;

import java.util.Objects;

public class User {

    private long id;
    private String name;
    private String surname;
    private long teamId;
    private boolean isChosen;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(long id, String name, String surname, long teamId, boolean isChosen) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.teamId = teamId;
        this.isChosen = isChosen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && teamId == user.teamId && isChosen == user.isChosen && Objects.equals(name, user.name) && Objects.equals(surname, user.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, teamId, isChosen);
    }
}