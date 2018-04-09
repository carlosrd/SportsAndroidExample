package com.carlosrd.hsports.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SportPlayers {

    @SerializedName("title")
    private String name;

    @SerializedName("players")
    private ArrayList<Player> players;

    @SerializedName("type")
    private String type;

    public SportPlayers(String name, ArrayList<Player> players, String type) {
        this.name = name;
        this.players = players;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public String getType() {
        return type;
    }

}
