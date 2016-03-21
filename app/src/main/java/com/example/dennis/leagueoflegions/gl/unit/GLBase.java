package com.example.dennis.leagueoflegions.gl.unit;

import com.example.dennis.leagueoflegions.gl.GLUnit;
import com.example.dennis.leagueoflegions.gl.shape.Square;
import com.example.dennis.leagueoflegions.model.GameObject;

public class GLBase extends GLUnit {

    private Square square;

    public GLBase(GameObject gameObject) {
        super(gameObject);
        square = new Square(gameObject.getColor());
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
