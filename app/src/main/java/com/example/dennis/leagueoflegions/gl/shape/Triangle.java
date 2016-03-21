package com.example.dennis.leagueoflegions.gl.shape;

import android.opengl.GLES20;

public class Triangle extends Shape{

    private static float vertices[] = {
            // in counterclockwise order:
            0.0f,  0.622008459f, 0.0f,      // top
            -0.5f, -0.311004243f, 0.0f,     // bottom left
            0.5f, -0.311004243f, 0.0f       // bottom right
    };
    private static short drawOrder[] = {0, 1, 2};


    public Triangle(float[] color) {
        super(vertices, drawOrder, GLES20.GL_TRIANGLES, color);
    }
}
