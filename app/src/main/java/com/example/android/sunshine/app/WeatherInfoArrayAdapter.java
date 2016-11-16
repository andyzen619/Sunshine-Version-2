package com.example.android.sunshine.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by andy on 13/11/16.
 */

public class WeatherInfoArrayAdapter extends ArrayAdapter {

    private ArrayList<WeatherInfo> weatherInfoCollection;


    public WeatherInfoArrayAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherInfo selectedWeatherInfo = (WeatherInfo) getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_forcast, parent, false);
        }
        TextView mainFragmentWeatherText = (TextView) convertView.findViewById(R.id.list_item_forcast_textview);
        mainFragmentWeatherText.setText(selectedWeatherInfo.getMainFormat());
        return convertView;
    }
}
