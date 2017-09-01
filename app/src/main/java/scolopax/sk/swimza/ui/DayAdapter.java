package scolopax.sk.swimza.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import scolopax.sk.swimza.R;
import scolopax.sk.swimza.data.DataShare;
import scolopax.sk.swimza.data.DatabaseContract;
import scolopax.sk.swimza.data.DayObject;


/**
 * Created by scolopax on 08/08/2017.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {


    private static final int VIEW_TYPE_DAY = 0;
    private static final int VIEW_TYPE_SPACER = 1; // view that is "invisible" and fills space behind toobar and tabhost

    private Cursor cursor;
    private Context context;
    final private DayAdapterOnClickHandler clickHandler;


    public DayAdapter(DayAdapterOnClickHandler clickHandler, Context context) {
        this.clickHandler = clickHandler;
        this.context = context;
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

    public interface DayAdapterOnClickHandler {
        void onClick(Long id, DayObject dayObject);
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            int layoutId = -1;
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

        holder.txtDate.setText(DataShare.dateFormat.format(day.date));
        holder.txtDayName.setText(DataShare.getDateName(day.date, context));


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
    }

    @Override
    public int getItemCount() {
        if (null == cursor) return 0;
        return cursor.getCount();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView txtDate, txtDayName, txtDaySchedule, txtDayScheduleDetail, txtEveningSchedule;
        public final View container;

        public DayViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.container);
            txtDate = (TextView) view.findViewById(R.id.lv_day_date);
            txtDayName = (TextView) view.findViewById(R.id.lv_day_dateText);
            txtDaySchedule = (TextView) view.findViewById(R.id.lv_day_daySchedule);
            txtDayScheduleDetail = (TextView) view.findViewById(R.id.lv_view_day_daySchedule_details);
            txtEveningSchedule = (TextView) view.findViewById(R.id.lv_day_eveningSchedule);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            int idColumn = cursor.getColumnIndex(DatabaseContract.TableDay.COL_DAY_DATE);
            clickHandler.onClick(cursor.getLong(idColumn), getItem(adapterPosition));
        }
    }

    public void refreshCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }
}
