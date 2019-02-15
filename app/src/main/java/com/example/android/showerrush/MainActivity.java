package com.example.android.showerrush;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {

    Date date = new GregorianCalendar(2019, Calendar.FEBRUARY, 11).getTime();
    Shower shower1 = new Shower(1, 30, date);
    Shower shower2 = new Shower(2, 20, date);

    private List<Shower> showers;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showers = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        getShowers();
        setRecyclerView(showers);
    }

    public void goToShower(View view){

        Intent intent = new Intent(this, ShowerActivity.class);

        startActivity(intent);
    }

    private void setRecyclerView(List<Shower> showers){
        ShowerRecycleAdapter showerRecycleAdapter = new ShowerRecycleAdapter(this, showers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(showerRecycleAdapter);
    }

    public String loadJSONFromAsset() {
        String json = null;
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
