package com.sharee.halalspot.activities;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sharee.halalspot.R;
import com.sharee.halalspot.adapters.PhotoPagerAdapter;
import com.sharee.halalspot.beans.Photo;
import com.sharee.halalspot.beans.Place;
import com.sharee.halalspot.sync.GetPlaceDetailTask;
import com.sharee.utilities.Helper;
import com.sharee.utilities.OnAsyncTaskCompleted;

public class PlaceDetailActivity extends SherlockFragmentActivity {

	private Intent intent;
	private Button getDirectionBtn;
	private Button saveBtn;
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
	private GoogleMap placeMap;

	private double latitude;
	private double longitude;
	private double userLatitude;
	private double userLongitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		initView();
		initBackgroundTask();
		initMap();
		initAction();
	}

	private void initView() {
		setContentView(R.layout.activity_place_detail);
		setSupportProgressBarIndeterminateVisibility(true);

		getDirectionBtn = (Button) findViewById(R.id.btn_get_direction);
		saveBtn = (Button) findViewById(R.id.btn_save);
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

	private void initAction() {
		getDirectionBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://maps.google.com/maps?f=d&daddr="
								+ latitude + "," + longitude));
				intent.setComponent(new ComponentName(
						"com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity"));
				startActivity(intent);
			}
		});

		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void initMap() {
		placeMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_place)).getMap();
		placeMap.getUiSettings().setZoomControlsEnabled(false);
		placeMap.getUiSettings().setAllGesturesEnabled(false);
		placeMap.getUiSettings().setCompassEnabled(false);

		latitude = intent.getDoubleExtra(Helper.KEY_PLACE_LAT, 0.0);
		longitude = intent.getDoubleExtra(Helper.KEY_PLACE_LNG, 0.0);

		MarkerOptions options = new MarkerOptions();
		LatLng latLng = new LatLng(latitude, longitude);
		options.position(latLng);
		placeMap.addMarker(options);
		placeMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
	}

	private void initBackgroundTask() {
		String placeId = intent.getStringExtra(Helper.KEY_PLACE_ID);
		Helper.log(placeId);
		GetPlaceDetailTask getPlaceDetail = new GetPlaceDetailTask(this,
				new OnAsyncTaskCompleted() {

					@Override
					public void onCompleted(boolean status, Object... objects) {
						setSupportProgressBarIndeterminateVisibility(false);
						if (status) {
							Place place = (Place) objects[0];
							updateViewAfterGetDetail(place);
						} else {
							String msg = (String) objects[0];
							Helper.toastShort(PlaceDetailActivity.this, msg);
						}
					}
				});
		getPlaceDetail.execute(placeId);
	}

	private void updateViewAfterGetDetail(Place place) {
		setPhotos(place.getPhotos());
		if (!place.getWebsite().equals("")) {
			placeWeb.setText(place.getWebsite());
			webContainer.setVisibility(View.VISIBLE);
			webBorder.setVisibility(View.VISIBLE);
			setWebsiteListener();
		}
		if (!place.getPhone().equals("")) {
			placePhone.setText(place.getPhone());
			callContainer.setVisibility(View.VISIBLE);
			callBorder.setVisibility(View.VISIBLE);
			setCallListener();
		}
	}

	private void setCallListener() {
		callContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setWebsiteListener() {
		webContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
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
