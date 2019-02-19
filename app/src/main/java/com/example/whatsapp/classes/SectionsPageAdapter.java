package com.example.whatsapp.classes;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {

	private final List<Fragment> mFragmentList = new ArrayList<>();
	private final List<String> mFragmenTitleList = new ArrayList<>();

	public void addFragment(Fragment fragment, String title){
		mFragmentList.add(fragment);
		mFragmenTitleList.add(title);
	}

	public SectionsPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return mFragmenTitleList.get(position);
	}

	@Override
	public Fragment getItem(int i) {
		return mFragmentList.get(i);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}
}
