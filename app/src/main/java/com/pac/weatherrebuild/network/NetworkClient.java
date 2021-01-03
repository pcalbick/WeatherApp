package com.pac.weatherrebuild.network;

import android.util.Log;

//import com.pac.weatherapp.db.entity.WeatherEntity;
import com.pac.weatherrebuild.model.DayForecast;
import com.pac.weatherrebuild.model.Forecast;
import com.pac.weatherrebuild.model.TimeLayout;
import com.pac.weatherrebuild.model.Values;
import com.pac.weatherrebuild.model.Weather;
import com.pac.weatherrebuild.model.WeatherStatus;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkClient {

    private static final String TAG = "DownloadXmlData";

    public Result<Forecast> runForecast(String forecastUrl, String dayForecastUrl){
        XmlParser parser = new XmlParser();
        Weather weather;
        try {
            InputStream stream = downloadUrl(forecastUrl);
            weather = parser.parse(stream);

            Forecast forecast = new Forecast();
            displayTemps(weather, forecast);
            displayCover(weather, forecast);
            displayPrecipitation(weather, forecast);

            stream = downloadUrl(dayForecastUrl);
            weather = parser.parse(stream);
            displayTempGraph(weather, forecast);

            stream.close();
            return new Result.Success<>(forecast);
        } catch(Exception e){
            e.printStackTrace(); //getResources.getString(R.string.connection_error);
            return new Result.Error<>(e);
        }
    }

    private Forecast displayTempGraph(Weather weather, Forecast forecast){
        Values max = null;
        Values min = null;
        for (Values v : weather.getValues()) {
            if (v.getType().equals("maximum"))
                max = v;
            else if (v.getType().equals("minimum"))
                min = v;
        }
        assert max != null;
        assert min != null;

        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;
        List<Integer> minArr = new ArrayList<>();
        List<Integer> maxArr = new ArrayList<>();
        for (int i = 0; i < max.getValues().size(); i++) {
            if (minValue > min.getValue(i))
                minValue = min.getValue(i);
            if (maxValue < max.getValue(i))
                maxValue = max.getValue(i);
            minArr.add(min.getValues().get(i));
            maxArr.add(max.getValues().get(i));
        }

        forecast.setMaxTemperature(maxValue);
        forecast.setMinTemperature(minValue);
        forecast.setMaxArray(maxArr);
        forecast.setMinArray(minArr);

        TimeLayout timeLayout = null;
        if (weather.getTimes().containsKey(weather.getWeatherStatusKey()))
            timeLayout = weather.getTime(weather.getWeatherStatusKey());
        assert timeLayout != null;

        List<String> days = new ArrayList<>();
        List<String> skies = new ArrayList<>();
        for (int i = 0; i < weather.getWeatherStatuses(weather.getWeatherStatusKey()).size(); i++) {
            days.add(timeLayout.getStartTime(i));
            skies.add(weather.getWeatherStatus(weather.getWeatherStatusKey(), i).getSummary());
        }

        forecast.setDays(days);
        forecast.setSkies(skies);

        Log.d(TAG, "displayTempGraph: SET");

        return forecast;
    }

    private Forecast displayCover(Weather weather, Forecast forecast){
        Values cover = weather.getValue("Cloud Cover Amount", null);

        if(cover == null){
            Log.d(TAG, "displayTempGraph: Skipping Cloud Cover Forecast Parsing");
            return forecast;
        }

        List<Integer> cloudCover = new ArrayList<>();
        for (int i = 0; i < cover.getValues().size(); i++) {
            cloudCover.add(cover.getValue(i));
        }
        forecast.setCloudCover(cloudCover);

        Log.d(TAG, "displayCover: SET");

        return forecast;
    }

    private Forecast displayPrecipitation(Weather weather, Forecast forecast){
        //sometimes weather status info is missing from api // might be about to switch to seven day forecast if null
        /*WeatherStatus status = weather.getWeatherStatus(weather.getWeatherStatusKey(),0);
        if(status == null){
            Log.d(TAG, "displayTempGraph: Skipping Weather Status Forecast Parsing");
            return forecast;
        }*/

        //if(status.isStatus()) {
            Values precip = weather.getValue("12 Hourly Probability of Precipitation", null);

            List<Integer> precipitation = new ArrayList<>();
            int precipAverage = 0;
            for (int i = 0; i < precip.getValues().size(); i++) {
                precipitation.add(precip.getValue(i));
                precipAverage += precip.getValue(i);
            }

            forecast.setPrecipitationList(precipitation);
            forecast.setPrecipitation(precipAverage/precip.getValues().size());

            Log.d(TAG, "displayPrecipitation: SET");
        //}

        return forecast;
    }

    private Forecast displayTemps(Weather weather, Forecast forecast){
        Values hourly = weather.getValue(null,"hourly");

        if(hourly == null){
            Log.d(TAG, "displayTempGraph: Skipping Temperature Forecast Parsing");
            return forecast;
        }

        TimeLayout time = weather.getTime(hourly.getTime());

        int hi = Integer.MIN_VALUE;
        String timeHi = "";
        int lo = Integer.MAX_VALUE;
        String timeLo = "";
        for (int i = 0; i < hourly.getValues().size(); i++) {
            if (hourly.getValues().get(i) > hi) {
                hi = hourly.getValues().get(i);
                timeHi = time.getStartTime(i);
            }
            if (hourly.getValues().get(i) < lo) {
                lo = hourly.getValues().get(i);
                timeLo = time.getStartTime(i);
            }
        }

        forecast.setTemperature(hourly.getValue(0));
        forecast.setHighTemperature(hi);
        forecast.setLowTemperature(lo);
        forecast.setHighTime(timeHi);
        forecast.setLowTime(timeLo);

        Log.d(TAG, "displayTemps: SET");

        return forecast;
    }

    private InputStream downloadUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty("User-Agent:","calbick.peter@student.ccm.edu");

        conn.setReadTimeout(10000);
        conn.setConnectTimeout(20000);
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
        if(responseCode >= 400 && responseCode <= 499)
            throw new Exception("Bad Authentication Status " + responseCode);
        return conn.getInputStream();
    }
}
