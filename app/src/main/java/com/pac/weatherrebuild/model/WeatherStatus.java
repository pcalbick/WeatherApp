package com.pac.weatherrebuild.model;

import java.util.ArrayList;
import java.util.List;

public class WeatherStatus {

    private boolean status;

    private String coverage;
    private String intensity;
    private String weatherType;
    private String visibility;
    private String summary;

    private List<WeatherStatus> additive = new ArrayList<>();

    public void setStatus(boolean status) { this.status = status; }

    public void setSummary(String summary) { this.summary = summary; }

    public void setCoverage(String coverage) { this.coverage = coverage; }

    public void setIntensity(String intensity) { this.intensity = intensity; }

    public void setWeatherType(String type) { weatherType = type; }

    public void setVisibility(String visibility) { this.visibility = visibility; }

    public void setAdditive(WeatherStatus add) { additive.add(add); }

    public boolean isStatus() {
        return status;
    }

    public String getSummary() {
        return summary;
    }

    public String getCoverage() {
        return coverage;
    }

    public String getIntensity() {
        return intensity;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public String getVisibility() {
        return visibility;
    }

    public List<WeatherStatus> getAdditive() { return additive; }
}
