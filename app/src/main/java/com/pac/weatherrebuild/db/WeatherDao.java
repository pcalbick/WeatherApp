package com.pac.weatherrebuild.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather_table WHERE place_name like :place LIMIT 1")
    LiveData<WeatherEntity> findWeatherByName(String place);

    @Query("SELECT * FROM weather_table WHERE lat like :lat AND lng like :lng LIMIT 1")
    LiveData<WeatherEntity> findWeather(long lat, long lng);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlace(WeatherEntity place);

    @Update
    void updatePlace(WeatherEntity place);

    @Delete
    void deletePlace(WeatherEntity place);
}
