package com.example.dennis.leagueoflegions;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
    }
}
