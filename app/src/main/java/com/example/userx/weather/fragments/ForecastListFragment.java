package com.example.userx.weather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.userx.weather.R;
import com.example.userx.weather.activities.MainActivity;
import com.example.userx.weather.fragments.dialog.DetailsDialog;
import com.example.userx.weather.logic.UIObserver;
import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Forecast;
import com.example.userx.weather.model.Weather;
import com.example.userx.weather.utils.ForecastArrayAdapter;

public class ForecastListFragment extends ListFragment implements UIObserver<Forecast> {

    public static final String ARG_IDX = "arg_idx";

    private ForecastArrayAdapter adapter;

    private int idx = 0;

    public ForecastListFragment() {
        super();
    }// ForecastListFragment

    @Override
    public void onStart() {
        super.onStart();

        // Register to Observable
        DataManager.register(DataManager.FORECAST, this);
    }// onStart

    @Override
    public void onStop() {
        super.onStop();

        // Unregister to Observable
        DataManager.unRegister(DataManager.FORECAST, this);
    }// onStop

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast_list, container, false);
    }// onCreateView

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve idx from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            idx = args.getInt(ARG_IDX);
        } else {
            idx = savedInstanceState.getInt(ARG_IDX);
        }

        // Create ArrayAdapter object to wrap the data source
        adapter = new ForecastArrayAdapter(getActivity(), DataManager.getInstance().getForecast().getDay(idx));
        setListAdapter(adapter);

        setRetainInstance(true);

        // Init views
        View mContent = view.findViewById(R.id.fragment_forecast_list_content);
        mContent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_search));
    }// onViewCreated

    // Handle Item click event
    @Override
    public void onListItemClick(ListView l, View view, int position, long id){

        Weather weather = (Weather) l.getAdapter().getItem(position);

        // Show dialog
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DetailsDialog detailsDialog = DetailsDialog.newInstance("Details", weather);
        detailsDialog.show(fm, "details_popup_fragment");
    }// onListItemClick

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_IDX, idx);
        super.onSaveInstanceState(outState);
    }// onSaveInstanceState

    @Override
    public void update(Forecast o) {
        adapter.notifyDataSetChanged();
    }// update

}// ForecastListFragment
