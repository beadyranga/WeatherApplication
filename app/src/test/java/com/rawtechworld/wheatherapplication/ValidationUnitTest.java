package com.rawtechworld.wheatherapplication;

import android.location.Location;
import android.location.LocationManager;

import com.rawtechworld.weatherapplication.activities.MainActivity;
import com.rawtechworld.weatherapplication.controller.Utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ValidationUnitTest {
    Location location;

    @Test
    public void getLocationText() {
        assertEquals("Unknown location", Utils.getLocationText(location));

    }
}