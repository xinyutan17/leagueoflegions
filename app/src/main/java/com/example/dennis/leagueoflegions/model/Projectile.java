package com.example.dennis.leagueoflegions.model;

import com.example.dennis.leagueoflegions.model.unit.Unit;

import java.util.ArrayList;

public class Projectile extends GameObject {
    private static final float DEFAULT_SCALE = 1.0f;
    private static final float DEFAULT_SPEED = 500f;
    private static final float DAMAGE_RADIUS = 3.0f;

    private Game game;
    private Player originPlayer;
    private float startX, startY;
    private float speed, dir, range;
    private float damage;

    public Projectile(Game game, Player originPlayer, float x, float y, float dir, float range, float damage) {
        super(game, x, y, DEFAULT_SCALE, (float)(dir*180f/Math.PI));
        this.game = game;
        this.originPlayer = originPlayer;
        this.startX = x;
        this.startY = y;
        this.speed = DEFAULT_SPEED;
        this.dir = dir;
        this.range = range;
        this.damage = damage;
    }

    @Override
    public void tick() {
        setX(getX() + (float)(speed*Math.cos(dir)*game.getElapsedTime()));
        setY(getY() + (float)(speed*Math.sin(dir)*game.getElapsedTime()));

        if (Math.sqrt(Math.pow(getX()-startX, 2) + Math.sqrt(Math.pow(getY()-startY, 2))) > range) {
            destroy();
            return;
        }

        ArrayList<Unit> enemyUnits = game.getEnemyUnitsWithinRadius(originPlayer, getX(), getY(), DAMAGE_RADIUS);
        Unit closestEnemy = game.getClosestUnit(getX(), getY(), enemyUnits);
        if (closestEnemy != null) {
            sendDamage(closestEnemy, damage);
        }
    }

    public void sendDamage(Unit unit, float damage) {
        unit.receiveDamage(damage);
        destroy();
    }

    public Player getOriginPlayer() {
        return originPlayer;
    }
}
