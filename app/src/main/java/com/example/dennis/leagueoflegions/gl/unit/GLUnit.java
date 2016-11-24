package com.example.dennis.leagueoflegions.gl.unit;

import android.graphics.Path;
import android.graphics.PathMeasure;

import com.example.dennis.leagueoflegions.gl.GLObject;
import com.example.dennis.leagueoflegions.gl.shape.Circle;
import com.example.dennis.leagueoflegions.gl.shape.FilledCircle;
import com.example.dennis.leagueoflegions.gl.shape.Line;
import com.example.dennis.leagueoflegions.model.unit.Unit;

public abstract class GLUnit extends GLObject {
    private Line pathLine;
    private float[] pathVertices;
    private Circle rangeCircle;
    private FilledCircle visionCircle;

    private static final float[] VISION_COLOR = new float[]{1.0f, 1.0f, 1.0f, 0.0f};

    public GLUnit(Unit unit) {
        super(unit);
        pathLine = new Line(unit.getColor());
        rangeCircle = new Circle(unit.getColor());
        rangeCircle.setDotted(true);
        visionCircle = new FilledCircle(VISION_COLOR);
        visionCircle.setZ(-1f);
    }

    @Override
    public void tick() {
        Unit unit = (Unit) getGameObject();
        Path path = unit.getRemainingPath();
        if (!path.isEmpty()) {
            PathMeasure pm = new PathMeasure(path, false);

            int numVertices = (int) pm.getLength();
            float[] vertices = new float[3 * numVertices];
            float[] pathXY = new float[2];
            for (int i = 0; i < numVertices; i++) {
                pm.getPosTan(i, pathXY, null);
                vertices[3*i + 0] = pathXY[0];
                vertices[3*i + 1] = pathXY[1];
                vertices[3*i + 2] = 0f;
            }
            pathLine.setVertices(vertices);
        }

        rangeCircle.setScale(unit.getRange() / unit.getScale());
        visionCircle.setScale(unit.getVision() / unit.getScale());
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);
        if (!((Unit) getGameObject()).getRemainingPath().isEmpty())
            pathLine.draw(mVPMatrix);

        Unit unit = (Unit) getGameObject();
        if (unit.getPlayer().equals(unit.getGame().getCurrentPlayer())) {
            rangeCircle.draw(getMVPMatrix());
            visionCircle.draw(getMVPMatrix());
        }
    }
}
