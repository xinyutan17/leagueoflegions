package com.example.dennis.leagueoflegions.gl.shape;

import android.opengl.GLES20;

public class Point extends Shape {
    private float[] vertices;

    public Point(float[] color) {
        super(color);
        setGL_DRAW_TYPE(GLES20.GL_POINTS);

        vertices = new float[]{0f, 0f, 0f};
        setVertices(vertices);
    }

    public void setXYZ(float x, float y, float z) {
        vertices[0] = x;
        vertices[1] = y;
        vertices[2] = z;
        resetVertices(vertices);
    }

    @Override
    public String toString() {
        return "Point";
    }
}
