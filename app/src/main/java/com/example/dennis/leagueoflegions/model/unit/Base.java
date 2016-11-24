package com.example.dennis.leagueoflegions.model.unit;

import com.example.dennis.leagueoflegions.model.Player;

public class Base extends Unit {
    private static final UnitType UNIT_TYPE = UnitType.BASE;
    private static final float DEFAULT_SCALE = 10f;
    private static final float DEFAULT_ROTATION = 0f;

    private static final float DEFAULT_SIZE = 1f;
    private static final float DEFAULT_HEALTH = 10;
    private static final float DEFAULT_DAMAGE = 1f;
    private static final float DEFAULT_RANGE = 200f;
    private static final float DEFAULT_VISION = 1000f;
    private static final float DEFAULT_SPEED = 3f;

    private static final float DEFAULT_SPAWN_RATE = 3.0f;
    private static final float MIN_SPAWN_DIST = 4f;
    private static final float MAX_SPAWN_DIST = 8f;

    private float spawnRate;
    private float lastSpawnTime;

    public Base(Player player, float x, float y) {
        super(player, x, y, DEFAULT_SCALE, DEFAULT_ROTATION);
        setBaseSize(DEFAULT_SIZE);
        setBaseHealth(DEFAULT_HEALTH);
        setBaseDamage(DEFAULT_DAMAGE);
        setBaseRange(DEFAULT_RANGE);
        setBaseVision(DEFAULT_VISION);
        setBaseSpeed(DEFAULT_SPEED);

        spawnRate = DEFAULT_SPAWN_RATE;
        lastSpawnTime = player.getGame().getTime();
    }

    @Override
    public UnitType getUnitType()
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
            Unit unit;
            if (Math.random() < 0.5) {
                unit = new Soldier(selfPlayer, getX() + xOffset, getY() + yOffset);
            } else {
                unit = new Archer(selfPlayer, getX() + xOffset, getY() + yOffset);
            }
            selfPlayer.addUnit(unit);
            lastSpawnTime = gameTime;
        }
    }
}
