package com.example.dennis.leagueoflegions.gl.unit;

import com.example.dennis.leagueoflegions.gl.GLUnit;
import com.example.dennis.leagueoflegions.gl.shape.AnimatedCircle;
import com.example.dennis.leagueoflegions.model.unit.Army;

public class GLArmy extends GLUnit {
    private static final float BODY_RADIUS = 1f;

    private AnimatedCircle bodyAnimatedCircle;

    public GLArmy(Army army) {
        super(army);
        bodyAnimatedCircle = new AnimatedCircle(army.getColor(), BODY_RADIUS);
    }

    @Override
    public void tick() {
        super.tick();

        bodyAnimatedCircle.tick();
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);

        float[] mMVPMatrix = getMVPMatrix(mVPMatrix);
        bodyAnimatedCircle.draw(mMVPMatrix);
    }
}
