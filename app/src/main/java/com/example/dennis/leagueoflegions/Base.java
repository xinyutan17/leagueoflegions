package com.example.dennis.leagueoflegions;

public class Base extends Unit {
    private static final String TYPE = "rectangle";

    public Base(int x, int y) {
        super(x, y);
    }

    public String getType()
    {
        return TYPE;
    }
}
