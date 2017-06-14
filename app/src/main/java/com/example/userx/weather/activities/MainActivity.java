package com.example.userx.weather.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.example.userx.weather.fragments.MainFragment;
import com.example.userx.weather.fragments.ForecastFragment;
import com.example.userx.weather.R;
import com.example.userx.weather.logic.UIObserver;
import com.example.userx.weather.model.DataManager;
import com.example.userx.weather.model.Weather;
import com.example.userx.weather.service.AlarmReceiver;
import com.example.userx.weather.service.Notifications;
import com.example.userx.weather.utils.Printer;


public class MainActivity extends AppCompatActivity implements UIObserver<Weather>, LocationListener {

    private boolean requested = false;

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    public final static String EXTRA_LAT = "com.example.userx.weather.LAT";
    public final static String EXTRA_LON = "com.example.userx.weather.MLON";

    private static final String SELECTED_ITEM = "arg_selected_item";

    // Points to the last opened fragment
    private Fragment lastFragment;

    // Index of select item on BottomNavigation
    private int mSelectedItem;

    // ProgressDialog for gps and request
    ProgressDialog progressDialog;

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    private double lastLat = 0;
    private double lastLon = 0;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.extra_menu, menu);
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }// isNetworkConnected

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.gps_button) {

            if (!isNetworkConnected()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No Internet connection");
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }

            askForCoords();

        }// R.id.gps_button
        else if (item.getItemId() == R.id.search_button) {

            if (!isNetworkConnected()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No Internet connection");
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }

            // This makes a dialog for receiving the city name
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("City name:");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String cityName = input.getText().toString();

                    if (cityName == null || cityName.equals("")) return;

                    // Make the requests
                    requested = true;
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage(getString(R.string.dialog_search));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    System.out.println("UPDATE: " + DataManager.getInstance().newRequestByCity(cityName));
                }
            });

            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }// R.id.search_button
        return true;
    }// onOptionsItemSelected

    private void initUI(Bundle savedInstanceState) {
        // BottomNavigation and its actionListener
        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        // Set the correct fragment
        MenuItem selectedItem = (savedInstanceState == null) ? bottomNav.getMenu().getItem(0) :
                bottomNav.getMenu().findItem(savedInstanceState.getInt(SELECTED_ITEM, 0));
        selectFragment(selectedItem);

    }// initUI

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI(savedInstanceState);

        // Start the alarm if needed
        AlarmReceiver.startAlarm(this);

        // Init DataManager and load cache
        DataManager.getInstance().initialize(MainActivity.this);
    }// onCreate

    @Override
    protected void onStart() {
        super.onStart();

        // Register to Observable
        DataManager.register(DataManager.WEATHER, MainActivity.this);
    }// onStart

    @Override
    protected void onResume() {
        super.onResume();

        // Show initial value
        update(DataManager.getInstance().getWeather());

        DataManager.getInstance().fakeUpdate();
    }// onResume

    @Override
    protected void onStop() {
        super.onStop();

        // Unregister to Observable
        DataManager.register(DataManager.WEATHER, MainActivity.this);
    }// onStop

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }// onSaveInstanceState

    @Override
    public void onBackPressed() {
        // Add functions
        BottomNavigationView mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);

        if (mSelectedItem != homeItem.getItemId()) {
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }// onBackPressed

    private void selectFragment(MenuItem item) {

        if (mSelectedItem == item.getItemId())
            return;

        Fragment frag = null;

        // Init corresponding fragment
        switch (item.getItemId()) {

            case R.id.menu_home:
                frag = MainFragment.newInstance();
                break;

            case R.id.menu_search:
                frag = ForecastFragment.newInstance();
                break;

            case R.id.menu_notifications:
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra(EXTRA_LAT, lastLat);
                intent.putExtra(EXTRA_LON, lastLon);
                startActivity(intent);
                return;
        }// switch

        // Update selected item
        mSelectedItem = item.getItemId();

        /*
        // uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() != item.getItemId());
        }
        */

        // TODO Optimization, do not recreate the same fragment
        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Remove last fragment
            if (lastFragment != null)
                ft.remove(lastFragment);

            ft.add(R.id.container, frag, frag.getTag());
            lastFragment = frag;
            ft.commit();
        }
    }// selectFragment

    @Override
    public void update(Weather w) {

        if (w == null) return;

        if (requested) {
            requested = false;
            progressDialog.hide();
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.setTitle(w.getCity() + ", " + w.getCountry());

    }// update


    private boolean checkPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {



            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            return false;
        }

        return true;
    }// checkPermission

    private void askForCoords() {
        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkPermission()) {

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.dialog_location));
                progressDialog.setCancelable(false);
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            locationManager.removeUpdates(MainActivity.this);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                });
                progressDialog.show();

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }

            } else {
                showLocationSettingsDialog();
            }
        }
    }// askForCoords

    private void showLocationSettingsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.location_settings);
        alertDialog.setMessage(R.string.location_settings_message);
        alertDialog.setPositiveButton(R.string.location_settings_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }// showLocationSettingsDialog

    // GPS FROM HERE

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    askForCoords();
                }
            }
        }
    }// onRequestPermissionsResult

    @Override
    public void onLocationChanged(Location location) {
        progressDialog.hide();

        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            Log.e("LocationManager", "Error while trying to stop listening for location updates. This is probably a permissions issue", e);
        }

        lastLat = location.getLatitude();
        lastLon = location.getLongitude();

        System.out.println("GPS REQ: " + DataManager.getInstance().newRequestByCoords(lastLat, lastLon));
    }// onLocationChanged

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }// onStatusChanged

    @Override
    public void onProviderEnabled(String provider) {
    }// onProviderEnabled

    @Override
    public void onProviderDisabled(String provider) {
    }// onProviderDisabled
}// MainActivity
