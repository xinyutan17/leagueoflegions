package com.example.dennis.leagueoflegions.model;

import com.example.dennis.leagueoflegions.gl.GLObject;

/**
 * Created by Dennis on 3/20/2016.
 */
public abstract class GameObject {
    private static final String DEBUG_TAG = "GameObject";

    private GLObject glObject;
    public GameObject() {
        glObject = null;
    }

    public GLObject getGLObject() {
        return glObject;
    }

    public void setGlObject(GLObject glObject) {
        this.glObject = glObject;
    }

    public void draw(float[] mVPMatrix) {
        glObject.draw(mVPMatrix);
    }

    public void tick() {
        glObject.tick();
    }

    public abstract float getX();
    public abstract float getY();
    public abstract float getSize();
    public abstract float[] getColor();
    public abstract void instantiateGLObject();
}
