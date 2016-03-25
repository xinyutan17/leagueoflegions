package com.example.dennis.leagueoflegions.gl;

import android.opengl.Matrix;
import android.util.Log;

import com.example.dennis.leagueoflegions.model.GameObject;
import com.example.dennis.leagueoflegions.view.GameActivity;

public abstract class GLObject {
    private static final String DEBUG_TAG = "GLObject";

    private final float[] mMVPMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    private GameObject gameObject;

    public GLObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public float[] getMVPMatrix(float[] mVPMatrix) {
        float x = gameObject.getX();
        float y = gameObject.getY();
        float scale = gameObject.getScale();
        float rotation = gameObject.getRotation();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x, y, 0f);
        Matrix.scaleM(mModelMatrix, 0, scale, scale, 1f);
        Matrix.rotateM(mModelMatrix, 0, rotation, 0f, 0f, 1f);

        Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, mModelMatrix, 0);

        if (GameActivity.DEBUGGING) {
            Log.d(DEBUG_TAG, gameObject.toString());
            GLUtility.logMatrix("mModelMatrix", mModelMatrix);
            GLUtility.logMatrix("mMVPMatrix", mMVPMatrix);
        }

        return mMVPMatrix;
    }

    public abstract void draw(float[] mVPMatrix);
    public abstract void tick();
}
