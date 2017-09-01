package scolopax.sk.swimza.data;


import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import scolopax.sk.swimza.R;

/**
 * Created by scolopax on 08/08/2017.
 */

public class DataShare {
    public static final String URL = "http://www.plavarenzilina.sk/program/";
    public static final String URL_SAUNA = "http://www.plavarenzilina.sk/sauna/";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);


    public static String getDateName(Date day, Context context) {
        String ret = "";


        Calendar cal = Calendar.getInstance();
        cal.setTime(day);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);


        switch (dayOfWeek) {
            case Calendar.MONDAY:
                ret = context.getResources().getString(R.string.day_moday);
                break;
            case Calendar.TUESDAY:
                ret = context.getResources().getString(R.string.day_tuesday);
                break;
            case Calendar.WEDNESDAY:
                ret = context.getResources().getString(R.string.day_wednesday);
                break;
            case Calendar.THURSDAY:
                ret = context.getResources().getString(R.string.day_thursday);
                break;
            case Calendar.FRIDAY:
                ret = context.getResources().getString(R.string.day_friday);
                break;
            case Calendar.SATURDAY:
                ret = context.getResources().getString(R.string.day_saturday);
                break;
            case Calendar.SUNDAY:
                ret = context.getResources().getString(R.string.day_sunday);
                break;

        }
        return ret;
    }

}
