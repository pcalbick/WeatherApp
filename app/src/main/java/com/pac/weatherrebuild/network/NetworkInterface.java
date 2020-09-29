package com.pac.weatherrebuild.network;

public interface NetworkInterface<T> {

    void onComplete(Result<T> result);
}
