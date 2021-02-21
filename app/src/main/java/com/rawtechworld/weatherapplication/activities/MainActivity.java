package com.rawtechworld.weatherapplication.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.rawtechworld.weatherapplication.controller.AppController;
import com.rawtechworld.weatherapplication.models.WeatherData;
import com.rawtechworld.wheatherapplication.BuildConfig;
import com.rawtechworld.wheatherapplication.R;
import com.rawtechworld.weatherapplication.controller.Utils;
import com.rawtechworld.weatherapplication.services.LocationService;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * The only activity in this Project
 * This will binding with service to fetch the location.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Used in checking for runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * The BroadcastReceiver used to listen from broadcasts from the service.
     */
    private MyReceiver myReceiver;

    /**
     * A reference to the service used to get location updates
     */
    private LocationService mLocationService = null;

    /**
     * Updated Location value
     */
    private static Location mUpdatedLocation;

    /**
     * Tracks the bound state of the service.
     */
    private boolean mBound = false;

    /**
     * Summary of the Fetch Weather
     */
    private TextView mSummaryView;

    /**
     * Weather Fetching Data Object
     */
    private WeatherData mWeatherData;

    /**
     * Progress dialog
     */

    private ProgressDialog mPDialog;

    /**
     * state of the connection to the service.
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            mLocationService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocationService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        setContentView(R.layout.activity_main);

        if (!checkPermissions()) {
            requestPermissions();
        }

        mPDialog = new ProgressDialog(this);
        mPDialog.setMessage("Please wait...");
        mPDialog.setCancelable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Bind to the service.
        bindService(new Intent(this, LocationService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
        // UI elements.
        Button mFetchWhetherUpdatesButton = (Button) findViewById(R.id.fetch_whether_update);
        mSummaryView = findViewById(R.id.timeZone);
        if (mLocationService != null) {
            mLocationService.requestLocationUpdates();
        }
        mFetchWhetherUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    mLocationService.requestLocationUpdates();
                }

                if (mUpdatedLocation != null) {
                    getWhetherRequest(Utils.WEATHER_URL + Utils.getLocationText(mUpdatedLocation));
                } else {
                    Toast.makeText(MainActivity.this, R.string.wait_for_location,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground,
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();

    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Request Permissions for Access Fine Location Access
     */
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showMessageOKCancel(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_PERMISSIONS_REQUEST_CODE);
                            }
                        });
            }
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Method to Display the permission rationale
     * @param okListener Listener to bind for button click
     */
    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.permission_rationale)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mLocationService.requestLocationUpdates();
            } else {
                // Permission denied.
                Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    /**
     * Receiver for broadcasts sent by {@link LocationService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationService.EXTRA_LOCATION);
            if (location != null) {
                Toast.makeText(MainActivity.this, Utils.getLocationText(location),
                        Toast.LENGTH_SHORT).show();
                mUpdatedLocation = location;
            }
        }
    }

    /**
     * Method to Send the Weather Request
     * @param url Wheather Request Url
     */
    private void getWhetherRequest(String url) {
        showDialog();
        mWeatherData = new WeatherData();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parsing json object response
                    mWeatherData.setLatitude(response.getString("latitude"));
                    mWeatherData.setLongitude(response.getString("longitude"));
                    mWeatherData.setTimeZone(response.getString("timezone"));
                    mWeatherData.setCurrentlyUpdate(response.getJSONObject("currently"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, mWeatherData.getCurrentlyUpdate().toString());
                try {
                    updateUserInterface();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Json Exception Occurred");
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /**
     * Update UI Text View with the Received Weather Data.
     * @throws JSONException exception when  decoding values.
     */
    private void updateUserInterface() throws JSONException {
        String updateDataString = " \nLatitude : " + mWeatherData.getLatitude() + "\n" +
                "Longitude : " + mWeatherData.getLongitude() + "\n" +
                "Time Zone : " + mWeatherData.getTimeZone() + "\n" +
                "summary   : " + mWeatherData.getCurrentlyUpdate().getString("summary") + "\n" +
                "icon      : " + mWeatherData.getCurrentlyUpdate().getString("icon") + "\n" +
                "time      : " + mWeatherData.getCurrentlyUpdate().getString("time") + "\n" +
                "temperature : " + mWeatherData.getCurrentlyUpdate().getString("temperature");
        mSummaryView.setText(updateDataString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationService.removeLocationUpdates();
    }

    /**
     * Display Dialog
     */
    private void showDialog() {
        if (!mPDialog.isShowing()) {
            mPDialog.show();
        }
    }

    /**
     * Hide dialog
     */
    private void hideDialog() {
        if (mPDialog.isShowing()) {
            mPDialog.dismiss();
        }
    }
}