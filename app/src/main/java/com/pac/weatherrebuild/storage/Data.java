package com.pac.weatherrebuild.storage;

import android.content.SharedPreferences;

import com.pac.weatherrebuild.model.DayForecast;
import com.pac.weatherrebuild.model.Forecast;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static final String REQUEST_TIME = "requestTime";

    private final String temp = "temp";
    private final String tempHi = "tempHi";
    private final String tempLo = "tempLo";
    private final String cloudCover = "cloudCover";
    private final String precipitation = "precipitation";

    private final String listSize = "listSize";
    private final String maxTemp = "maxTemp";
    private final String minTemp = "minTemp";

    SharedPreferences sharedPref;

    public Data(SharedPreferences sharedPref){
        this.sharedPref = sharedPref;
    }

    public void writeRequestTime(String time){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(REQUEST_TIME,time);
        editor.apply();
    }

    public void writeForecast(Forecast forecast){
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(temp, forecast.getTemperature());
        editor.putInt(tempHi, forecast.getHighTemperature());
        editor.putInt(tempLo, forecast.getLowTemperature());
        editor.putInt(cloudCover, forecast.getCloudCover().get(0));
        editor.putInt(precipitation, forecast.getPrecipitation().get(0));

        editor.putInt(listSize, forecast.getDays().size());
        editor.putInt(maxTemp, forecast.getMaxTemperature());
        editor.putInt(minTemp, forecast.getMinTemperature());
        for(int i=0; i<forecast.getDays().size(); i++){
            String day = "day" + i;
            editor.putString(day, forecast.getDays().get(i));
            editor.putInt(day + "_max", forecast.getMaxArray().get(i));
            editor.putInt(day + "_min", forecast.getMinArray().get(i));
            editor.putString(day + "_sky", forecast.getSkies().get(i));
        }

        editor.apply();
    }

    public String readRequestTime(){
        return sharedPref.getString(REQUEST_TIME,"");
    }

    public Forecast readForecast(){
        Forecast forecast = new Forecast();

        //24 Hour
        forecast.setTemperature(sharedPref.getInt(temp,0));
        forecast.setHighTemperature(sharedPref.getInt(tempHi,0));
        forecast.setLowTemperature(sharedPref.getInt(tempLo,0));

        List<Integer> cloud = new ArrayList<>();
        cloud.add(sharedPref.getInt(cloudCover,0));
        List<Integer> precip = new ArrayList<>();
        precip.add(sharedPref.getInt(precipitation,0));

        forecast.setCloudCover(cloud);
        forecast.setPrecipitation(precip);

        //7Day
        forecast.setMaxTemperature(sharedPref.getInt(maxTemp,0));
        forecast.setMinTemperature(sharedPref.getInt(minTemp,0));

        int size = sharedPref.getInt(listSize,0);
        List<Integer> max = new ArrayList<>();
        List<Integer> min = new ArrayList<>();
        List<String> days = new ArrayList<>();
        List<String> skies = new ArrayList<>();
        for(int i=0; i<size; i++){
            String day = "day" + i;
            max.add(sharedPref.getInt(day + "_max",0));
            min.add(sharedPref.getInt(day + "_min", 0));
            days.add(sharedPref.getString(day,""));
            skies.add(sharedPref.getString(day + "_sky",""));
        }

        forecast.setMaxArray(max);
        forecast.setMinArray(min);
        forecast.setDays(days);
        forecast.setSkies(skies);

        return forecast;
    }
}
