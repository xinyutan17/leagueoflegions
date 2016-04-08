package com.example.dennis.leagueoflegions.gl.unit;

import com.example.dennis.leagueoflegions.gl.GLUnit;
import com.example.dennis.leagueoflegions.gl.shape.AnimatedCircle;
import com.example.dennis.leagueoflegions.model.unit.Soldier;

public class GLSoldier extends GLUnit {
    private AnimatedCircle bodyAnimatedCircle;

    public GLSoldier(Soldier soldier) {
        super(soldier);
        bodyAnimatedCircle = new AnimatedCircle(soldier.getColor());
    }

    @Override
    public void tick() {
        super.tick();

        bodyAnimatedCircle.tick();
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);
        bodyAnimatedCircle.draw(getMVPMatrix());
    }
}
