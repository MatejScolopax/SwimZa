package scolopax.sk.swimza.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import scolopax.sk.swimza.R;
import scolopax.sk.swimza.data.DayObject;
import scolopax.sk.swimza.ui.base.BaseDialog;
import scolopax.sk.swimza.util.DateUtils;


/**
 * Created by scolopax on 29/08/2017.
 */

public class DayDetailDialog extends BaseDialog {


    @BindView(R.id.lv_day_daySchedule)
    TextView txtDaySchedule;

    @BindView(R.id.lv_view_day_daySchedule_details)
    TextView txtDayScheduleDetail;

    @BindView(R.id.lv_day_eveningSchedule)
    TextView txtEveningSchedule;

    @BindView(R.id.detail_dialog_caption)
    TextView txtCaption;

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

        txtCaption.setText(DateUtils.getDateName(dayObject.date, context) + " - " + DateUtils.dateFormat.format(dayObject.date));
        txtDaySchedule.setText(dayObject.daytime);
        txtEveningSchedule.setText(dayObject.eveningTime);
        txtDayScheduleDetail.setText(spliToLines(dayObject.daySchedule));

        if (dayObject.eveningTime!=null && dayObject.eveningTime.length()>0){
            txtEveningSchedule.setVisibility(View.VISIBLE);
        } else {
            txtEveningSchedule.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_ok)
    void close() {
        DayDetailDialog.this.dismiss();
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
