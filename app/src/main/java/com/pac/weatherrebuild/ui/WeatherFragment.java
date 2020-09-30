package com.pac.weatherrebuild.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.weatherrebuild.BaseApp;
import com.pac.weatherrebuild.R;
import com.pac.weatherrebuild.Repository;
import com.pac.weatherrebuild.model.Forecast;
import com.pac.weatherrebuild.storage.Data;
import com.pac.weatherrebuild.ui.forecastgraph.ForecastAdapter;
import com.pac.weatherrebuild.viewmodel.WeatherViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WeatherFragment extends Fragment {

    private static final String TAG = "WeatherFragment";

    private final long hour = 3600000;

    private WeatherViewModel mViewModel;
    private Repository repository;

    private TextView temperature;
    private TextView highTemp;
    private TextView lowTemp;

    private TextView skyStatus;
    private Image skyPic;
    private TextView precipitationChance;
    private TextView cloudCover;

    public RecyclerView recyclerView;
    public RecyclerView.Adapter<?> mAdapter;
    public List<WeatherViewModel.ForecastDataSet> data = new ArrayList<>();

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        temperature = this.getView().findViewById(R.id.temp);
        highTemp = this.getView().findViewById(R.id.temp_hi);
        lowTemp = this.getView().findViewById(R.id.temp_lo);

        skyStatus = this.getView().findViewById(R.id.sky_status);
        //skyPic = this.getView().findViewById(R.id.sky_picture);
        precipitationChance = this.getView().findViewById(R.id.precipitation_chance);
        cloudCover = this.getView().findViewById(R.id.cloud_cover);

        repository = ((BaseApp)getActivity().getApplication()).getRepository();
        mViewModel = repository.getViewModel();

        //Forecast Weather Chart
        recyclerView = view.findViewById(R.id.day_container);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ForecastAdapter(data);
        recyclerView.setAdapter(mAdapter);

        setObservers();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getActivity();
        assert context != null;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Data sharedData = new Data(sharedPref);

        Calendar currentDate = GregorianCalendar.getInstance();
        Date requestTime = currentDate.getTime();

        //For testing
        //sharedData.clear();

        if(!sharedPref.getAll().isEmpty()) {
            Date savedDate = parseSavedDate(Objects.requireNonNull(sharedPref.getString(Data.REQUEST_TIME, "")));
            Log.d(TAG, "checkSharedPreferences: Time to update: " + (hour - (requestTime.getTime() - savedDate.getTime())) + "ms");
            if(requestTime.getTime() - savedDate.getTime() > hour) {
                setWeather(sharedPref,requestTime,sharedData);
            } else {
                Log.d(TAG, "checkSharedPreferences: UI Updated From Stored Data");
                repository.updateForecastUI(sharedData.readForecast());
            }
        } else {
            setWeather(sharedPref,requestTime,sharedData);
        }
    }

    private void setWeather(SharedPreferences sharedPref, Date requestTime, Data sharedData){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Data.REQUEST_TIME, requestTime.toString());
        editor.apply();

        //Hard Coded for tests
        repository.getCurrentWeather(38.99, -77.01, requestTime,sharedData);
    }

    private void setObservers(){
        LifecycleOwner owner = getViewLifecycleOwner();

        //Temperature Observers
        mViewModel.getTemperature().observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer i) {
                if(i != null) {
                    temperature.setText(String.format(Locale.US,"%d", i));
                } else {
                    temperature.setText(R.string.null_placeholder);
                }
            }
        });
        mViewModel.getLowTemp().observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer i) {
                if(i != null) {
                    lowTemp.setText(String.format(Locale.US,"%d", i));
                } else {
                    lowTemp.setText(R.string.null_placeholder);
                }
            }
        });
        mViewModel.getHighTemp().observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer i) {
                if(i != null) {
                    highTemp.setText(String.format(Locale.US,"%d", i));
                } else {
                    highTemp.setText(R.string.null_placeholder);
                }
            }
        });

        //Precipitation Observers
        mViewModel.getPrecipitation().observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer i) {
                if(i != null) {
                    precipitationChance.setText(String.format(Locale.US,"%d%%", i));
                } else {
                    precipitationChance.setText(R.string.null_placeholder);
                }
            }
        });
        mViewModel.getSkySummary().observe(owner, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String s) {
                if(s != null) {
                    skyStatus.setText(String.format(Locale.US, "%s", s));
                } else {
                    skyStatus.setText(R.string.null_placeholder);
                }
            }
        });
        mViewModel.getCloudCover().observe(owner, new Observer<List<Integer>>() {
            @Override
            public void onChanged(@Nullable final List<Integer> i) {
                if(i != null) {
                    cloudCover.setText(String.format(Locale.US,"%d", i.get(0)));
                } else {
                    cloudCover.setText(R.string.null_placeholder);
                }
            }
        });

        //Forecast List Observer
        mViewModel.getForecastDataSet().observe(owner, new Observer<List<WeatherViewModel.ForecastDataSet>>() {
            @Override
            public void onChanged(@Nullable final List<WeatherViewModel.ForecastDataSet> forecastDataSets) {
                if(forecastDataSets != null && forecastDataSets.size() > data.size()) {
                    data.clear();
                    for (int i = 0; i < forecastDataSets.size(); i++) {
                        data.add(forecastDataSets.get(i));
                        mAdapter.notifyItemInserted(i);
                    }
                } else {
                    for(int i = 0; i < data.size(); i++){
                        mAdapter.notifyItemChanged(i);
                    }
                }
            }
        });
    }

    private Date parseSavedDate(String savedDate) {
        Log.d(TAG, "parseSavedDate: " + savedDate);
        int year = Integer.parseInt(savedDate.substring(savedDate.lastIndexOf(" ")+1));
        int month = parseMonth(savedDate.substring(savedDate.indexOf(" ")+1,savedDate.indexOf(" ")+4));
        int day = Integer.parseInt(savedDate.substring(savedDate.indexOf(" ")+5,savedDate.indexOf(" ")+7));
        int hour = Integer.parseInt(savedDate.substring(savedDate.indexOf(":")-2,savedDate.indexOf(":")));
        int minute = Integer.parseInt(savedDate.substring(savedDate.indexOf(":")+1,savedDate.indexOf(":")+3));
        int second = Integer.parseInt(savedDate.substring(savedDate.indexOf(":")+4,savedDate.indexOf(":")+6));
        Calendar calendar = new GregorianCalendar(year,month,day,hour,minute,second);
        return calendar.getTime();
    }

    private int parseMonth(String month){
        switch(month){
            case "Jan":
                return 0;
            case "Feb":
                return 1;
            case "Mar":
                return 2;
            case "Apr":
                return 3;
            case "May":
                return 4;
            case "Jun":
                return 5;
            case "Jul":
                return 6;
            case "Aug":
                return 7;
            case "Sep":
                return 8;
            case "Oct":
                return 9;
            case "Nov":
                return 10;
            default:
                return 11;
        }
    }
}
