package com.example.userx.weather.fragments;


import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.userx.weather.R;
import com.example.userx.weather.fragments.dialog.DetailsDialog;
import com.example.userx.weather.logic.UIObserver;
import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Weather;
import com.example.userx.weather.utils.Printer;


public class MainFragment extends Fragment implements UIObserver<Weather> {

    private static final String ARG_TEXT = "arg_text";
    private static final String ARG_COLOR = "arg_color";

    public static Fragment newInstance() {

        MainFragment frag = new MainFragment();

        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }// newInstance

    public MainFragment() {
        super();
    }// constructor

    @Override
    public void onStart() {
        super.onStart();

        // Register to Observable
        DataManager.register(DataManager.WEATHER, this);
    }// onStart

    @Override
    public void onStop() {
        super.onStop();

        // Unregister to Observable
        DataManager.unRegister(DataManager.WEATHER, this);
    }// onStop

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_fragment, container, false);
    }// Override

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Define the iconView and the onClickListener to show the details
        TextView iconView = (TextView) view.findViewById(R.id.iconView);
        iconView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf"));
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DetailsDialog deatilsDialog = DetailsDialog.newInstance("Details", DataManager.getInstance().getWeather());
                deatilsDialog.show(fm, "details_popup_fragment");
            }
        });

        update(DataManager.getInstance().getWeather());
    }// onViewCreated

    @Override
    public void onResume() {
        super.onResume();

        // update(DataManager.getInstance().getWeather());
    }// onResume

    @Override
    public void update(Weather w) {

        if (w == null) return;

        setText(R.id.iconView, Printer.pIcon(w.getId(), w.dayLight(w)));
        setText(R.id.tempView, Printer.pTemp(w.getTemperature(), true));
        setText(R.id.descrView, w.getDescription());
        setText(R.id.windView, Printer.pWind(w.getWind()));
        setText(R.id.humidView, Printer.pHumid(w.getHumidity()));

        if (getView() != null) {
            ImageView bgView = (ImageView) getView().findViewById(R.id.bgView);

            Drawable draw = Printer.getWallpaper(
                    DataManager.getInstance().getWeather().dayLight(
                            DataManager.getInstance().getWeather()), w, getContext());

            bgView.setBackground(draw);
        }
    }// update

    private void setText(int id, String text) {
        // Utility method to write on textView
        if (getView() != null) {
            View mContent = getView().findViewById(R.id.fragment_main_content);
            TextView tv = (TextView) mContent.findViewById(id);
            tv.setText(text);
        }
    }// setText

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }// onSaveInstanceState

}// MainFragment
