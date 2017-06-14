package com.example.userx.weather.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.userx.weather.R;
import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Weather;

import java.util.ArrayList;

/**
 * Created by userx on 26/01/17.
 */

public class ForecastArrayAdapter extends ArrayAdapter<Weather> {

    public ForecastArrayAdapter(Context context, ArrayList<Weather> weathers) {
        super(context, 0, weathers);
    }// constructor

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Weather weather = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.forecast_item, parent, false);
        }

        RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.f_layout);
        rl.setBackgroundColor(Printer.pColor(weather.getId()));

        // Lookup view for data population
        TextView iconView = (TextView) convertView.findViewById(R.id.f_iconView);
        // TextView dayView = (TextView) convertView.findViewById(R.id.f_dayView);
        TextView tempView = (TextView) convertView.findViewById(R.id.f_tempView);
        TextView descrView = (TextView) convertView.findViewById(R.id.f_descrView);
        TextView hourView = (TextView) convertView.findViewById(R.id.f_hourView);


        iconView.setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "fonts/weather.ttf"));

        // Populate the data into the template view using the data object
        iconView.setText(Printer.pIcon(weather.getId(), weather.dayLight(DataManager.getInstance().getWeather())));

        tempView.setText(Printer.pTemp(weather.getTemperature(), false));
        // dayView.setText(Printer.pDay(weather.getDate()));
        descrView.setText(weather.getDescription());
        hourView.setText(Printer.pHour(weather.getDate()));
        // Return the completed view to render on screen
        return convertView;
    }// getView

}// ForecastArrayAdapter
