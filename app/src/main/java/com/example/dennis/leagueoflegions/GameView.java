package com.example.dennis.leagueoflegions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    public static final int FPS = 60;

    private volatile boolean playing;
    private Thread gameThread = null;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Game game;

    public GameView(Context context, Game game){
        super(context);
        paint = new Paint();
        surfaceHolder = getHolder();
        this.game = game;

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

            //First we lock the area of memory we will be drawing to
            canvas = surfaceHolder.lockCanvas();


            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            paint.setColor(Color.BLUE);
            ArrayList<Player> players = game.getPlayers();
            for(Player p : players)
            {
                for(Unit unit : p.getUnits())
                {
                    int x = unit.getX();
                    int y = unit.getY();

                    for(int i = -10; i <= 10; i++) {
                        for (int j = -10; j <= 10; j++) {
                            canvas.drawCircle(x + 20*i, y + 20*j, 5, paint);
                        }
                    }
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
}
