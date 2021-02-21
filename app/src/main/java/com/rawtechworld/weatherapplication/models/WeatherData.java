package com.rawtechworld.weatherapplication.models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Model Class to Map Received JSON Data.
 */
public class WeatherData {
    String mLatitude;
    String mLongitude;
    String mTimeZone;
    JSONObject mCurrentlyUpdate;
    JSONObject mHourlyUpdate;
    JSONObject mDailyUpdate;
    JSONArray mAlerts;
    JSONObject mFlags;
    String mOffSet;

    public WeatherData(){}

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public JSONObject getCurrentlyUpdate() {
        return mCurrentlyUpdate;
    }

    public void setCurrentlyUpdate(JSONObject mCurrentlyUpdate) {
        this.mCurrentlyUpdate = mCurrentlyUpdate;
    }

    public JSONObject getHourlyUpdate() {
        return mHourlyUpdate;
    }

    public void setHourlyUpdate(JSONObject mHourlyUpdate) {
        this.mHourlyUpdate = mHourlyUpdate;
    }

    public JSONObject getDailyUpdate() {
        return mDailyUpdate;
    }

    public void setDailyUpdate(JSONObject mDailyUpdate) {
        this.mDailyUpdate = mDailyUpdate;
    }

    public JSONArray getAlerts() {
        return mAlerts;
    }

    public void setAlerts(JSONArray mAlerts) {
        this.mAlerts = mAlerts;
    }

    public JSONObject getFlags() {
        return mFlags;
    }

    public void setFlags(JSONObject mFlags) {
        this.mFlags = mFlags;
    }

    public String getOffSet() {
        return mOffSet;
    }

    public void setOffSet(String mOffSet) {
        this.mOffSet = mOffSet;
    }

}
