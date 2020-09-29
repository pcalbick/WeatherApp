package com.pac.weatherrebuild;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.pac.weatherrebuild.model.DayForecast;
import com.pac.weatherrebuild.model.Forecast;
import com.pac.weatherrebuild.network.NetworkClient;
import com.pac.weatherrebuild.network.NetworkInterface;
import com.pac.weatherrebuild.network.Result;
import com.pac.weatherrebuild.storage.Data;
import com.pac.weatherrebuild.ui.FormatDate;
import com.pac.weatherrebuild.viewmodel.WeatherViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Repository implements NetworkInterface {

    private static final String TAG = "Repository";

    private static Repository sInstance;
    private Data sharedData;
    private AppExecutors mExecutors;
    private Handler handler = new Handler();

    public WeatherViewModel model;

    public Repository(AppExecutors executors){
        mExecutors = executors;
    }

    public static Repository getInstance(AppExecutors executors){
        if(sInstance == null){
            synchronized (Repository.class){
                if(sInstance == null){
                    sInstance = new Repository(executors);
                }
            }
        }
        return sInstance;
    }

    public void setViewModel(WeatherViewModel model){
        this.model = model;
    }

    public WeatherViewModel getViewModel() { return model; }

    //public LiveData<List<LocationEntity>> getLocations(){
    //    return mObservableLocations;
    //}

    //public LiveData<LocationEntity> getLocation(final int locationId){
    //    return mDatabase.locationDao().loadLocation(locationId);
    //}

    //public LiveData<LocationEntity> searchLocation(final String locationName){
    //    return mDatabase.locationDao().loadLocationFromName(locationName);
    //}

    /*public void deleteLocation(final int locationId){
        mDatabase.locationDao().deleteLocation(locationId);
    }*/

    //Network calls
    public void getCurrentWeather(final double lat, final double lng,
                                  Date requestTime, Data sharedData){
        this.sharedData = sharedData;

        FormatDate formatDate = new FormatDate();
        String start = formatDate.formatCurrent(requestTime);
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.YEAR, 4);
        String end = formatDate.formatCurrent(endTime.getTime());

        //7 Day Forecast Request
        final String dayForecastUrl = "https://graphical.weather.gov/xml/SOAP_server/ndfdSOAPclientByDay.php?whichClient=NDFDgenByDay&lat=" +
                lat + "&lon=" + lng + "&format=24+hourly&startDate=" +
                start + "&numDays=7&Unit=e&Submit=Submit";

        Log.d(TAG, "downloadFromApi: " + dayForecastUrl);

        //24 Hour Forecast Request
        //Api Options
        String temp = "temp=temp&"; //Temperatures
        String minTemp = "mint=mint&"; //Minimum Temperatures
        String maxTemp = "maxt=maxt&"; //Maximum Temperatures
        String percip = "pop12=pop12&"; //Precipitation Chance
        String weather = "wx=wx&"; //Precipitation
        String cover = "sky=sky&"; //Cloud Coverage

        final String forecastUrl = "https://graphical.weather.gov/xml/SOAP_server/ndfdXMLclient.php?whichClient=NDFDgen&" +
                "lat=" + lat + "&lon=" + lng +
                "&product=time-series&begin=" + start +
                "&end=" + end + "&" +
                temp +
                percip +
                weather +
                cover +
                "Submit=Submit";

        Log.d(TAG, "downloadFromApi: " + forecastUrl);

        final NetworkClient networkClient = new NetworkClient();

        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<Forecast> result = networkClient.runForecast(forecastUrl,dayForecastUrl);
                    onComplete(result);
                } catch (Exception e) {
                    Result<Forecast> errorResult = new Result.Error<>(e);
                    onComplete(errorResult);
                }
            }
        });
    }

    private Forecast forecast;
    private final Runnable forecastRunnable = new Runnable() {
        @Override
        public void run() {
            sharedData.writeForecast(forecast);
            updateForecastUI(forecast);
        }
    };

    private final Runnable errorRunnable = new Runnable() {
        @Override
        public void run() {
            errorUI();
        }
    };

    public void updateForecastUI(Forecast forecast){
        model.setTemperature(forecast.getTemperature());
        //model.setHighTime(forecast.getHighTime());    //not shown for now
        //model.setLowTime(forecast.getLowTime());      //not shown for now
        model.setPrecipitation(forecast.getPrecipitation());
        model.setCloudCover(forecast.getCloudCover());

        model.setSkySummary(forecast.getSkies().get(0));
        model.setHighTemp(forecast.getMaxArray().get(0));
        model.setLowTemp(forecast.getMinArray().get(0));

        for(int i=0; i<forecast.getDays().size(); i++){
            FormatDate format = new FormatDate();
            String date = forecast.getDays().get(i);
            String day = getDayOfWeek(date,format) + "\n"
                    + format.format(date, FormatDate.fullDayFormatUS);
            model.setForecastChart(i,
                    day,
                    forecast.getSkies().get(i),
                    new int[] {forecast.getMaxArray().get(i),forecast.getMinArray().get(i)},
                    new int[] {forecast.getMinTemperature(),forecast.getMaxTemperature()});
        }
    }

    private String getDayOfWeek(String date, FormatDate format){
        switch (format.format(date, FormatDate.dayOnlyFormat)){
            case "Mon":
                return "Monday";
            case "Tue":
                return "Tuesday";
            case"Wed":
                return "Wednesday";
            case "Thu":
                return "Thursday";
            case "Fri":
                return "Friday";
            case "Sat":
                return "Saturday";
            default:
                return "Sunday";
        }
    }

    private void errorUI(){
        //error ui here
    }

    @Override
    public void onComplete(Result result) {
        if(result instanceof Result.Success){
            forecast = (Forecast) ((Result.Success<?>) result).data;
            handler.post(forecastRunnable);
        } else {
            handler.post(errorRunnable);
            Log.d(TAG, "onComplete: Error! " + ((Result.Error<?>) result).exception.getMessage());
        }
    }

    /*public void getWeather(final int id,
                           final double lat, final double lng,
                           final String date,
                           Date requestTime) {
        LiveData<WeatherEntity> weatherEntity = mDatabase.weatherDao().loadWeather(id);

        if(weatherEntity != null) {
            weatherEntity.getValue().setRequestTime(requestTime);

            String sevenUrl = "https://graphical.weather.gov/xml/sample_products/browser_interface/ndfdBrowserClientByDay.php?lat=" +
                    lat + "&lon=" + -lng + "&format=24+hourly&startDate=" +
                    date + "&numDays=7";

            NetworkClient downloadXmlData1 = new NetworkClient(weatherEntity);
            downloadXmlData1.execute(sevenUrl);
        }
    }*/

    /*public void createData(AppExecutors executors){
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.createData(mDatabase);
            }
        });
    }*/
}
