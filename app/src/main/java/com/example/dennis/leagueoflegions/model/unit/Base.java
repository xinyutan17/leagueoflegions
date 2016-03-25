package com.example.dennis.leagueoflegions.model.unit;

import com.example.dennis.leagueoflegions.model.Player;
import com.example.dennis.leagueoflegions.model.Unit;

public class Base extends Unit {
    private static final UnitType UNIT_TYPE = UnitType.BASE;
    private static final float DEFAULT_SCALE = 10f;
    private static final float DEFAULT_ROTATION = 0f;
    private static final float DEFAULT_SIZE = 10f;
    private static final float DEFAULT_SPEED = 5f;

    private static final float DEFAULT_SPAWN_RATE = 3.0f;

    private float spawnRate;
    private float lastSpawnTime;

    public Base(Player player, float x, float y) {
        super(player, x, y, DEFAULT_SCALE, DEFAULT_ROTATION, DEFAULT_SIZE, DEFAULT_SPEED);

        spawnRate = DEFAULT_SPAWN_RATE;
        lastSpawnTime = player.getGame().getTime();
    }

    @Override
    public void tick() {
        super.tick();

        float gameTime = getPlayer().getGame().getTime();
        if (gameTime - lastSpawnTime > spawnRate) {
            Player selfPlayer = getPlayer();
            double radius = 4 * (1 + Math.random()) * getSize();
            double angle = Math.random() * 2*Math.PI;
            float xOffset = (float)(radius * Math.cos(angle));
            float yOffset = (float)(radius * Math.sin(angle));
            selfPlayer.addUnit(new Army(selfPlayer, getX() + xOffset, getY() + yOffset));
            lastSpawnTime = gameTime;
        }
    }

    @Override
    public UnitType getType()
    {
        return UNIT_TYPE;
    }
}
