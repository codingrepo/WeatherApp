package com.weatherproject.testproject.weatherapp;


import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    EditText etCity;
    Button submitButton;
    ProgressBar progressBar;
    ListView listView;
    CustomListAdapter listAdapter;
    Handler handler;
    public static final String CITY ="city";
    public static final String GSON_OBJ ="gson_obj";
    ArrayList<ForecastData> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        etCity = (EditText)findViewById(R.id.editCityName);
        submitButton =(Button) findViewById(R.id.button);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);
        listView =(ListView) findViewById(R.id.listViewData);

        if(savedInstanceState!=null && savedInstanceState.containsKey(CITY)){
            etCity.setText(savedInstanceState.getString(CITY));
        }
        if(savedInstanceState!=null && savedInstanceState.containsKey(GSON_OBJ)){
            Type type = new TypeToken<ArrayList<ForecastData>>(){}.getType();
            String jsonString = savedInstanceState.getString(GSON_OBJ, "");
            arrayList = (new Gson()).fromJson(jsonString, type);
            listAdapter = new CustomListAdapter(this, R.layout.weather_data_row, arrayList);
            listView.setAdapter(listAdapter);

        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = etCity.getText().toString();
                arrayList.clear();
                findWeather(city);

            }
        });
    }

    //Handling app rotation for city name
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String city = etCity.getText().toString();
        outState.putString(CITY, city);
        if(arrayList.size()!=0) {
            Gson gson = new Gson();
            String json = gson.toJson(arrayList);
            outState.putString(GSON_OBJ, json);
        }
        super.onSaveInstanceState(outState);
    }

    public void findWeather(final String city){
        progressBar.setVisibility(View.VISIBLE);
        new Thread(){
            public void run(){

                final JSONObject json = FetchData.getJSON(getApplicationContext(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        progressBar.setVisibility(View.INVISIBLE);
        try {
            JSONObject details = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
            JSONObject item = details.getJSONObject("item");
            JSONArray forecast = item.getJSONArray("forecast");
            for(int i=0; i<forecast.length();i++){
                JSONObject data = forecast.getJSONObject(i);
                String date = data.getString("date");
                String day =data.getString("day");
                String high =data.getString("high");
                String low =data.getString("low");
                String textData =data.getString("text");
                arrayList.add(new ForecastData(date,day,textData,high,low));
            }

            listAdapter = new CustomListAdapter(this, R.layout.weather_data_row, arrayList);
            listView.setAdapter(listAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
