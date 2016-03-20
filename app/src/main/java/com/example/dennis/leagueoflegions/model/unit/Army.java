package com.example.dennis.leagueoflegions.model.unit;

import com.example.dennis.leagueoflegions.model.Player;
import com.example.dennis.leagueoflegions.model.Unit;

public class Army extends Unit {
    private static final UnitType UNIT_TYPE = UnitType.ARMY;
    private static final int DEFAULT_SIZE = 5;
    private static final int DEFAULT_SPEED = 5;

    public Army(Player player, int x, int y) {
        super(player, x, y);
        setSize(DEFAULT_SIZE);
        setSpeed(DEFAULT_SPEED);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public UnitType getType()
    {
        return UNIT_TYPE;
    }
}
