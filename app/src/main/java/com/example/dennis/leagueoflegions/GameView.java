package com.example.dennis.leagueoflegions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {
    private static final boolean DEBBUGGING = true;
    private static final String[] DEBUG_TEXT = new String[]{"","","","",""};
    private static Rect touchRect = null;

    private Game game;
    private volatile boolean playing;
    private Thread gameThread = null;
    private static final int FPS = 60;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Path path;
    private Paint pathPaint;
    private float pathX, pathY;
    private static final float TOUCH_TOLERANCE = 5;
    private Unit selectedUnit;
    private static final int SELECT_TOLERANCE = 20;

    public GameView(Context context, Game game){
        super(context);
        paint = new Paint();
        surfaceHolder = getHolder();
        this.game = game;
        path = new Path();
        selectedUnit = null;

        pathPaint = new Paint();
        pathPaint.setColor(Color.RED);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(4f);
    }

    @Override
    public void run()
    {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update(){
        ArrayList<Player> players = game.getPlayers();
        for(Player p : players)
        {
            for(Unit unit : p.getUnits())
            {
                unit.update();
            }
        }
    }

    private void draw()
    {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Draw map
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            // Draw units
            paint.setColor(Color.BLUE);
            ArrayList<Player> players = game.getPlayers();
            for(Player p : players) {
                for(Unit unit : p.getUnits()) {
                    canvas.drawRect(unit.getRect(), paint);
                }
            }

            // Draw path
            canvas.drawPath(path, pathPaint);

            // Debugging text
            if (DEBBUGGING) {
                paint.setTextSize(20);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.BLACK);
                for(int i = 0; i < DEBUG_TEXT.length; i++) {
                    canvas.drawText(DEBUG_TEXT[i], 10, 60+20*i, paint);
                }
            }

            // Debugging touch rect
            if (touchRect != null) {
                paint.setColor(Color.GREEN);
                canvas.drawRect(touchRect, paint);
            }

            // Unlock and draw the scene
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control(){
        try {
            gameThread.sleep(1000 / FPS);
        } catch (InterruptedException e) {

        }
    }

    // Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    // Make a new thread and start it
    // Execution moves to our R
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // check if unit selected
                touchRect = new Rect((int)(x - SELECT_TOLERANCE), (int)(y + SELECT_TOLERANCE), (int)(x + SELECT_TOLERANCE), (int)(y - SELECT_TOLERANCE));
                Rect unitRect = null;
                selectedUnit = null;
                for (Player player : game.getPlayers()) {
                    for (Unit unit : player.getUnits()) {
                        unitRect = unit.getRect();
                        DEBUG_TEXT[0] = String.format("UNIT (%d, %d) and TOUCH (%d, %d)", unitRect.centerX(), unitRect.centerY(), touchRect.centerX(), touchRect.centerY());
                        if (Math.abs(unitRect.centerX() - x) < 40 && Math.abs(unitRect.centerY() - y) < 40) {
                            selectedUnit = unit;
                            DEBUG_TEXT[1] = String.format("UNIT (%d, %d) SELECTED", unit.getX(), unit.getY());
                        } else {
                            DEBUG_TEXT[1] = String.format("UNIT (%d, %d) NOT SELECTED", unit.getX(), unit.getY());
                        }
                    }
                }
                if (selectedUnit == null) {
                    return true;
                }
                path.reset();
                path.moveTo(x, y);
                pathX = x;
                pathY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (selectedUnit == null) {
                    return true;
                }
                float dx = Math.abs(x - pathX);
                float dy = Math.abs(y - pathY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    path.quadTo(pathX, pathY, (x + pathX)/2, (y + pathY)/2);
                    pathX = x;
                    pathY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (selectedUnit == null) {
                    return true;
                }
                path.lineTo(pathX, pathY);
                selectedUnit.setPath(path);
                selectedUnit = null;
                break;
        }

        draw();
        return true;
    }
}
