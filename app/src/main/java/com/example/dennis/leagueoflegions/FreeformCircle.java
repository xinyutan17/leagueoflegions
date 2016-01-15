package com.example.dennis.leagueoflegions;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class FreeformCircle {

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private static final int BYTES_PER_SHORT = 2;
    private static final int BYTES_PER_FLOAT = 4;

    private static final int COORDS_PER_VERTEX = 3;                                 // the number of coordinates per vertex
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * BYTES_PER_FLOAT;   // the number of bytes per vertex

    private static final int NUMBER_OF_VERTICES = 20;                               // the number of vertices used to represent the circle
    private static final int TOTAL_NUMBER_OF_VERTICES = NUMBER_OF_VERTICES + 2;     // the number of vertices used to draw the circle
    private static final double DELTA_ANGLE = 2*Math.PI/NUMBER_OF_VERTICES;         // the angle of the circle that each triangle fills

    private static final float RADIUS_MIN = 0.9f;       // minimum radius value
    private static final float RADIUS_MAX = 1.1f;       // maximum radius value
    private static final float RADIUS_STEP = 0.01f;    // radius step at each update

    private final float[] color;        // the color of the circle (rgba)
    private final short[] drawOrder;    // the order to draw vertices
    private float[] radii;              // the radii for the vertices
    private float[] radiiStepDir;       // the direction of the radius step at each update
    private float[] vertices;           // the vertices

    public FreeformCircle(float[] color) {
        this.color = color;

        drawOrder = new short[TOTAL_NUMBER_OF_VERTICES];
        for(short i = 0; i < TOTAL_NUMBER_OF_VERTICES; i++) {
            drawOrder[i] = i;
        }

        radii = new float[COORDS_PER_VERTEX * NUMBER_OF_VERTICES];
        radiiStepDir = new float[COORDS_PER_VERTEX * NUMBER_OF_VERTICES];
        vertices = new float[COORDS_PER_VERTEX * TOTAL_NUMBER_OF_VERTICES];
        vertices[0] = vertices[1] = vertices[2] = 0f;
        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
            radii[i] = 1f;
            if (Math.random() < 0.5) {
                radiiStepDir[i] = 1;
            } else {
                radiiStepDir[i] = -1;
            }
            vertices[3*(i+1)+0] = (float) (radii[i]*Math.cos(i*DELTA_ANGLE));
            vertices[3*(i+1)+1] = (float) (radii[i]*Math.sin(i*DELTA_ANGLE));
            vertices[3*(i+1)+2] = 0f;
        }
        vertices[3*(NUMBER_OF_VERTICES+1)+0] = vertices[3*(0+1)+0];
        vertices[3*(NUMBER_OF_VERTICES+1)+1] = vertices[3*(0+1)+1];
        vertices[3*(NUMBER_OF_VERTICES+1)+2] = vertices[3*(0+1)+2];

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * BYTES_PER_SHORT);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void update() {
        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
            if (Math.random() < 0.33) {
                if (Math.random() < 0.1) {
                    radiiStepDir[i] *= -1;
                }
                radii[i] += radiiStepDir[i]*RADIUS_STEP;
                if (radii[i] < RADIUS_MIN) {
                    radii[i] = RADIUS_MIN;
                    radiiStepDir[i] = 1;
                } else if (radii[i] > RADIUS_MAX) {
                    radii[i] = RADIUS_MAX;
                    radiiStepDir[i] = -1;
                }
                vertices[3*(i+1)+0] = (float) (radii[i]*Math.cos(i*DELTA_ANGLE));
                vertices[3*(i+1)+1] = (float) (radii[i]*Math.sin(i*DELTA_ANGLE));
            }
        }
        vertices[3*(NUMBER_OF_VERTICES+1)+0] = vertices[3*(0+1)+0];
        vertices[3*(NUMBER_OF_VERTICES+1)+1] = vertices[3*(0+1)+1];

        vertexBuffer.clear();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLRenderer.checkGlError("glGetUniformLocation");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLRenderer.checkGlError("glUniformMatrix4fv");

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
