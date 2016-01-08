package com.example.dennis.leagueoflegions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class Map extends SurfaceView
{
    private Bitmap[][] tiledImages;
    private Bitmap finalImage;

    public Map(Context context)
    {
        super(context);
        tiledImages = new Bitmap[2][1];
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
        return combineImages(tiledImages);
    }

    private Bitmap combineImages(Bitmap [][] images)
    {
        Bitmap[] parts = new Bitmap[20];
        for(int j = 0; j < parts.length; j++)
        {
            parts[j] = getImage();
        }
        Bitmap result = Bitmap.createBitmap(parts[0].getWidth() * 2, parts[0].getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();

        int counter = 0;

        for (int x = 0; x < canvas.getWidth(); x += 200) {
            for (int y = 0; y < canvas.getHeight(); y += 200) {
                canvas.drawBitmap(parts[counter], x, y, paint);
                counter++;
                if(counter == 20)
                    counter = 0;
            }
        }

        return result;
    }

    private Bitmap getImage() {

        int value = (int)(Math.random() * 12);
        Bitmap bm = null;

        switch (value){
            case 0:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.forest_tile);
                break;
            case 1:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.desert_tile);
                break;
            case 2:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.forest_tile);
                break;
            case 3:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.ice_tile);
                break;
            case 4:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.jungle_tile);
                break;
            case 5:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.mountain_tile);
                break;
            case 6:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.swamp_tile);
                break;
            case 7:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.wall_tile);
                break;
            case 8:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.water_tile);
                break;
            case 9:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.ground_tile);
                break;
            case 10:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.ground_tile);
                break;
            case 11:
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.ground_tile);
                break;
        }
        return bm;
    }
}