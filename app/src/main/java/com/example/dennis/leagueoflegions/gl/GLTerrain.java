package com.example.dennis.leagueoflegions.gl;

import com.example.dennis.leagueoflegions.model.Terrain;

public abstract class GLTerrain extends GLObject {
    public GLTerrain(Terrain terrain) {
        super(terrain);
    }

    @Override
    public void tick() {
        // do something
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);
        // do something
    }
}
