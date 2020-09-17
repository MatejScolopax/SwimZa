package scolopax.sk.swimza.data;

import android.database.Cursor;

import java.util.Date;


/**
 * This class represents schedule for 1 day
 */
public class DayObject {

    public Date date;
    public String daytime;
    public String eveningTime;
    public String daySchedule;
    public String eveningSchedule;

    public DayObject(Date date, String daytime, String eveningTime, String daySchedule, String eveningSchedule) {
        super();
        this.date = date;
        this.daytime = daytime;
        this.eveningTime = eveningTime;
        this.daySchedule = daySchedule;
        this.eveningSchedule = eveningSchedule;
    }

    public DayObject(Cursor c) {
        this(new Date(c.getLong(DatabaseContract.TableDay.COL_IDX_DATE)),
                c.getString(DatabaseContract.TableDay.COL_IDX_DAYTIME),
                c.getString(DatabaseContract.TableDay.COL_IDX_EVENINGTIME),
                c.getString(DatabaseContract.TableDay.COL_IDX_DAYSCHEDULE),
                c.getString(DatabaseContract.TableDay.COL_IDX_EVENINGSCHEDULE));
    }
}
