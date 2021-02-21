/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
