package com.example.dennis.leagueoflegions.view;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.dennis.leagueoflegions.model.Game;
import com.example.dennis.leagueoflegions.model.Player;
import com.example.dennis.leagueoflegions.model.Unit;

public class GameView extends GLSurfaceView {
    private static final String DEBUG_TAG = "GameView";

    private static final int TOUCH_MOVE_TOLERANCE = 5;
    private static final int TOUCH_SELECT_TOLERANCE = 20;

    private Game game;

    private final GameRenderer mRenderer;
    private final GestureDetector mGestureDetector;
    private final ScaleGestureDetector mScaleGestureDetector;

    private Path path;
    private float pathX, pathY;
    private Unit selectedUnit;
    private boolean drawing;

    public GameView(Context context, Game game){
        super(context);
        setEGLContextClientVersion(2);

        mRenderer = new GameRenderer(game);
        setRenderer(mRenderer);
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                mRenderer.setViewX(mRenderer.getViewX() + distanceX / 100);
                mRenderer.setViewY(mRenderer.getViewY() + distanceY / 100);
                Log.d(DEBUG_TAG, "onScroll: (" + mRenderer.getViewX() + ", " + mRenderer.getViewY() + ")");

                requestRender();
                return true;
            }
        });

        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float mScaleFactor = mRenderer.getProjectionScale() * detector.getScaleFactor();
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
                mRenderer.setProjectionScale(mScaleFactor);
                Log.d(DEBUG_TAG, "onScale: " + mRenderer.getProjectionScale());

                requestRender();
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        /** TODO: only do one of the following
         * select unit to draw path
         * pan map
         * zoom map (scale)
         */
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                selectedUnit = null;
                for (Player player : game.getPlayers()) {
                    for (Unit unit : player.getUnits()) {
                        if (Math.abs(unit.getX() - x) < unit.getSize() + TOUCH_SELECT_TOLERANCE &&
                                Math.abs(unit.getY() - y) < unit.getSize() + TOUCH_SELECT_TOLERANCE) {
                            selectedUnit = unit;
                            drawing = true;
                        }
                    }
                }
                if (drawing) {
                    path.reset();
                    path.moveTo(selectedUnit.getX(), selectedUnit.getY());
                    PathMeasure pm = new PathMeasure(path, false);
                    pathX = selectedUnit.getX();
                    pathY = selectedUnit.getY();
                    selectedUnit.setPath(path);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (drawing) {
                    if (Math.abs(x - pathX) >= TOUCH_MOVE_TOLERANCE || Math.abs(y - pathY) >= TOUCH_MOVE_TOLERANCE) {
                        path.quadTo(pathX, pathY, (x + pathX) / 2, (y + pathY) / 2);
                        pathX = x;
                        pathY = y;
                        selectedUnit.updatePath(path);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (drawing) {
                    path.lineTo(pathX, pathY);
                    selectedUnit.updatePath(path);

                    path.reset();
                    pathX = pathY = 0;
                    selectedUnit = null;
                    drawing = false;
                }
                break;
        }

        return true;
    }
}
