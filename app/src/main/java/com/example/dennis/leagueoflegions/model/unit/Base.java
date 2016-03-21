package com.example.dennis.leagueoflegions.model.unit;

import com.example.dennis.leagueoflegions.gl.unit.GLBase;
import com.example.dennis.leagueoflegions.model.Player;
import com.example.dennis.leagueoflegions.model.Unit;

public class Base extends Unit {
    private static final UnitType UNIT_TYPE = UnitType.BASE;
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_SPEED = 5;

    private static final float DEFAULT_SPAWN_RATE = 3.0f;

    private float spawnRate;
    private float lastSpawnTime;

    public Base(Player player, float x, float y) {
        super(player, x, y);

        setSize(DEFAULT_SIZE);
        setSpeed(DEFAULT_SPEED);

        spawnRate = DEFAULT_SPAWN_RATE;
        lastSpawnTime = player.getGame().getTime();
    }

    @Override
    public void tick() {
        super.tick();

        float gameTime = getPlayer().getGame().getTime();
        if (gameTime - lastSpawnTime > spawnRate) {
            Player selfPlayer = getPlayer();
            int xOffset = (int)((1+Math.random())*getSize());
            if (Math.random() < 0.5) {
                xOffset *= -1;
            }
            int yOffset = (int)((1+Math.random())*getSize());
            if (Math.random() < 0.5) {
                yOffset *= -1;
            }
            selfPlayer.addUnit(new Army(selfPlayer, getX() + xOffset, getY() + yOffset));
            lastSpawnTime = gameTime;
        }
    }

    @Override
    public UnitType getType()
    {
        return UNIT_TYPE;
    }

    @Override
    public void instantiateGLObject() {
        setGlObject(new GLBase(this));
    }
}
