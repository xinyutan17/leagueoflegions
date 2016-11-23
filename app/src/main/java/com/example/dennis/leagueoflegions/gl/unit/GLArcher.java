package com.example.dennis.leagueoflegions.gl.unit;

import com.example.dennis.leagueoflegions.gl.shape.Triangle;
import com.example.dennis.leagueoflegions.model.unit.Archer;

public class GLArcher extends GLUnit {
    private Triangle bodyTriangle;

    public GLArcher(Archer archer) {
        super(archer);
        bodyTriangle = new Triangle(archer.getColor());
    }

    @Override
    public void tick() {
        super.tick();

        bodyTriangle.tick();
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);
        bodyTriangle.draw(getMVPMatrix());
    }
}
