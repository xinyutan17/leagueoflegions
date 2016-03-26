package com.example.dennis.leagueoflegions.model;

import android.graphics.Path;
import android.graphics.PathMeasure;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Unit extends GameObject {
    public enum UnitType {BASE, ARMY}

    // Unit
    private Player player;  // the player that owns this unit
    private float[] color;  // this unit's color

    // Attributes
    private float size;     // the unit's size, i.e. number of individuals
    private float health;   // the health of each individual
    private float damage;   // the damage per second of each individual
    private float range;    // the unit's range
    private float speed;    // the unit's speed

    // Multipliers (set by Terrain elements)
    private float size_mult;
    private float health_mult;
    private float damage_mult;
    private float range_mult;
    private float speed_mult;

    // Path
    private boolean pathing;    // whether or not user is pathing this unit
    private Path path;          // the unit's current path
    private PathMeasure pm;     // path measure handler
    private float pathDist;     // the unit's path distance traveled
    private Path remainingPath; // the unit's remaining path

    public Unit(Player player, float x, float y, float scale, float rotation)
    {
        super(x, y, scale, rotation);

        // Unit
        this.player = player;
        this.color = player.getColor();

        // Attributes
        size = 1f;
        speed = 1f;
        health = 1f;
        damage = 1f;
        range = 1f;

        // Modifiers
        size_mult = 1f;
        speed_mult = 1f;
        health_mult = 1f;
        damage_mult = 1f;
        range_mult = 1f;

        // Path
        pathing = false;
        path = new Path();
        pm = new PathMeasure();
        pathDist = 0f;
        remainingPath = new Path();
    }

    // Unit
    public abstract UnitType getType();

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    @Override
    public float getScale() {
        return super.getScale() * getSize();
    }

    @Override
    public void setScale(float scale) {
        // do nothing
    }

    // Attributes
    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    // Modifiers

    public float getSizeMult() {
        return size_mult;
    }

    public void setSizeMult(float size_mult) {
        this.size_mult = size_mult;
    }

    public float getSpeedMult() {
        return speed_mult;
    }

    public void setSpeedMult(float speed_mult) {
        this.speed_mult = speed_mult;
    }

    public float getHealthMult() {
        return health_mult;
    }

    public void setHealthMult(float health_mult) {
        this.health_mult = health_mult;
    }

    public float getDamageMult() {
        return damage_mult;
    }

    public void setDamageMult(float damage_mult) {
        this.damage_mult = damage_mult;
    }

    public float getRangeMult() {
        return range_mult;
    }

    public void setRangeMult(float range_mult) {
        this.range_mult = range_mult;
    }

    // Path
    public Path getPath() {
        return path;
    }

    public Path getRemainingPath() {
        return remainingPath;
    }

    public float[] getPathEnd() {
        if (pm.getLength() == 0) {
            return new float[] {getX(), getY()};
        }
        float[] xy = new float[2];
        pm.getPosTan(pm.getLength(), xy, null);
        return xy;
    }

    public void beginPathing() {
        pathing = true;
        path.reset();
        path.moveTo(getX(), getY());
        pm.setPath(path, false);
    }

    public void updatePathing(float x, float y) {
        float[] pathEnd = getPathEnd();
        path.quadTo((pathEnd[0]+x)/2, (pathEnd[1]+y)/2, x, y);
        pm.setPath(path, false);
    }

    public void endPathing(float x, float y) {
        pathing = false;
        path.lineTo(x, y);
        pm.setPath(path, false);
    }

    @Override
    public void tick(){
        Game game = getPlayer().getGame();
        ArrayList<Unit> units = game.getEnemyUnitsWithinRadius(getPlayer(), getX(), getY(), range);
        float[] randomLottery = randomLottery(units.size());
        for (int i = 0; i < units.size(); i++) {
            sendDamage(units.get(i), randomLottery[i] * game.getElapsedTime() * getSize() * damage);
        }

        if (!path.isEmpty() && pm.getLength() > 0) {
            pathDist += speed;
            if (pathDist > pm.getLength()) {
                pathDist = pm.getLength();
            }
            float[] pathXY = new float[2];
            pm.getPosTan(pathDist, pathXY, null);
            setX(pathXY[0]);
            setY(pathXY[1]);

            remainingPath.reset();
            pm.getSegment(pathDist, pm.getLength(), remainingPath, true);

            if (pathDist == pm.getLength() && !pathing) {
                path.reset();
                pm.setPath(null, false);
                pathDist = 0f;
                remainingPath.reset();
            }
        }
    }

    public void sendDamage(Unit unit, float damage) {
        unit.receiveDamage(damage);
    }

    public void receiveDamage(float damage) {
        size -= damage / (health);
        if (size <= 0) {
            getPlayer().removeUnit(this);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s at (%f, %f)", Arrays.toString(getColor()), getType().toString(), getX(), getY());
    }

    private float[] randomLottery(int size) {
        float[] randomLottery = new float[size];
        float randomTotal = 0;
        for (int i = 0; i < size; i++) {
            float r = (float) Math.random();
            randomLottery[i] = r;
            randomTotal += r;
        }
        for (int i = 0; i < size; i++) {
            randomLottery[i] /= randomTotal;
        }
        return randomLottery;
    }
}
