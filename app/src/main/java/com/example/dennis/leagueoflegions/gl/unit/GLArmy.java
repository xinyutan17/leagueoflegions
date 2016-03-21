package com.example.dennis.leagueoflegions.gl.unit;

import com.example.dennis.leagueoflegions.gl.GLUnit;
import com.example.dennis.leagueoflegions.gl.shape.AnimatedCircle;
import com.example.dennis.leagueoflegions.gl.shape.Shape;
import com.example.dennis.leagueoflegions.model.GameObject;

public class GLArmy extends GLUnit {

    private Shape animatedCircle;

    public GLArmy(GameObject gameObject) {
        super(gameObject);
        animatedCircle = new AnimatedCircle(gameObject.getColor());
    }

    @Override
    public void draw(float[] mVPMatrix) {
        float[] mMVPMatrix = getMVPMatrix(mVPMatrix);
        animatedCircle.draw(mMVPMatrix);
    }
}
