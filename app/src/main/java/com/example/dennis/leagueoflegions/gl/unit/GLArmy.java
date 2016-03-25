package com.example.dennis.leagueoflegions.gl.unit;

import com.example.dennis.leagueoflegions.gl.GLUnit;
import com.example.dennis.leagueoflegions.gl.shape.AnimatedCircle;
import com.example.dennis.leagueoflegions.model.unit.Army;

public class GLArmy extends GLUnit {

    private AnimatedCircle animatedCircle;

    public GLArmy(Army army) {
        super(army);
        animatedCircle = new AnimatedCircle(army.getColor());
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);

        float[] mMVPMatrix = getMVPMatrix(mVPMatrix);
        animatedCircle.draw(mMVPMatrix);
    }

    @Override
    public void tick() {
        animatedCircle.tick();
    }
}
