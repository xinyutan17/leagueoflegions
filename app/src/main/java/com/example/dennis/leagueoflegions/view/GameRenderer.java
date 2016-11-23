/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.dennis.leagueoflegions.view;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.dennis.leagueoflegions.gl.GLObject;
import com.example.dennis.leagueoflegions.gl.GLProjectile;
import com.example.dennis.leagueoflegions.gl.unit.GLArcher;
import com.example.dennis.leagueoflegions.gl.unit.GLBase;
import com.example.dennis.leagueoflegions.gl.unit.GLSoldier;
import com.example.dennis.leagueoflegions.model.Game;
import com.example.dennis.leagueoflegions.model.GameObject;
import com.example.dennis.leagueoflegions.model.Projectile;
import com.example.dennis.leagueoflegions.model.unit.Unit;
import com.example.dennis.leagueoflegions.model.unit.Archer;
import com.example.dennis.leagueoflegions.model.unit.Base;
import com.example.dennis.leagueoflegions.model.unit.Soldier;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class GameRenderer implements GLSurfaceView.Renderer {
    private static final String DEBUG_TAG = "GameRenderer";
    private static final String[] DEBUG_TEXT = new String[]{"","","","",""};

    private static final float CAMERA_DISTANCE = 500f;
    public static final float MIN_FOVY = 30f;
    public static final float MAX_FOVY = 150f;
    public static final int FPS = 60;

    private final float[] mVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private float viewportRatio;
    private float screenWidth;
    private float screenHeight;
    private float viewX;
    private float viewY;
    private float fieldOfViewY;

    private Game game;
    private ArrayList<GLObject> glObjects;

    private long mLastTime;

    public GameRenderer(Game game) {
        this.game = game;
        glObjects = new ArrayList<GLObject>();
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(1f, 1f, 1f, 1f);

        viewX = 0f;
        viewY = 0f;
        fieldOfViewY = 120f;

        mLastTime = System.currentTimeMillis();

        tick(); // Set up initial units
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        screenWidth = width;
        screenHeight = height;
        viewportRatio = (float) width / height;

        // Setup view-projection matrix
        Matrix.perspectiveM(mProjectionMatrix, 0, fieldOfViewY, viewportRatio, -1, 1);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, CAMERA_DISTANCE, 0, 0, 0f, 0f, 1.0f, 0.0f);
        Matrix.translateM(mViewMatrix, 0, -viewX, -viewY, 0f);
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public synchronized void onDrawFrame(GL10 unused) {
        long now = System.currentTimeMillis();
        if (now - mLastTime < 1000.0/FPS) {
            return;
        }

        tick();
        draw();

        mLastTime = now;
    }

    public void tick() {
        game.tick();

        for (GLObject glObject : glObjects) {
            glObject.tick();
        }

        addAllGLObjects(game.getGameObjectAddQueue());
        removeAllGLObjects(game.getGameObjectRemoveQueue());

        game.tickCleanUp();
    }

    private void draw() {
        // Draw background
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Draw GameObjects
        for (GLObject glObject : glObjects) {
            glObject.draw(mVPMatrix);
        }

        /**
         if (DEBBUGGING) {
            DEBUG_TEXT[0] = game.getTime() + "";
            for(int i = 0; i < DEBUG_TEXT.length; i++) {
            canvas.drawText(DEBUG_TEXT[i], 10, 60+20*i, debugPaint);
            }
         }
         */
    }

    public void addAllGLObjects(ArrayList<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Unit) {
                Unit unit = (Unit) gameObject;
                switch (unit.getUnitType()) {
                    case BASE:
                        glObjects.add(new GLBase((Base) unit));
                        break;
                    case SOLDIER:
                        glObjects.add(new GLSoldier((Soldier) unit));
                        break;
                    case ARCHER:
                        glObjects.add(new GLArcher((Archer) unit));
                        break;
                    default:
                        Log.e(DEBUG_TAG, "unknown UnitType: " + unit.getUnitType());
                }
            } else if (gameObject instanceof Projectile) {
                glObjects.add(new GLProjectile((Projectile) gameObject));
            }
        }
    }

    public void removeAllGLObjects(ArrayList<GameObject> gameObjects) {
        ArrayList<GLObject> glObjectRemoveQueue = new ArrayList<GLObject>();
        for (GLObject glObject : glObjects) {
            if (gameObjects.contains(glObject.getGameObject())) {
                glObjectRemoveQueue.add(glObject);
            }
        }
        glObjects.removeAll(glObjectRemoveQueue);
    }

    public float getViewX() {
        return viewX;
    }

    public float getViewY() {
        return viewY;
    }

    public void setView(float x, float y) {
        viewX = x;
        viewY = y;

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, CAMERA_DISTANCE, 0, 0, 0f, 0f, 1.0f, 0.0f);
        Matrix.translateM(mViewMatrix, 0, -viewX, -viewY, 0f);
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

    public float getFieldOfViewY() {
        return fieldOfViewY;
    }

    public void setFieldOfViewY(float fovy) {
        fieldOfViewY = fovy;

        Matrix.perspectiveM(mProjectionMatrix, 0, fieldOfViewY, viewportRatio, -1, 1);
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

    public float[] getScreenCoors(float screenX, float screenY) {
        screenY = screenHeight - screenY;   // invert y, as Android uses top-left, OpenGL uses bottom-left

        float[] screenCoors = new float[4];
        screenCoors[0] = 2f * screenX / screenWidth - 1f;
        screenCoors[1] = 2f * screenY / screenHeight - 1f;
        screenCoors[2] = 1f / CAMERA_DISTANCE;  // THE MAGIC VALUE
        screenCoors[3] = 1f;

        return screenCoors;
    }

    public void normalize(float[] vector) {
        float scaleFactor = vector[3];
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= scaleFactor;
        }
    }

    public float[] screenToWorld(float[] screenCoors) {
        float[] mInvertedVPMatrix = new float[16];
        Matrix.invertM(mInvertedVPMatrix, 0, mVPMatrix, 0);

        float[] worldCoors = new float[4];
        Matrix.multiplyMV(worldCoors, 0, mInvertedVPMatrix, 0, screenCoors, 0);
        normalize(worldCoors);
        return worldCoors;
    }

    public float[] screenToEye(float[] screenCoors) {
        float[] mInvertedPMatrix = new float[16];
        Matrix.invertM(mInvertedPMatrix, 0, mProjectionMatrix, 0);

        float[] eyeCoors = new float[4];
        Matrix.multiplyMV(eyeCoors, 0, mInvertedPMatrix, 0, screenCoors, 0);
        normalize(eyeCoors);
        return eyeCoors;
    }

    public float[] worldToEye(float[] worldCoors) {
        float[] eyeCoors = new float[4];
        Matrix.multiplyMV(eyeCoors, 0, mViewMatrix, 0, worldCoors, 0);
        normalize(eyeCoors);
        return eyeCoors;
    }
}
