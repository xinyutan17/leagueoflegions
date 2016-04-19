package com.example.dennis.leagueoflegions.model;

public abstract class GameObject {
    private static final String DEBUG_TAG = "GameObject";

    private Game game;
    private float x, y;
    private float scale;
    private float rotation;
    private boolean destroyed;

    public GameObject(Game game, float x, float y, float scale, float rotation) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.rotation = rotation;
        this.destroyed = false;
        game.addGameObjectAddQueue(this);
    }

    public Game getGame() {
        return game;
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

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
        game.addGameObjectRemoveQueue(this);
    }

    public abstract void tick();
}
