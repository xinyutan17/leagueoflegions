package com.example.dennis.leagueoflegions.gl.unit;

import com.example.dennis.leagueoflegions.gl.GLUnit;
import com.example.dennis.leagueoflegions.gl.shape.Square;
import com.example.dennis.leagueoflegions.model.unit.Base;

public class GLBase extends GLUnit {

    private Square bodySquare;

    public GLBase(Base base) {
        super(base);
        bodySquare = new Square(base.getColor());
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);
        bodySquare.draw(getMVPMatrix());
    }
}
