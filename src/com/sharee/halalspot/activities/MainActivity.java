package com.sharee.halalspot.activities;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.sharee.halalspot.R;
import com.sharee.halalspot.adapters.NearbyListAdapter;
import com.sharee.halalspot.beans.Place;
import com.sharee.halalspot.sync.GetNearbyTask;
import com.sharee.utilities.Helper;
import com.sharee.utilities.OnAsyncTaskCompleted;

public class MainActivity extends SherlockActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private ListView nearbyList;
	private ProgressBar nearbyProgress;
	private ArrayList<Place> nearbyPlaces;
	private LocationClient locClient;
	private Location currLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		locClient = new LocationClient(this, this, this);
		initViews();

	}

	private void initViews() {
		setContentView(R.layout.activity_main);
		nearbyList = (ListView) findViewById(R.id.list_nearby);
		nearbyProgress = (ProgressBar) findViewById(R.id.progress_nearby);
	}

	private void loadNearby(Location loc, final Context context) {
		nearbyProgress.setVisibility(View.VISIBLE);
		GetNearbyTask getNearby = new GetNearbyTask(this,
				new OnAsyncTaskCompleted() {

					@Override
					@SuppressWarnings("unchecked")
					public void onCompleted(boolean status,
							Object... objects) {
						nearbyProgress.setVisibility(View.GONE);
						if (status) {
							nearbyPlaces = (ArrayList<Place>) objects[0];
							setUpListView(context, nearbyPlaces);						
						}
					}
				});
		getNearby.execute(loc.getLongitude(), loc.getLatitude());
	}
	
	private void setUpListView(Context context, ArrayList<Place> places){
		NearbyListAdapter adapter = new NearbyListAdapter(
				context, nearbyPlaces);
		nearbyList.setAdapter(adapter);
		nearbyList.setOnItemClickListener(adapter);
		
		PauseOnScrollListener pauseListener = new PauseOnScrollListener(
				ImageLoader.getInstance(), false, true);
		nearbyList.setOnScrollListener(pauseListener);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Helper.log("Location Client : Connection Failed.");
	}

	@Override
	public void onConnected(Bundle bundle) {
		currLocation = locClient.getLastLocation();
		if (nearbyList.getAdapter() == null || nearbyList.getAdapter().isEmpty()) {
			loadNearby(currLocation, this);
		}
		Helper.log("curr location : " + currLocation.getLongitude() + ", "
				+ currLocation.getLatitude());
	}

	@Override
	public void onDisconnected() {
		Helper.log("Location Client : Disconnected.");
	}

	@Override
	protected void onStart() {
		super.onStart();
		locClient.connect();
	}

	@Override
	protected void onStop() {
		locClient.disconnect();
		super.onStop();
	}

}
