package com.example.dennis.leagueoflegions.model;

import com.example.dennis.leagueoflegions.gl.GLUnit;

import java.util.ArrayList;

public class Player {

    private Game game;
    private float[] color;
    private ArrayList<Unit> units;
    private ArrayList<GLUnit> glUnits;

    public Player(Game game, float[] color)
    {
        this.game = game;
        this.color = color;
        units = new ArrayList<>();
        glUnits = new ArrayList<>();
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
