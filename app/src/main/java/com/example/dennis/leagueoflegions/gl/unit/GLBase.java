package com.example.dennis.leagueoflegions.gl.unit;

import com.example.dennis.leagueoflegions.gl.GLUnit;
import com.example.dennis.leagueoflegions.gl.shape.Square;
import com.example.dennis.leagueoflegions.model.unit.Base;

public class GLBase extends GLUnit {

    private Square square;

    public GLBase(Base base) {
        super(base);
        square = new Square(base.getColor());
    }

    @Override
    public void draw(float[] mVPMatrix) {
        float[] mMVPMatrix = getMVPMatrix(mVPMatrix);
        square.draw(mMVPMatrix);
    }

    @Override
    public void tick() {

    }
}
