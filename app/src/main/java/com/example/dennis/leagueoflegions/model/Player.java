package com.example.dennis.leagueoflegions.model;

import java.util.ArrayList;

public class Player {

    private Game game;
    private ArrayList<Unit> units;
    private float[] color;

    public Player(Game game, float[] color)
    {
        this.game = game;
        units = new ArrayList<>();
        this.color = color;
    }

    public void addUnit(Unit unit)
    {
        unit.setColor(color);
        units.add(unit);
    }

    public ArrayList<Unit> getUnits()
    {
        return units;
    }

    public float[] getColor() {
        return color;
    }

    public Game getGame() {
        return game;
    }
}
