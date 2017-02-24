package com.example.o_omo.spacecrawler;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //GameView Object
    private GameView gameView;
    static MediaPlayer buttonSound;
    //Initialize Image Button
    private ImageButton buttonPlay;
    //high score button
    private ImageButton buttonScore;
    volatile boolean gameAlive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();
        //getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        //Initializing game view object
        gameView = new GameView(this,size.x,size.y);

        //Setting game orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //initialize button sound
        buttonSound = MediaPlayer.create(MainActivity.this, R.raw.buttonsound);
        //Casting Image Button
        buttonPlay =(ImageButton) findViewById(R.id.buttonPlay);

        //Adding click listener to button play
        buttonPlay.setOnClickListener(this);

        //initialize the highscore button
        buttonScore = (ImageButton) findViewById(R.id.buttonScore);
        //Adding click listener to button score
        buttonScore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==buttonPlay){
            //Starting game activity when the ImageButton Play is clicked
            buttonSound.start();
            gameAlive = true;
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        }
        if(view==buttonScore){
            //transition from Mainactivityto HighScore activity
            startActivity(new Intent(MainActivity.this, high_score.class));

        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("SURE TO QUIT?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (gameAlive){
                            if (gameView.gameOnSOund.isPlaying()){
                                gameView.gameOnSOund.stop();
                                gameView.gameOnSOund.release();
                            }
                        }
                        Intent startMain = new Intent (Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameAlive){
            if (gameView.gameOnSOund.isPlaying()){
                gameView.gameOnSOund.stop();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameAlive) {
            if (!gameView.gameOnSOund.isPlaying()) {
                gameView.gameOnSOund.start();
            }
        }
    }

}

