package com.example.o_omo.spacecrawler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by o_omo on 2/14/2017.
 */

public class Boom {
    //bitmap object
    private Bitmap bitmap;
    //coordinate variables
    private int x, y;

    //constructor
    public Boom(Context context){
        //getting boom image from drawable resources
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.boom);
        //setting the coordinate outside the screen so that it wont show up in the screen,
        //thus it will only be visible for some miliseconds after collision
        x = -250;
        y = -250;
    }

    //setters for x and y to make it visible at the place of collision
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    //getters

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
