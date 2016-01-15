package com.example.dennis.leagueoflegions;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {

    private GameView gameView;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
//        String game_mode = extras.getString(Game.GAME_TYPE);
        Game.GameType gameType = (Game.GameType) getIntent().getSerializableExtra(Game.GAME_TYPE);

        game = new Game();
        Map map = new Map(this);
        gameView = new GameView(this, game, map);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
