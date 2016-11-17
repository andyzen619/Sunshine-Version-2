package com.example.android.sunshine.app.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.app.R;

import static com.example.android.sunshine.app.DetailActivity.selectedWeatherInfo;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    private TextView weatherInfoView;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        weatherInfoView = (TextView) rootView.findViewById(R.id.detail_activity_weather_textview);
        weatherInfoView.setText(selectedWeatherInfo);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail, menu);
    }
}
