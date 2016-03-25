package com.example.dennis.leagueoflegions.gl.shape;

import android.opengl.GLES20;
import android.util.Log;

import com.example.dennis.leagueoflegions.gl.GLUtility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class Shape {
    private static final String DEBUG_TAG = "Shape";

    public static final int BYTES_PER_SHORT = 2;
    public static final int BYTES_PER_FLOAT = 4;
    public static final int COORDS_PER_VERTEX = 3;
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * BYTES_PER_FLOAT;

    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private float[] vertices;       // the vertices composing the Shape
    private short[] drawOrder;      // the order to draw the vertices
    private FloatBuffer verticesBuffer;
    private ShortBuffer drawOrderBuffer;
    private int GL_DRAW_TYPE;       // GL_TRIANGLES, GL_TRIANGLE_FAN, GL_TRIANGLE_STRIP

    private float[] color;          // the color of the Shape (rgba)

    public Shape(float[] vertices, short[] drawOrder, int GL_DRAW_TYPE, float[] color) {
        createProgram();
        setVertices(vertices);
        setDrawOrder(drawOrder);
        setGL_DRAW_TYPE(GL_DRAW_TYPE);
        setColor(color);
    }

    public Shape(float[] color) {
        createProgram();
        vertices = null;
        drawOrder = null;
        GL_DRAW_TYPE = -1;
        setColor(color);
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

    private void createProgram() {
        int vertexShader = GLUtility.loadShader(GLES20.GL_VERTEX_SHADER, getVertexShaderCode());
        int fragmentShader = GLUtility.loadShader(GLES20.GL_FRAGMENT_SHADER, getFragmentShaderCode());

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        verticesBuffer = bb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);
    }

    public void resetVertices(float[] vertices) {
        if (vertices.length != this.vertices.length) {
            setVertices(vertices);
            return;
        }
        this.vertices = vertices;
        verticesBuffer.clear();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);
    }

    public short[] getDrawOrder() {
        return drawOrder;
    }

    public void setDrawOrder(short[] drawOrder) {
        this.drawOrder = drawOrder;
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * BYTES_PER_SHORT);
        dlb.order(ByteOrder.nativeOrder());
        drawOrderBuffer = dlb.asShortBuffer();
        drawOrderBuffer.put(drawOrder);
        drawOrderBuffer.position(0);
    }

    public void resetDrawOrder(short[] drawOrder) {
        if (drawOrder.length != this.drawOrder.length) {
            setDrawOrder(drawOrder);
            return;
        }
        this.drawOrder = drawOrder;
        drawOrderBuffer.clear();
        drawOrderBuffer.put(drawOrder);
        drawOrderBuffer.position(0);
    }

    public int getGL_DRAW_TYPE() {
        return GL_DRAW_TYPE;
    }

    public void setGL_DRAW_TYPE(int GL_DRAW_TYPE) {
        this.GL_DRAW_TYPE = GL_DRAW_TYPE;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public void draw(float[] mMVPMatrix) {
        if (vertices == null || drawOrder == null || GL_DRAW_TYPE == -1) {
            Log.e(DEBUG_TAG, "failed to draw shape: null vertices, drawOrder, or GL_DRAW_TYPE");
            return;
        }

        GLES20.glUseProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, verticesBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLUtility.checkGlError("glGetUniformLocation");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLUtility.checkGlError("glUniformMatrix4fv");

        GLES20.glLineWidth(2f); // TODO: only do this when drawing lines
        GLES20.glDrawElements(GL_DRAW_TYPE, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawOrderBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void tick() {
        // Override to do something at each game tick
    }
}
