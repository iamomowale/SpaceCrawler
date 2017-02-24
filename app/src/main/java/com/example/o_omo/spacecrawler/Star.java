package com.example.o_omo.spacecrawler;

import java.util.Random;

/**
 * Created by o_omo on 2/14/2017.
 */

public class Star {
    private int x, y, speed, maxX, maxY, minX, minY;
    public Star(int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;
        minX =0;
        minY=0;
        Random generator = new Random();
        speed = generator.nextInt(10);

        //generating a random coordinate but keeping coordinate inside the screen  size
        x=generator.nextInt(maxX);
        y=generator.nextInt(maxY);
    }

    public void update(int playerSpeed){
        //animating the star horizontally left side by decreasing x coordinate with player speed
        x-=playerSpeed;
        x-=speed;
        //if the star reached the left edge of the screen
        if (x<0){
            //again starting the star from right edge, this will give an infinite scrolling background effect
            x=maxX;
            Random generator = new Random();
            y= generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }
    }

    public float getStarWidth(){
        //making the star width random so that it will give a real look
        float minX = 1.0f;
        float maxX = 4.0f;
        Random rand = new Random();
        float finalX = rand.nextFloat() * (maxX - minX) + minX;
        return finalX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
