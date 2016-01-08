package com.example.dennis.leagueoflegions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
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

    private Map map;

    // new variables added for tesing tiled images
    Bitmap mBitmap;
    BitmapDrawable mDrawable;
    private int numTimesDrawn;

    public GameView(Context context, Game game, Map map){
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

        this.map = map;
        numTimesDrawn = 0;
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

            // added in order to tile images to render the map

            /*
            for(int j = 0; j < canvas.getWidth(); j += 200)
            {
                for(int k = 0; k < canvas.getHeight(); k += 200)
                {
                    Rect rect = new Rect(j, k, j + 200, k + 200);
                    mBitmap = map.loadImage();
                    mDrawable = new BitmapDrawable(getResources(), mBitmap);
                    //mDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    mDrawable.setBounds(rect);
                    // testing code added for tiled images
                    mDrawable.draw(canvas);
                }
            }
            */

            if(numTimesDrawn == 0)
            {
                map.setFinalImage();
                numTimesDrawn++;
            }

            Rect rect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
            mBitmap = map.getFinalImage();
            mDrawable = new BitmapDrawable(getResources(), mBitmap);
            mDrawable.setBounds(rect);
            mDrawable.draw(canvas);

            // Draw units
            ArrayList<Player> players = game.getPlayers();
            for(Player player : players) {
                for(Unit unit : player.getUnits()) {
                    unitPaint.setColor(unit.getColor());
                    if(unit.getType().equals("base")) {
                        canvas.drawRect(unit.getRect(), unitPaint);
                    } else if (unit.getType().equals("army")){
                        int x = unit.getX();
                        int y = unit.getY();

                        for(int i = -10; i <= 10; i++) {
                            for (int j = -10; j <= 10; j++) {
                                canvas.drawCircle(x + 20*i, y + 20*j, 5, unitPaint);
                            }
                        }
                    }
                    if (!unit.getPath().isEmpty()) {
                        pathPaint.setColor(unit.getColor());
                        pathPaint.setAlpha(64);
                        canvas.drawPath(unit.getRemainingPath(), pathPaint);
                    }
                }
            }

            if (DEBBUGGING) {
                // Debugging text
                debugPaint.setTextSize(20);
                debugPaint.setTextAlign(Paint.Align.LEFT);
                debugPaint.setColor(Color.BLACK);
                for(int i = 0; i < DEBUG_TEXT.length; i++) {
                    canvas.drawText(DEBUG_TEXT[i], 10, 60+20*i, debugPaint);
                }
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