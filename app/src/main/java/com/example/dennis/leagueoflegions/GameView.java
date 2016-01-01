package com.example.dennis.leagueoflegions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    private static final boolean DEBBUGGING = true;
    private static final String[] DEBUG_TEXT = new String[]{"","","","",""};

    private Game game;
    private volatile boolean playing;
    private Thread gameThread = null;
    private static final int FPS = 60;

    private Paint debugPaint;
    private Paint unitPaint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Path path;
    private Paint pathPaint;
    private Paint mapPaint;
    private float pathX, pathY;
    private boolean drawingPath;
    private static final float TOUCH_TOLERANCE = 5;
    private Unit selectedUnit;
    private static final int SELECT_TOLERANCE = 20;

    public GameView(Context context, Game game){
        super(context);

        surfaceHolder = getHolder();
        this.game = game;
        path = new Path();
        selectedUnit = null;

        mapPaint = new Paint();
        debugPaint = new Paint();
        unitPaint = new Paint();
        pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(4f);
        drawingPath = false;
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
            mapPaint.setColor(Color.WHITE);
            canvas.drawPaint(mapPaint);

            // Draw units
            ArrayList<Player> players = game.getPlayers();
            for(Player player : players) {
                for(Unit unit : player.getUnits()) {
                    unitPaint.setColor(unit.getColor());
                    canvas.drawRect(unit.getRect(), unitPaint);
                    if (!unit.getPath().isEmpty()) {
                        pathPaint.setColor(unit.getColor());
                        pathPaint.setAlpha(64);
                        canvas.drawPath(unit.getRemainingPath(), pathPaint);
                    }
                }
            }
            /*
            if (DEBBUGGING) {
                // Debugging text
                debugPaint.setTextSize(20);
                debugPaint.setTextAlign(Paint.Align.LEFT);
                debugPaint.setColor(Color.BLACK);
                for(int i = 0; i < DEBUG_TEXT.length; i++) {
                    canvas.drawText(DEBUG_TEXT[i], 10, 60+20*i, debugPaint);
                }
            }
            */
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
                selectedUnit = null;
                for (Player player : game.getPlayers()) {
                    for (Unit unit : player.getUnits()) {
                        if (Math.abs(unit.getX() - x) < Unit.UNIT_SIZE + SELECT_TOLERANCE &&
                                Math.abs(unit.getY() - y) < Unit.UNIT_SIZE + SELECT_TOLERANCE) {
                            selectedUnit = unit;
                            drawingPath = true;
                            DEBUG_TEXT[0] = String.format("UNIT (%d, %d) SELECTED", unit.getX(), unit.getY());
                        }
                    }
                }
                if (drawingPath) {
                    path.reset();
                    path.moveTo(selectedUnit.getX(), selectedUnit.getY());
                    pathX = selectedUnit.getX();
                    pathY = selectedUnit.getY();
                    selectedUnit.setPath(path);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (drawingPath) {
                    float dx = Math.abs(x - pathX);
                    float dy = Math.abs(y - pathY);
                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        path.quadTo(pathX, pathY, (x + pathX)/2, (y + pathY)/2);
                        pathX = x;
                        pathY = y;
                    }
                    selectedUnit.updatePath(path);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (drawingPath) {
                    path.lineTo(pathX, pathY);
                    selectedUnit.updatePath(path);

                    selectedUnit = null;
                    drawingPath = false;
                    path.reset();
                }
                break;
        }

        draw();
        return true;
    }
}