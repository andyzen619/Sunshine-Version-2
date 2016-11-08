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

    //TODO:2
    /**
     * Prepare the weather high/lows for presentation.
     */
    public String formatHighLows(double high, double low) {
        String result = "High: " + high + " Low: " + low;
        return result;
    }

    //TODO:3
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
            String result = dayofTheWeek + "--" + weatherDescription + "-- " + max + "/" + min;

            resultStr[i]= result;

        }
        return resultStr;
    }

    public String getDayOfTheWeek(int day) {
        String result;
        GregorianCalendar calendar = new GregorianCalendar();
        int dayOfMonth;
        String dayOfWeek;
        calendar.add(Calendar.DAY_OF_MONTH, day);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CANADA);

        result = dayOfWeek + " " + dayOfMonth;
        return result;
    }

//    public static void main(String [] args) throws JSONException {
//        String WEATHER_DATA_MTV_JUN_4 = "{\"cod\":\"200\",\"message\":4.2116,\"city\":{\"id\":\"5375480\",\"name\":\"Mountain View\",\"coord\":{\"lon\":-122.075,\"lat\":37.4103},\"country\":\"United States of America\",\"population\":0},\"cnt\":7,\"list\":[{\"dt\":1401912000,\"temp\":{\"day\":20.17,\"min\":12.3,\"max\":20.17,\"night\":12.3,\"eve\":17.74,\"morn\":14.05},\"pressure\":1012.43,\"humidity\":77,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":1.67,\"deg\":253,\"clouds\":0},{\"dt\":1401998400,\"temp\":{\"day\":18.9,\"min\":10.74,\"max\":18.9,\"night\":10.74,\"eve\":15.54,\"morn\":14.02},\"pressure\":1009.89,\"humidity\":76,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":1.51,\"deg\":225,\"clouds\":0},{\"dt\":1402084800,\"temp\":{\"day\":13.59,\"min\":13.57,\"max\":14.1,\"night\":14.1,\"eve\":14.04,\"morn\":13.57},\"pressure\":1022.58,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":8.92,\"deg\":325,\"clouds\":0},{\"dt\":1402171200,\"temp\":{\"day\":13.71,\"min\":13.71,\"max\":13.93,\"night\":13.93,\"eve\":13.73,\"morn\":13.93},\"pressure\":1021.29,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":6.41,\"deg\":326,\"clouds\":0},{\"dt\":1402257600,\"temp\":{\"day\":13.55,\"min\":13.52,\"max\":13.72,\"night\":13.52,\"eve\":13.72,\"morn\":13.62},\"pressure\":1022.14,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":9.72,\"deg\":320,\"clouds\":0},{\"dt\":1402344000,\"temp\":{\"day\":12.72,\"min\":12.72,\"max\":13.22,\"night\":13.22,\"eve\":13.19,\"morn\":13.06},\"pressure\":1027.87,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":7.85,\"deg\":322,\"clouds\":7},{\"dt\":1402430400,\"temp\":{\"day\":13.11,\"min\":12.89,\"max\":13.35,\"night\":13.26,\"eve\":13.35,\"morn\":12.89},\"pressure\":1029.35,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":11.01,\"deg\":330,\"clouds\":0}]}";
//
//        JsonWeatherExtractor jsonWeatherExtractor = new JsonWeatherExtractor();
//        jsonWeatherExtractor.getDayOfTheWeek(1);
//        jsonWeatherExtractor.getWeatherDataFromJson(WEATHER_DATA_MTV_JUN_4, 7);
//    }
}
