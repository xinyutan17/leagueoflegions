package com.example.dennis.leagueoflegions.gl.unit;

import com.example.dennis.leagueoflegions.gl.shape.ShiftingCircle;
import com.example.dennis.leagueoflegions.model.unit.Soldier;

public class GLSoldier extends GLUnit {
    private ShiftingCircle bodyShiftingCircle;

    public GLSoldier(Soldier soldier) {
        super(soldier);
        bodyShiftingCircle = new ShiftingCircle(soldier.getColor());
    }

    @Override
    public void tick() {
        super.tick();

        bodyShiftingCircle.tick();
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);
        bodyShiftingCircle.draw(getMVPMatrix());
    }
}
