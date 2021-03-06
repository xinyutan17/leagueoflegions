package com.example.dennis.leagueoflegions.model;

import com.example.dennis.leagueoflegions.model.unit.Unit;

import java.util.ArrayList;

public class Player {

    private Game game;
    private float[] color;
    private ArrayList<Unit> units;

    public Player(Game game, float[] color)
    {
        this.game = game;
        this.color = color;
        units = new ArrayList<>();
    }

    public Game getGame() {
        return game;
    }

    public float[] getColor() {
        return color;
    }

    public void addUnit(Unit unit)
    {
        unit.setColor(color);
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public ArrayList<Unit> getUnits()
    {
        return units;
    }
}
