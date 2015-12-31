package com.example.dennis.leagueoflegions;

import java.util.ArrayList;

public class Player {

    private ArrayList<Unit> units;

    public Player()
    {
        units = new ArrayList<>();
    }

    public void addUnit(Unit unit)
    {
        units.add(unit);
    }

    public ArrayList<Unit> getUnits()
    {
        return units;
    }
}
