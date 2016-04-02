package com.example.dennis.leagueoflegions.gl.shape;

import android.opengl.GLES20;

public class DottedCircle extends Shape {
    public static final int NUMBER_VERTICES = 50;                          // the number of vertices used to represent the circle
    public static final double DELTA_ANGLE = 2*Math.PI/NUMBER_VERTICES;    // the angle of the circle that each triangle fills

    public DottedCircle(float[] color) {
        super(color);

        float[] vertices = new float[COORDS_PER_VERTEX * NUMBER_VERTICES];
        for (int i = 0; i < NUMBER_VERTICES; i++) {
            vertices[3*i+0] = (float) Math.cos(i*DELTA_ANGLE);
            vertices[3*i+1] = (float) Math.sin(i*DELTA_ANGLE);
            vertices[3*i+2] = 0f;
        }
        setVertices(vertices);

        setGL_DRAW_TYPE(GLES20.GL_LINES);
    }

    @Override
    public String toString() {
        return "DottedCircle";
    }
}
