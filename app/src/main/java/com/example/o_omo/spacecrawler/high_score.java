package com.example.o_omo.spacecrawler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class high_score extends AppCompatActivity {

    TextView textView,textView2,textView3,textView4;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        //Setting game orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //initializing the textViews
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        sharedPreferences = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //setting the values to the textViews
        textView.setText("1.  "+sharedPreferences.getInt("Score1",0));
        textView2.setText("2.  "+sharedPreferences.getInt("Score2",0));
        textView3.setText("3.  "+sharedPreferences.getInt("Score3",0));
        textView4.setText("4.  "+sharedPreferences.getInt("Score4",0));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startActivity(new Intent(high_score.this, MainActivity.class));
        return true;
    }
}
