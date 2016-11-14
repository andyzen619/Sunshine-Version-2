package com.example.android.sunshine.app;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by andy on 13/11/16.
 */

public class WeatherInfoArrayAdapter extends ArrayAdapter {


    public WeatherInfoArrayAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
