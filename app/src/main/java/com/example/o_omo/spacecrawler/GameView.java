package com.example.o_omo.spacecrawler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by o_omo on 2/14/2017.
 */

public class GameView extends SurfaceView implements Runnable {
    //context to be used in onTouchEvent to go back to main activity on game over
    Context context;
    //The media player objects to confiure the background music
    static MediaPlayer gameOnSOund;
    final MediaPlayer killedEnemySound;
    final MediaPlayer gameOverSound;
    //a screenX holder
    int screenX, screenY;
    //to count the number of misses
    int countMisses, friendCollision;
    //indicator that the enemy has just entered the game screen
    boolean flag, friendflag;
    //an indicator if the game is over
    public boolean isGameOver;
    //the score holder
    int score;
    //the high scores holder
    int highScore[] = new int[4];
    //using Shared Preferences to store the high Scores
    SharedPreferences sharedprefrences;
    //boolean variable to track if the game is currently playing or not
    volatile boolean playing;

    //initialize game thread
    private Thread gameThread = null;

    //adding a played
    private Player player;

    //These objects will be used for drawing
    private Paint paint, redPaint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //Create a reference to class friend
    private Friend friend;

    //Adding enemies object array
    private Enemy enemies;
    //adding 4 enemies
    private int enemyCount = 3;

    //adding a stars Array List
    private ArrayList<Star> stars = new ArrayList<Star>();
    //defining a boom object to display blast
    private Boom boom;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        //initializig the media players for the game sounds
        gameOnSOund = MediaPlayer.create(context, R.raw.gameon);
        killedEnemySound = MediaPlayer.create(context, R.raw.killedenemy);
        gameOverSound = MediaPlayer.create(context, R.raw.gameover);
        //starting the gameOn sound
        gameOnSOund.start();
        //initialize these variable in the constructor
        this.screenX = screenX;
        this.screenY = screenY;
        this.context = context;
        countMisses = 0;
        friendCollision =0;
        isGameOver = false;
        score= 0;
        sharedprefrences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);
        //initializing the array high scores with the previous values
        highScore[0] = sharedprefrences.getInt("Score1",0);
        highScore[1] = sharedprefrences.getInt("Score2",0);
        highScore[2] = sharedprefrences.getInt("Score3",0);
        highScore[3] = sharedprefrences.getInt("Score4",0);

        //Initializing player object
        player = new Player(context, screenX, screenY);

        //initialize drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
        redPaint = new Paint();

        //Adding specific number of stars to the i.e 100
        int starNums = 100;
        for(int i=0; i<starNums; i++){
            Star s = new Star(screenX,screenY);
            stars.add(s);
        }
        //initializing enemy object array
        enemies = new Enemy(context, screenX, screenY);
//        for(int i=0; i<enemyCount; i++){
//            enemies[i] = new Enemy(context,screenX,screenY);
//        }
        //initializing boom object
        boom = new Boom(context);

        //initializinf Friend class
        friend = new Friend(context, screenX, screenY);
    }

    @Override
    public void run() {
        while(playing){
            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control the game
            control();
        }

    }

    private void update() {
        //increment score as time passes
        score++;
        //update player position
        player.update();

        //setting boom outside the screen
        boom.setX(-250);
        boom.setY(-250);

        //Updating the stars with player speed
        for (Star s : stars){
            s.update(player.getSpeed());
        }
        if(enemies.getX()==screenX){
            flag=true;
        }
        if(friend.getX()==screenX){
            friendflag=true;
        }
        enemies.update(player.getSpeed());
        //updating the enemy coordinates with respect to player speed
//        for (int i=0; i<enemyCount; i++){
//            enemies[i].update(player.getSpeed());
            //if collision occur with player
            if(Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())){
                //display boom at that location
                boom.setX(enemies.getX());
                boom.setY(enemies.getY());
                //moving enemy outside the left edge and play a sound on collision
                killedEnemySound.start();
                enemies.setX(-200);
            }//Then when a player misses the enemy
        else{
                //if the enemy has just entered
                if(flag){
                    //if player's x coordinate is more than the enemy's x coordinate, i mean when enemy just pass the player
                    if( enemies.getDetectCollision().exactCenterX()<=20){
                        //increase enemy count missed
                        countMisses++;
                        //Setting the flag false so that the else pasrt is executed only when new enemy enter the screen
                        flag=false;
                        //if no of misses is equal to 5, then game is over
                        if (countMisses==5){
                            //setting playing false to stop the game
                            playing = false;
                            isGameOver = true;

                            //stopping the gameOn music
                            stopMusic();
                            //play the gameOver sound
                            gameOverSound.start();

                            //Assigning the score to the highscore integer array
                            for(int i=0; i<4; i++){
                                if(highScore[i]<score){
                                    final int finalI = i;
                                    highScore[i] = score;
                                    break;
                                }
                            }
                            //storing the scores through shard preferences
                            SharedPreferences.Editor e = sharedprefrences.edit();
                            for (int i=0; i<4; i++){
                                int j = i+1;
                                e.putInt("Score"+j, highScore[i]);
                            }
                            e.apply();
                        }
                    }
                }
            }
        //updating the friend ships coordinates
        friend.update(player.getSpeed());

        if(friendflag){
            //checking for a collision between player and a friend
            if(Rect.intersects(player.getDetectCollision(), friend.getDetectCollision())){
                //display the boom at the collision
                boom.setX(friend.getX());
                boom.setY(friend.getY());
                friend.setX(-200);
                friendCollision++;
                friendflag=false;
                //when you killed 3 friend ship
                if(friendCollision==3) {
                    //setting playing false to stop the game
                    playing = false;
                    //setting the isGameover true as game is over
                    isGameOver = true;
                    //stopping the gameOn music
                    stopMusic();
                    //play the gameOver sound
                    gameOverSound.start();

                    //Assigning the score to the highscore integer array
                    for(int i=0; i<4; i++){
                        if(highScore[i]<score){
                            final int finalI = i;
                            highScore[i] = score;
                            break;
                        }
                    }
                    //storing the scores through shard preferences
                    SharedPreferences.Editor e = sharedprefrences.edit();
                    for (int i=0; i<4; i++){
                        int j = i+1;
                        e.putInt("Score"+j, highScore[i]);
                    }
                    e.apply();
                }
            }

        }

    }
    private void draw() {
        //checking if surface is valid
        if(surfaceHolder.getSurface().isValid()){
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing a background colour for canvas
            canvas.drawColor(Color.BLACK);
            //setting the paint colour to white to draw the stars
            paint.setColor(Color.WHITE);
            paint.setTextSize(20);
            redPaint.setColor(Color.RED);
            redPaint.setTextSize(20);
            //drawing all stars
            for (Star s : stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(),s.getY(),paint);
            }

            //drawing the score on the game screen
            paint.setTextSize(20);
            canvas.drawText("Score: "+score,60,50,paint);
            redPaint.setTextSize(20);
            canvas.drawText("Enemy Missed: "+countMisses+"  Friend Killed: "+friendCollision,screenX-350,50,redPaint);
            //Drawing the player
            canvas.drawBitmap(player.getBitmap(),player.getX(),player.getY(),paint);
            //drawing the enemies

                canvas.drawBitmap(enemies.getBitmap(), enemies.getX(), enemies.getY(),paint);

            //drawing boom object
            canvas.drawBitmap(boom.getBitmap(), boom.getX(), boom.getY(), paint);

            //drawing friend
            canvas.drawBitmap(friend.getBitmap(), friend.getX(), friend.getY(), paint);

            //drawing game over when the game is over
            if(isGameOver){
                paint.setTextSize(90);
                paint.setTextAlign(Paint.Align.CENTER);
                DashPathEffect dash = new DashPathEffect(new float[]{15.0f,5.0f},1.0f);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);


                paint.setPathEffect(dash);
                paint.setStrokeWidth(2f);


                int ypos = (int) ((canvas.getHeight()/2) - ((paint.descent() + paint.ascent()) /2));
                canvas.drawText("Game Over", canvas.getWidth()/2,ypos,paint);
            }
            //unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    private void control() {
        try {
            gameThread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void pause() {
        //When the game is paused, setting the variable to false and stop the thread
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void resume() {
        //When the game resume, start the thread and set isplaying to true
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                //Stopping the boosting when screen is released
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                //boosting the space jet when screen is pressed
                player.setBoosting();
                break;
        }
        //if the game's over, tapping on game over screen send you to Mainactivity
        if (isGameOver){
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));
            }
        }
        return true;
    }
    public static void stopMusic(){
        gameOnSOund.stop();
    }
}
