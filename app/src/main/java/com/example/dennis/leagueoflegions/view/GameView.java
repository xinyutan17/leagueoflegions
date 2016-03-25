package com.example.dennis.leagueoflegions.view;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
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

    private Unit selectedUnit;
    private Path path;
    private float pathX, pathY;
    private boolean pathing;

    public GameView(Context context, Game game){
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

        selectedUnit = null;
        path = new Path();
        pathX = pathY = 0;
        pathing = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float[] screenCoors = mRenderer.getScreenCoors(event.getX(), event.getY());
        float[] worldCoors = mRenderer.screenToWorld(screenCoors);
        float x = worldCoors[0];
        float y = worldCoors[1];

        ArrayList<Unit> selectedUnits = game.getUnitsWithinRadius(x, y, mRenderer.getFieldOfViewY()/50f * TOUCH_SELECT_TOLERANCE);

        if (pathing) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(x - pathX) >= TOUCH_MOVE_TOLERANCE || Math.abs(y - pathY) >= TOUCH_MOVE_TOLERANCE) {
                        path.quadTo(pathX, pathY, (x + pathX) / 2, (y + pathY) / 2);
                        pathX = x;
                        pathY = y;
                        selectedUnit.updatePath(path);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    path.lineTo(pathX, pathY);
                    selectedUnit.updatePath(path);

                    path.reset();
                    pathX = pathY = 0;
                    selectedUnit = null;
                    pathing = false;
                    break;
                default:
                    break;
            }
            return true;
        }

        if (!selectedUnits.isEmpty()) {
//            Log.d(DEBUG_TAG, "SELECTED UNITS");
//            for (Unit unit : selectedUnits) {
//                Log.d(DEBUG_TAG, unit.toString());
//            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                selectedUnit = selectedUnits.get(0);
                pathing = true;

                path.reset();
                path.moveTo(selectedUnit.getX(), selectedUnit.getY());
                PathMeasure pm = new PathMeasure(path, false);
                pathX = selectedUnit.getX();
                pathY = selectedUnit.getY();
                selectedUnit.setPath(path);

                return true;
            }
        }

        if (selectedUnits.isEmpty()) {
            mScaleGestureDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);

            return true;
        }

        return true;
    }
}
