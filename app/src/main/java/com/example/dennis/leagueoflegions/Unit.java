package com.example.dennis.leagueoflegions;

import android.content.Context;
import android.graphics.Paint;

public class Unit {
    private int x, y;
    private int speed;

    public Unit()
    {
        speed = 1;
        x = 10;
        y = 10;
    }

    public void update(){
        x += speed;
        y += speed;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
