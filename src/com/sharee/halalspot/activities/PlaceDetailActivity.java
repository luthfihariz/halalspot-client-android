package com.sharee.halalspot.activities;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharee.halalspot.R;
import com.sharee.halalspot.adapters.PhotoPagerAdapter;
import com.sharee.halalspot.beans.Halal;
import com.sharee.halalspot.beans.HalalBodies;
import com.sharee.halalspot.beans.Photo;
import com.sharee.halalspot.beans.Place;
import com.sharee.halalspot.sync.GetPlaceDetailTask;
import com.sharee.utilities.Api;
import com.sharee.utilities.Helper;
import com.sharee.utilities.OnAsyncTaskCompleted;

public class PlaceDetailActivity extends SherlockFragmentActivity {

	private Intent intent;
	private Button getDirectionBtn;
	private ImageView halalLogo;
	private ImageView halalBodiesArrow;
	private TextView placeName;
	private TextView placeCat;
	private TextView placeAddr;
	private TextView placeWeb;
	private TextView placePhone;
	private TextView placeCity;
	private TextView placeHalal;
	private ViewPager photosPager;
	private ScrollView scrollView;
	private RelativeLayout webContainer;
	private RelativeLayout webBorder;
	private RelativeLayout callContainer;
	private RelativeLayout callBorder;
	private RelativeLayout bodiesContainer;
	private GoogleMap placeMap;

	private double latitude;
	private double longitude;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();		
		initView();
		initBackgroundTask();
		initMap();
		initAction();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_place_detail);
		setSupportProgressBarIndeterminateVisibility(true);
		
		scrollView = (ScrollView) findViewById(R.id.scroll_container);
		getDirectionBtn = (Button) findViewById(R.id.btn_get_direction);
		halalLogo = (ImageView) findViewById(R.id.img_halal_logo);
		placeName = (TextView) findViewById(R.id.txt_name);
		placeAddr = (TextView) findViewById(R.id.txt_address);
		placeCat = (TextView) findViewById(R.id.txt_category);
		placePhone = (TextView) findViewById(R.id.txt_call);
		placeWeb = (TextView) findViewById(R.id.txt_website);
		placeCity = (TextView) findViewById(R.id.txt_city);
		placeHalal = (TextView) findViewById(R.id.txt_halal_value);
		halalBodiesArrow = (ImageView) findViewById(R.id.bodies_arrow);
		photosPager = (ViewPager) findViewById(R.id.pager_place_photos);		
		bodiesContainer = (RelativeLayout) findViewById(R.id.rl_halal_bodies);
		webContainer = (RelativeLayout) findViewById(R.id.rl_website_container);
		webBorder = (RelativeLayout) findViewById(R.id.rl_website_border);
		callContainer = (RelativeLayout) findViewById(R.id.rl_call_container);
		callBorder = (RelativeLayout) findViewById(R.id.rl_call_border);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);		
		actionBar.setTitle(intent.getStringExtra(Helper.KEY_PLACE_NAME));
		actionBar.setSubtitle(intent.getStringExtra(Helper.KEY_PLACE_CATNAME));
		
		String halalLogoFilename = intent.getStringExtra(HalalBodies.KEY_BODY_LOGOURL);
		ImageLoader.getInstance().displayImage(Api.getHalalLogoUrl(halalLogoFilename), halalLogo);
		
		placeName.setText(intent.getStringExtra(Helper.KEY_PLACE_NAME));
		placeAddr.setText(intent.getStringExtra(Helper.KEY_PLACE_ADDR));
		placeCat.setText(intent.getStringExtra(Helper.KEY_PLACE_CATNAME)
				.toUpperCase(Locale.getDefault()));
		placeCity.setText(intent.getStringExtra(Helper.KEY_PLACE_CITY) + ", "
				+ intent.getStringExtra(Helper.KEY_PLACE_COUNTRY));
		placeHalal.setText(intent.getStringExtra(HalalBodies.KEY_BODY_NAME));
		
		
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
		
		placeMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitude+","+longitude));
				startActivity(intent);
			}
		});
	}

	private void initBackgroundTask() {
		String placeId = intent.getStringExtra(Helper.KEY_PLACE_ID);
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
		Helper.log(placeId);
	}

	private void updateViewAfterGetDetail(Place place) {
		setPhotos(place.getPhotos());
		if (!place.getWebsite().equals("")) {
			placeWeb.setText(place.getWebsite());
			webContainer.setVisibility(View.VISIBLE);
			webBorder.setVisibility(View.VISIBLE);
			
			String url = place.getWebsite();
			if (!url.startsWith("http://") && !url.startsWith("https://")){
				url = "http://" + url;
			}
			setWebsiteListener(url);
		}
		if (!place.getPhone().equals("")) {
			placePhone.setText(place.getPhone());
			callContainer.setVisibility(View.VISIBLE);
			callBorder.setVisibility(View.VISIBLE);
			setCallListener(place.getPhone());
		}
						
		if(place.getHalal().getBodies()!=null) setBodiesListener(place.getHalal());
	}

	private void setBodiesListener(final Halal halal) {
		halalBodiesArrow.setVisibility(View.VISIBLE);
		bodiesContainer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Helper.log("addr before intent : "+halal.getBodies().getAddress());
				Intent intent = new  Intent();
				intent.setClass(PlaceDetailActivity.this, HalalBodiesActivity.class);
				intent.putExtra(HalalBodies.KEY_BODY_NAME, halal.getBodies().getName());
				intent.putExtra(HalalBodies.KEY_BODY_SHORTNAME, halal.getBodies().getShortName());
				intent.putExtra(HalalBodies.KEY_BODY_OVERVIEW, halal.getBodies().getOverview());
				intent.putExtra(HalalBodies.KEY_BODY_ADDRESS, halal.getBodies().getAddress());
				intent.putExtra(HalalBodies.KEY_BODY_COUNTRY, halal.getBodies().getCountry());
				intent.putExtra(HalalBodies.KEY_BODY_PHONE, halal.getBodies().getPhone());
				intent.putExtra(HalalBodies.KEY_BODY_WEBSITE, halal.getBodies().getWebsite());
				intent.putExtra(HalalBodies.KEY_BODY_EMAIL, halal.getBodies().getEmail());
				intent.putExtra(HalalBodies.KEY_BODY_LOGOURL, halal.getBodies().getLogoUrl());
				startActivity(intent);
			}
		});
	}

	private void setCallListener(final String phoneNumber) {
		callContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent callIntent = new Intent(Intent.ACTION_DIAL);
					callIntent.setData(Uri.parse("tel:"+phoneNumber));
					startActivity(callIntent);
				} catch (ActivityNotFoundException ex) {
					Helper.log("err : "+ex.getMessage());
					Helper.toastShort(PlaceDetailActivity.this, getString(R.string.action_fail));
				}
			}
		});
	}

	private void setWebsiteListener(final String url) {
		webContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Helper.log("url : "+url);
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);						
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
