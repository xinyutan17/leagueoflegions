package com.example.dennis.leagueoflegions;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        String game_mode = extras.getString(Game.GAME_MODE);
    }
}
