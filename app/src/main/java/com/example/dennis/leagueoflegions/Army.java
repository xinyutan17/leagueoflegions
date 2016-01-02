package com.example.dennis.leagueoflegions;

public class Army extends Unit {
    private final static String TYPE = "army";
    
    public Army(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType()
    {
        return TYPE;
    }
}
