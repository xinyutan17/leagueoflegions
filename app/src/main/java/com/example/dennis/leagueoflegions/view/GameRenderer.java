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

import com.example.dennis.leagueoflegions.gl.GLTerrain;
import com.example.dennis.leagueoflegions.gl.GLUnit;
import com.example.dennis.leagueoflegions.gl.unit.GLArmy;
import com.example.dennis.leagueoflegions.gl.unit.GLBase;
import com.example.dennis.leagueoflegions.model.Game;
import com.example.dennis.leagueoflegions.model.Unit;
import com.example.dennis.leagueoflegions.model.unit.Army;
import com.example.dennis.leagueoflegions.model.unit.Base;

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
    private static final boolean DEBBUGGING = true;
    private static final String[] DEBUG_TEXT = new String[]{"","","","",""};
    private static final int FPS = 60;

    private final float[] mVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private float viewportRatio;
    private float viewX;
    private float viewY;
    private float projectionScale;

    private Game game;
    private ArrayList<GLUnit> glUnits;
    private ArrayList<GLUnit> glUnitRemoveQueue;
    private ArrayList<GLTerrain> glTerrains;

    private long mLastTime;

    public GameRenderer(Game game) {
        this.game = game;
        glUnits = new ArrayList<GLUnit>();
        glTerrains = new ArrayList<GLTerrain>();

        mLastTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        viewX = 0f;
        viewY = 0f;
        projectionScale = 1f;

        addAllGLUnits(game.getUnits());
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        viewportRatio = (float) width / height;
    }

    @Override
    public void onDrawFrame(GL10 unused) {
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

        addAllGLUnits(game.getUnitAddQueue());
        for (GLUnit glUnit : glUnits) {
            if (game.getUnitRemoveQueue().contains(glUnit.getGameObject())) {
                glUnitRemoveQueue.add(glUnit);
            }
        }
        glUnits.removeAll(glUnitRemoveQueue);
        glUnitRemoveQueue.clear();

        game.tickCleanUp();
    }

    private void draw() {
        // Setup view-projection matrix
        Matrix.frustumM(mProjectionMatrix, 0, -viewportRatio, viewportRatio, -1, 1, projectionScale, 1000);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -100, 0, 0, 0f, 0f, 1.0f, 0.0f);
        Matrix.translateM(mViewMatrix, 0, viewX, viewY, 0f);
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw background
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Draw Terrains
        for(GLTerrain glTerrain : glTerrains) {
            glTerrain.draw(mVPMatrix);
        }

        // Draw Units
        for (GLUnit glUnit : glUnits) {
            glUnit.draw(mVPMatrix);
        }

        DEBUG_TEXT[0] = game.getTime() + "";
        /**
         if (DEBBUGGING) {
             for(int i = 0; i < DEBUG_TEXT.length; i++) {
                canvas.drawText(DEBUG_TEXT[i], 10, 60+20*i, debugPaint);
             }
         }
         */
    }

    public void addAllGLUnits(ArrayList<Unit> units) {
        for (Unit unit : units) {
            switch (unit.getType()) {
                case ARMY:
                    glUnits.add(new GLArmy((Army) unit));
                    break;
                case BASE:
                    glUnits.add(new GLBase((Base) unit));
                    break;
                default:
                    Log.e(DEBUG_TAG, "unknown UnitType: " + unit.getType());
            }
        }
    }

    public float getViewX() {
        return viewX;
    }

    public void setViewX(float x) {
        viewX = x;
    }

    public float getViewY() {
        return viewY;
    }

    public void setViewY(float y) {
        viewY = y;
    }

    public float getProjectionScale() {
        return projectionScale;
    }

    public void setProjectionScale(float scale) {
        projectionScale = scale;
    }
}
