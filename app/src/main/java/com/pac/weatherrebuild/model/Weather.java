package com.pac.weatherrebuild.model;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Weather {

    private static final String TAG = "Weather";

    private Hashtable<String, TimeLayout> timeLayoutTable = new Hashtable<>();
    private List<Values> values = new ArrayList<>();
    private Hashtable<String, List<WeatherStatus>> status = new Hashtable<>();

    public void setWeatherStatus(Hashtable<String,List<WeatherStatus>> pair) { status = pair; }

    public WeatherStatus getWeatherStatus(String key, int index) { return status.get(key).get(index); }

    public List<WeatherStatus> getWeatherStatuses(String key) { return status.get(key); }

    public String getWeatherStatusKey() {
        return status.keys().nextElement();
    }

    public void addValues(Values values) { this.values.add(values); }

    public Values getValue(@Nullable String name, @Nullable String type){
        if(name != null){
            for(Values v : values){
                if(v.getName().equals(name))
                    return v;
            }
        }
        if(type != null){
            for(Values v : values){
                if(v.getType().equals(type))
                    return v;
            }
        }
        return null;
    }

    public List<Values> getValues(){ return values; }

    public void addTime(Hashtable<String,TimeLayout> time){
        if(!timeLayoutTable.containsKey(time.keys().nextElement()))
            timeLayoutTable.put(time.keys().nextElement(), time.elements().nextElement());
    }

    public TimeLayout getTime(String key){
        return timeLayoutTable.get(key);
    }

    public String getTimeKey(int index) {
        Set<String> timeSet = timeLayoutTable.keySet();
        Iterator<String> timeIterator = timeSet.iterator();

        String key = "";
        int i = 0;
        while(timeIterator.hasNext()){
            key = timeIterator.next();
            if(i == index) break;
            i++;
        }

        return key;
    }

    public Hashtable<String, TimeLayout> getTimes(){
        return timeLayoutTable;
    }
}
