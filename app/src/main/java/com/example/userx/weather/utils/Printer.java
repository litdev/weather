package com.example.userx.weather.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.userx.weather.R;
import com.example.userx.weather.model.Weather;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by userx on 25/01/17.
 */

public class Printer {

    private static Map<Integer, String> D_ICON_MAP = new HashMap<>();
    private static Map<Integer, String> N_ICON_MAP = new HashMap<>();

    private static Map<Integer, Integer> COLOR_MAP = new HashMap<>();

    private static final String UNCODED_ICON = "\uf071";

    private static final int C_THUNDER = Color.parseColor("#AF71B0");
    private static final int C_DRIZZLE = Color.parseColor("#60FFBD");
    private static final int C_RAIN = Color.parseColor("#076BB6");
    private static final int C_SNOW = Color.parseColor("#D9F9F5");
    private static final int C_MIST = Color.parseColor("#A2A5A3");
    private static final int C_CLEAR = Color.parseColor("#F27E43");
    private static final int C_CLOUDS = Color.parseColor("#ACB2B2");
    private static final int C_DEFAULT = Color.parseColor("#9FCDB3");

    static {
        D_ICON_MAP.put(200, "\uf01d");
        N_ICON_MAP.put(200, "\uf01d");
        COLOR_MAP.put(200, C_THUNDER);

        D_ICON_MAP.put(201, "\uf01e");
        N_ICON_MAP.put(201, "\uf01e");
        COLOR_MAP.put(201, C_THUNDER);

        D_ICON_MAP.put(202, "\uf01e");
        N_ICON_MAP.put(202, "\uf01e");
        COLOR_MAP.put(202, C_THUNDER);

        D_ICON_MAP.put(210, "\uf016");
        N_ICON_MAP.put(210, "\uf016");
        COLOR_MAP.put(210, C_THUNDER);

        D_ICON_MAP.put(211, "\uf016");
        N_ICON_MAP.put(211, "\uf016");
        COLOR_MAP.put(211, C_THUNDER);

        D_ICON_MAP.put(212, "\uf016");
        N_ICON_MAP.put(212, "\uf016");
        COLOR_MAP.put(212, C_THUNDER);

        D_ICON_MAP.put(230, "\uf068");
        N_ICON_MAP.put(230, "\uf06a");
        COLOR_MAP.put(230, C_THUNDER);

        D_ICON_MAP.put(231, "\uf00e");
        N_ICON_MAP.put(231, "\uf02c");
        COLOR_MAP.put(231, C_THUNDER);

        D_ICON_MAP.put(232, "\uf010");
        N_ICON_MAP.put(232, "\uf02d");
        COLOR_MAP.put(232, C_THUNDER);

        for (int i = 300; i <= 302; ++i) {
            D_ICON_MAP.put(i, "\uf009");
            N_ICON_MAP.put(i, "\uf029");
            COLOR_MAP.put(i, C_DRIZZLE);
        }

        for (int i = 310; i <= 314; ++i) {
            D_ICON_MAP.put(i, "\uf004");
            N_ICON_MAP.put(i, "\uf024");
            COLOR_MAP.put(i, C_DRIZZLE);
        }

        D_ICON_MAP.put(321, "\uf008");
        N_ICON_MAP.put(321, "\uf028");
        COLOR_MAP.put(321, C_DRIZZLE);

        for (int i = 500; i <= 504; ++i) {
            D_ICON_MAP.put(i, "\uf019");
            N_ICON_MAP.put(i, "\uf019");
            COLOR_MAP.put(i, C_RAIN);
        }

        for (int i = 500; i <= 504; ++i) {
            D_ICON_MAP.put(i, "\uf019");
            N_ICON_MAP.put(i, "\uf019");
            COLOR_MAP.put(i, C_RAIN);
        }

        D_ICON_MAP.put(511, "\uf017");
        N_ICON_MAP.put(511, "\uf017");
        COLOR_MAP.put(511, C_RAIN);

        for (int i = 520; i <= 522; ++i) {
            D_ICON_MAP.put(i, "\uf018");
            N_ICON_MAP.put(i, "\uf018");
            COLOR_MAP.put(i, C_RAIN);
        }

        D_ICON_MAP.put(531, "\uf018");
        N_ICON_MAP.put(531, "\uf018");
        COLOR_MAP.put(531, C_RAIN);

        for (int i = 600; i <= 602; ++i) {
            D_ICON_MAP.put(i, "\uf01b");
            N_ICON_MAP.put(i, "\uf01b");
            COLOR_MAP.put(i, C_SNOW);
        }

        int[] vals = new int[]{611, 612, 615, 616, 620, 621, 622};

        for (int i : vals) {
            D_ICON_MAP.put(i, "\uf00a");
            N_ICON_MAP.put(i, "\uf02a");
            COLOR_MAP.put(i, C_RAIN);
        }

        D_ICON_MAP.put(701, "\uf003");
        N_ICON_MAP.put(701, "\uf04a");
        COLOR_MAP.put(701, C_MIST);

        for (int i = 711; i <= 761; i += 10) {
            D_ICON_MAP.put(i, "\uf063");
            N_ICON_MAP.put(i, "\uf063");
            COLOR_MAP.put(i, C_MIST);
        }

        D_ICON_MAP.put(762, "\uF063");
        N_ICON_MAP.put(762, "\uF063");
        COLOR_MAP.put(762, C_MIST);

        D_ICON_MAP.put(771, "\uf082");
        N_ICON_MAP.put(771, "\uf082");
        COLOR_MAP.put(771, C_MIST);

        D_ICON_MAP.put(781, "\uf056");
        N_ICON_MAP.put(781, "\uf056");
        COLOR_MAP.put(781, C_MIST);



        D_ICON_MAP.put(800, "\uf00d");
        N_ICON_MAP.put(800, "\uf02e");
        COLOR_MAP.put(800, C_CLEAR);

        D_ICON_MAP.put(801, "\uf002");
        N_ICON_MAP.put(801, "\uf086");
        COLOR_MAP.put(801, C_CLOUDS);

        D_ICON_MAP.put(802, "\uf002");
        N_ICON_MAP.put(802, "\uf086");
        COLOR_MAP.put(802, C_CLOUDS);

        D_ICON_MAP.put(803, "\uf013");
        N_ICON_MAP.put(803, "\uf013");
        COLOR_MAP.put(803, C_CLOUDS);

        D_ICON_MAP.put(804, "\uf013");
        N_ICON_MAP.put(804, "\uf013");
        COLOR_MAP.put(804, C_CLOUDS);

    }// static

    private static DecimalFormat tempFormatter = new DecimalFormat("###.#");

    public static final double TEMP_CONST = 273.15;

    public static final long DAY_TIME = 24 * 60 * 60 * 1000;

    private Printer() { }// void constructor

    public static String pTemp(String val, boolean unit) {
        if (val == null) return "NO VALUE";
        return tempFormatter.format(Double.parseDouble(val) - TEMP_CONST) + ((unit) ? "°C" : "°");
    }// pTemp

    // UTILITY METHOD
    public static Date nextDays(Date date, int n) {
        return new Date(date.getTime() + (DAY_TIME * n));
    }// nextDays

    public static String pRainSnow(String val) {
        return ((val == null || val.equals("")) ? "--" : val) + " mm";
    }// pRainSnow

    public static String pWind(String val) {
        return "Wind: " + val + "m/s";
    }// pWind

    public static String pHumid(String val) {
        return "Humidity: " + val + "%";
    }// pHumid

    public static String pPress(String val) {
        return "Pressure: " + val + "hPa";
    }// pPress

    public static String pDay(Date date) {
        return new SimpleDateFormat("dd").format(date);
    }// pDay

    public static String pEDay(Date date) {
        return new SimpleDateFormat("EEE").format(date);
    }// pEDay

    public static String pEEEDay(Date date) {
        return new SimpleDateFormat("EEEE").format(date);
    }// pEEEDay

    public static String pHour(Date date) {
        return new SimpleDateFormat("HH").format(date);
    }// pHour

    public static String pHourMin(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }// pHourMin

    public static String pIcon(Integer code, boolean day) {
        String icon;
        if (day) {
            icon = D_ICON_MAP.get(code);
        } else {
            icon = N_ICON_MAP.get(code);
        }
        if(icon == null) {
            return UNCODED_ICON;
        }
        return icon;
    }// pIcon

    public static Integer pColor(Integer code) {
        Integer color = COLOR_MAP.get(code);
        return (color != null) ? color : C_DEFAULT;
    }

    public static Drawable getWallpaper(boolean d, Weather w, Context c) {

        Drawable im = ContextCompat.getDrawable(c, R.drawable.x_default);

        if (w == null) return im;

        // Day wallpaper
        if (d) {
            if (w.getId() >= 200 && w.getId() < 300) { // THUNDERSTORM
                im = ContextCompat.getDrawable(c, R.drawable.x_thunder);
            } else if (w.getId() >= 300 && w.getId() < 400) { // DRIZZLE
                im = ContextCompat.getDrawable(c, R.drawable.x_rainy);
            } else if (w.getId() >= 500 && w.getId() < 600) { // RAIN
                im = ContextCompat.getDrawable(c, R.drawable.d_rainy);
            } else if (w.getId() >= 600 && w.getId() < 700) { // SNOW
                im = ContextCompat.getDrawable(c, R.drawable.d_snow);
            } else if (w.getId() >= 700 && w.getId() < 800) { // ATMOSPHERE
                im = ContextCompat.getDrawable(c, R.drawable.d_fog);
            } else if (w.getId() == 800) { // CLEAR
                im = ContextCompat.getDrawable(c, R.drawable.d_clear);
            } else if (w.getId() > 800 && w.getId() < 900) { // CLOUDY
                im = ContextCompat.getDrawable(c, R.drawable.d_cloudy);
            }
        } else {
            if (w.getId() >= 200 && w.getId() < 300) { // THUNDERSTORM
                im = ContextCompat.getDrawable(c, R.drawable.x_thunder);
            } else if (w.getId() >= 300 && w.getId() < 400) { // DRIZZLE
                im = ContextCompat.getDrawable(c, R.drawable.x_rainy);
            } else if (w.getId() >= 500 && w.getId() < 600) { // RAIN
                im = ContextCompat.getDrawable(c, R.drawable.n_rainy);
            } else if (w.getId() >= 600 && w.getId() < 700) { // SNOW
                im = ContextCompat.getDrawable(c, R.drawable.n_snow);
            } else if (w.getId() >= 700 && w.getId() < 800) { // ATMOSPHERE
                im = ContextCompat.getDrawable(c, R.drawable.n_fog);
            } else if (w.getId() == 800) { // CLEAR
                im = ContextCompat.getDrawable(c, R.drawable.n_clear);
            } else if (w.getId() > 800 && w.getId() < 900) { // CLOUDY
                im = ContextCompat.getDrawable(c, R.drawable.n_cloudy);
            }
        }

        return im;
    }// getWallpaper

    // Title, text, icon
    public static Object[] pNotify(Weather m, Weather n) {

        String mId = "" + m.getId();
        String nId = "" + n.getId();

        int id = n.getId();

        System.out.println(id);

        Object[] obj = new Object[3];

        // Different codes
        if (mId.charAt(0) != nId.charAt(0)) {
            if (id >= 200 && id < 300) { // THUNDERSTORM
                obj[0] = "Thunderstorm";
                obj[1] = "Run to home, There is gonna be a thunderstorm!";
                obj[2] = R.drawable.not_thunder;
            } else if (id >= 300 && id < 400) { // DRIZZLE
                obj[0] = "Drizzle";
                obj[1] = "Some drop here, some drop there...";
                obj[2] = R.drawable.not_drizzle;
            } else if (id >= 500 && id < 600) { // RAIN
                obj[0] = "Rain";
                obj[1] = "Listen to Grandma: take your umbrella!";
                obj[2] = R.drawable.not_rainy;
            } else if (id >= 600 && id < 700) { // SNOW
                obj[0] = "Snow";
                obj[1] = "There is gonna be a snowballs war";
                obj[2] = R.drawable.not_snow;
            } else if (id >= 700 && id < 800) { // ATMOSPHERE
                obj[0] = "Atmosphere";
                obj[1] = "Ok ok, there may be fog or something else";
                obj[2] = R.drawable.not_atmo;
            } else if (id == 800) { // CLEAR
                obj[0] = "Clear";
                obj[1] = n.dayLight(m) ? "The sun is out! Take a walk." :
                        "Moon and stars, what more?";
                obj[2] = n.dayLight(m) ? R.drawable.not_clear : R.drawable.not_moon;
            } else if (id > 800 && id < 900) { // CLOUDY
                obj[0] = "Cloudy";
                obj[1] = "Mmmm, all is so gray...";
                obj[2] = R.drawable.not_cloudy;
            }

            return obj;
        }

        return new Object[0];

    }// pNotify

}// Printer
