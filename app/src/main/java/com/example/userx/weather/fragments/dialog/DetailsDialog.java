package com.example.userx.weather.fragments.dialog;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.userx.weather.R;
import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Weather;
import com.example.userx.weather.utils.DetailsArrayAdapter;
import com.example.userx.weather.utils.ForecastArrayAdapter;
import com.example.userx.weather.utils.Printer;

/**
 * Created by userx on 27/01/17.
 */

public class DetailsDialog extends DialogFragment {

    private Weather weather;

    public DetailsDialog() { }// constructor

    public static DetailsDialog newInstance(String title, Weather weather) {
        DetailsDialog frag = new DetailsDialog();

        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.weather = weather;

        return frag;
    }// newInstance

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_popup_fragment, container);
    }// onCreateView

    @Override
    public void onResume()
    {
        // This is a trick to enlarge the Dialog dimensions
        super.onResume();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Window win = getDialog().getWindow();

        win.setAttributes(lp);
    }// onResume

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (weather != null) {

            StringBuilder sb = new StringBuilder();
            sb.append(Printer.pDay(weather.getDate())).append(" ");
            sb.append(Printer.pEEEDay(weather.getDate())).append(", ");
            sb.append(Printer.pHourMin(weather.getDate()));

            TextView dayView = (TextView) view.findViewById(R.id.d_dayView);
            dayView.setText(sb.toString());

            ListView listView = (ListView) view.findViewById(R.id.d_list_details);
            listView.setAdapter(new DetailsArrayAdapter(getActivity(), weather.toDetailsArray()));
        }
    }// onViewCreated

}// DetailsDialog
