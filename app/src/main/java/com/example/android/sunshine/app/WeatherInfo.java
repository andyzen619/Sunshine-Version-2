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
    String unitsKey;
    String metricValue;
    private double hi;
    private double low;
    private SharedPreferences preferences;
    public static Context context;

    public WeatherInfo(String date, String description, Double hi, Double low, Context context) {
        this.date = date;
        this.description = description;
        this.hi = hi;
        this.low = low;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
        this.unitsKey = context.getResources().getString(R.string.units_of_measurment_key);
        this.metricValue = context.getResources().getString(R.string.units_metric_value);
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getHi() {
        String unitSelection = preferences.getString(unitsKey, metricValue);
        Double result;
        if(unitSelection.equals(metricValue)) {
            result = hi;
        }
        else {
            result = hi * 1.8 + 32.0;
        }
        return Math.ceil(result);
    }

    public double getLow() {
        unitsKey = context.getResources().getString(R.string.units_of_measurment_key);
        String unitSelection = preferences.getString(unitsKey, metricValue);
        Double result;
        if(unitSelection.equals(metricValue)) {
            result = low;
        }
        else {
            result = low * 1.8 + 32.0;
        }
        return Math.ceil(result);
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
                (int) getLow();
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
