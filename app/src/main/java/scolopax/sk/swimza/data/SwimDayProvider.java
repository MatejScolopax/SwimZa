package scolopax.sk.swimza.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by scolopax on 08/08/2017.
 */

public class SwimDayProvider extends ContentProvider {

    private static final int SWIMDAY = 200;
    private static final int SWIMDAY_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY_DAY, DatabaseContract.TABLE_DAY, SWIMDAY);

        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY_DAY, DatabaseContract.TABLE_DAY + "/#", SWIMDAY_WITH_ID);
    }

    private SwimDBHelper swimDBHelper;

    @Override
    public boolean onCreate() {
        swimDBHelper = new SwimDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseContract.TABLE_DAY);

        switch (sUriMatcher.match(uri)) {
            case SWIMDAY: {
                break;
            }
            case SWIMDAY_WITH_ID: {
                queryBuilder.appendWhere(DatabaseContract.TableDay.COL_DAY_DATE + "=" + uri.getLastPathSegment());
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        SQLiteDatabase db = swimDBHelper.getWritableDatabase();
        cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = swimDBHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case SWIMDAY: {
                long id = db.insert(DatabaseContract.TABLE_DAY, null, contentValues);
                returnUri = DatabaseContract.TableDay.buildDayUri(id);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = swimDBHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case SWIMDAY:
                rowsDeleted = db.delete(DatabaseContract.TABLE_DAY, selection, selectionArgs);
                break;
            case SWIMDAY_WITH_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(DatabaseContract.TABLE_DAY, DatabaseContract.TableDay.COL_DAY_DATE + "=" + id, null);
                } else {
                    rowsDeleted = db.delete(DatabaseContract.TABLE_DAY, DatabaseContract.TableDay.COL_DAY_DATE + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("This provider does not support updates");
    }
}
