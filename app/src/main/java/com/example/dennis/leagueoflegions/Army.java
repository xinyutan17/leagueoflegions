package com.example.dennis.leagueoflegions;

public class Army extends Unit {
    private final static String type = "circle";
    
    public Army(int x, int y) {
        super(x, y);
    }

    public String getType()
    {
        return type;
    }
}
