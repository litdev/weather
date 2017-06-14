package com.example.userx.weather.logic;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Forecast;
import com.example.userx.weather.model.Weather;
import com.example.userx.weather.utils.AppData;
import com.example.userx.weather.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

/**
 * Created by userx on 24/01/17.
 */

public class RemoteData extends AsyncTask<String, String, JSONObject> {

    public static final String REQ_WEATHER = "weather";
    public static final String REQ_FORECAST = "forecast";

    public static final String TAG = "RemoteData";

    private static final String API_REQ_PRE = "http://api.openweathermap.org/data/2.5/";
    private static final String API_REQ_SEP= "?";
    private static final String API_REQ_LAT= "lat=";
    private static final String API_REQ_LON= "&lon=";
    private static final String API_REQ_CITY= "q=";
    private static final String API_REQ_APPKEY= "&APIKEY=";

    private static final String API_TEST = "http://192.168.0.12:8000/weather";
    private static final String EXT_TEST = ".json";

    private static int val = 0;

    private final JSONReader PARSER;
    private final String URL;

    public static RemoteData newByCity(JSONReader parser, String city) {

        if (parser == null || city == null) return null;

        StringBuilder reqBuilder = new StringBuilder(API_REQ_PRE);
        reqBuilder.append(parser.getReq());
        reqBuilder.append(API_REQ_SEP);
        reqBuilder.append(API_REQ_CITY);
        reqBuilder.append(city);
        reqBuilder.append(API_REQ_APPKEY);
        reqBuilder.append(AppData.APPKEY);

        Log.d(TAG, "REQ: " + reqBuilder.toString());

        return new RemoteData(parser, reqBuilder.toString());

        /*
        // TEST
        if (parser.getClass() == WeatherJSONReader.class)
            val = (val == 0) ? 1 : 0;
        System.out.println("val: " + val);
        return new RemoteData(parser, API_TEST + val + EXT_TEST);
        */
    }// newInstance

    public static RemoteData newBYCoords(JSONReader parser, double lat, double lon) {
        if (parser == null) return null;

        StringBuilder reqBuilder = new StringBuilder(API_REQ_PRE);
        reqBuilder.append(parser.getReq());
        reqBuilder.append(API_REQ_SEP);
        reqBuilder.append(API_REQ_LAT);
        reqBuilder.append(lat);
        reqBuilder.append(API_REQ_LON);
        reqBuilder.append(lon);
        reqBuilder.append(API_REQ_APPKEY);
        reqBuilder.append(AppData.APPKEY);

        Log.d(TAG, "REQ: " + reqBuilder.toString());

        return new RemoteData(parser, reqBuilder.toString());

        /*
        // TEST
        return new RemoteData(parser, API_TEST + val + EXT_TEST);
        */
    }// newInstance

    public void acts() {
        execute(URL);
    }// acts

    private RemoteData(JSONReader parser, String url) {
        PARSER = parser;
        URL = url;
    }// constructor

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject parsedObj = null;
        if (params != null && params.length > 0) {
            // Getting JSON from URL
            parsedObj = JSONParser.URLtoJSON(params[0]);
        }

        return parsedObj;
    }// doInBackground

    @Override
    public void onPostExecute(JSONObject jObj) {

        Object obj = PARSER.read(jObj);
        updataData(obj, PARSER.getType());
    }// onPostExecute

    // Update DataManager
    private void updataData(Object obj, Integer code) {

        Log.d(TAG, "UpdateData has been called:");
        if (obj == null || code == null) {
            DataManager.getInstance().fakeUpdate();
            return;
        }

        if (code == DataManager.WEATHER) {
            DataManager.getInstance().setWeather((Weather) obj);
        } else if (code == DataManager.FORECAST) {
            DataManager.getInstance().setForecast((Forecast) obj);
        }
    }// updataData

}// RemoteData
