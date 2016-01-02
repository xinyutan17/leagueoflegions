package com.example.dennis.leagueoflegions;

import android.graphics.Color;

import java.util.ArrayList;

public class Game {
    public final static String GAME_MODE = "game_mode";

    private long startTime; // milliseconds
    private float gameTime; // seconds
    private ArrayList<Player> players;

    public Game(){
        startTime = System.currentTimeMillis();
        gameTime = 0f;

        players = new ArrayList<Player>();

        Player p1 = new Player(this, Color.BLUE);
        p1.addUnit(new Base(p1, 200, 200));
        p1.addUnit(new Army(p1, 50, 50));

        Player p2 = new Player(this, Color.RED);
        p2.addUnit(new Base(p2, 100, 100));
        p2.addUnit(new Army(p2, 50, 50));

        players.add(p1);
        players.add(p2);
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public void updateTime() {
        gameTime = (System.currentTimeMillis() - startTime)/1000f;
    }

    public float getTime() {
        return gameTime;
    }
}
