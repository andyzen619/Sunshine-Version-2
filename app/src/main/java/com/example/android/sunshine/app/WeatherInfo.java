package com.example.android.sunshine.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import java.io.Serializable;

/**
 * Created by andy on 13/11/16.
 */

public class WeatherInfo implements Serializable {

    private String date;
    private String description;
    private double hi;
    private double low;
    private SharedPreferences preferences;

    public WeatherInfo(String date, String description, Double hi, Double low, Context context) {
        this.date = date;
        this.description = description;
        this.hi = hi;
        this.low = low;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getHi() {
        String unitSelection = preferences.getString("UnitsKey", "Metric"); //TODO: get temperature based selected units setting
        if(unitSelection == )
        return hi;
    }

    public double getLow() {
        return low;
    }

    public String getMainFormat() {
        return getDate() +
                "                 " +
                getDescription() +
                "                 " +
                "High: " +
                (int) getHi() +
                "          " +
                "Low: " +
                (int)low;
    }

    public String getDetailFormat() {
        return getDate() +
                " -" +
                getDescription() +
                "- " +
                (int) getHi()+
                "/" +
                (int) getLow();
    }
}
