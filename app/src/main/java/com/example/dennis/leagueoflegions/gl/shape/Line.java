package com.example.dennis.leagueoflegions.gl.shape;

import android.opengl.GLES20;

public class Line extends Shape {

    public Line(float[] color) {
        super(color);
        setGL_DRAW_TYPE(GLES20.GL_LINE_STRIP);
        // vertices must be set manually by calling setVertices()
    }

    @Override
    public String toString() {
        return "Line";
    }
}
