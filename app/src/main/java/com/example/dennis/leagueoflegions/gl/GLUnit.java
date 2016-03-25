package com.example.dennis.leagueoflegions.gl;

import android.graphics.Path;
import android.graphics.PathMeasure;

import com.example.dennis.leagueoflegions.gl.shape.Circle;
import com.example.dennis.leagueoflegions.gl.shape.Line;
import com.example.dennis.leagueoflegions.model.Unit;

public abstract class GLUnit extends GLObject {
    private Line pathLine;
    private Circle rangeCircle;

    public GLUnit(Unit unit) {
        super(unit);
        pathLine = new Line(unit.getColor());
        rangeCircle = new Circle(unit.getColor(), unit.getRange() / unit.getScale());
    }

    @Override
    public void tick() {
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
        pathLine.setVertices(vertices);
        pathLine.setDrawOrder(drawOrder);
    }

    @Override
    public void draw(float[] mVPMatrix) {
        if (!((Unit) getGameObject()).getRemainingPath().isEmpty())
            pathLine.draw(mVPMatrix);
        rangeCircle.draw(mVPMatrix);
    }
}
