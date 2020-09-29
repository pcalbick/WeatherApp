package com.pac.weatherrebuild.model;

import java.util.ArrayList;
import java.util.List;

public class TimeLayout {

    private List<String> startTimes = new ArrayList<>();
    private List<String> endTimes = new ArrayList<>();

    public void addStart(String time){
        startTimes.add(time);
    }

    public List<String> getStartTimes() { return startTimes; }

    public String getStartTime(int index) {
        if(startTimes.size() != 0)
            return startTimes.get(index);
        return null;
    }

    public void addEnd(String time){
        endTimes.add(time);
    }

    public String getEndTime(int index) {
        if(endTimes.size() != 0)
            return endTimes.get(index);
        return null;
    }

    public int getStartCount() { return startTimes.size(); }

    public int getEndCount() { return endTimes.size(); }
}
