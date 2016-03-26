package com.example.dennis.leagueoflegions.gl.shape;

import android.opengl.GLES20;

public class Square extends Shape {

    private static float vertices[] = {
            (float) Math.cos(Math.PI/4 + 0*Math.PI/2), (float) Math.sin(Math.PI/4 + 0*Math.PI/2), 0.0f, // top right
            (float) Math.cos(Math.PI/4 + 1*Math.PI/2), (float) Math.sin(Math.PI/4 + 1*Math.PI/2), 0.0f, // top left
            (float) Math.cos(Math.PI/4 + 2*Math.PI/2), (float) Math.sin(Math.PI/4 + 2*Math.PI/2), 0.0f, // bottom left
            (float) Math.cos(Math.PI/4 + 3*Math.PI/2), (float) Math.sin(Math.PI/4 + 3*Math.PI/2), 0.0f, // bottom right
    };

    public Square(float[] color) {
        super(vertices, GLES20.GL_TRIANGLE_FAN, color);
    }

    @Override
    public String toString() {
        return "Square";
    }
}

