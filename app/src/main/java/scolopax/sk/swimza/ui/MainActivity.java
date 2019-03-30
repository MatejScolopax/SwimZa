package scolopax.sk.swimza.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Date;

import butterknife.BindView;
import scolopax.sk.swimza.R;
import scolopax.sk.swimza.ui.base.BaseActivity;
import scolopax.sk.swimza.util.DateUtils;

/**
 * Created by scolopax on 08/08/2017.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar_layout)
    View toolbarLayout;

    private HomePagerAdapter pagerAdapter;
    private int toolbarHeight;
    private int pageListViewVerticalScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.toolbarHeight = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.day_today) + DateUtils.dateFormat.format(new Date()));
        setUpPages();
    }

    private void setUpPages() {
        this.pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), this, toolbarLayout, viewPager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.removeAllTabs();
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pagerAdapter.getPageTitle(i)));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("tab", tab.getPosition() + "");
                if (tab.getPosition() == HomePagerAdapter.PAGE_POOL) {
                    getSupportActionBar().setTitle(getString(R.string.day_today) + DateUtils.dateFormat.format(new Date()));
                } else {
                    getSupportActionBar().setTitle(getString(R.string.day_today) + DateUtils.getDateName(new Date(), MainActivity.this));
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            new SettingsDialog(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
