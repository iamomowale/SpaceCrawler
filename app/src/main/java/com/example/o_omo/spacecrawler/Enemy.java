package com.example.o_omo.spacecrawler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by o_omo on 2/14/2017.
 */

public class Enemy {
    //bitmap for the enemy from Drawable resource file
    private Bitmap bitmap;

    //x and y coordinates
    private int x, y;

    //enemy speed
    private int speed = -1;

    //min and max coordinates to keep the enemy inside the screen
    private int maxX, maxY, minX, minY;

    //creating a rect object which will be use to detect collision between our sheep and enemy sheep
    private Rect detectCollision;

    public Enemy(Context context, int screenX, int screenY) {
        //initialize bitmap to drawable resources
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        //initialize min and max coordinates
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        //generating a random coordinate to add enemies
        Random generator = new Random();
        speed = generator.nextInt(6)+10;
        x=screenX;
        y= generator.nextInt(maxY) - bitmap.getHeight();

        //initializing rect object
        detectCollision = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
    }

    public void update(int playerSpeed){
        //decreasing x coordinate so that enemy will move right to left
        x-=playerSpeed;
        x-=speed;
        //if the enemy reaches the left edge
        if(x<minX-bitmap.getWidth()){
            //adding the enemy again to the right edge
            Random generator = new Random();
            speed = generator.nextInt(10)+10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }
        //adding the top, left, bottom and right to the rect object on update
        detectCollision.left = x;
        detectCollision.top =y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }
    //adding a setter to x coordinate so that we can change it after collission
    public void setX(int x){
        this.x = x;
    }
    //getter for getting the real object
    public Rect getDetectCollision(){
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}


