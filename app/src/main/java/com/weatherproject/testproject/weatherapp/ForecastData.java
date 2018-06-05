package com.weatherproject.testproject.weatherapp;


public class ForecastData {
    private String date, day, textData, high, low;

    public  ForecastData(String date, String day, String textData, String high, String low){
        this.date =date;
        this.day = day;
        this.textData = textData;
        this.high = high;
        this.low =low;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getTextData() {
        return textData;
    }
}
