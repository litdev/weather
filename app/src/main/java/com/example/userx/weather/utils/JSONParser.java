package com.example.userx.weather.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by userx on 24/01/17.
 */

public class JSONParser {

    public static JSONObject URLtoJSON(String url) {

        InputStream      inputS  = null;
        JSONObject       jObj    = null;
        String           json    = "";

        // Make the connection and obtain the stream
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            // If wrong url
            if(connection == null)
                return null;

            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            // If not success
            if(responseCode != 200)
                return null;

            inputS = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read from the stream and parse the answer
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputS, "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();
            String line = null;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                Log.d(i++ + "", line.substring(line.length() - 20, line.length()));
                sb.append(line).append("\n");
            }
            reader.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // Make the JSON object from computed string
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // Return JSON String
        return jObj;
    }// URLtoJSON

}// JSONParser