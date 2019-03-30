package scolopax.sk.swimza.ui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import scolopax.sk.swimza.R;

/**
 * Created by scolopax on 08/08/2017.
 */

final class HomePagerAdapter extends FragmentPagerAdapter implements ScrollingFragment.VerticalScrolling, ViewPager.OnPageChangeListener {

	@SuppressWarnings("unused")
	private static final String TAG = "HomePagerAdapter";
	private View toolBarLayout;
	private int toolbarPosition;
	private int toolbarHeight;
	private ScrollingFragment[] fragmentsArr;

	private static final int PAGE_POOL = 0;
	private static final int PAGE_SAUNA = 1;

	private Context context;

	public HomePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Context context, View toolBarLayout,  ViewPager viewPager) {
		super(fragmentManager);
		this.context = context;
		this.toolBarLayout = toolBarLayout;
		this.toolbarHeight = context.getResources().getDimensionPixelSize(R.dimen.toolbar_height);
		toolbarPosition = 0;
		viewPager.addOnPageChangeListener(this);
		viewPager.setOffscreenPageLimit(2);
		this.fragmentsArr = new ScrollingFragment[] {new PoolFragment().addListener(this), new SaunaFragment().addListener(this)};
	}

	@Override
	public ScrollingFragment getItem(int position) {
		return fragmentsArr[position];
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case PAGE_POOL:
				return context.getResources().getString(R.string.home_pool);
			case PAGE_SAUNA:
				return context.getResources().getString(R.string.home_sauna);
		}
		return "";
	}

	@Override
	public void scrolledBy(int dy) {
		if (toolBarLayout != null) {
			toolbarPosition += dy * (-1);

			if (toolbarPosition > 0)
				toolbarPosition = 0;

			if (toolbarPosition < -1 * toolbarHeight)
				toolbarPosition = -1 * toolbarHeight;

			toolBarLayout.setTranslationY(toolbarPosition);
		}
	}

	public void scrollDownToolBar() {
		toolBarLayout.animate()
				.translationY(0)
				.setDuration(300)
				.start();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		scrollDownToolBar();
		getItem(position).scrollToTop();
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
}
