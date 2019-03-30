package scolopax.sk.swimza.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by scolopax on 08/08/2017.
 */

public class DatabaseContract {

    /* Database schema information */

    public static final String TABLE_DAY = "table_day";
    public static final String CONTENT_AUTHORITY_DAY = "plavaren.sk.plavarenza2";


    /*  Customers  */

    public static final class TableDay implements BaseColumns
    {
        public static final String COL_DAY_DATE = "day_date";
        public static final String COL_DAY_DAYTIME = "day_daytime";
        public static final String COL_DAY_EVENINGTIME = "day_eveningtime";
        public static final String COL_DAY_DAYSCHEDULE = "day_dayschedule";

        public static final int COL_IDX_DATE = 0;
        public static final int COL_IDX_DAYTIME= 1;
        public static final int COL_IDX_EVENINGTIME = 2;
        public static final int COL_IDX_DAYSCHEDULE = 3;

        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY_DAY)
                .appendPath(TABLE_DAY)
                .build();

        public static Uri buildDayUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String[] getProjection()
        {
            return new String[] { TableDay.COL_DAY_DATE, TableDay.COL_DAY_DAYTIME, TableDay.COL_DAY_EVENINGTIME, TableDay.COL_DAY_DAYSCHEDULE };
        }
    }

}
