package com.example.android.showerrush;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonWriter;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.example.android.showerrush.model.Shower;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class ShowerActivity extends AppCompatActivity {

    public int id;
    public ArrayList<Shower> showers;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;
    private Button button;

    private final static String FILE_NAME = "showers.json";
    private final static String TAG = "showeract";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower);
        chronometer = findViewById(R.id.chronometer);
        button = findViewById(R.id.stop);
        showers = getIntent().getParcelableArrayListExtra("showers");
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
        else {
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }
    }

    public void finishShower(View view) throws ParseException, IOException {
        long length = SystemClock.elapsedRealtime() - chronometer.getBase() - pauseOffset;
        Date date = GregorianCalendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
        String strDate = dateFormat.format(date);

        Shower shower = new Shower();
        shower.setLength(length);
        shower.setDate(strDate);
        shower.setId(showers.size());

        showers.add(shower);
        try {
            saveShower();
        }
        catch  (Exception e){
            e.printStackTrace();
        }
        finally {
            finish();
        }
    }

    public void writeShowersArray(JsonWriter writer) throws IOException {
        writer.beginArray();
        for (Shower shower : showers) {
            writeShower(writer, shower);
        }
        writer.endArray();
    }


    public void saveShower()throws IOException{
            File JSONfile = new File(getExternalFilesDir(null).getPath(), FILE_NAME);
            JSONfile.getParentFile().mkdirs();
            JSONfile.createNewFile();
            OutputStream out = new FileOutputStream(JSONfile);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            writeShowersArray(writer);
            writer.flush();
            writer.close();
            out.close();
    }

    public void writeShower(JsonWriter writer, Shower shower) throws IOException {
        writer.beginObject();
        writer.name("id").value(shower.getId());
        writer.name("length").value(shower.getLength());
        writer.name("date").value(shower.getStrDate());
        writer.endObject();
    }

}
