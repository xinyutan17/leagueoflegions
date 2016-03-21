package com.example.dennis.leagueoflegions.model;

public abstract class GameObject {
    private static final String DEBUG_TAG = "GameObject";

    private float x, y;
    private float scale;
    private float rotation;

    public GameObject(float x, float y, float scale, float rotation) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.rotation = rotation;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public abstract void tick();
}
