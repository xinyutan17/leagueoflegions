package com.example.dennis.leagueoflegions.gl;

import android.opengl.Matrix;

import com.example.dennis.leagueoflegions.model.GameObject;

/**
 * Created by Dennis on 3/20/2016.
 */
public abstract class GLObject {
    private static final String DEBUG_TAG = "GLObject";

    private static float[] IDENTITY_MATRIX = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    private final float[] mMVPMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private final float[] mTranslationMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mScaleMatrix = new float[16];

    private GameObject gameObject;

    public GLObject(GameObject gameObject) {
        this.gameObject = gameObject;
        gameObject.setGlObject(this);
    }

    public float[] getMVPMatrix(float[] mVPMatrix) {
        float x = (float) gameObject.getX();
        float y = (float) gameObject.getY();
        float size = (float) gameObject.getSize();

        Matrix.translateM(mTranslationMatrix, 0, IDENTITY_MATRIX, 0, x, y, 0f);
        Matrix.scaleM(mScaleMatrix, 0, IDENTITY_MATRIX, 0, size, size, 1f);
        Matrix.setIdentityM(mRotationMatrix, 0);

        Matrix.multiplyMM(mModelMatrix, 0, mTranslationMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mModelMatrix, 0, mModelMatrix, 0, mScaleMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, mModelMatrix, 0);

        return mMVPMatrix;
    }

    public String toString(float[] matrix) {
        String s = "";
        for (int i = 0; i < matrix.length; i++) {
            s += matrix[i] + ", ";
        }
        return s;
    }

    public abstract void draw(float[] mVPMatrix);
    public abstract void tick();
}
