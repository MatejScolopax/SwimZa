package scolopax.sk.swimza.data;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by scolopax on 08/08/2017.
 */

public class SwimDBHelper extends SQLiteOpenHelper {

    private static final String TAG = SwimDBHelper.class.getSimpleName();

    public static final String DB_NAME = "customers.db";
    public static final int DB_VERSION = 2;
    private Resources resources;

    private static final String SQL_CREATE_TABLE_DAY=
            "CREATE TABLE " + DatabaseContract.TABLE_DAY + " ("
                    + DatabaseContract.TableDay.COL_DAY_DATE + " TIMESTAMP PRIMARY KEY, "
                    + DatabaseContract.TableDay.COL_DAY_DAYTIME 	+ " VARCHAR NULL, "
                    + DatabaseContract.TableDay.COL_DAY_EVENINGTIME + " VARCHAR NULL, "
                    + DatabaseContract.TableDay.COL_DAY_DAYSCHEDULE + " VARCHAR NULL, "
                    + DatabaseContract.TableDay.COL_DAY_EVENINGSCHEDULE + " VARCHAR NULL "
                    + ")";

    public SwimDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        resources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_DAY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_DAY );
        onCreate(db);
    }

}
