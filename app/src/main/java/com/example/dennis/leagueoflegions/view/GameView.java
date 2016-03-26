package com.example.dennis.leagueoflegions.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.dennis.leagueoflegions.model.Game;
import com.example.dennis.leagueoflegions.model.Unit;

import java.util.ArrayList;

public class GameView extends GLSurfaceView {
    private static final String DEBUG_TAG = "GameView";

    private static final int TOUCH_MOVE_TOLERANCE = 5;
    private static final int TOUCH_SELECT_TOLERANCE = 20;

    private Game game;

    private final GameRenderer mRenderer;
    private final GestureDetector mGestureDetector;
    private final ScaleGestureDetector mScaleGestureDetector;

    private Unit pathingUnit;

    public GameView(Context context, Game game) {
        super(context);
        setEGLContextClientVersion(2);

        this.game = game;
        mRenderer = new GameRenderer(game);
        setRenderer(mRenderer);
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                float viewX = mRenderer.getViewX() + 0.02f * mRenderer.getFieldOfViewY() * distanceX;
                float viewY = mRenderer.getViewY() + 0.02f * mRenderer.getFieldOfViewY() * distanceY;
                mRenderer.setView(viewX, viewY);
                return true;
            }
        });

        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float[] screenCoors = mRenderer.getScreenCoors(detector.getFocusX(), detector.getFocusY());

                // Calculate world coordinates before scaling
                float[] worldCoors = mRenderer.screenToWorld(screenCoors);

                // Scale
                float fovy = 1f/detector.getScaleFactor() * mRenderer.getFieldOfViewY();
                fovy = Math.max(GameRenderer.MIN_FOVY, Math.min(fovy, GameRenderer.MAX_FOVY));
                mRenderer.setFieldOfViewY(fovy);

                // Calculate eye coordinates of worldCoors and screenCoors after scaling
                float[] eyeCoorsWorld = mRenderer.worldToEye(worldCoors);
                float[] eyeCoorsScreen = mRenderer.screenToEye(screenCoors);

                // Move Camera
                float viewX = mRenderer.getViewX() + (eyeCoorsWorld[0] - eyeCoorsScreen[0]);
                float viewY = mRenderer.getViewY() + (eyeCoorsWorld[1] - eyeCoorsScreen[1]);
                mRenderer.setView(viewX, viewY);

                return true;
            }
        });

        pathingUnit = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] screenCoors = mRenderer.getScreenCoors(event.getX(), event.getY());
        float[] worldCoors = mRenderer.screenToWorld(screenCoors);
        float x = worldCoors[0];
        float y = worldCoors[1];

        ArrayList<Unit> selectedUnits = game.getUnitsWithinRadius(x, y, mRenderer.getFieldOfViewY()/50f * TOUCH_SELECT_TOLERANCE);
//        if (!selectedUnits.isEmpty()) {
//            Log.d(DEBUG_TAG, "SELECTED UNITS");
//            for (Unit unit : selectedUnits) {
//                Log.d(DEBUG_TAG, unit.toString());
//            }
//        }

        if (pathingUnit != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float[] pathEnd = pathingUnit.getPathEnd();
                    if (Math.abs(x - pathEnd[0]) >= TOUCH_MOVE_TOLERANCE || Math.abs(y - pathEnd[1]) >= TOUCH_MOVE_TOLERANCE) {
                        pathingUnit.updatePathing(x, y);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    pathingUnit.endPathing(x, y);
                    pathingUnit = null;
                    break;
            }
        } else if (!selectedUnits.isEmpty() && event.getAction() == MotionEvent.ACTION_DOWN) {
                pathingUnit = selectedUnits.get(0);
                pathingUnit.beginPathing();
        } else if (selectedUnits.isEmpty()) {
            mScaleGestureDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
        }

        return true;
    }
}
