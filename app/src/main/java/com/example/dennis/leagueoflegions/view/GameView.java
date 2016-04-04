package com.example.dennis.leagueoflegions.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.dennis.leagueoflegions.model.Game;
import com.example.dennis.leagueoflegions.model.Unit;
import com.example.dennis.leagueoflegions.model.unit.Army;

import java.util.ArrayList;
import java.util.HashMap;

public class GameView extends GLSurfaceView {
    private static final String DEBUG_TAG = "GameView";

    private static final int TOUCH_MOVE_TOLERANCE = 5;
    private static final int TOUCH_SELECT_TOLERANCE = 30;

    private Game game;

    private final GameRenderer mRenderer;
    private final GestureDetector mGestureDetector;
    private final ScaleGestureDetector mScaleGestureDetector;

    private HashMap<Integer, Unit> pathingUnits;
    private enum TouchType {NONE, SINGLE_PATHING, MULTI_PATHING, PANNING, ZOOMING};
    private TouchType touchType;

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

        pathingUnits = new HashMap<Integer, Unit>();
        touchType = TouchType.NONE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        float[] worldCoors = mRenderer.screenToWorld(mRenderer.getScreenCoors(event.getX(index), event.getY(index)));
        float x = worldCoors[0];
        float y = worldCoors[1];
        ArrayList<Unit> selectedUnits = game.getUnitsWithinRadius(x, y, mRenderer.getFieldOfViewY()/50f * TOUCH_SELECT_TOLERANCE);
        Unit selectedUnit = null;
        if (!selectedUnits.isEmpty()) {
            selectedUnit = game.getClosestUnit(x, y, selectedUnits);
        }

        int mActivePointerId = event.getPointerId(index);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (selectedUnit != null) {
                    touchType = TouchType.SINGLE_PATHING;
                    selectedUnit.beginPathing();
                    pathingUnits.put(mActivePointerId, selectedUnit);
                } else {
                    touchType = TouchType.PANNING;
                    mGestureDetector.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (touchType == TouchType.SINGLE_PATHING && selectedUnit != null) {
                    if (selectedUnit instanceof Army) {
                        // If selecting the same army, then split that army
                        // and begin pathing the newly created half army.
                        for (Unit unit : pathingUnits.values()) {
                            if (unit.equals(selectedUnit)) {
                                selectedUnit = ((Army) unit).split(x, y);
                                break;
                            }
                        }
                    }
                    touchType = TouchType.MULTI_PATHING;
                    selectedUnit.beginPathing();
                    pathingUnits.put(mActivePointerId, selectedUnit);
                } else if (touchType == TouchType.PANNING){
                    touchType = TouchType.ZOOMING;
                    mScaleGestureDetector.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                switch (touchType) {
                    case SINGLE_PATHING:
                    case MULTI_PATHING:
                        for (int pi : pathingUnits.keySet()) {
                            int indexi = event.findPointerIndex(pi);
                            float[] worldCoorsi = mRenderer.screenToWorld(mRenderer.getScreenCoors(event.getX(indexi), event.getY(indexi)));
                            float xi = worldCoorsi[0];
                            float yi = worldCoorsi[1];
                            Unit pathingUnit = pathingUnits.get(pi);
                            float[] pathEnd = pathingUnit.getPathEnd();
                            if (Math.abs(xi - pathEnd[0]) >= TOUCH_MOVE_TOLERANCE || Math.abs(yi - pathEnd[1]) >= TOUCH_MOVE_TOLERANCE) {
                                pathingUnit.updatePathing(xi, yi);
                            }
                        }
                        break;
                    case PANNING:
                        mGestureDetector.onTouchEvent(event);
                        break;
                    case ZOOMING:
                        mScaleGestureDetector.onTouchEvent(event);
                        break;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                switch (touchType) {
                    case MULTI_PATHING:
                        Unit pathingUnit = pathingUnits.get(mActivePointerId);
                        pathingUnit.endPathing(x, y);
                        pathingUnits.remove(mActivePointerId);
                        touchType = TouchType.SINGLE_PATHING;
                        break;
                    case ZOOMING:
                        mScaleGestureDetector.onTouchEvent(event);
                        touchType = TouchType.PANNING;
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                switch (touchType) {
                    case SINGLE_PATHING:
                        Unit pathingUnit = pathingUnits.get(mActivePointerId);
                        pathingUnit.endPathing(x, y);
                        pathingUnits.remove(mActivePointerId);
                        touchType = TouchType.NONE;
                        break;
                    case PANNING:
                        mGestureDetector.onTouchEvent(event);
                        touchType = TouchType.NONE;
                        break;
                }
                break;
        }
        return true;
    }
}
