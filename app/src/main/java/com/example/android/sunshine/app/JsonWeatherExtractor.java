package com.example.android.sunshine.app;

import org.json.JSONException;

/**
 * Created by Andy DESK on 11/2/2016.
 */

public class JsonWeatherExtractor {

    //TODO:1
    private String getReadableDateString(long time){
        return "";
    }

    //TODO:2
    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        return "";
    }

    //TODO:3
    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {
        return null;
    }
}
