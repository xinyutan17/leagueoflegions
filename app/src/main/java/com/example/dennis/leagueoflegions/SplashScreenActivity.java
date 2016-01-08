package com.example.dennis.leagueoflegions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
                intent.putExtra(Game.GAME_MODE, "campaign");
                break;
            case R.id.button_multiplayer:
                intent.putExtra(Game.GAME_MODE, "multiplayer");
                break;
            case R.id.button_tutorial:
                intent.putExtra(Game.GAME_MODE, "tutorial");
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
