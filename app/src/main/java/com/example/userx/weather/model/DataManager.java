package com.example.userx.weather.model;

import android.app.AlarmManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.userx.weather.logic.DataLoader;
import com.example.userx.weather.logic.DataWriter;
import com.example.userx.weather.logic.ForecastJSONReader;
import com.example.userx.weather.logic.JSONReader;
import com.example.userx.weather.logic.RemoteData;
import com.example.userx.weather.logic.UIObserver;
import com.example.userx.weather.logic.WeatherJSONReader;
import com.example.userx.weather.service.Notifications;
import com.example.userx.weather.utils.AppData;
import com.example.userx.weather.utils.Printer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by userx on 25/01/17.
 */

public class DataManager {

    private static final String TAG = "model.DataManager";

    private static final String DEFAULT_CITY= "roma";

    public static final String PREFS_NAME = "weather_data";
    public static final String DATA_WEATHER = "uptime_weather";
    public static final String DATA_FORECAST = "uptime_forecast";
    public static final String DATA_NOTIFICATION = "last_notif";

    private static final long T_UPDATE = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
    private static final long TF_UPDATE = 3 * AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    public static final double EPSILON = 0.06;

    public static final Integer WEATHER = 100;
    public static final Integer FORECAST = 101;

    private static final HashMap<Integer, ArrayList<UIObserver>> OBSERVER_MAP = new HashMap<>();

    static {
        OBSERVER_MAP.put(WEATHER, new ArrayList<UIObserver>());
        OBSERVER_MAP.put(FORECAST, new ArrayList<UIObserver>());
    }// static

    public static synchronized void register(Integer code, UIObserver ob) {
        // Chack on parameters
        if (ob == null || code == null || (code != WEATHER && code != FORECAST)) return;

        // Check on double adding
        ArrayList<UIObserver> obs = OBSERVER_MAP.get(code);
        if (obs.contains(ob)) return;

        // Add teh observer
        obs.add(ob);
    }// register

    public static synchronized void unRegister(Integer code, UIObserver ob) {
        // Chack on parameters
        if (ob == null || code == null || (code != WEATHER && code != FORECAST)) return;

        ArrayList<UIObserver> obs = OBSERVER_MAP.get(code);
        obs.remove(ob);
    }// unRegister

    // Be carefull with concurrency
    private synchronized void updateObservers(Object obj, Integer code) {
        if (obj == null || code == null) return;
        for (UIObserver ob : OBSERVER_MAP.get(code)) {
            ob.update(obj);
        }
    }// updateObservers

    private static DataManager instance;

    public synchronized static DataManager getInstance() {
        return (instance == null) ? instance = new DataManager() : instance;
    }// getInstance


    // Instance variable
    private Context context;

    private boolean alreadyInit;

    private Weather weather;
    private Forecast forecast;

    private long lastWeatherUpdate;
    private long lastForecastUpdate;

    private DataManager() { }// constructor

    public synchronized void initialize(Context context) {

        if (alreadyInit) return;

        if (context == null) return;

        this.context = context;

        alreadyInit = true;

        loadCachedUpdate();

        // Load Weather if exists
        weather = new Weather();
        try {
            ObjectInputStream ois = new ObjectInputStream(context.openFileInput(AppData.CACHE_WEATHER));
            weather = (Weather) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            Log.d(TAG, "catch e: " + e);
        }

        forecast = new Forecast();
        loadForecastCache();

        // Look for an update if needed
        if (!updateData()) {
            Log.d(TAG, "!updateData");
            newRequestByCity(DEFAULT_CITY);
        }
    }// initialize

    public Weather getWeather() {
        return weather;
    }// getWeather

    public Forecast getForecast() {
        return forecast;
    }// getForecast

    public synchronized void setWeather(Weather weather) {
        this.weather = weather;

        updateWeatherCache();
        updateWallpaper();
        updateObservers(weather, WEATHER);
    }// setWeather

    public synchronized void setForecast(Forecast forecast) {

        if (this.forecast == null) return;

        for (int i = 0; i < Forecast.DAYS; ++i) {
            this.forecast.getDay(i).clear();
            if (forecast != null)
                this.forecast.getDay(i).addAll(forecast.getDay(i));
        }

        updateForecastCache();
        notifyIfChanged();
        updateObservers(this.forecast, FORECAST);
    }// setWeather

    private synchronized void notifyIfChanged() {

        if (context == null || !forecast.hasDays(0)) return;

        if (weather.isInit()) {
            // Generate new notification
            Object[] not = Printer.pNotify(weather, forecast.getDay(0).get(0));

            // If it is invalid, return
            if (not.length != 3) return;


            // Retrieve last notification code
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
            int oldNot = settings.getInt(DATA_NOTIFICATION, -1);

            // Check if the new notification has to be displayed
            if (oldNot == -1 || oldNot != (int) not[2]) {
                Notifications.showNotification(context,
                        (String) not[0], (String) not[1], (int) not[2]);

                // Update the new notification
                SharedPreferences.Editor editor = context.getSharedPreferences(
                        DataManager.PREFS_NAME, 0).edit();
                editor.putInt(DataManager.DATA_NOTIFICATION, (int) not[2]);
                editor.commit();
            }
        }
    }// notifyIfChanged

    public boolean isWeatherUpdate() {
        return weather != null && System.currentTimeMillis() - lastWeatherUpdate < T_UPDATE;
    }// isWeatherUpdate

    public boolean isForecastUpdate() {
        return weather != null && System.currentTimeMillis() - lastForecastUpdate < TF_UPDATE;
    }// isWeatherUpdate

    public synchronized boolean updateData() {

        System.out.println("aaa");
        if (!alreadyInit) return false;

        System.out.println("bbb");
        if (weather.getCity() != null && !weather.getCity().equals("")) {
            // Notifications.blankNotification(context, "Data is gonna be updated");

            Log.d(TAG, "Data is gonna be updated");

            if (!isWeatherUpdate())
                RemoteData.newByCity(WeatherJSONReader.READER, weather.getCity()).acts();
            if (!isForecastUpdate())
                RemoteData.newByCity(ForecastJSONReader.READER, weather.getCity()).acts();
            return true;
        }

        return false;
    }// updateData

    public boolean newRequestByCity(String city) {
        boolean check = false;

        // New city update
        if (city != null && !city.equals("") && !city.equals(weather.getCity())) {

            city = city.replace(" ", "");
            RemoteData.newByCity(WeatherJSONReader.READER, city).acts();
            RemoteData.newByCity(ForecastJSONReader.READER, city).acts();
            return true;
        }

        // Same city, update needed
        if (check = (check || !isWeatherUpdate()))
            RemoteData.newByCity(WeatherJSONReader.READER, city).acts();
        if (check = (check || !isForecastUpdate()))
            RemoteData.newByCity(ForecastJSONReader.READER, city).acts();

        return check;
    }// newRequestByCity

    public boolean newRequestByCoords(double lat, double lon) {

        boolean check = false;

        if (check = (check || !isWeatherUpdate()))
            RemoteData.newBYCoords(WeatherJSONReader.READER, lat, lon).acts();
        if (check = (check || !isForecastUpdate()))
            RemoteData.newBYCoords(ForecastJSONReader.READER, lat, lon).acts();

        // Check last coords
        if (!check && (Math.abs(weather.getLat() - lat) > EPSILON || Math.abs(weather.getLon() - lon) > EPSILON)) {
            RemoteData.newBYCoords(WeatherJSONReader.READER, lat, lon).acts();
            RemoteData.newBYCoords(ForecastJSONReader.READER, lat, lon).acts();
            return true;
        }

        return check;
    }// newRequestByCoords

    @Deprecated
    private void loadWeatherCache() {
        DataLoader.newWeatherLoader(context).execute();
    }// loadWeatherCache

    private void loadForecastCache() {
        DataLoader.newForecastLoader(context).execute();
    }// loadForecastCache

    private void updateForecastCache() {
        try {
            DataWriter.newForecastWriter(context, forecast,
                    lastForecastUpdate = SystemClock.elapsedRealtime()).execute();
        } catch (Exception e) {
            Log.d(TAG, "updateForecastCache err: " + e);
        }
    }// updateForecastCache

    private synchronized void updateWallpaper() {
        if (context == null) return;

        Drawable draw = Printer.getWallpaper(getWeather().dayLight(getWeather()), getWeather(), context);

        // SET WALLPAPER
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setBitmap(((BitmapDrawable) draw).getBitmap());
        } catch (IOException e) {
            Log.d(TAG, "Wallpaper set failed: " + e);
        }
    }// updateWallpaper

    private void updateWeatherCache() {
        try {
            DataWriter.newWeatherWriter(context, weather,
                    lastWeatherUpdate = SystemClock.elapsedRealtime()).execute();
        } catch (Exception e) {
            Log.d(TAG, "updateWeatherCache err: " + e);
        }
    }// updateWeatherCache

    public void fakeUpdate() {
        updateObservers(getWeather(), WEATHER);
        updateObservers(getForecast(), FORECAST);
    }// fakeUpdate

    private void loadCachedUpdate() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        lastWeatherUpdate = settings.getLong(DATA_WEATHER, 0);
        lastForecastUpdate = settings.getLong(DATA_FORECAST, 0);
    }// loadCachedUpdate

}// DataManager
