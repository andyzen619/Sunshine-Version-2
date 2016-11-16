package com.example.android.sunshine.app;

import java.io.Serializable;

/**
 * Created by andy on 13/11/16.
 */

public class WeatherInfo implements Serializable {

    private String date;
    private String description;
    private double hi;
    private double low;

    public WeatherInfo(String date, String description, Double hi, Double low) {
        this.date = date;
        this.description = description;
        this.hi = hi;
        this.low = low;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getHi() {
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
