package com.example.dennis.leagueoflegions.model.unit;

import com.example.dennis.leagueoflegions.model.Player;
import com.example.dennis.leagueoflegions.model.Unit;

public class Base extends Unit {
    private static final UnitType UNIT_TYPE = UnitType.BASE;
    private static final float DEFAULT_SCALE = 10f;
    private static final float DEFAULT_ROTATION = 0f;

    private static final float DEFAULT_SIZE = 1f;
    private static final float DEFAULT_HEALTH = 10;
    private static final float DEFAULT_DAMAGE = 1f;
    private static final float DEFAULT_RANGE = 20f;
    private static final float DEFAULT_SPEED = 3f;

    private static final float DEFAULT_SPAWN_RATE = 3.0f;
    private static final float MIN_SPAWN_DIST = 4f;
    private static final float MAX_SPAWN_DIST = 8f;

    private float spawnRate;
    private float lastSpawnTime;

    public Base(Player player, float x, float y) {
        super(player, x, y, DEFAULT_SCALE, DEFAULT_ROTATION);
        setSize(DEFAULT_SIZE);
        setHealth(DEFAULT_HEALTH);
        setDamage(DEFAULT_DAMAGE);
        setRange(DEFAULT_RANGE);
        setSpeed(DEFAULT_SPEED);

        spawnRate = DEFAULT_SPAWN_RATE;
        lastSpawnTime = player.getGame().getTime();
    }

    @Override
    public UnitType getType()
    {
        return UNIT_TYPE;
    }

    public float getSpawnRate() {
        return spawnRate;
    }

    public void setSpawnRate(float spawnRate) {
        this.spawnRate = spawnRate;
    }

    @Override
    public void tick() {
        super.tick();

        float gameTime = getPlayer().getGame().getTime();
        if (gameTime - lastSpawnTime > spawnRate) {
            Player selfPlayer = getPlayer();
            double radius = (MIN_SPAWN_DIST + Math.random()*(MAX_SPAWN_DIST - MIN_SPAWN_DIST)) * getScale() * getSize();
            double angle = Math.random() * 2*Math.PI;
            float xOffset = (float)(radius * Math.cos(angle));
            float yOffset = (float)(radius * Math.sin(angle));
            selfPlayer.addUnit(new Army(selfPlayer, getX() + xOffset, getY() + yOffset));
            lastSpawnTime = gameTime;
        }
    }
}
