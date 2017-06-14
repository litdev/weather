package com.example.userx.weather.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Forecast;
import com.example.userx.weather.model.Weather;
import com.example.userx.weather.utils.AppData;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by userx on 04/02/17.
 */

public class DataLoader extends AsyncTask<String, Object, Object> {

    private Loader loader;

    @Override
    protected Object doInBackground(String... params) {

        if (loader == null || loader.context() == null) return null;

        return loader.read();
    }// doInBackground

    @Override
    protected void onPostExecute(Object obj) {
        loader.load(obj);
    }// onPostExecute

    public interface Loader<T> {
        public Context context();
        public T read();
        public void load(T t);
    }// Loader

    public static DataLoader newWeatherLoader(final Context context) {

        Loader wLoader = new Loader<Weather>() {

            private static final String TAG = "logic.Loader.Weather";

            @Override
            public Context context() {
                return context;
            }// context

            @Override
            public Weather read() {
                Weather weather = null;
                try {
                    ObjectInputStream ois = new ObjectInputStream(context.openFileInput(AppData.CACHE_WEATHER));
                    weather = (Weather) ois.readObject();
                    ois.close();
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    Log.d(TAG, "catch e: " + e);
                }
                return weather;
            }// read

            @Override
            public void load(Weather weather) {
                if (weather != null)
                    DataManager.getInstance().setWeather(weather);
            }// load


        };

        DataLoader dl = new DataLoader();
        dl.loader = wLoader;
        return dl;
    }// newWeatherLoader

    public static DataLoader newForecastLoader(final Context context) {

        Loader fLoader = new Loader<Forecast>() {

            private static final String TAG = "logic.Loader.Forecast";

            @Override
            public Context context() {
                return context;
            }// context

            @Override
            public Forecast read() {
                Forecast forecast = null;
                try {
                    ObjectInputStream ois = new ObjectInputStream(context.openFileInput(AppData.CACHE_FORECAST));
                    forecast = (Forecast) ois.readObject();
                    ois.close();
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    Log.d(TAG, "catch e: " + e);
                }
                return forecast;
            }// read

            @Override
            public void load(Forecast forecast) {
                if (forecast != null)
                    DataManager.getInstance().setForecast(forecast);
            }// load
        };

        DataLoader dl = new DataLoader();
        dl.loader = fLoader;
        return dl;
    }// newForecastLoader

}// DataLoader
