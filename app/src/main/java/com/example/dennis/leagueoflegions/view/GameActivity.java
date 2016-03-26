package com.example.dennis.leagueoflegions.view;

import android.app.Activity;
import android.os.Bundle;

import com.example.dennis.leagueoflegions.gl.GLTerrain;
import com.example.dennis.leagueoflegions.gl.GLUnit;
import com.example.dennis.leagueoflegions.model.Game;

import java.util.ArrayList;

public class GameActivity extends Activity {
    public static final boolean DEBUGGING = false;

    private GameView gameView;
    private Game game;
    private ArrayList<GLUnit> glUnits;
    private ArrayList<GLTerrain> glTerrains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
//        String game_mode = extras.getString(Game.GAME_TYPE);
        Game.GameType gameType = (Game.GameType) getIntent().getSerializableExtra(Game.GAME_TYPE);

        game = new Game();
        float[] blue = {0f, 0f, 1f, 1f};
        float[] red  = {1f, 0f, 0f, 1f};
        float[] green = {0f, 1f, 0f, 1f};
        game.addPlayer(blue, -500f, 0f);
        game.addPlayer(green, 0f, 0f);
        game.addPlayer(red, 500f, 0f);

        gameView = new GameView(this, game);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        gameView.pause();
    }

    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
//        gameView.resume();
    }
}
