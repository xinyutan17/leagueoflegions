package com.example.dennis.leagueoflegions;

public class Base extends Unit {
    private static final String TYPE = "rectangle";

    public static final float SPAWN_TIME = 3.0f;
    public static final int SPAWN_DISTANCE = 20;

    private float lastSpawnTime;

    public Base(Player player, int x, int y) {
        super(player, x, y);

        lastSpawnTime = player.getGame().getTime();
    }

    @Override
    public void update() {
        super.update();

        float updateTime = getPlayer().getGame().getTime();
        if (updateTime - lastSpawnTime > SPAWN_TIME) {
            Player selfPlayer = getPlayer();
            int spawnX = getX() + (int)(2*(Math.random()-0.5)*2*SPAWN_DISTANCE);
            int spawnY = getY() + (int)(2*(Math.random()-0.5)*2*SPAWN_DISTANCE);
            selfPlayer.addUnit(new Army(selfPlayer, spawnX, spawnY));
            lastSpawnTime = updateTime;
        }
    }

    public String getType()
    {
        return TYPE;
    }
}
