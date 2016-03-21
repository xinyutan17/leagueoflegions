package com.example.dennis.leagueoflegions.model.unit;

import com.example.dennis.leagueoflegions.model.Player;
import com.example.dennis.leagueoflegions.model.Unit;

public class Army extends Unit {
    private static final UnitType UNIT_TYPE = UnitType.ARMY;
    private static final float DEFAULT_SCALE = 5f;
    private static final float DEFAULT_ROTATION = 0f;
    private static final float DEFAULT_SIZE = 5f;
    private static final float DEFAULT_SPEED = 10f;

    public Army(Player player, float  x, float y) {
        super(player, x, y, DEFAULT_SCALE, DEFAULT_ROTATION, DEFAULT_SIZE, DEFAULT_SPEED);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public UnitType getType()
    {
        return UNIT_TYPE;
    }
}
