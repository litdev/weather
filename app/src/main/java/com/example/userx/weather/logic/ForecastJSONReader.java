package com.example.userx.weather.logic;

import android.util.Log;

import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Forecast;
import com.example.userx.weather.model.Weather;
import com.example.userx.weather.utils.Printer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

/**
 * Created by userx on 28/01/17.
 */

public class ForecastJSONReader implements JSONReader<Forecast> {

    private static final String TAG = "ForecastJSONReader";

    public final static ForecastJSONReader READER = new ForecastJSONReader();

    private ForecastJSONReader() { }// constructor

    @Override
    public Forecast read(JSONObject obj) {
        Forecast forecast = new Forecast();

        if (obj == null) return null;

        int code = obj.optInt("cod", -1);

        // If not success
        if (code != 200) return null;

        try {
            int cnt = obj.getInt("cnt");

            JSONObject cityObj = obj.optJSONObject("city");
            String city = cityObj.getString("name");

            JSONObject coordObj = cityObj.optJSONObject("coord");
            double lat = coordObj.getDouble("lat");
            double lon = coordObj.getDouble("lon");

            JSONArray list = obj.getJSONArray("list");

            int day = -1;
            int idx = -1;

            // For each day read the related weather list
            for (int i = 0; i < cnt; ++i) {

                Weather weather = new Weather();

                weather.setCity(city);

                weather.setLat(lat);
                weather.setLon(lon);

                JSONObject jObk = list.getJSONObject(i);

                weather.setDate(new Date(jObk.getLong("dt") * 1000));

                JSONObject mainObj = jObk.getJSONObject("main");
                weather.setTemperature("" + mainObj.getDouble("temp"));
                weather.setMinTemp("" + mainObj.getDouble("temp_min"));
                weather.setMaxTemp("" + mainObj.getDouble("temp_max"));
                weather.setPressure("" + mainObj.getDouble("pressure"));
                weather.setHumidity("" + mainObj.getDouble("humidity"));

                JSONObject cloudObj = jObk.getJSONObject("clouds");
                weather.setCloudiness(cloudObj.getString("all"));

                JSONObject snowObj = jObk.optJSONObject("snow");
                weather.setSnow((snowObj == null) ? "" : snowObj.optString("3h"));

                JSONObject rainObj = jObk.optJSONObject("rain");
                weather.setRain((rainObj == null) ? "" : rainObj.optString("3h"));

                JSONObject windObj = jObk.getJSONObject("wind");
                weather.setWind(windObj.getString("speed"));
                weather.setWindDirectionDegree(windObj.optDouble("deg"));

                JSONObject wObj = jObk.getJSONArray("weather").getJSONObject(0);
                weather.setId(wObj.getInt("id"));
                weather.setDescription(wObj.getString("description"));

                weather.setInit(true);

                if (day != Integer.parseInt(Printer.pDay(weather.getDate()))) {
                    day = Integer.parseInt(Printer.pDay(weather.getDate()));
                    idx++;
                    if (idx >= Forecast.DAYS)
                        break;
                }

                forecast.getDay(idx).add(weather);
            }
        } catch (JSONException e) {
            Log.d(TAG, "err: " + e);
        }
        return forecast;
    }// read

    @Override
    public Integer getType() {
        return DataManager.FORECAST;
    }// getType

    @Override
    public String getReq() {
        return RemoteData.REQ_FORECAST;
    }// getReq

}// ForecastJSONReader
