package com.example.dennis.leagueoflegions.model;

/**
 * Created by Dennis on 3/20/2016.
 */
public abstract class GameObject {
    public abstract void tick();

    public abstract float getX();
    public abstract float getY();
    public abstract float getSize();
    public abstract float[] getColor();
}
