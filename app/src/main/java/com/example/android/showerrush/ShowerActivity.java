package com.example.android.showerrush;

import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class ShowerActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower);
        chronometer = findViewById(R.id.chronometer);
        button = findViewById(R.id.stop);
        startChrono();

    }

    private void startChrono() {
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
            chronometer.start();
            running = true;

            button.setBackgroundColor(getResources().getColor(R.color.yellow));
            button.setText("PAUSAR");
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

            button.setBackgroundColor(getResources().getColor(R.color.green));
            button.setText("REANUDAR");
        }
        else{
            startChrono();
        }

    }
    public void resetChrono(View view){

        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void finish(View view){

    }

}
