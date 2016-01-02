package com.example.dennis.leagueoflegions;

public class Base extends Unit {
    private static final String TYPE = "base";

    public Base(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType()
    {
        return TYPE;
    }
}
