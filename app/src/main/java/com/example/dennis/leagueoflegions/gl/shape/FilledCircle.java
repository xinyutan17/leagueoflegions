package com.example.dennis.leagueoflegions.gl.shape;

import android.opengl.GLES20;

public class FilledCircle extends Shape {
    public static final int NUMBER_VERTICES = 50;                          // the number of vertices used to represent the circle
    public static final int TOTAL_VERTICES = NUMBER_VERTICES + 2;          // the number of vertices used to draw the circle
    public static final double DELTA_ANGLE = 2*Math.PI/NUMBER_VERTICES;    // the angle of the circle that each triangle fills

    private float[] radii;      // the radii for the vertices

    public FilledCircle(float[] color) {
        super(color);

        radii = new float[NUMBER_VERTICES];
        float[] vertices = new float[COORDS_PER_VERTEX * TOTAL_VERTICES];
        vertices[0] = vertices[1] = vertices[2] = 0f;
        for (int i = 0; i < NUMBER_VERTICES; i++) {
            radii[i] = 1f;
            vertices[3*(i+1)+0] = (float) (radii[i]*Math.cos(i*DELTA_ANGLE));
            vertices[3*(i+1)+1] = (float) (radii[i]*Math.sin(i*DELTA_ANGLE));
            vertices[3*(i+1)+2] = 0f;
        }
        vertices[3*(NUMBER_VERTICES+1)+0] = vertices[3*(0+1)+0];
        vertices[3*(NUMBER_VERTICES+1)+1] = vertices[3*(0+1)+1];
        vertices[3*(NUMBER_VERTICES+1)+2] = vertices[3*(0+1)+2];
        setVertices(vertices);

        setGL_DRAW_TYPE(GLES20.GL_TRIANGLE_FAN);
    }

    public float[] getRadii() {
        return radii;
    }

    public void setRadii(float[] radii) {
        this.radii = radii;
    }

    @Override
    public String toString() {
        return "FilledCircle";
    }
}
