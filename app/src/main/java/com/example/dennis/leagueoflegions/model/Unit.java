package com.example.dennis.leagueoflegions.model;

import android.graphics.Path;
import android.graphics.PathMeasure;

public abstract class Unit extends GameObject {
    public enum UnitType {BASE, ARMY}

    // Unit
    private Player player;  // the player that owns this unit
    private int color;      // this unit's color
    private float x, y;     // the (x, y) coordinate of this unit

    // Path
    private Path path;          // this unit's current path
    private PathMeasure pm;     // path handler
    private float pathDist;     // path distance traveled (0 to 1 representing fraction of path traveled)
    private Path remainingPath; // this unit's remaining path

    // Attributes
    private int size;       // this unit's size (i.e. how "powerful" it is)
    private float speed;    // the speed with which to follow the path

    // Modifiers
    // TODO

    public Unit(Player player, float x, float y)
    {
        // Unit
        this.player = player;
        this.color = player.getColor();
        this.x = x;
        this.y = y;

        // Path
        this.path = new Path();
        this.pm = new PathMeasure();
        this.pathDist = 0;
        this.remainingPath = new Path();

        // Attributes (must be set by subclass)
        this.size = 0;
        this.speed = 0.1f;

        // Modifiers
        // TODO
    }

    public void update(){
        if (!path.isEmpty()) {
            pathDist += speed;
            if (pathDist > pm.getLength()) {
                pathDist = pm.getLength();
            }
            float[] pathXY = new float[2];
            pm.getPosTan(pathDist, pathXY, null);
            float dx = (int)(pathXY[0] - x);
            float dy = (int)(pathXY[1] - y);
            x += dx;
            y += dy;

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

    // Unit
    public abstract UnitType getType();

    public Player getPlayer() {
        return player;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getX(){
        return (int) x;
    }

    public int getY(){
        return (int) y;
    }

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

    // Attributes
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
