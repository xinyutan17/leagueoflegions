package com.example.dennis.leagueoflegions.gl.shape;

import android.opengl.GLES20;

public class Triangle extends Shape{

    private static float vertices[] = {
            (float) Math.cos(Math.PI + 0*2*Math.PI/3), (float) Math.sin(Math.PI + 0*2*Math.PI/3), 0.0f, // top
            (float) Math.cos(Math.PI + 1*2*Math.PI/3), (float) Math.sin(Math.PI + 1*2*Math.PI/3), 0.0f, // bottom left
            (float) Math.cos(Math.PI + 2*2*Math.PI/3), (float) Math.sin(Math.PI + 2*2*Math.PI/3), 0.0f  // bottom right
    };

    public Triangle(float[] color) {
        super(vertices, GLES20.GL_TRIANGLE_FAN, color);
    }

    @Override
    public String toString() {
        return "Triangle";
    }
}
