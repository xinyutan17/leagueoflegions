package com.example.dennis.leagueoflegions.gl.shape;

public class ShiftingCircle extends Circle {
    private static final float RADIUS_MIN = 0.6f;
    private static final float RADIUS_MAX = 1f;
    private static final float RADIUS_STEP = 0.03f;

    private static final int NUMBER_CORNERS = 10;
    private static final int VERTICES_PER_CORNER = NUMBER_VERTICES / NUMBER_CORNERS;

    private float[] corners;

    public ShiftingCircle(float[] color) {
        super(color);
        corners = new float[NUMBER_CORNERS];
        for (int i = 0; i < corners.length; i++) {
            corners[i] = (float)(RADIUS_MIN + Math.random()*(RADIUS_MAX - RADIUS_MIN));
        }
    }

    @Override
    public void tick() {
        for (int i = 0; i < corners.length; i++) {
            corners[i] += (float)(2*(Math.random()-0.5) * RADIUS_STEP);
            if (corners[i] < RADIUS_MIN) {
                corners[i] = RADIUS_MIN;
            } else if (corners[i] > RADIUS_MAX) {
                corners[i] = RADIUS_MAX;
            }
        }

        float[] vertices = new float[COORDS_PER_VERTEX * NUMBER_VERTICES];
        for (int i = 0; i < NUMBER_VERTICES; i++) {
            float radius;
            if (i % VERTICES_PER_CORNER == 0) {
                radius = corners[i/VERTICES_PER_CORNER];
            } else {
                // linear interpolate
                float prevRadius = corners[i/VERTICES_PER_CORNER];
                float nextRadius = corners[(i/VERTICES_PER_CORNER + 1) % corners.length];
                float step = 1f * (i % VERTICES_PER_CORNER) / VERTICES_PER_CORNER;
                radius = prevRadius + step * (nextRadius - prevRadius);
            }
            vertices[3*i+0] = (float)(radius * Math.cos(i * DELTA_ANGLE));
            vertices[3*i+1] = (float)(radius * Math.sin(i * DELTA_ANGLE));
            vertices[3*i+2] = 0f;
        }
        resetVertices(vertices);
    }

    @Override
    public String toString() {
        return "ShiftingCircle";
    }
}
