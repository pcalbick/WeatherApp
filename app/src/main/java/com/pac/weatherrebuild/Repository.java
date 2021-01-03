package com.pac.weatherrebuild;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;

import com.pac.weatherrebuild.db.WeatherDao;
import com.pac.weatherrebuild.db.WeatherDatabase;
import com.pac.weatherrebuild.db.WeatherEntity;
import com.pac.weatherrebuild.model.Forecast;
import com.pac.weatherrebuild.network.NetworkClient;
import com.pac.weatherrebuild.network.NetworkInterface;
import com.pac.weatherrebuild.network.Result;
import com.pac.weatherrebuild.storage.Data;
import com.pac.weatherrebuild.ui.FormatDate;
import com.pac.weatherrebuild.viewmodel.WeatherViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

public class Repository implements NetworkInterface {

    private static final String TAG = "Repository";

    private final WeatherDao mWeatherDao;
    private LiveData<WeatherEntity> mFullWeather;

    //private static Repository sInstance;
    private Data sharedData;
    private AppExecutors mExecutors;
    private final Handler handler = new Handler();

    public WeatherViewModel model;

    //Set up database and executors
    public Repository(Application application){
        WeatherDatabase db = WeatherDatabase.getDatabase(application);
        mWeatherDao = db.weatherDao();
    }

    public void setExecutors(AppExecutors executors){
        mExecutors = executors;
    }

    /*public static Repository getInstance(Application application){
        if(sInstance == null){
            synchronized (Repository.class){
                if(sInstance == null){
                    sInstance = new Repository(application);
                }
            }
        }
        return sInstance;
    }*/

    //Get weather from database
    public LiveData<WeatherEntity> getWeatherFromDatabase(int lat, int lng){
        final int latitude = lat;
        final int longitude = lng;
        Callable<LiveData<WeatherEntity>> getWeather = new Callable<LiveData<WeatherEntity>>() {
            @Override
            public LiveData<WeatherEntity> call() throws Exception {
                return mWeatherDao.findWeather(latitude,longitude);
            }
        };
        try {
            return getWeather.call();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<WeatherEntity> getWeatherFromDatabase(String place){
        final String p = place;
        Callable<LiveData<WeatherEntity>> getWeather = new Callable<LiveData<WeatherEntity>>() {
            @Override
            public LiveData<WeatherEntity> call() throws Exception {
                return mWeatherDao.findWeatherByName(p);
            }
        };
        try {
            return getWeather.call();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //Network calls
    public void getCurrentWeather(final double lat, final double lng,
                                  Date requestTime, Data sharedData){
        model.setLoading(View.VISIBLE);

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

        model.setLoading(View.GONE);
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
