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

        Element table = doc.select("body").first();

        Elements div = table.select("div.milenia-grid-item");

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        for (int i = 0; i < div.size(); i++) {
            try {

                Elements datePart1 = div.get(i).select("div.milenia-entity-date-date");
                Elements datePart2 = div.get(i).select("div.milenia-entity-date-month-year");

                String a = datePart1.get(0).text();
                String b = datePart2.get(0).text();
                String dateString = a.replaceAll("\\u00A0", "") + b.replaceAll("\\u00A0", "");

                Elements scheduleElement = div.get(i).select("a");
                String daySchedule = scheduleElement.get(0).text();
                String eveningScheudle= "";
                if (scheduleElement.size() == 2){
                    eveningScheudle = scheduleElement.get(1).text();
                }

                Elements timeElement = div.get(i).select("h2");
                String dayTime = timeElement.get(0).text();
                String eveningTime = "";
                if (timeElement.size()==2){
                    eveningTime = timeElement.get(1).text();
                }

                dayList.add(new DayObject(df.parse(dateString), dayTime, eveningTime , daySchedule, eveningScheudle));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return dayList;
    }
}
