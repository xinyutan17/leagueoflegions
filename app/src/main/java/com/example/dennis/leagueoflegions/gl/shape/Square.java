package com.example.dennis.leagueoflegions.gl.shape;

import android.opengl.GLES20;

public class Square extends Shape {

    private static float vertices[] = {
            // in counterclockwise order:
            -0.5f,  0.5f, 0.0f,     // top left
            -0.5f, -0.5f, 0.0f,     // bottom left
            0.5f, -0.5f, 0.0f,      // bottom right
            0.5f,  0.5f, 0.0f       // top right
    };
    private static short drawOrder[] = {0, 1, 2, 3};

    public Square(float[] color) {
        super(vertices, drawOrder, GLES20.GL_TRIANGLE_STRIP, color);
    }
}

