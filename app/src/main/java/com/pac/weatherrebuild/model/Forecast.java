package com.pac.weatherrebuild.model;

import java.util.ArrayList;
import java.util.List;

public class Forecast {

    private List<Integer> cloudCover = new ArrayList<>();
    private List<Integer> precipitation = new ArrayList<>();
    private int temperature;
    private int highTemperature;
    private int lowTemperature;
    private String highTime;
    private String lowTime;
    private String precipitationSummary;

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setHighTemperature(int highTemperature){
        this.highTemperature = highTemperature;
    }

    public void setLowTemperature(int lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public void setHighTime(String highTime) {
        this.highTime = highTime;
    }

    public void setLowTime(String lowTime) {
        this.lowTime = lowTime;
    }

    public void setPrecipitation(List<Integer> precipitation) {
        this.precipitation = precipitation;
    }

    public void setPrecipitationSummary(String precipitationSummary) {
        this.precipitationSummary = precipitationSummary;
    }

    public void setCloudCover(List<Integer> cloudCover) {
        this.cloudCover = cloudCover;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHighTemperature() {
        return highTemperature;
    }

    public int getLowTemperature() {
        return lowTemperature;
    }

    public String getHighTime() {
        return highTime;
    }

    public String getLowTime() {
        return lowTime;
    }

    public List<Integer> getPrecipitation() {
        return precipitation;
    }

    public String getPrecipitationSummary() {
        return precipitationSummary;
    }

    public List<Integer> getCloudCover() {
        return cloudCover;
    }

    //7Day Forecast
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
