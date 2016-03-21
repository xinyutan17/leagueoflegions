package com.example.dennis.leagueoflegions.gl;

import android.opengl.Matrix;

import com.example.dennis.leagueoflegions.model.GameObject;

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
    private final float[] mTempMatrix = new float[16];

    private GameObject gameObject;

    public GLObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public float[] getMVPMatrix(float[] mVPMatrix) {
        float x = (float) gameObject.getX();
        float y = (float) gameObject.getY();
        float scale = (float) gameObject.getScale();
        float rotation = (float) gameObject.getRotation();

        Matrix.translateM(mTranslationMatrix, 0, IDENTITY_MATRIX, 0, x, y, 0f);
        Matrix.scaleM(mScaleMatrix, 0, IDENTITY_MATRIX, 0, scale, scale, 1f);
        Matrix.setRotateM(mRotationMatrix, 0, rotation, 0f, 0f, 1f);

        Matrix.multiplyMM(mTempMatrix, 0, mTranslationMatrix, 0, mRotationMatrix, 0);
        Matrix.multiplyMM(mModelMatrix, 0, mTempMatrix, 0, mScaleMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, mModelMatrix, 0);

        return mMVPMatrix;
    }

    public abstract void draw(float[] mVPMatrix);
    public abstract void tick();
}
