package com.example.dennis.leagueoflegions;

public class Army extends Unit {
    private final static String TYPE = "army";

    public Army(Player player, int x, int y) {
        super(player, x, y);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public String getType()
    {
        return TYPE;
    }
}
