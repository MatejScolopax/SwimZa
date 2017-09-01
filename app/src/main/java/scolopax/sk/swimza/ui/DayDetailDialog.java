package scolopax.sk.swimza.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import scolopax.sk.swimza.R;
import scolopax.sk.swimza.data.DataShare;
import scolopax.sk.swimza.data.DayObject;


/**
 * Created by scolopax on 29/08/2017.
 */

public class DayDetailDialog extends Dialog {

    @Bind(R.id.lv_day_daySchedule)
    TextView txtDaySchedule;
    @Bind(R.id.lv_view_day_daySchedule_details)
    TextView txtDayScheduleDetail;
    @Bind(R.id.lv_day_eveningSchedule)
    TextView txtEveningSchedule;

    private Context context;
    private DayObject dayObject;

    public DayDetailDialog(@NonNull Context context, DayObject dayObject) {
        super(context);
        this.context = context;
        this.dayObject = dayObject;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_daydetail);
        ButterKnife.bind(this);

        this.setTitle(DataShare.getDateName(dayObject.date, context) + " - " + DataShare.dateFormat.format(dayObject.date));

        txtDaySchedule.setText(dayObject.daytime);
        txtEveningSchedule.setText(dayObject.eveningTime);
        txtDayScheduleDetail.setText(spliToLines(dayObject.daySchedule));
    }

    private String spliToLines(String joined) {
        try {
            String delimeter = "\\d{2}.\\d{2}-"; // "08.00-"

            String[] parts = joined.split("(?=" + delimeter + ")");   //This uses look-arounds to split on empty string just before the delimiter.

            StringBuilder sb = new StringBuilder();

            for (String part : parts) {
                if (part.length() > 0 && !part.equals("\n")) // do not insert "empty" row
                    sb.append(part).append("\n");
            }

            if (sb.length() > 1) // last line separatr remove
                sb.setLength(sb.length() - 1);

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return joined;
        }
    }
}
