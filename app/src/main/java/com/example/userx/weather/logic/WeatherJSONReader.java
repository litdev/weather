package com.example.userx.weather.logic;

import android.util.Log;

import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by userx on 28/01/17.
 */

public class WeatherJSONReader implements JSONReader<Weather> {

    private static final String TAG = "WeatherJSONReader";

    public final static WeatherJSONReader READER = new WeatherJSONReader();

    private WeatherJSONReader() { }// constructor

    @Override
    public Weather read(JSONObject obj) {

        Weather weather = new Weather();

        if (obj == null) return null;

        int code = obj.optInt("cod", -1);

        // If not success
        if (code != 200) return null;

        try {
            weather.setCity(obj.getString("name"));
            weather.setDate(new Date(obj.getLong("dt") * 1000));

            JSONObject windObj = obj.getJSONObject("wind");
            weather.setWind(windObj.getString("speed"));
            weather.setWindDirectionDegree(windObj.optDouble("deg"));

            JSONObject sysObj = obj.getJSONObject("sys");
            weather.setCountry(sysObj.getString("country"));
            weather.setSunrise(new Date(sysObj.getLong("sunrise") * 1000));
            weather.setSunset(new Date(sysObj.getLong("sunset") * 1000));

            JSONObject mainObj = obj.getJSONObject("main");
            weather.setTemperature("" + mainObj.getDouble("temp"));
            weather.setMinTemp("" + mainObj.getDouble("temp_min"));
            weather.setMaxTemp("" + mainObj.getDouble("temp_max"));
            weather.setPressure("" + mainObj.getDouble("pressure"));
            weather.setHumidity("" + mainObj.getDouble("humidity"));

            JSONObject cloudObj = obj.getJSONObject("clouds");
            weather.setCloudiness(cloudObj.getString("all"));

            JSONObject snowObj = obj.optJSONObject("snow");
            weather.setSnow((snowObj == null) ? "" : snowObj.optString("3h"));

            JSONObject rainObj = obj.optJSONObject("rain");
            weather.setRain((rainObj == null) ? "" : rainObj.optString("3h"));

            JSONObject coordObj = obj.optJSONObject("coord");
            weather.setLat(coordObj.getDouble("lat"));
            weather.setLon(coordObj.getDouble("lon"));

            JSONObject wObj = obj.getJSONArray("weather").getJSONObject(0);
            weather.setDescription(wObj.getString("description"));
            weather.setId(wObj.getInt("id"));

            weather.setInit(true);

        } catch (JSONException e) {
            Log.d(TAG, "err: " + e);
        }

        return weather;
    }// read

    @Override
    public Integer getType() {
        return DataManager.WEATHER;
    }// getType

    @Override
    public String getReq() {
        return RemoteData.REQ_WEATHER;
    }// getReq

}// WeatherJSONReader
