package com.example.userx.weather.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.userx.weather.R;
import com.example.userx.weather.model.Weather;

import java.util.ArrayList;

/**
 * Created by userx on 26/01/17.
 */

public class DetailsArrayAdapter extends ArrayAdapter<String[]> {

    public DetailsArrayAdapter(Context context, ArrayList<String[]> couples) {
        super(context, 0, couples);
    }// constructor

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        String[] couple = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.details_popup_item, parent, false);
        }

        // Lookup view for data population
        TextView labelView = (TextView) convertView.findViewById(R.id.i_label);
        TextView valueView = (TextView) convertView.findViewById(R.id.i_value);

        labelView.setText(couple[0]);
        valueView.setText(couple[1]);

        // Return the completed view to render on screen
        return convertView;
    }

}// ForecastArrayAdapter
