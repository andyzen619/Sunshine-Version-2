package com.example.android.sunshine.app.Fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.sunshine.app.BuildConfig;
import com.example.android.sunshine.app.DetailActivity;
import com.example.android.sunshine.app.JsonWeatherExtractor;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.WeatherInfo;
import com.example.android.sunshine.app.WeatherInfoArrayAdapter;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

public class ForcastFragment extends Fragment {

    public ArrayAdapter forcastAdapter;
    private ListView listView;
    public WeatherInfo[] weatherObjects = new WeatherInfo[7];
    SharedPreferences sharedPref;
    String userSetLocation;

    public ForcastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userSetLocation = sharedPref.getString("LocationKey", "Toronto,ca");

        FetchWeatherTask task = new FetchWeatherTask();
        task.execute(userSetLocation);
        try {
            weatherObjects = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        forcastAdapter = new WeatherInfoArrayAdapter(
                getActivity(),
                R.layout.list_item_forcast,
                R.id.main_activity_forcast_list_item,
                new ArrayList(Arrays.asList(weatherObjects)));

        listView = (ListView) rootView.findViewById(R.id.main_activity_forcast_listvew);
        listView.setAdapter(forcastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedWeatherInfo = ((WeatherInfo) forcastAdapter.getItem(i)).getDetailFormat();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("forcast", selectedWeatherInfo);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeatherInfo();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecast_fragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_Refresh) {
            updateWeatherInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void updateWeatherInfo() {
        FetchWeatherTask task = new FetchWeatherTask();
        WeatherInfo[] weatherInfo = new WeatherInfo[7];
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userSetLocation = sharedPref.getString("LocationKey", "Toronto,ca");
        task.execute(userSetLocation);

        try {
            weatherInfo = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        forcastAdapter.clear();
        forcastAdapter.addAll(weatherInfo);
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, WeatherInfo[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected WeatherInfo[] doInBackground(String... params) {

            if(params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String format = "json";
            String units = "metric";
            int days = 7;

            JsonWeatherExtractor jsonWeatherExtractor = new JsonWeatherExtractor(getActivity());
            WeatherInfo[] extractedWeatherInfo = new WeatherInfo[7];

            try {
                String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                //String names of our queries
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNIT_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";

                Uri urlUri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNIT_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(days))
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();

                //URL url = new URL(ur);
                URL url = new URL(urlUri.toString());
                Log.v(LOG_TAG, "Built url: " + urlUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forcast JSON string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                extractedWeatherInfo = jsonWeatherExtractor.getWeatherDataFromJson(forecastJsonStr, 7);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return extractedWeatherInfo;
         }
    }
}
