package scolopax.sk.swimza.ui;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import scolopax.sk.swimza.R;
import scolopax.sk.swimza.data.DatabaseContract;
import scolopax.sk.swimza.data.DayObject;
import scolopax.sk.swimza.util.DateUtils;


/**
 * Created by scolopax on 08/08/2017.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {


    private static final int VIEW_TYPE_DAY = 0;
    private static final int VIEW_TYPE_SPACER = 1; // view that is "invisible" and fills space behind toolbar and tabhost

    private Cursor cursor;
    private Context context;
    final private DayAdapterOnClickHandler clickHandler;


    public DayAdapter(DayAdapterOnClickHandler clickHandler, Context context) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    public interface DayAdapterOnClickHandler {
        void onClick(Long id, DayObject dayObject, View sharedTransitionView);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 ? VIEW_TYPE_SPACER : VIEW_TYPE_DAY);
    }

    public DayObject getItem(int position) {
        if (null == cursor) return null;
        cursor.moveToPosition(position);
        return new DayObject(cursor);
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_day, parent, false);
            view.setFocusable(true);
            return new DayViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        cursor.moveToPosition(position);

        holder.container.setVisibility(getItemViewType(position) == VIEW_TYPE_SPACER ? View.VISIBLE : View.GONE);
        DayObject day = getItem(position);

        holder.txtDate.setText(DateUtils.dateFormat.format(day.date));
        holder.txtDayName.setText(DateUtils.getDateName(day.date, context));


        if (day.daytime != null && day.daytime.length() > 0) {
            holder.txtDaySchedule.setText(day.daytime);
            holder.txtDaySchedule.setVisibility(View.VISIBLE);
        } else {
            holder.txtDaySchedule.setVisibility(View.GONE);
        }

        if (day.daySchedule != null && day.daySchedule.length() > 0) {
            holder.txtDayScheduleDetail.setText(day.daySchedule);
            holder.txtDayScheduleDetail.setVisibility(View.VISIBLE);
        } else {
            holder.txtDayScheduleDetail.setVisibility(View.GONE);
        }

        if (day.eveningTime != null && day.eveningTime.length() > 0) {
            holder.txtEveningSchedule.setText(day.eveningTime);
            holder.txtEveningSchedule.setVisibility(View.VISIBLE);
        } else {
            holder.txtEveningSchedule.setVisibility(View.GONE);
        }

        if (day.eveningSchedule != null && day.eveningSchedule.length() > 0) {
            holder.txtEveningScheduleDetail.setText(day.eveningSchedule);
            holder.txtEveningScheduleDetail.setVisibility(View.VISIBLE);
        } else {
            holder.txtEveningScheduleDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (null == cursor) return 0;
        return cursor.getCount();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView txtDate, txtDayName, txtDaySchedule, txtEveningSchedule, txtEveningScheduleDetail;
        public final View container;
        public final TextSwitcher txtDayScheduleDetail;
        public boolean isExtended = false;

        public DayViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.container);
            txtDate = view.findViewById(R.id.lv_day_date);
            txtDayName = view.findViewById(R.id.lv_day_dateText);
            txtDaySchedule = view.findViewById(R.id.lv_day_daySchedule);
            txtDayScheduleDetail = view.findViewById(R.id.lv_view_day_daySchedule_details);
            txtEveningSchedule = view.findViewById(R.id.lv_day_eveningSchedule);
            txtEveningScheduleDetail = view.findViewById(R.id.lv_day_eveningSchedule_details);

            txtDayScheduleDetail.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    return new TextView(context);
                }
            });

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            int idColumn = cursor.getColumnIndex(DatabaseContract.TableDay.COL_DAY_DATE);

            DayObject a = getItem(adapterPosition);

            isExtended = !isExtended;
            if (isExtended){
                txtDayScheduleDetail.setText(spliToLines(a.daySchedule));

            } else {
                txtDayScheduleDetail.setText(a.daySchedule);
                notifyItemChanged(adapterPosition);
            }

            clickHandler.onClick(cursor.getLong(idColumn), getItem(adapterPosition),container);
        }
    }

    public void refreshCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
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
