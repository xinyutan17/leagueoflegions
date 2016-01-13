package com.example.dennis.leagueoflegions;

import android.graphics.Color;

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

        Player p1 = new Player(this, Color.BLUE);
        p1.addUnit(new Base(p1, 100, 100));
        players.add(p1);

        Player p2 = new Player(this, Color.RED);
        p2.addUnit(new Base(p2, 800, 100));
        players.add(p2);

        startTime = System.currentTimeMillis();
        gameTime = 0f;
        elapsedTime = 0f;
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
}
