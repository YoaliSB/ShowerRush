package com.example.android.showerrush;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import com.example.android.showerrush.model.Shower;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class ShowerActivity extends AppCompatActivity {

    public int id;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    private final static String FILE_NAME = "showers.json";
    private final static String TAG = "showeract";

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

    public void finish(View view) throws ParseException, IOException {
        long length = SystemClock.elapsedRealtime() - chronometer.getBase();
        Date date = GregorianCalendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
        String strDate = dateFormat.format(date);

        Shower shower = new Shower();
        shower.setLength(length);
        shower.setDate(strDate);
        shower.setId(3);

        List<Shower> showers = new LinkedList<>();
        showers.add(shower);

        saveShower(showers);
    }

    public void writeShowersArray(JsonWriter writer, List<Shower> showers) throws IOException {
        writer.beginArray();
        for (Shower shower : showers) {
            writeShower(writer, shower);
        }
        writer.endArray();
    }


    public void saveShower(List<Shower> showers)throws IOException{
            File JSONfile = new File(getExternalFilesDir(null).getPath(), FILE_NAME);
            JSONfile.getParentFile().mkdirs();
            JSONfile.createNewFile();
            OutputStream out = new FileOutputStream(JSONfile);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            writeShowersArray(writer, showers);
            writer.close();
            writer.flush();
            out.close();

//        Path filepath = Paths.get(getExternalFilesDir(null).getPath(), FILE_NAME);
//        try {
//            JsonWriter writer = new JsonWriter(new FileWriter(filepath.toFile()));
//            GSON gson
//            gson.toJson(shower, Shower.class, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void writeShower(JsonWriter writer, Shower shower) throws IOException {
        writer.beginObject();
        writer.name("id").value(shower.getId());
        writer.name("length").value(shower.getLength());
        writer.name("date").value(shower.getStrDate());
        writer.endObject();
    }

}
