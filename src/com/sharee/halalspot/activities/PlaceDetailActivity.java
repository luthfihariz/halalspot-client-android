package com.sharee.halalspot.activities;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.sharee.halalspot.R;
import com.sharee.halalspot.adapters.PhotoPagerAdapter;
import com.sharee.halalspot.beans.Photo;
import com.sharee.halalspot.beans.Place;
import com.sharee.halalspot.sync.GetPlaceDetailTask;
import com.sharee.utilities.Helper;
import com.sharee.utilities.OnAsyncTaskCompleted;

public class PlaceDetailActivity extends SherlockFragmentActivity {

	private Intent intent;
	private TextView placeName;
	private TextView placeCat;
	private TextView placeAddr;
	private TextView placeWeb;
	private TextView placePhone;
	private TextView placeCity;
	private ViewPager photosPager;
	private ScrollView scrollView;
	private RelativeLayout webContainer;
	private RelativeLayout webBorder;
	private RelativeLayout callContainer;
	private RelativeLayout callBorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		intent = getIntent();
		initView();
		initBackgroundTask();
	}

	private void initView() {
		setContentView(R.layout.activity_place_detail);
		setSupportProgressBarIndeterminateVisibility(true);

		placeName = (TextView) findViewById(R.id.txt_name);
		placeAddr = (TextView) findViewById(R.id.txt_address);
		placeCat = (TextView) findViewById(R.id.txt_category);
		placePhone = (TextView) findViewById(R.id.txt_call);
		placeWeb = (TextView) findViewById(R.id.txt_website);
		placeCity = (TextView) findViewById(R.id.txt_city);
		photosPager = (ViewPager) findViewById(R.id.pager_place_photos);
		scrollView = (ScrollView) findViewById(R.id.scroll_container);
		webContainer = (RelativeLayout) findViewById(R.id.rl_website_container);
		webBorder = (RelativeLayout) findViewById(R.id.rl_website_border);
		callContainer = (RelativeLayout) findViewById(R.id.rl_call_container);
		callBorder = (RelativeLayout) findViewById(R.id.rl_call_border);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(intent.getStringExtra(Helper.KEY_PLACE_NAME));
		actionBar.setSubtitle(intent.getStringExtra(Helper.KEY_PLACE_CATNAME));
		placeName.setText(intent.getStringExtra(Helper.KEY_PLACE_NAME));
		placeAddr.setText(intent.getStringExtra(Helper.KEY_PLACE_ADDR));
		placeCat.setText(intent.getStringExtra(Helper.KEY_PLACE_CATNAME)
				.toUpperCase(Locale.getDefault()));
		placeCity.setText(intent.getStringExtra(Helper.KEY_PLACE_CITY) + ", "
				+ intent.getStringExtra(Helper.KEY_PLACE_COUNTRY));
	}

	private void initBackgroundTask() {
		String placeId = intent.getStringExtra(Helper.KEY_PLACE_ID);
		Helper.log(placeId);
		GetPlaceDetailTask getPlaceDetail = new GetPlaceDetailTask(this,
				new OnAsyncTaskCompleted() {

					@Override
					public void onCompleted(boolean status,
							Object... objects) {
						setSupportProgressBarIndeterminateVisibility(false);
						if (status) {
							Place place = (Place) objects[0];
							updateView(place);
						} else {
							String msg = (String) objects[0];
							Helper.toastShort(PlaceDetailActivity.this, msg);
						}
					}
				});
		getPlaceDetail.execute(placeId);
	}

	private void updateView(Place place) {
		setPhotos(place.getPhotos());
		if(!place.getWebsite().equals("")){
			placeWeb.setText(place.getWebsite());
			webContainer.setVisibility(View.VISIBLE);
			webBorder.setVisibility(View.VISIBLE);
		}		
		if(!place.getPhone().equals("")){
			placePhone.setText(place.getPhone());		
			callContainer.setVisibility(View.VISIBLE);
			callBorder.setVisibility(View.VISIBLE);
		}		
	}

	private void setPhotos(ArrayList<Photo> photos) {		
		PhotoPagerAdapter adapter = new PhotoPagerAdapter(
				getSupportFragmentManager(), photos);
		photosPager.setAdapter(adapter);
		photosPager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				scrollView.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}
}
