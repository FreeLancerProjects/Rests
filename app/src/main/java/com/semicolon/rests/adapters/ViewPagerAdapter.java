package com.semicolon.rests.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.semicolon.rests.Fragments.Main;

import java.util.ArrayList;

/**
 *
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Main> fragments = new ArrayList<>();
	private Main currentFragment;

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);

		fragments.clear();
		fragments.add(Main.newInstance(0));
		fragments.add(Main.newInstance(1));
		fragments.add(Main.newInstance(2));
		fragments.add(Main.newInstance(3));
		fragments.add(Main.newInstance(4));
	}

	@Override
	public Main getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = ((Main) object);
		}
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public Main getCurrentFragment() {
		return currentFragment;
	}
}