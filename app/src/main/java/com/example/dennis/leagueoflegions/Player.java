package com.example.dennis.leagueoflegions;

import java.util.ArrayList;

public class Player {

    private ArrayList<Unit> units;
    private int color;

    public Player(int color)
    {
        units = new ArrayList<>();
        this.color = color;
    }

    public void addUnit(Unit unit)
    {
        units.add(unit);
    }

    public ArrayList<Unit> getUnits()
    {
        return units;
    }

    public int getColor() {
        return color;
    }
}
