package com.pac.weatherrebuild.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName =  "weather_table")
public class WeatherEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int uid;

    @ColumnInfo(name = "place_name")
    private String placeName;

    @ColumnInfo(name = "lat")
    private int latitude;

    @ColumnInfo(name = "lng")
    private int longitude;

    //Day Forecast
    @ColumnInfo(name = "day_max_temp")
    private int maxTemp;

    @ColumnInfo(name = "day_min_temp")
    private int minTemp;

    //Forecast
    @ColumnInfo(name = "forecast_precip")
    private int precipitation;

    @ColumnInfo(name = "forecast_temp")
    private int temperature;

    @ColumnInfo(name = "forecast_high_temp")
    private int highTemperature;

    @ColumnInfo(name = "forecast_low_temp")
    private int lowTemperature;

    @ColumnInfo(name = "forecast_high_time")
    private String highTime;

    @ColumnInfo(name = "forecast_low_time")
    private String lowTime;

    @ColumnInfo(name = "forecast_precip_sum")
    private String precipitationSummary;

    //Day Forecast
    @TypeConverters(Converters.class)
    @ColumnInfo(name = "max_temp_array")
    private List<Integer> maxArray;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "min_temp_array")
    private List<Integer> minArray;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "day_array")
    private List<String> days;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "skies_array")
    private List<String> skies;

    //Forecast
    @TypeConverters(Converters.class)
    @ColumnInfo(name = "cloud_array")
    private List<Integer> cloudCover;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "percip_array")
    private List<Integer> precipitationList;

    //Getters
    public String getPlaceName() {
        return placeName;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getPrecipitation() {
        return precipitation;
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

    public String getPrecipitationSummary() {
        return precipitationSummary;
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

    public List<Integer> getCloudCover() {
        return cloudCover;
    }

    public List<Integer> getPrecipitationList() {
        return precipitationList;
    }

    //Setters
    public void setPlaceName(@NonNull String placeName) {
        this.placeName = placeName;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setHighTemperature(int highTemperature) {
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

    public void setPrecipitationSummary(String precipitationSummary) {
        this.precipitationSummary = precipitationSummary;
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

    public void setCloudCover(List<Integer> cloudCover) {
        this.cloudCover = cloudCover;
    }

    public void setPrecipitationList(List<Integer> precipitationList) {
        this.precipitationList = precipitationList;
    }
}
