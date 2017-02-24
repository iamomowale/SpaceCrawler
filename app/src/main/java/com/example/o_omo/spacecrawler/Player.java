package com.example.o_omo.spacecrawler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by o_omo on 2/14/2017.
 */

public class Player {
    //Bitmap to get character from image
    private Bitmap bitmap;

    //coordinates
    private int x, y;

    //motion speed of the character
    private int speed = 0;
    //boolean variable to track if the ship is boosting or not
    private boolean boosting;
    //gravity value to add gravity effect on the ship
    private final int GRAVITY = -10;
    //controlling Y coordinate so that ship won't go outside the screen
    private int maxY, minY;
    //limit the bouds of the ship's speed
    private final int MIN_SPEED = 1, MAX_SPEED = 20;
    //creating a rect object which will be use to detect collision between our sheep and enemy sheep
    private Rect detectCollision;

    //constructor
    public Player(Context context, int screenX, int screenY) {
        x=75;
        y=50;
        speed=1;

        //getting bitmap from drawable resources
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        //calculating maxY
        maxY = screenY - bitmap.getHeight();
        //top edge's y point is 0 so min y will always be zero
        minY = 0;
        //setting the boosting value to false initialy
        boosting = false;
        //initializing rect object
        detectCollision = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
    }
    //setting boosting true
    public void setBoosting(){
        boosting = true;
    }
    //setting boosting true
    public void stopBoosting(){
        boosting = false;
    }

    //method to update coordinate of character
    public void update(){
        //if the ship is boosting
        if(boosting){
            //speeding up the ship
            speed+=1;
        }else{
            //slowing down if not boosting
            speed-=5;
        }
        //controlling the top speed
        if(speed>MAX_SPEED){
            speed=MAX_SPEED;
        }
        //controlling min speed so that it wont stop completely
        if(speed<MIN_SPEED){
            speed=MIN_SPEED;
        }
        //moving the ship down
        y-= speed + GRAVITY;
        //but controlling it so that it wont go off the screen
        if(y<minY){
            y=minY;
        }
        if(y>maxY){
            y=maxY;
        }
        //updating x coordinate
        //x++;
        //adding the top, left, bottom and right to the rect object on update
        detectCollision.left = x;
        detectCollision.top =y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }
    //getter for getting the real object
    public Rect getDetectCollision(){
        return detectCollision;
    }

    public int getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }


}
