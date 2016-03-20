package com.example.dennis.leagueoflegions.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dennis.leagueoflegions.R;
import com.example.dennis.leagueoflegions.model.Game;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    public void startGame(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, GameActivity.class);

        switch (view.getId()) {
            case R.id.button_campaign:
                intent.putExtra(Game.GAME_TYPE, Game.GameType.CAMPAIGN);
                break;
            case R.id.button_multiplayer:
                intent.putExtra(Game.GAME_TYPE, Game.GameType.MULTIPLAYER);
                break;
            case R.id.button_tutorial:
                intent.putExtra(Game.GAME_TYPE, Game.GameType.TUTORIAL);
                break;
        }
        startActivity(intent);
//        finish();
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, GLActivity.class);
        startActivity(intent);
    }
}
