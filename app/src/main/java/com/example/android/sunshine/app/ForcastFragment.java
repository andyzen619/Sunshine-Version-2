package com.example.android.sunshine.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

public class ForcastFragment extends Fragment {

    public ArrayAdapter forcastAdapter;
    private ListView listView;

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
        ArrayList<String> fakeData = new <String>ArrayList();
        fakeData.add("Today-Sunny-88/16");
        fakeData.add("Tomorrow-Rain-22/32");
        fakeData.add("Wednesday-Cloudy");
        fakeData.add("Thursday-Showers ");
        fakeData.add("Friday-Showers");
        fakeData.add("Saturday-Windy");

        forcastAdapter = new ArrayAdapter(
                getActivity(),
                R.layout.list_item_forcast,
                R.id.list_item_forcast_textview,
                fakeData);

        listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(forcastAdapter);

        return rootView;
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
            FetchWeatherTask task = new FetchWeatherTask();
            String[] weatherInfo = new String[7];
            task.execute("Toronto,ca");
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

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

            JsonWeatherExtractor jsonWeatherExtractor = new JsonWeatherExtractor();
            String[] extractedWeatherInfo = new String[7];

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

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            forcastAdapter.clear();
            forcastAdapter.addAll(strings);
        }
    }
}
