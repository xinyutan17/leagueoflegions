package com.example.dennis.leagueoflegions.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.dennis.leagueoflegions.model.Game;
import com.example.dennis.leagueoflegions.model.unit.Unit;
import com.example.dennis.leagueoflegions.model.unit.Soldier;

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
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        /**
         * Select unit -> single-pathing
         * Don't select unit -> panning
         * Select unit then select unit -> multi-pathing
         * Select unit then don't select unit -> zooming if non-unit pointer, pathing if unit pointer
         * Don't select unit then don't select unit -> zooming
         * Don't select unit then select unit -> zooming if non-unit pointer, pathing if unit pointer
         */
        if (e.getActionMasked() == MotionEvent.ACTION_MOVE) {
            if (!pathingUnits.isEmpty()) {
                for (int pi : pathingUnits.keySet()) {
                    int indexi = e.findPointerIndex(pi);
                    float[] worldCoorsi = mRenderer.screenToWorld(mRenderer.getScreenCoors(e.getX(indexi), e.getY(indexi)));
                    float xi = worldCoorsi[0];
                    float yi = worldCoorsi[1];

                    Unit pathingUnit = pathingUnits.get(pi);
                    float[] pathEnd = pathingUnit.getPathEnd();
                    if (Math.abs(xi - pathEnd[0]) >= TOUCH_MOVE_TOLERANCE || Math.abs(yi - pathEnd[1]) >= TOUCH_MOVE_TOLERANCE) {
                        pathingUnit.updatePathing(xi, yi);
                    }
                }
            } else {
                if (e.getPointerCount() == 1) {
                    mGestureDetector.onTouchEvent(e);
                } else {
                    mScaleGestureDetector.onTouchEvent(e);
                }
            }
            return true;
        }

        int index = e.getActionIndex();
        int pointerId = e.getPointerId(index);
        float[] worldCoors = mRenderer.screenToWorld(mRenderer.getScreenCoors(e.getX(index), e.getY(index)));
        float x = worldCoors[0];
        float y = worldCoors[1];
        ArrayList<Unit> selectedUnits = game.getUnitsWithinRadius(x, y, mRenderer.getFieldOfViewY() / 50f * TOUCH_SELECT_TOLERANCE);
        Unit selectedUnit = null;
        if (!selectedUnits.isEmpty()) {
            selectedUnit = game.getClosestUnit(x, y, selectedUnits);
        }

        if (pathingUnits.keySet().contains(pointerId)) {
            // pointer associated with pathing unit
            // (must be ACTION_UP or ACTION_POINTER_UP, b/c ACTION_MOVE handled above)
            Unit pathingUnit = pathingUnits.get(pointerId);
            pathingUnit.endPathing(x, y);
            pathingUnits.remove(pointerId);
        } else if (selectedUnit != null) {
            // pointer selected new unit
            if (selectedUnit instanceof Soldier) {
                // test if splitting
                for (Unit unit : pathingUnits.values()) {
                    if (unit.equals(selectedUnit)) {
                        selectedUnit = ((Soldier) selectedUnit).split(x, y);
                    }
                }
            }
            selectedUnit.beginPathing(x, y);
            pathingUnits.put(pointerId, selectedUnit);
        } else {
            // pointer did not select unit
            if (e.getPointerCount() == 1) {
                mGestureDetector.onTouchEvent(e);
            } else {
                mScaleGestureDetector.onTouchEvent(e);
            }
        }
        return true;
    }
}
