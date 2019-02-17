package com.example.whatsapp.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.whatsapp.fragments.ContatosFragment;
import com.example.whatsapp.fragments.ConversasFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

	String[] titulosAbas = {"CONVERSAS","CONTATOS"};

	public TabAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;

		switch (position){

			case 0:
				fragment = new ConversasFragment();
				break;

			case 1:
				fragment = new ContatosFragment();
				break;

				default:
					fragment = new ContatosFragment();

		}

		return fragment;
	}

	@Override
	public int getCount() {
		return titulosAbas.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titulosAbas[position];
	}
}
