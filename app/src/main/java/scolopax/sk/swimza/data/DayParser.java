package scolopax.sk.swimza.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by scolopax on 08/08/2017.
 */

public class DayParser {

    public static List<DayObject> parsePage(URL url) throws IOException, ParseException {
        List<DayObject> dayList = new ArrayList<>();
        Document doc = Jsoup.connect(url.toString()).get();

        Element table = doc.select("table").first();
        Elements trs = table.select("tr");

        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");

            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            Date result = df.parse(tds.get(0).text());
            dayList.add(parseDay(tds.get(1).text(), result));
        }

        return dayList;
    }

    private static DayObject parseDay(String dayString, Date date) {
        DayObject o_day;

        String part1 = null, part2 = null, part3 = null;

        String[] parts = dayString.split("\\(");
        String[] parts2;
        if (parts.length > 1) {
            part1 = parts[0];
            part2 = parts[1];

            parts2 = part2.split("\\)");
            if (parts2.length > 1) {
                part2 = parts2[0];
                part3 = parts2[1];
                o_day = new DayObject(date, trimFromEmpty(part1), trimFromEmpty(part3), trimFromEmpty(part2));
            } else {
                part3 = part2.substring(0, part2.length() - 1);
                o_day = new DayObject(date, trimFromEmpty(part1), "", trimFromEmpty(part3));
            }

        } else {
            o_day = new DayObject(date, trimFromEmpty(dayString), "", null);
        }
        return o_day;
    }

    public static ArrayList<String> parseSchedule(String part2) {
        if (part2.equals(""))
            return null;
        String[] parts = part2.split("\\,");

        ArrayList<String> arrayList = new ArrayList<>();

        for (String s : parts) {
            arrayList.add(s);
        }
        return arrayList;
    }

    private static String trimFromEmpty(String str) {

        if (str.length() > 0 && str.charAt(0) == ' ') {
            str = str.substring(1, str.length());
        }

        if (str.length() > 0 && str.charAt(str.length() - 1) == ' ') {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

}
