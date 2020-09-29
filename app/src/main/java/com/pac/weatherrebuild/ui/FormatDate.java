package com.pac.weatherrebuild.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatDate {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.US);

    //24 Hour
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
    //12 Hour
    public static final SimpleDateFormat time12Format = new SimpleDateFormat("h a", Locale.US);
    //Days of Week
    public static final SimpleDateFormat dayFormat = new SimpleDateFormat("d-EEE", Locale.US);
    public static final SimpleDateFormat dayOnlyFormat = new SimpleDateFormat("E", Locale.US);
    //Full Date
    public static final SimpleDateFormat fullDayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static final SimpleDateFormat fullDayFormatEU = new SimpleDateFormat("d / MM", Locale.US);
    public static final SimpleDateFormat fullDayFormatUS = new SimpleDateFormat("MM / d", Locale.US);

    private String formatTime(String time, SimpleDateFormat format){
        Date date = new Date();
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e){
            e.printStackTrace();
        }
        assert date != null;
        return format.format(date);
    }

    public String format(String time, SimpleDateFormat format){
        return formatTime(time,format);
    }

    public String formatCurrent(Date date){
        return dateFormat.format(date);
    }
}
