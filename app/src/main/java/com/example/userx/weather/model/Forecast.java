package com.example.userx.weather.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by userx on 28/01/17.
 */

public class Forecast implements Serializable {

    public static final int DAYS = 5;

    private final ArrayList<Weather>[] FORECAST = new ArrayList[DAYS];

    public Forecast() {
        initData();
    }// Forecast

    private void initData() {
        for (int i = 0; i < DAYS; ++i) {
            FORECAST[i] = new ArrayList<>();
        }
    }// initData

    public ArrayList<Weather> getDay(int day) {
        if (day < 0 || day >= DAYS) return null;
        return FORECAST[day];
    }// getDays

    public boolean hasDays(int day) {
        if (day < 0 || day >= DAYS) return false;
        return !FORECAST[day].isEmpty();
    }// hasDays

}// Forecast
