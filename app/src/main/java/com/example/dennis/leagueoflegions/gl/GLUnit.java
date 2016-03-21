package com.example.dennis.leagueoflegions.gl;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.dennis.leagueoflegions.model.Unit;
import com.example.dennis.leagueoflegions.view.GLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class GLUnit {
    private static final int BYTES_PER_SHORT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int COORDS_PER_VERTEX = 3;                                 // the number of coordinates per vertex
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * BYTES_PER_FLOAT;   // the number of bytes per vertex

    final float[] mMVPMatrix = new float[16];
    final float[] mModelMatrix = new float[16];
    final float[] mTranslationMatrix = new float[16];
    final float[] mRotationMatrix = new float[16];
    final float[] mScaleMatrix = new float[16];

    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private FloatBuffer verticesBuffer;
    private ShortBuffer drawOrderBuffer;

    private float[] color;          // the color of the GLUnit (rgba)
    private float[] vertices;       // the vertices composing the GLUnit
    private short[] drawOrder;      // the order to draw the vertices
    private int GL_DRAW_TYPE;       // GL_TRIANGLES, GL_TRIANGLE_FAN, GL_TRIANGLE_STRIP

    public GLUnit(float[] color, float[] vertices, short[] drawOrder, int GL_DRAW_TYPE) {
        int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, getVertexShaderCode());
        int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, getFragmentShaderCode());

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public String getVertexShaderCode() {
        return  "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}";
    }

    public String getFragmentShaderCode() {
        return  "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}";
    }

    public abstract float[] getColor();
    public abstract float[] getVertices();
    public abstract short[] getDrawOrder();
    public abstract void update();

    private void setVertices(float[] vertices, short[] drawOrder) {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        verticesBuffer = bb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * BYTES_PER_SHORT);
        dlb.order(ByteOrder.nativeOrder());
        drawOrderBuffer = dlb.asShortBuffer();
        drawOrderBuffer.put(drawOrder);
        drawOrderBuffer.position(0);
    }

    public void draw(float[] vpMatrix, Unit unit) {
        float x = (float) unit.getX();
        float y = (float) unit.getY();
        float size = (float) unit.getSize();

        float[] scratch = new float[16];

        for (int i = 0; i < scratch.length; i++) {
            scratch[i] = 0f;
        }
        Matrix.translateM(mTranslationMatrix, 0, scratch, 0, x, y, 0f);

        Matrix.setIdentityM(scratch, 0);
        Matrix.scaleM(mScaleMatrix, 0, scratch, 0, size, size, 1f);

        Matrix.setIdentityM(mRotationMatrix, 0);


        Matrix.multiplyMM(mModelMatrix, 0, mTranslationMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mModelMatrix, 0, mModelMatrix, 0, mScaleMatrix, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, vpMatrix, 0, mModelMatrix, 0);

        /**
         * TODO: draw unit color + unit path
         */

        GLES20.glUseProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, verticesBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLRenderer.checkGlError("glGetUniformLocation");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLRenderer.checkGlError("glUniformMatrix4fv");

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawOrderBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
