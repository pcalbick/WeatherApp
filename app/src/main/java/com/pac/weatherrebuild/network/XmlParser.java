package com.pac.weatherrebuild.network;

import android.util.Log;
import android.util.Xml;

import com.pac.weatherrebuild.model.TimeLayout;
import com.pac.weatherrebuild.model.Values;
import com.pac.weatherrebuild.model.Weather;
import com.pac.weatherrebuild.model.WeatherStatus;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class XmlParser {

    private static final String TAG = "XmlParser";

    private static final String ns = null;

    public Weather parse(InputStream in) throws XmlPullParserException, IOException {
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private Weather readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Weather weather = new Weather();

        parser.require(XmlPullParser.START_TAG, ns, "dwml");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("data")){
                weather = readData(parser, weather);
            } else {
                skip(parser);
            }
        }
        Log.d(TAG, "readFeed: XML Data Parsed");
        return weather;
    }

    private Weather readData(XmlPullParser parser, Weather weather) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "data");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("time-layout")){
                weather.addTime(readTime(parser));
            }
            else if(name.equals("parameters")){
                readParameters(parser,weather);
            }
            else{
                skip(parser);
            }
        }
        return weather;
    }

    //Create Parameters here
    private Weather readParameters(XmlPullParser parser, Weather weather) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, "parameters");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("temperature")){
                weather.addValues(readValue(parser)); //hourly
            }
            else if(name.equals("probability-of-precipitation")){
                weather.addValues(readValue(parser)); //12 hour
            }
            else if(name.equals("cloud-amount")){
                weather.addValues(readValue(parser)); //total
            }
            else if(name.equals("weather")){
                weather.setWeatherStatus(readWeather(parser));
            }
            else{
                skip(parser);
            }
        }
        return weather;
    }

    //Weather Conditions parsed here : Weather Summary and Type of Precipitation
    private Hashtable<String, List<WeatherStatus>> readWeather(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "weather");
        List<WeatherStatus> statuses = new ArrayList<>();
        String timeLayout = parser.getAttributeValue(null, "time-layout");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("weather-conditions")){
                statuses.add(readConditions(parser));
            }
            else{
                skip(parser);
            }
        }
        Hashtable<String, List<WeatherStatus>> pair = new Hashtable<>();
        pair.put(timeLayout,statuses);
        return pair;
    }

    private WeatherStatus readConditions(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, "weather-conditions");
        WeatherStatus status = new WeatherStatus();
        if(parser.getAttributeValue(null,"weather-summary") != null)
            status.setSummary(parser.getAttributeValue(null, "weather-summary"));
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("value")) {
                //Primary Weather Status Value
                if(parser.getAttributeValue(null,"additive") == null) {
                    parser.require(XmlPullParser.START_TAG, ns, "value");
                    status.setStatus(true);
                    status.setCoverage(parser.getAttributeValue(null, "coverage"));
                    status.setIntensity(parser.getAttributeValue(null, "intensity"));
                    status.setWeatherType(parser.getAttributeValue(null, "weather-type"));
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        if (parser.getName().equals("visibility")) {
                            parser.require(XmlPullParser.START_TAG, ns, "visibility");
                            if(!parser.isEmptyElementTag()) {
                                status.setVisibility(readText(parser));
                                parser.require(XmlPullParser.END_TAG, ns, "visibility");
                            }
                        } else {
                            skip(parser);
                        }
                    }
                }
                //Additive Weather Status Values
                else {
                    WeatherStatus additive = new WeatherStatus();
                    parser.require(XmlPullParser.START_TAG, ns, "value");
                    additive.setStatus(true);
                    additive.setCoverage(parser.getAttributeValue(null, "coverage"));
                    additive.setIntensity(parser.getAttributeValue(null, "intensity"));
                    additive.setWeatherType(parser.getAttributeValue(null, "weather-type"));
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        if (parser.getName().equals("visibility")) {
                            parser.require(XmlPullParser.START_TAG, ns, "visibility");
                            if(!parser.isEmptyElementTag()) {
                                additive.setVisibility(readText(parser));
                                parser.require(XmlPullParser.END_TAG, ns, "visibility");
                            }
                        } else {
                            skip(parser);
                        }
                    }
                    status.setAdditive(additive);
                }
            }
            else{
                skip(parser);
            }
        }
        return status;
    }

    //Time Layouts parsed here
        //period-name attribute to some tags -> today, tomorrow, etc
    private Hashtable<String, TimeLayout> readTime(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "time-layout");
        TimeLayout timeLayout = new TimeLayout();
        String key = "";
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("layout-key")){
                parser.require(XmlPullParser.START_TAG, ns, "layout-key");
                key = readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, "layout-key");
            }
            else if(name.equals("start-valid-time")){
                parser.require(XmlPullParser.START_TAG, ns, "start-valid-time");
                timeLayout.addStart(readText(parser));
                parser.require(XmlPullParser.END_TAG, ns, "start-valid-time");
            }
            else if(name.equals("end-valid-time")){
                parser.require(XmlPullParser.START_TAG, ns, "end-valid-time");
                timeLayout.addEnd(readText(parser));
                parser.require(XmlPullParser.END_TAG, ns, "end-valid-time");
            }
            else{
                skip(parser);
            }
        }
        Hashtable<String,TimeLayout> table = new Hashtable<>();
        table.put(key,timeLayout);
        return table;
    }

    //All integer lists parsed here
    private Values readValue(XmlPullParser parser) throws XmlPullParserException, IOException{
        Values values = new Values();
        values.addTime(parser.getAttributeValue(null,"time-layout"));
        values.addType(parser.getAttributeValue(null,"type"));
        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("name")){
                parser.require(XmlPullParser.START_TAG, ns, "name");
                values.addName(readText(parser));
                parser.require(XmlPullParser.END_TAG, ns, "name");
            }
            else if(name.equals("value")){
                if(parser.getAttributeValue(null,"xsi:nil") != null){
                    values.addValue(values.getValue(values.getValues().size()-1));
                    skip(parser);
                }
                else {
                    parser.require(XmlPullParser.START_TAG, ns, "value");
                    values.addValue(Integer.parseInt(readText(parser)));
                    parser.require(XmlPullParser.END_TAG, ns, "value");
                }
            }
            else{
                skip(parser);
            }
        }
        return values;
    }

    //Grab Text between XML tags
    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException{
        String text = "";
        if(parser.next() == XmlPullParser.TEXT){
            text = parser.getText();
            parser.nextTag();
        }
        return text;
    }

    //Skip unimportant tags
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(parser.getEventType() != XmlPullParser.START_TAG){
            throw new IllegalStateException();
        }
        int depth = 1;
        while(depth != 0){
            switch (parser.next()) {
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
            }
        }
    }
}
