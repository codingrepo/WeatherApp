package com.weatherproject.testproject.weatherapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<ForecastData> {
    private final Context context;
    ArrayList<ForecastData> arrayList = new ArrayList<>();
    TextView date;
    TextView textData;
    TextView high;
    TextView low;

    public CustomListAdapter(@NonNull Context context, int resource, ArrayList<ForecastData> arrayList) {
        super(context, resource, arrayList);
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.weather_data_row, parent, false);

        date = (TextView) rowView.findViewById(R.id.textViewDate);
        textData = (TextView) rowView.findViewById(R.id.textViewText);
        high = (TextView) rowView.findViewById(R.id.textViewHigh);
        low = (TextView) rowView.findViewById(R.id.textViewLow );
        String dateS = arrayList.get(position).getDate();
        String dayS = arrayList.get(position).getDay();
        String highS = arrayList.get(position).getHigh();
        String lowS = arrayList.get(position).getLow();
        String textDataS = arrayList.get(position).getTextData();

        String dateDay= dateS+", "+dayS;
        date.setText(dateDay);
        textData.setText(textDataS);
        String highAppend = "High "+highS+"C";
        high.setText(highAppend);
        String lowAppend = "Low "+lowS+"C";
        low.setText(lowAppend);

        return rowView;
    }
}
