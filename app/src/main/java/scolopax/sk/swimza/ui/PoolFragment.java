package scolopax.sk.swimza.ui;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import scolopax.sk.swimza.R;
import scolopax.sk.swimza.data.DatabaseContract;
import scolopax.sk.swimza.data.DayObject;
import scolopax.sk.swimza.data.ParseHtmlTask;
import scolopax.sk.swimza.util.DateUtils;
import scolopax.sk.swimza.util.ConnectUtils;


/**
 * Created by scolopax on 26/08/2017.
 */

public class PoolFragment extends ScrollingFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int toolbarHeight;
    private DayAdapter dayAdapter;
    private static final int DAY_LOADER_ID = 0;
    private boolean isScrollTrigered = true;

    @BindView(R.id.recycler_view_swim)
    RecyclerView recyclerView;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    @Override
    public void scrollToTop() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pool, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.toolbarHeight = getResources().getDimensionPixelSize(R.dimen.toolbar_height);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager cardLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(cardLayoutManager);

        dayAdapter = new DayAdapter(new DayAdapter.DayAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, DayObject dayObject, View sharedTransitionView) {

              //  new DayDetailDialog(getContext(), dayObject);
            }
        }, getContext());

        recyclerView.setAdapter(dayAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScrollTrigered) {
                    triggerScroll(dy);
                    Log.v("scroll", "pool " + dy);
                }
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        swipeContainer.setProgressViewOffset(true, 0, toolbarHeight * 2 + (toolbarHeight / 10));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (ConnectUtils.isConnected(getActivity())) {
                    new DownloadTask(getActivity()).execute();
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.state_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

        getActivity().getSupportLoaderManager().initLoader(DAY_LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // if there is connection and user preference, download actual schedule
        if (ConnectUtils.isConnected(getActivity()) && SettingsDialog.isDownloadAutomatic(getContext())) {
            new PoolFragment.DownloadTask(getActivity()).execute();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = DatabaseContract.TableDay.getProjection();
        String order = DatabaseContract.TableDay.COL_DAY_DATE + " ASC ";
        String where = DatabaseContract.TableDay.COL_DAY_DATE + "  == " + DateUtils.getYesterdayDate().getTime();   // I need day from today, so for comparison set one day ago (I'm comparing milliseconds)
        return new CursorLoader(getContext(), DatabaseContract.TableDay.CONTENT_URI, projection, null, null, order);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        dayAdapter.refreshCursor(data);
        updateEmptyVisibility();

        //if there is no connection, user might have already downloaded schedule, so scroll for him to actual date
        if (!ConnectUtils.isConnected(getActivity())) {
            int pastDays = getPastDaysCount();
            if (pastDays-1 > 0) {

                isScrollTrigered = false;

                //scroll to actual day
                recyclerView.scrollToPosition(pastDays-1);
                //fix toolbar and tabhost overlap
                recyclerView.scrollBy(0, -2 * getResources().getDimensionPixelSize(R.dimen.toolbar_height));

                isScrollTrigered = true;
            }
        }
    }


    /**
     *
     * @return count of stored past day-schedules
     */
    private int getPastDaysCount() {

        Cursor cursor = getActivity().getContentResolver().query(DatabaseContract.TableDay.CONTENT_URI, DatabaseContract.TableDay.getProjection(),
                DatabaseContract.TableDay.COL_DAY_DATE + " <= " + new Date().getTime(), null, null);

        if (cursor != null) {
            return cursor.getCount();
        }

        return 0;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dayAdapter.refreshCursor(null);
        updateEmptyVisibility();
    }

    private void updateEmptyVisibility() {
        txtEmpty.setVisibility(dayAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private class DownloadTask extends ParseHtmlTask {

        public DownloadTask(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPreExecute();

            if (swipeContainer != null) {
                swipeContainer.setRefreshing(false);
            }
        }
    }
}
