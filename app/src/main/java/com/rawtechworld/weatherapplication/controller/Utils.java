package com.rawtechworld.weatherapplication.controller;


import android.content.Context;
import android.location.Location;

import com.rawtechworld.wheatherapplication.R;

import java.text.DateFormat;
import java.util.Date;

public class Utils {
    /**
     * URL for Weather Fetching
      */
    public static final String WEATHER_URL = "https://api.darksky.net/forecast/2bb07c3bece89caf533ac9a5d23d8417/";

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                  location.getLatitude() + ", " + location.getLongitude() ;
    }

    /**
     * Get the Location Title for update
     */
    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

}
