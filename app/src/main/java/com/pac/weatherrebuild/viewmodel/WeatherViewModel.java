package com.pac.weatherrebuild.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

//import com.pac.weatherapp.BaseApp;
import com.pac.weatherrebuild.Repository;
//import com.pac.weatherapp.db.entity.LocationEntity;

import java.util.ArrayList;
import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private static final String TAG = "WeatherViewModel";

    //private final Repository mRepository;

    //TEMP GRAPH
    /*weatherEntity.setLowestTemperature(minValue);
    weatherEntity.setHighestTemperature(maxValue);
    weatherEntity.setMinTemperatures(minArr);
    weatherEntity.setMaxTemperatures(maxArr);
    weatherEntity.setDays(days);
    weatherEntity.setSkies(skies);*/

    private MutableLiveData<Integer> temperature = new MutableLiveData<>();
    private MutableLiveData<Integer> highTemp = new MutableLiveData<>();
    private MutableLiveData<Integer> lowTemp = new MutableLiveData<>();
    private MutableLiveData<String> highTime = new MutableLiveData<>();
    private MutableLiveData<String> lowTime = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> precipitationList = new MutableLiveData<>();
    private MutableLiveData<Integer> precipitation = new MutableLiveData<>();
    private MutableLiveData<String> skySummary = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> cloudCover = new MutableLiveData<>();

    private MutableLiveData<Integer> weekMaxTemp = new MutableLiveData<>();
    private MutableLiveData<Integer> weekMinTemp = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> weekMaxArray = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> weekMinArray = new MutableLiveData<>();
    private MutableLiveData<List<String>> weekDays = new MutableLiveData<>();
    private MutableLiveData<List<String>> weekSkies = new MutableLiveData<>();

    private List<ForecastDataSet> forecastChartData = new ArrayList<>();
    private MutableLiveData<List<WeatherViewModel.ForecastDataSet>> forecastDataSets = new MutableLiveData<>();

    private MutableLiveData<Integer> loading = new MutableLiveData<>();

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    //private final MediatorLiveData<LocationEntity> mObservableLocation;

    public WeatherViewModel(Application application) {
        super(application);
        /*mObservableLocation = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableLocation.setValue(null);

        mRepository = ((BaseApp) application).getRepository();
        LiveData<LocationEntity> location = mRepository.getLocation(id);

        // observe the changes of the products from the database and forward them
        mObservableLocation.addSource(location, mObservableLocation::setValue);*/
    }

    //Setters
    public void setTemperature(Integer temp){
        temperature.setValue(temp);
    }

    public void setHighTemp(Integer temp){
        highTemp.setValue(temp);
    }

    public void setLowTemp(Integer temp){
        lowTemp.setValue(temp);
    }

    public void setHighTime(String time){
        highTime.setValue(time);
    }

    public void setLowTime(String time){
        lowTime.setValue(time);
    }

    public void setPrecipitationList(List<Integer> precip){
        precipitationList.setValue(precip);
    }

    public void setPrecipitation(Integer precipitation){
        this.precipitation.setValue(precipitation);
    }

    public void setSkySummary(String precipSum){
        skySummary.setValue(precipSum);
    }

    public void setCloudCover(List<Integer> cover){
        cloudCover.setValue(cover);
    }

    //7Day Setters

    public void setWeekMaxTemp(Integer maxTemp) { weekMaxTemp.setValue(maxTemp); }

    public void setWeekMinTemp(Integer minTemp) { weekMinTemp.setValue(minTemp); }

    public void setWeekMaxArray(List<Integer> arr) { weekMaxArray.setValue(arr); }

    public void setWeekMinArray(List<Integer> arr) { weekMinArray.setValue(arr); }

    public void setWeekDays(List<String> arr) { weekDays.setValue(arr); }

    public void setWeekSkies(List<String> arr) { weekSkies.setValue(arr); }

    public void setLoading(Integer visibility) { loading.setValue(visibility); }

    //Getters
    public LiveData<Integer> getTemperature(){
        return temperature;
    }

    public LiveData<Integer> getHighTemp(){
        return highTemp;
    }

    public LiveData<Integer> getLowTemp(){
        return lowTemp;
    }

    public LiveData<String> getHighTime(){
        return highTime;
    }

    public LiveData<String> getLowTime(){
        return lowTime;
    }

    public LiveData<List<Integer>> getPrecipitationList(){
        return precipitationList;
    }

    public LiveData<Integer> getPrecipitation(){
        return precipitation;
    }

    public LiveData<String> getSkySummary(){
        return skySummary;
    }

    public LiveData<List<Integer>> getCloudCover() {
        return cloudCover;
    }

    //7Day Getters
    public LiveData<Integer> getWeekMaxTemp() { return weekMaxTemp; }

    public LiveData<Integer> getWeekMinTemp() { return weekMinTemp; }

    public LiveData<List<Integer>> getWeekMaxArray() { return weekMaxArray; }

    public LiveData<List<Integer>> getWeekMinArray() { return weekMinArray; }

    public LiveData<List<String>> getWeekDays() { return weekDays; }

    public LiveData<List<String>> getWeekSkies() { return weekSkies; }

    public LiveData<Integer> getLoading() {
        return loading;
    }

    //7Day Forecast
    public void setForecastChart(int index, String day, String sky, int[] graph, int[] bounds) {
        ForecastDataSet set = new ForecastDataSet(day,sky,graph,bounds);
        if(index > forecastChartData.size()-1) {
            forecastChartData.add(set);
            forecastDataSets.setValue(forecastChartData);
        } else if(!forecastChartData.contains(set)) {
            forecastChartData.set(index, set);
            forecastDataSets.setValue(forecastChartData);
        }
    }

    public LiveData<List<ForecastDataSet>> getForecastDataSet() { return forecastDataSets; }

    public class ForecastDataSet {
        String day;
        String sky;
        int[] graph;
        int[] graphBounds;

        public ForecastDataSet(String d, String s, int[] g, int[] gb){
            day = d;
            sky = s;
            graph = g;
            graphBounds = gb;
        }

        public String getDay() {
            return day;
        }

        public String getSky() {
            return sky;
        }

        public int[] getGraph() {
            return graph;
        }

        public int[] getGraphBounds() {
            return graphBounds;
        }
    }
}
