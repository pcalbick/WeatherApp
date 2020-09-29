package com.pac.weatherrebuild;

import android.app.Application;

public class BaseApp extends Application {

    private AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        appExecutors = new AppExecutors();
    }

    public AppExecutors getAppExecutors() { return appExecutors; }

    //public AppDatabase getDatabase(){ return AppDatabase.getInstance(this,getAppExecutors()); }

    public Repository getRepository(){
        return Repository.getInstance(getAppExecutors());
    }
}
