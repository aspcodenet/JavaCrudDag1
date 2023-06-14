package se.systementor.javacruddag1.models;

import java.util.UUID;



public class Player {
    private UUID id;
    private String name;

    public Player(UUID id) {
        this.id = id;
    }
    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
