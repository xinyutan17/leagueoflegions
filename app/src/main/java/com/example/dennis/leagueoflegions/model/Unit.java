package com.example.dennis.leagueoflegions.model;

import android.graphics.Path;
import android.graphics.PathMeasure;

import java.util.Arrays;

public abstract class Unit extends GameObject {
    public enum UnitType {BASE, ARMY}

    // Unit
    private Player player;  // the player that owns this unit
    private float[] color;  // this unit's color
    private float size;     // this unit's size (i.e. how "powerful" it is)
    private float speed;    // the speed with which to follow the path

    // Modifiers
    // TODO

    // Path
    private Path path;          // this unit's current path
    private PathMeasure pm;     // path handler
    private float pathDist;     // path distance traveled (0 to 1 representing fraction of path traveled)
    private Path remainingPath; // this unit's remaining path

    public Unit(Player player, float x, float y, float scale, float rotation, float size, float speed)
    {
        super(x, y, scale, rotation);

        // Unit
        this.player = player;
        this.color = player.getColor();
        this.size = size;
        this.speed = speed;

        // Modifiers
        // TODO

        // Path
        this.path = new Path();
        this.pm = new PathMeasure();
        this.pathDist = 0f;
        this.remainingPath = new Path();
    }

    // Unit
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

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    @Override
    public float getScale() {
        return getSize();
    }

    @Override
    public void setScale(float scale) {
        // do nothing
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    // Modifiers
    // TODO

    // Path
    public Path getPath() {
        return path;
    }

    public Path getRemainingPath() {
        return remainingPath;
    }

    public void setPath(Path path) {
        this.path.set(path);
        pm.setPath(path, false);
        pathDist = 0f;
        remainingPath.set(path);
    }

    public void updatePath(Path path) {
        this.path.set(path);
        pm.setPath(path, false);
        // path is being lengthened:
        // don't change pathDist or remainingPath
    }

    @Override
    public void tick(){
        if (!path.isEmpty()) {
            pathDist += speed;
            if (pathDist > pm.getLength()) {
                pathDist = pm.getLength();
            }
            float[] pathXY = new float[2];
            pm.getPosTan(pathDist, pathXY, null);

            float x = getX();
            float y = getY();
            float dx = (int)(pathXY[0] - x);
            float dy = (int)(pathXY[1] - y);
            setX(x + dx);
            setY(y + dy);

            remainingPath.reset();
            pm.getSegment(pathDist, pm.getLength(), remainingPath, true);

            if (pathDist == pm.getLength()) {
                path.reset();
                remainingPath.reset();
                pm.setPath(null, false);
                pathDist = 0;
            }
        }
    }

    // Abstract
    public abstract UnitType getType();

    @Override
    public String toString() {
        return String.format("%s %s at (%f, %f)", Arrays.toString(getColor()), getType().toString(), getX(), getY());
    }
}
