package com.example.android.showerrush;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.showerrush.adapters.ShowerRecycleAdapter;
import com.example.android.showerrush.model.Shower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private List<Shower> showers;
    private RecyclerView recyclerView;
    private TextView recordT, totalT, averageT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showers = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleView);
        recordT = findViewById(R.id.record);
        totalT = findViewById(R.id.totalShowers);
        averageT = findViewById(R.id.average);
        getShowers();
        setRecord();
        setTotalShowers();
        setAverage();
        setRecyclerView(showers);
    }

    public void goToShower(View view){

        Intent intent = new Intent(this, ShowerActivity.class);

        startActivity(intent);
    }

    public void setTotalShowers(){

        totalT.setText(String.valueOf(showers.size()));
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

        long curr;
        long min = showers.get(0).getLength();

        for(int i=1; i<showers.size();i++){

            curr = showers.get(i).getLength();
            if(curr<min){
                min = curr;
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
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray showersJSON = obj.getJSONArray("showers");
            for(int i = 0; i < showersJSON.length(); i++) {
                JSONObject jsonObject = showersJSON.getJSONObject(i);

                Shower shower = new Shower();
                shower.setId(jsonObject.getInt("id"));
                shower.setDate(jsonObject.getString("date"));
                shower.setLength(jsonObject.getInt("length"));

                showers.add(shower);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}
