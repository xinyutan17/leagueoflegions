package com.example.dennis.leagueoflegions.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceView;

import com.example.dennis.leagueoflegions.R;

import java.util.ArrayList;

public class Map extends SurfaceView
{
    private Bitmap finalImage;
    public static final int mapWidth = 50;
    public static final int mapHeight = 50;
    private TerrainType [][] map;

    public enum TerrainType {
        CLIFF, DESERT, FOREST, GROUND, ICE, JUNGLE, MOUNTAIN, SWAMP, WALL, WATER
    }

    public Map(Context context)
    {
        super(context);
        map = new TerrainType[mapWidth][mapHeight];
    }

    public Bitmap getFinalImage()
    {
        return finalImage;
    }

    public void setFinalImage()
    {
        finalImage = loadImage();
    }

    public Bitmap loadImage()
    {
        initializeMap();
        return combineImages();
    }

    public void initializeMap()
    {
        setToGround();

        int x = 0;
        int y = 0;

        for(int j = 0; j < 10; j++)
        {
            setRandomPoints(10, x, y);
            x += 20;
            y += 20;
        }
    }

    public void setToGround()
    {
        for(int x = 0; x < map.length; x++)
        {
            for(int y = 0; y < map[0].length; y++)
            {
                map[x][y] = TerrainType.GROUND;
            }
        }
    }

    public void setRandomPoints(int radius, int x, int y)
    {
        for(int j = 0; j < mapWidth; j++)
        {
            for(int i = 0; i < mapHeight; i++)
            {
                double distance = Math.sqrt((x - j) * (x - j) + (y - i) * (y - i));
                if(distance < radius)
                {
                    map[j][i] = getTerrainType();
                }
            }
        }
    }

    private Bitmap combineImages()
    {
        Bitmap [][] drawings = new Bitmap[mapWidth][mapHeight];
        for(int j = 0; j < mapWidth; j++)
        {
            for(int i = 0; i < mapHeight; i++)
            {
                Bitmap bm = getImage(map[j][i]);
                bm = getResizedBitmap(bm, 100, 100);
                drawings[j][i] = bm;
            }
        }

        Bitmap result = Bitmap.createBitmap(drawings[0][0].getWidth() * 2, drawings[0][0].getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();

        int x_counter = 0;
        int y_counter = 0;

        for (int x = 0; x < canvas.getWidth(); x += 20) {
            for (int y = 0; y < canvas.getHeight(); y += 20) {
                canvas.drawBitmap(drawings[x_counter][y_counter], x, y, paint);
                x_counter++;
                if(x_counter == map.length)
                    x_counter = 0;
            }
            y_counter++;


            if(y_counter == map[0].length)
                y_counter = 0;
        }

        return result;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private TerrainType getTerrainType()
    {
        int value = (int)(Math.random() * 9);

        switch (value) {
            case 0:
                return TerrainType.CLIFF;
            case 1:
                return TerrainType.DESERT;
            case 2:
                return TerrainType.FOREST;
            case 3:
                return TerrainType.WATER;
            case 4:
                return TerrainType.ICE;
            case 5:
                return TerrainType.JUNGLE;
            case 6:
                return TerrainType.MOUNTAIN;
            case 7:
                return TerrainType.SWAMP;
            case 8:
                return TerrainType.WALL;
            case 9:
                return TerrainType.WATER;
        }
        return TerrainType.SWAMP;
    }

    private Bitmap getImage(TerrainType terrain) {

        Bitmap bm = null;
        switch (terrain){
            case CLIFF:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.cliff_tile);
                break;
            case DESERT:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.desert_tile);
                break;
            case FOREST:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.forest_tile);
                break;
            case GROUND:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.ground_tile);
                break;
            case ICE:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.ice_tile);
                break;
            case JUNGLE:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.jungle_tile);
                break;
            case MOUNTAIN:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.mountain_tile);
                break;
            case SWAMP:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.swamp_tile);
                break;
            case WALL:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.wall_tile);
                break;
            case WATER:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.water_tile);
                break;
        }
        return bm;
    }
}
