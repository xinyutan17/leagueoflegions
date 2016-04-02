package com.example.dennis.leagueoflegions.model.unit;

import com.example.dennis.leagueoflegions.model.Player;
import com.example.dennis.leagueoflegions.model.Unit;

public class Army extends Unit {
    private static final UnitType UNIT_TYPE = UnitType.ARMY;
    private static final float DEFAULT_SCALE = 1f;
    private static final float DEFAULT_ROTATION = 0f;

    private static final float DEFAULT_SIZE = 5f;
    private static final float DEFAULT_HEALTH = 1f;
    private static final float DEFAULT_DAMAGE = 0.1f;
    private static final float DEFAULT_RANGE = 30f;
    private static final float DEFAULT_SPEED = 10f;

    public Army(Player player, float  x, float y) {
        super(player, x, y, DEFAULT_SCALE, DEFAULT_ROTATION);
        setSize(DEFAULT_SIZE);
        setHealth(DEFAULT_HEALTH);
        setDamage(DEFAULT_DAMAGE);
        setRange(DEFAULT_RANGE);
        setSpeed(DEFAULT_SPEED);
    }

    @Override
    public UnitType getUnitType()
    {
        return UNIT_TYPE;
    }

    @Override
    public void tick() {
        super.tick();
    }
}
