package com.example.dennis.leagueoflegions.view;

import android.app.Activity;
import android.os.Bundle;

import com.example.dennis.leagueoflegions.gl.GLTerrain;
import com.example.dennis.leagueoflegions.gl.unit.GLUnit;
import com.example.dennis.leagueoflegions.model.Game;
import com.example.dennis.leagueoflegions.model.Player;
import com.example.dennis.leagueoflegions.model.unit.Base;

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

        Player p1 = new Player(game, blue);
        p1.addUnit(new Base(p1, 0f, 0f));
        game.addPlayer(p1);

        Player p2 = new Player(game, green);
        p2.addUnit(new Base(p2, 0f, 1500f));
        game.addPlayer(p2);

        Player p3 = new Player(game, red);
        p3.addUnit(new Base(p3, 1500f, 0f));
        game.addPlayer(p3);

        game.setCurrentPlayer(p1);

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
