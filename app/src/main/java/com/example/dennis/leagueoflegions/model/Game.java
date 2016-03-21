package com.example.dennis.leagueoflegions.model;

import com.example.dennis.leagueoflegions.model.unit.Base;

import java.util.ArrayList;

public class Game {
    public static final String GAME_TYPE = "game_type";
    public enum GameType {CAMPAIGN, MULTIPLAYER, TUTORIAL}

    private Map map;
    private ArrayList<Player> players;

    private long startTime;     // start time (milliseconds)
    private float gameTime;     // game time (seconds)
    private float elapsedTime;  // elapsed time since last update (seconds)

    public Game(){
        players = new ArrayList<Player>();
        map = new Map();

        startTime = System.currentTimeMillis();
        gameTime = 0f;
        elapsedTime = 0f;
    }

    public void addPlayer(float[] color, float baseX, float baseY) {
        Player player = new Player(this, color);
        player.addUnit(new Base(player, baseX, baseY));
        players.add(player);
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public void updateTime() {
        float prevGameTime = gameTime;
        gameTime = (System.currentTimeMillis() - startTime)/1000f;
        elapsedTime = gameTime - prevGameTime;
    }

    public float getTime() {
        return gameTime;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void tick() {

    }
}
