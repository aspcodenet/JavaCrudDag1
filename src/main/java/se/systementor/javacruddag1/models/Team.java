package se.systementor.javacruddag1.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.UUID;

public class Team {
    private UUID id;
    private String name;
    private int foundedYear;
    private String city;

    @JsonProperty("players")
    private ArrayList<Player> players = new ArrayList<>();



    public Team(){
    }
    public Team(UUID id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Iterable<Player> GetPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
