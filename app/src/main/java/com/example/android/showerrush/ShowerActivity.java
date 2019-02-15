package com.example.android.showerrush;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

public class ShowerActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower);
        chronometer = findViewById(R.id.chronometer);
        startChrono();

    }

    private void startChrono() {
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void start(View view){
        startChrono();

    }
    public void pauseChrono(View view){

        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }

    }
    public void resetChrono(View view){

        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void finish(View view){

    }

}
