package scolopax.sk.swimza.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.List;

/**
 * Created by scolopax on 08/08/2017.
 */

public abstract class ParseHtmlTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = ParseHtmlTask.class.getSimpleName();

    private Context context;


    public ParseHtmlTask(Context context) {
        super();
        this.context = context;
    }

    protected abstract void onPostExecute(Boolean result);

    @Override
    protected Boolean doInBackground(Void... params) {

        List<DayObject> result = null;

        try {
            URL url = new URL(Cons.URL);
            result = DayParser.parsePage(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result != null) {

            ContentValues[] valuesArr = new ContentValues[result.size()];

            int i = 0;
            for (DayObject d : result) {
                final ContentValues values = new ContentValues();
                values.put(DatabaseContract.TableDay.COL_DAY_DATE, d.date.getTime());
                values.put(DatabaseContract.TableDay.COL_DAY_DAYTIME, d.daytime);
                values.put(DatabaseContract.TableDay.COL_DAY_EVENINGTIME, d.eveningTime);
                values.put(DatabaseContract.TableDay.COL_DAY_DAYSCHEDULE, d.daySchedule);
                values.put(DatabaseContract.TableDay.COL_DAY_EVENINGSCHEDULE, d.eveningSchedule);
                valuesArr[i++] = values;
            }

            ContentResolver resolver = context.getContentResolver();

            if (valuesArr.length > 0) {

                int deleted = resolver.delete(DatabaseContract.TableDay.CONTENT_URI, null, null);
                Log.v(TAG, " deleted = " + deleted);

                int inserted = resolver.bulkInsert(DatabaseContract.TableDay.CONTENT_URI, valuesArr);
                Log.v(TAG, " inserted = " + inserted);

            } else {
                Log.v(TAG, "nothing downloaded, nothing to do");
            }
            return true;
        }
        return false;
    }
}
