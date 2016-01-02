package com.example.dennis.leagueoflegions;

import android.graphics.Color;

import java.util.ArrayList;

public class Game {
    public final static String GAME_MODE = "game_mode";

    private ArrayList<Player> players;

    public Game(){
        players = new ArrayList<Player>();

        Player p1 = new Player(Color.BLUE);
        p1.addUnit(new Base(100, 100));
        p1.addUnit(new Army(100, 500));

        Player p2 = new Player(Color.RED);
        p2.addUnit(new Base(800, 100));
        p2.addUnit(new Army(800, 500));

        players.add(p1);
        players.add(p2);
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }
}
