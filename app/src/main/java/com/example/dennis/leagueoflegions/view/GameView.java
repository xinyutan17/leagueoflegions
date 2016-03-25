package com.example.dennis.leagueoflegions.view;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.opengl.GLSurfaceView;
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

//        float[] worldCoors = mRenderer.screenToWorld(x, y);
//        Log.d(DEBUG_TAG, Arrays.toString(worldCoors));

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
