package com.example.dennis.leagueoflegions.gl;

import android.graphics.Path;
import android.graphics.PathMeasure;

import com.example.dennis.leagueoflegions.gl.shape.Line;
import com.example.dennis.leagueoflegions.model.Unit;

public abstract class GLUnit extends GLObject {
    private Line line;

    public GLUnit(Unit unit) {
        super(unit);
        line = new Line(unit.getColor());
    }

    @Override
    public void draw(float[] mVPMatrix) {
        Unit unit = (Unit) getGameObject();
        Path path = unit.getRemainingPath();
        PathMeasure pm = new PathMeasure(path, false);
        if (path.isEmpty()) {
            return;
        }

        int numVertices = (int) pm.getLength();
        float[] vertices = new float[3 * numVertices];
        short[] drawOrder = new short[numVertices];
        float[] pathXY = new float[2];
        for (int i = 0; i < numVertices; i++) {
            pm.getPosTan(i, pathXY, null);
            vertices[3*i + 0] = pathXY[0];
            vertices[3*i + 1] = pathXY[1];
            vertices[3*i + 2] = 0f;
            drawOrder[i] = (short) i;
        }
        line.setVertices(vertices);
        line.setDrawOrder(drawOrder);
        line.draw(mVPMatrix);
    }
}
