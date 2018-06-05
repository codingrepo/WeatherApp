package com.weatherproject.testproject.weatherapp;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchData {

    private static final String YAHOO_API_ENDPOINT = "https://query.yahooapis.com/v1/public/yql?q=%s&format=json";

    public static JSONObject getJSON(Context context, String city){
        String unit ="c";
        try {
            String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='" + unit + "'", city);

            URL url = new URL(String.format(YAHOO_API_ENDPOINT, Uri.encode(YQL)));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer();
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
            JSONObject data = new JSONObject(json.toString());
            JSONObject queryResults = data.optJSONObject("query");

            int count = queryResults.optInt("count");

            if (count == 0) {
                return null;
            }

            return data;
        }catch(Exception e){

            return null;
        }
    }
}
