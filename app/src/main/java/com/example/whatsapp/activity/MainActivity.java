package com.example.whatsapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.whatsapp.R;
import com.example.whatsapp.adapter.TabAdapter;
import com.example.whatsapp.helper.Logout;
import com.example.whatsapp.helper.SlindingTabLayout;

public class MainActivity extends AppCompatActivity {

	private Toolbar toolbar;

	private SlindingTabLayout slidingTabLayout;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = findViewById(R.id.mainToolbar);

		toolbar.setTitle("WhatsApp");
		setSupportActionBar(toolbar);

		slidingTabLayout = findViewById(R.id.mainTabs);
		viewPager        = findViewById(R.id.mainViewPager);

		//Configurar o SlidingTabs
		slidingTabLayout.setDistributeEvenly(true); //para distribuir as tabs de forma igual
		//slidingTabLayout.setSelectedIndicatorColors(Color.rgb(255,255,255));
		slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

		//Configurar o adapter para as Tabs
		TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabAdapter);
		slidingTabLayout.setViewPager(viewPager);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.action_logout:
				Logout.Logout();
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}
}
