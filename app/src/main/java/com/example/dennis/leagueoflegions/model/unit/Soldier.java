package com.example.dennis.leagueoflegions.model.unit;

import com.example.dennis.leagueoflegions.model.Player;
import com.example.dennis.leagueoflegions.model.Unit;

public class Soldier extends Unit {
    private static final UnitType UNIT_TYPE = UnitType.SOLDIER;
    private static final float DEFAULT_SCALE = 1f;
    private static final float DEFAULT_ROTATION = 0f;

    private static final float DEFAULT_SIZE = 5f;
    private static final float DEFAULT_HEALTH = 1f;
    private static final float DEFAULT_DAMAGE = 0.1f;
    private static final float DEFAULT_RANGE = 50f;
    private static final float DEFAULT_SPEED = 10f;

    public Soldier(Player player, float x, float y) {
        super(player, x, y, DEFAULT_SCALE, DEFAULT_ROTATION);
        setBaseSize(DEFAULT_SIZE);
        setBaseHealth(DEFAULT_HEALTH);
        setBaseDamage(DEFAULT_DAMAGE);
        setBaseRange(DEFAULT_RANGE);
        setBaseSpeed(DEFAULT_SPEED);
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


    public Soldier split(float x, float y) {
        Player player = getPlayer();
        Soldier soldier = new Soldier(player, x, y);
        player.addUnit(soldier);
        soldier.setBaseSize(getBaseSize() / 2);
        this.setBaseSize(getBaseSize() / 2);
        return soldier;
    }
}
