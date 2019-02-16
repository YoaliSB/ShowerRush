package com.example.android.showerrush;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.showerrush.adapters.ShowerRecycleAdapter;
import com.example.android.showerrush.model.Shower;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Shower> showers;
    private RecyclerView recyclerView;
    private TextView recordT, totalT, averageT,lts;
    private final static double ltsPerSecondOMS = 0.33;

    private final static String FILE_NAME = "showers.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showers = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recordT = findViewById(R.id.record);
        totalT = findViewById(R.id.totalShowers);
        averageT = findViewById(R.id.average);
        lts = findViewById(R.id.lts);

        getShowers();
        setRecord();
        setTotalShowers();
        setAverage();
        setSavedLts();
        setRecyclerView(showers);
    }

    public void goToShower(View view){

        Intent intent = new Intent(this, ShowerActivity.class);
        intent.putExtra("showers", showers);

        startActivity(intent);
    }

    public void setTotalShowers(){

        totalT.setText(String.valueOf(showers.size()));
    }

    public void setSavedLts(){

        long sum = 0;

        for(int i=0; i<showers.size();i++){

            sum+=showers.get(i).getLength();
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(sum);
        double consumedLts = seconds * ltsPerSecondOMS;
        double savedLts = 200 - consumedLts;

        if(savedLts<0){
            lts.setTextColor(Color.RED);
        }
        lts.setText(String.valueOf(savedLts));
    }

    public void setAverage(){

        long sum = 0;

        for(int i=0; i<showers.size();i++){

            sum+=showers.get(i).getLength();
        }

        long average = sum/showers.size();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(average);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(average)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(average));

        String recordStr = String.format("%02d:%02d", minutes, seconds);

        averageT.setText(recordStr);
    }

    public void setRecord(){
        long min = showers.get(0).getLength();
        if(showers.size()>0){
            long curr;
            for(int i=1; i<showers.size();i++){
                curr = showers.get(i).getLength();
                if(curr<min){
                    min = curr;
                }
            }
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(min);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(min)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(min));

        String recordStr = String.format("%02d:%02d", minutes, seconds);
        recordT.setText(recordStr);
    }

    private void setRecyclerView(List<Shower> showers){
        ShowerRecycleAdapter showerRecycleAdapter = new ShowerRecycleAdapter(this, showers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(showerRecycleAdapter);
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("showers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void getShowers(){
        try {
            File JSONfile = new File(getExternalFilesDir(null).getPath(), FILE_NAME);
            JSONfile.getParentFile().mkdirs();
            JSONfile.createNewFile();
            FileInputStream in = new FileInputStream(JSONfile);
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            Log.d("READING", reader.peek().toString());
            showers = (ArrayList<Shower>) readShowersArray(reader);
            reader.close();
            in.close();
        } catch ( ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<Shower> readShowersArray(JsonReader reader) throws IOException, ParseException {
        List<Shower> showers = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            showers.add(readShower(reader));
        }
        reader.endArray();
        return showers;
    }


    public Shower readShower(JsonReader reader) throws IOException, ParseException {
        String date = null;
        Long length = new Long(0);
        int id = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("length")) {
                length = reader.nextLong();
            } else if (name.equals("date")) {
                date = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        Shower shower = new Shower();
        shower.setId(id);
        shower.setDate(date);
        shower.setLength(length);

        return shower;
    }

    private String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
}
