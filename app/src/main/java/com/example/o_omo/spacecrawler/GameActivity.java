package com.example.o_omo.spacecrawler;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

public class GameActivity extends AppCompatActivity {
    //declaring GameView
    private GameView gameView;
    ImageButton yesNow, noNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();
        //getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        //Initializing game view object
        gameView = new GameView(this,size.x,size.y);
        //Casting dialog images
        yesNow = (ImageButton) findViewById(R.id.yesNowButton);
        noNow = (ImageButton) findViewById(R.id.noNowButton);

        //adding it to content view
        setContentView(gameView);
        //Setting game orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //resuming game when activity resume

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public void onBackPressed() {
        gameView.stopMusic();
        gameView.pause();
//        final Dialog dialog = new Dialog(GameActivity.this);
//        dialog.setContentView(R.layout.custom_dialog);
//        dialog.setTitle("Paused!");
//        dialog.show();
//        yesNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GameView.stopMusic();
//                startActivity(new Intent(GameActivity.this, MainActivity.class));
//            }
//        });
//        noNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                gameView.gameOnSOund.start();
//                gameView.resume();
//            }
//        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(!gameView.isGameOver){
        builder.setMessage("Your Current Score Is "+gameView.score+"\n     SURE TO QUIT?")
                .setCancelable(false)
                .setTitle("PAUSED...")
                .setIcon(R.mipmap.my_launcher)
                .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gameView.gameOnSOund.stop();
                        startActivity(new Intent(GameActivity.this, MainActivity.class));

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                gameView.resume();
            }
        });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            GameView.stopMusic();
            startActivity(new Intent(GameActivity.this, MainActivity.class));
        }

    }
}
