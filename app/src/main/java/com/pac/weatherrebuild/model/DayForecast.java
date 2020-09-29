package com.pac.weatherrebuild.model;

import java.util.ArrayList;
import java.util.List;

public class DayForecast {

    private int maxTemperature;
    private int minTemperature;
    private List<Integer> maxArray = new ArrayList<>();
    private List<Integer> minArray = new ArrayList<>();
    private List<String> days = new ArrayList<>();
    private List<String> skies = new ArrayList<>();

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void setMaxArray(List<Integer> maxArray) {
        this.maxArray = maxArray;
    }

    public void setMinArray(List<Integer> minArray) {
        this.minArray = minArray;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public void setSkies(List<String> skies) {
        this.skies = skies;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public List<Integer> getMaxArray() {
        return maxArray;
    }

    public List<Integer> getMinArray() {
        return minArray;
    }

    public List<String> getDays() {
        return days;
    }

    public List<String> getSkies() {
        return skies;
    }
}
