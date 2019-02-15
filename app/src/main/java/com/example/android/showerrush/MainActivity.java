package com.example.android.showerrush;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import com.example.android.showerrush.adapters.ShowerRecycleAdapter;
import com.example.android.showerrush.model.Shower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Shower> showers;
    private RecyclerView recyclerView;

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
            showers = readShowersArray(reader);
            reader.close();
            in.close();
//            reader.beginObject();

//            JSONObject obj = new JSONObject(loadJSONFromAsset());
//            JSONArray showersJSON = obj.getJSONArray("showers");
//            for(int i = 0; i < showersJSON.length(); i++) {
//                JSONObject jsonObject = showersJSON.getJSONObject(i);
//
//                Shower shower = new Shower();
//                shower.setId(jsonObject.getInt("id"));
//                shower.setDate(jsonObject.getString("date"));
//                shower.setLength(jsonObject.getInt("length"));
//
//                showers.add(shower);
//            }
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
