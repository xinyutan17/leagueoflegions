package com.example.dennis.leagueoflegions;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;

public class Unit {
    public static final int UNIT_SIZE = 20;

    private int x, y;
    private int dx, dy;
    private int speed;
    private int color;

    private Rect rect;
    private Path path;
    private PathMeasure pm;
    private float pathDist; // 0 to 1, representing percentage of path traveled.

    public Unit(int x, int y)
    {
        color = Color.BLACK;
        speed = 5;
        this.x = x;
        this.y = y;
        dx = dy = 0;
        rect = new Rect(x - UNIT_SIZE, y + UNIT_SIZE, x + UNIT_SIZE, y - UNIT_SIZE);
        path = new Path();
        pm = new PathMeasure();
        pathDist = 0;
    }

    public void update(){
        if (!path.isEmpty()) {
            pathDist += speed;
            if (pathDist > pm.getLength()) {
                pathDist = pm.getLength();
            }
            float[] pathXY = new float[2];;
            pm.getPosTan(pathDist, pathXY, null);
            dx = (int)(pathXY[0] - x);
            dy = (int)(pathXY[1] - y);
            x += dx;
            y += dy;
            rect.offset(dx, dy);

            if (pathDist == pm.getLength()) {
                path.reset();
                pm.setPath(null, false);
            }
        }
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setPath(Path path) {
        this.path.set(path);
        pm.setPath(path, false);
        pathDist = 0;
    }

    public void updatePath(Path path) {
        this.path.set(path);
        pm.setPath(path, false);
    }

    public Path getPath() {
        return path;
    }

    public Rect getRect() {
        return rect;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
