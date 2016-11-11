package com.example.android.sunshine.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Andy DESK on 11/2/2016.
 */

public class JsonWeatherExtractor {


    public String getReadableDateString(long time){
        String dateFormat = "dd MM yyyy";
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    public String formatHighLows(double high, double low) {
        String result = "High: " + (int) high + "          " + "Low: " + (int)low;
        return result;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        String dayList = "list";
        String temperature = "temp";
        String maxTemp = "max";
        String minTemp = "min";
        String description = "description";
        String weatherAtt = "weather";
        String[] resultStr = new String[7];

        JSONObject baseJsonString = new JSONObject(forecastJsonStr);
        JSONArray listOfDays = baseJsonString.getJSONArray(dayList);

        Date date = new Date();
        Long currentTime = date.getTime();


        for(int i = 0; i < listOfDays.length(); i ++) {
            JSONObject day = (JSONObject) listOfDays.get(i);
            JSONObject temp = day.getJSONObject(temperature);
            Double max = temp.getDouble(maxTemp);
            Double min = temp.getDouble(minTemp);
            JSONArray weatherArray = day.getJSONArray(weatherAtt);
            JSONObject weather  = (JSONObject) weatherArray.get(0);
            String weatherDescription = weather.getString(description);

            String dayofTheWeek = getDayOfTheWeek(i);
            String result = dayofTheWeek + "                 " + weatherDescription + "                 " + formatHighLows(max, min);

            resultStr[i]= result;

        }
        return resultStr;
    }

    public String getDayOfTheWeek(int day) {
        String result;
        GregorianCalendar calendar = new GregorianCalendar();
        int dayOfMonth;
        String dayOfWeek;
        String monthOfYear;
        calendar.add(Calendar.DAY_OF_MONTH, day);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.CANADA);
        monthOfYear = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.CANADA);
        result = dayOfWeek + ", " + monthOfYear + " "  + dayOfMonth;
        return result;
    }
}
