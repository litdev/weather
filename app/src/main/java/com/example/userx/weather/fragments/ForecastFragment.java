package com.example.userx.weather.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;
import android.widget.TextView;
import com.example.userx.weather.R;
import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Forecast;
import com.example.userx.weather.model.Weather;
import com.example.userx.weather.utils.Printer;

import java.util.Date;


public class ForecastFragment extends Fragment {

    public static final String ARG_COLOR = "arg_color";

    public static Fragment newInstance() {

        Fragment frag = new ForecastFragment();

        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }// newInstance

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Define the TabHost Structure and Fragments
        FragmentTabHost tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.layout.menu_fragment);


        // Define Bundles and Arguments
        Bundle[] args = new Bundle[Forecast.DAYS];

        for (int i = 0;i < Forecast.DAYS; ++i) {
            args[i] = new Bundle();
            args[i].putInt(ARG_COLOR, ContextCompat.getColor(getContext(), R.color.color_search));
            args[i].putInt(ForecastListFragment.ARG_IDX, i);
        }

        Weather cW = DataManager.getInstance().getWeather();

        addTab(tabHost, cW.getDate(), args[0]);

        for (int i = 1; i < Forecast.DAYS; ++i) {
            addTab(tabHost, Printer.nextDays(cW.getDate(), i), args[i]);
        }

        // Define TabWidget Look
        final TabWidget tw = (TabWidget) tabHost.findViewById(android.R.id.tabs);
        for (int i = 0; i < tw.getChildCount(); ++i)
        {
            final View tabView = tw.getChildTabViewAt(i);
            final TextView tv = (TextView)tabView.findViewById(android.R.id.title);
            tv.setTextSize(20);
        }

        return tabHost;
    }// onCreateView

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO ???
    }// onViewCreated

    private void addTab(FragmentTabHost tabHost, Date day, Bundle bundle) {
        String vDay = Printer.pDay(day);
        String sDay = Printer.pEDay(day);

        tabHost.addTab(tabHost.newTabSpec("day" + vDay).setIndicator(vDay + " " + sDay),
                ForecastListFragment.class, bundle);
    }// addTab

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }// onSaveInstanceState

}// ForecastFragment
