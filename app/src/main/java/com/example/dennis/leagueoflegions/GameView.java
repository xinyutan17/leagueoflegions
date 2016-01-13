package com.example.dennis.leagueoflegions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    private static final boolean DEBBUGGING = true;
    private static final String[] DEBUG_TEXT = new String[]{"","","","",""};

    private static final int FPS = 60;
    private static final int TOUCH_MOVE_TOLERANCE = 5;
    private static final int TOUCH_SELECT_TOLERANCE = 20;

    // Game
    private Game game;
    private Thread gameThread;
    private volatile boolean playing;

    // Painting
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint debugPaint;
    private Paint mapPaint;
    private Paint unitPaint;
    private Paint pathPaint;

    // Touch and Drawing
    private Path path;
    private float pathX, pathY;
    private Unit selectedUnit;
    private boolean drawing;

    // Map
    private Map map;
    Bitmap mBitmap;
    BitmapDrawable mDrawable;
    private int numTimesDrawn;

    public GameView(Context context, Game game, Map map){
        super(context);

        // Game
        this.game = game;
        this.gameThread = null;
        this.playing = false;

        // Painting
        this.surfaceHolder = getHolder();
        this.canvas = null;
        debugPaint = new Paint();
        debugPaint.setTextSize(20);
        debugPaint.setTextAlign(Paint.Align.LEFT);
        debugPaint.setColor(Color.BLACK);
        mapPaint = new Paint();
        unitPaint = new Paint();
        pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(4f);

        // Touch and Drawing
        path = new Path();
        pathX = pathY = 0;
        selectedUnit = null;
        drawing = false;

        // Map
        this.map = map;
        mBitmap = null;
        mDrawable = null;
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
        game.updateTime();
        DEBUG_TEXT[0] = game.getTime() + "";

        ArrayList<Player> players = new ArrayList<Player>(game.getPlayers());   // make a copy
        for(Player p : players)
        {
            // WARNING: currently each players units can be changed
            // while iterating through this units (e.g. base spawns new armies)
            // Make sure each unit gets properly updated
            ArrayList<Unit> units = p.getUnits();
            for(int i = 0; i < units.size(); i++)
            {
                units.get(i).update();
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

//            if(numTimesDrawn == 0)
//            {
//                map.setFinalImage();
//                numTimesDrawn++;
//            }
//
//            Rect rect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
//            mBitmap = map.getFinalImage();
//            mDrawable = new BitmapDrawable(getResources(), mBitmap);
//            mDrawable.setBounds(rect);
//            mDrawable.draw(canvas);

            // Draw units
            ArrayList<Player> players = game.getPlayers();
            for(Player player : players) {
                for(Unit unit : player.getUnits()) {
                    int x = unit.getX();
                    int y = unit.getY();
                    int size = unit.getSize();
                    unitPaint.setColor(unit.getColor());
                    switch (unit.getType()) {
                        case BASE:
                            canvas.drawRect(x-size, y+size, x+size, y-size, unitPaint);
                            break;
                        case ARMY:
                            canvas.drawCircle(x, y, size, unitPaint);
                            break;
                        default:
                            throw new RuntimeException("Unknown unit type.");
                    }
                    if (!unit.getPath().isEmpty()) {
                        pathPaint.setColor(unit.getColor());
                        pathPaint.setAlpha(64);
                        canvas.drawPath(unit.getRemainingPath(), pathPaint);
                    }
                }
            }

            if (DEBBUGGING) {
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

        draw();
        return true;
    }
}
