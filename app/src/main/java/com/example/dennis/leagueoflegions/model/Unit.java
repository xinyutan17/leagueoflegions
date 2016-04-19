package com.example.dennis.leagueoflegions.model;

import android.graphics.Path;
import android.graphics.PathMeasure;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Unit extends GameObject {
    private static final String DEBUG_TAG = "Unit";

    public enum UnitType {BASE, SOLDIER, ARCHER}

    // Unit
    private Player player;  // the player that owns this unit
    private float[] color;  // this unit's color

    // Attributes
    // size = sizeMult * baseSize
    // scale = super.scale * size
    // health = healthMult * baseHealth * size
    // damage = damageMult * baseDamage * size
    // range = rangeMult * baseRange + scale
    // speed = speedMult * baseSpeed / size

    // Attribute base values
    private float baseSize;     // the unit's baseSize, i.e. number of individuals
    private float baseHealth;   // the baseHealth of each individual
    private float baseDamage;   // the baseDamage per second of each individual
    private float baseRange;    // the unit's baseRange
    private float baseSpeed;    // the unit's baseSpeed

    // Attribute multipliers (set by Terrain elements)
    private float sizeMult;
    private float healthMult;
    private float damageMult;
    private float rangeMult;
    private float speedMult;

    // Path
    private boolean pathing;    // whether or not user is pathing this unit
    private Path path;          // the unit's current path
    private PathMeasure pm;     // path measure handler
    private float pathDist;     // the unit's path distance traveled
    private Path remainingPath; // the unit's remaining path

    public Unit(Player player, float x, float y, float scale, float rotation)
    {
        super(player.getGame(), x, y, scale, rotation);

        // Unit
        this.player = player;
        this.color = player.getColor();

        // Attributes
        baseSize = 1f;
        baseSpeed = 1f;
        baseHealth = 1f;
        baseDamage = 1f;
        baseRange = 1f;

        // Modifiers
        sizeMult = 1f;
        speedMult = 1f;
        healthMult = 1f;
        damageMult = 1f;
        rangeMult = 1f;

        // Path
        pathing = false;
        path = new Path();
        pm = new PathMeasure();
        pathDist = 0f;
        remainingPath = new Path();
    }

    // Unit
    public abstract UnitType getUnitType();

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
    public void destroy() {
        super.destroy();
        player.removeUnit(this);
    }

    public float getBaseScale() {
        return super.getScale();
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
        return sizeMult * baseSize;
    }

    public float getBaseSize() {
        return baseSize;
    }

    public void setBaseSize(float size) {
        this.baseSize = size;
    }

    public float getSpeed() {
        return speedMult * baseSpeed / getSize();
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(float speed) {
        this.baseSpeed = speed;
    }

    public float getHealth() {
        return healthMult * baseHealth * getSize();
    }

    public float getBaseHealth() {
        return baseHealth;
    }

    public void setBaseHealth(float health) {
        this.baseHealth = health;
    }

    public float getDamage() {
        return damageMult * baseDamage * getSize();
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(float damage) {
        this.baseDamage = damage;
    }

    public float getRange() {
        return rangeMult * baseRange + getScale();
    }

    public float getBaseRange() {
        return baseRange;
    }

    public void setBaseRange(float range) {
        this.baseRange = range;
    }

    // Modifiers

    public float getSizeMult() {
        return sizeMult;
    }

    public void setSizeMult(float size_mult) {
        this.sizeMult = size_mult;
    }

    public float getSpeedMult() {
        return speedMult;
    }

    public void setSpeedMult(float speed_mult) {
        this.speedMult = speed_mult;
    }

    public float getHealthMult() {
        return healthMult;
    }

    public void setHealthMult(float health_mult) {
        this.healthMult = health_mult;
    }

    public float getDamageMult() {
        return damageMult;
    }

    public void setDamageMult(float damage_mult) {
        this.damageMult = damage_mult;
    }

    public float getRangeMult() {
        return rangeMult;
    }

    public void setRangeMult(float range_mult) {
        this.rangeMult = range_mult;
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

    public boolean hasPath() {
        return !path.isEmpty() && pm.getLength() > 0;
    }

    public void beginPathing(float x, float y) {
        pathing = true;
        path.reset();
        path.moveTo(getX(), getY());
        pm.setPath(path, false);

        updatePathing(x, y);
    }

    public void updatePathing(float x, float y) {
        float[] pathEnd = getPathEnd();
        path.quadTo((pathEnd[0] + x) / 2, (pathEnd[1] + y) / 2, x, y);
        pm.setPath(path, false);
    }

    public void endPathing(float x, float y) {
        updatePathing(x, y);
        pathing = false;
    }

    public void resetPathing() {
        pathing = false;
        path.reset();
        pm.setPath(null, false);
        pathDist = 0f;
        remainingPath.reset();
    }

    @Override
    public void tick(){
        if (isDestroyed()) {
            return;
        }

        Game game = getGame();
        ArrayList<Unit> units = game.getEnemyUnitsWithinRadius(getPlayer(), getX(), getY(), getRange());
        float[] randomLottery = randomLottery(units.size());
        for (int i = 0; i < units.size(); i++) {
            sendDamage(units.get(i), randomLottery[i] * game.getElapsedTime() * getDamage());
        }

        if (hasPath()) {
            pathDist += baseSpeed;
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
                resetPathing();
            }
        }

        ArrayList<Unit> friendlyUnits = game.getFriendlyUnitsWithinRadius(player, getX(), getY(), baseRange);
        for (Unit unit : friendlyUnits) {
            merge(unit);
        }
    }

    public void sendDamage(Unit unit, float damage) {
        float dir = (float) Math.atan2(unit.getY() - getY(), unit.getX() - getX());
        Projectile proj = new Projectile(getGame(), getPlayer(), getX(), getY(), dir, getRange(), damage);
//        unit.receiveDamage(damage);
    }

    public void receiveDamage(float damage) {
        baseSize -= damage / (healthMult * baseHealth);
        if (baseSize <= 0) {
            destroy();
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s at (%f, %f)", Arrays.toString(getColor()), getUnitType().toString(), getX(), getY());
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

    public void merge(Unit other) {
        if (isDestroyed() || other.isDestroyed()) {
            return;
        }
        if (other.equals(this)) {
            return;
        }
        if (!other.getPlayer().equals(player)) {
            return;
        }
        if (!other.getUnitType().equals(getUnitType())) {
            return;
        }
        if (hasPath() || other.hasPath()) {
            return;
        }

        float weight = getSize() / (getSize() + other.getSize());
        float otherWeight = other.getSize() / (getSize() + other.getSize());
        setX(weight*getX() + otherWeight*other.getX());
        setY(weight*getY() + otherWeight*other.getY());
        baseSize = baseSize + other.getSize();

        other.destroy();
    }
}
