package com.example.dennis.leagueoflegions;

import java.util.ArrayList;

public class Game {
    public final static String GAME_MODE = "game_mode";

    private ArrayList<Player> players;

    public Game(){
        players = new ArrayList<Player>();

        Player p1 = new Player();
//        p1.addUnit(new Base(200, 200));

        Player p2 = new Player();
        p2.addUnit(new Base(100, 100));

        players.add(p1);
        players.add(p2);
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }
}
