package scolopax.sk.swimza.data;

import android.database.Cursor;

import java.util.Date;


/**
 * This class represents schedule for 1 day
 * example data:
 * <p>
 * 06.12.2013  //date
 * 06.00-15.00 //daytime
 * (06.00-08.00 7 dr., 11.30-13.00 6 dr., 14.00-14.30 7 dr., 14.30-15.00 5 dr.)  //daySchedule
 * 18.00-20.30 vecerne plavanie  //eveningTime
 */
public class DayObject {
    public Date date;
    public String daytime;
    public String eveningTime;
    public String daySchedule;

    public DayObject(Date date, String daytime, String eveningTime, String daySchedule) {
        super();
        this.date = date;
        this.daytime = daytime;
        this.eveningTime = eveningTime;
        this.daySchedule = daySchedule;
    }

    public DayObject(Cursor c) {
        this(new Date(c.getLong(DatabaseContract.TableDay.COL_IDX_DATE)),
                c.getString(DatabaseContract.TableDay.COL_IDX_DAYTIME),
                c.getString(DatabaseContract.TableDay.COL_IDX_EVENINGTIME),
                c.getString(DatabaseContract.TableDay.COL_IDX_DAYSCHEDULE));
    }
}
