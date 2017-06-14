package com.example.userx.weather.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Forecast;
import com.example.userx.weather.model.Weather;
import com.example.userx.weather.utils.AppData;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by userx on 04/02/17.
 */

public class DataWriter extends AsyncTask<String, Object, Boolean> {

    private Writer writer;

    @Override
    protected Boolean doInBackground(String... params) {

        if (writer == null || writer.context() == null) return null;

        return writer.write();
    }// doInBackground

    public interface Writer<T> {
        public Context context();
        public boolean write();
    }// Loader

    public static DataWriter newWeatherWriter(final Context context, final Weather weather,
                                              final long time) {

        Writer<Weather> wLoader = new Writer<Weather>() {

            private static final String TAG = "logic.Writer.Weather";

            @Override
            public Context context() {
                return context;
            }// context

            @Override
            public boolean write() {
                try {

                    if (weather == null) return false;

                    // write object
                    ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(
                            AppData.CACHE_WEATHER, Context.MODE_PRIVATE));
                    oos.writeObject(weather);
                    oos.close();

                    // write time update
                    SharedPreferences.Editor editor = context.getSharedPreferences(
                            DataManager.PREFS_NAME, 0).edit();
                    editor.putLong(DataManager.DATA_WEATHER, time);
                    editor.commit();

                    Log.d(TAG, "Weather has been updated");
                    return true;
                } catch (IOException | NullPointerException e) {
                    Log.d(TAG, "catch e: " + e);
                    return false;
                }
            }// read

        };

        DataWriter dw = new DataWriter();
        dw.writer = wLoader;
        return dw;
    }// newWeatherWriter

    public static DataWriter newForecastWriter(final Context context, final Forecast forecast,
                                               final long time) {

        Writer<Forecast> wLoader = new Writer<Forecast>() {

            private static final String TAG = "logic.Writer.Forecast";

            @Override
            public Context context() {
                return context;
            }// context

            @Override
            public boolean write() {
                try {

                    if (forecast == null) return false;

                    // write object
                    ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(
                            AppData.CACHE_FORECAST, Context.MODE_PRIVATE));
                    oos.writeObject(forecast);
                    oos.close();

                    // write time update
                    SharedPreferences.Editor editor = context.getSharedPreferences(
                            DataManager.PREFS_NAME, 0).edit();
                    editor.putLong(DataManager.DATA_FORECAST, time);
                    editor.commit();
                    return true;
                } catch (IOException | NullPointerException e) {
                    Log.d(TAG, "catch e: " + e);
                    return false;
                }
            }// read

        };

        DataWriter dw = new DataWriter();
        dw.writer = wLoader;
        return dw;
    }// newForecastWriter

}// DataLoader

