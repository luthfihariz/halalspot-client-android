package com.sharee.halalspot.activities;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.sharee.halalspot.R;
import com.sharee.halalspot.adapters.NearbyListAdapter;
import com.sharee.halalspot.beans.Place;
import com.sharee.halalspot.sync.GetNearbyTask;
import com.sharee.utilities.Helper;
import com.sharee.utilities.OnAsyncTaskCompleted;

public class MainActivity extends SherlockFragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private ListView nearbyList;
	private ProgressBar nearbyProgress;
	private RelativeLayout locationContainer;
	private ImageView gpsIcon;
	private GoogleMap nearbyMap;
	private Menu optionsMenu;
	private LocationClient locClient;
	private Location currLocation;

	private ArrayList<Place> nearbyPlaces;
	private boolean isLoading;
	private NearbyListAdapter adapter;
	private View footerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpLocationClientIfNeeded();
		initViews();
		initAction(this);
	}

	private void setUpLocationClientIfNeeded() {
		if (locClient == null) {
			locClient = new LocationClient(getApplicationContext(), this, this);
		}
	}

	private void initViews() {
		setContentView(R.layout.activity_main);
		nearbyList = (ListView) findViewById(R.id.list_nearby);
		nearbyProgress = (ProgressBar) findViewById(R.id.progress_nearby);
		locationContainer = (RelativeLayout) findViewById(R.id.rl_location);
		gpsIcon = (ImageView) findViewById(R.id.img_gps);
		nearbyMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_nearby)).getMap();
		nearbyMap.getUiSettings().setZoomControlsEnabled(false);
		nearbyMap.getUiSettings().setCompassEnabled(false);
	}

	private void initAction(final Context ctx) {
		locationContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ctx, SearchByCityActivity.class);
				intent.putExtra(Helper.KEY_PLACE_LAT, currLocation.getLatitude());
				intent.putExtra(Helper.KEY_PLACE_LNG, currLocation.getLongitude());
				startActivity(intent);
			}
		});
	}

	private void initMap(Location loc) {
		nearbyMap.setMyLocationEnabled(true);
		LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
		nearbyMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13),
				2000, null);
	}

	private void loadNearby(Location loc, final Context context,
			final boolean refresh, int skip) {
		isLoading = true;
		gpsIcon.setVisibility(View.INVISIBLE);
		nearbyProgress.setVisibility(View.VISIBLE);
		setRefreshActionButtonState(true);
		GetNearbyTask getNearby = new GetNearbyTask(this,
				new OnAsyncTaskCompleted() {

					@Override
					@SuppressWarnings("unchecked")
					public void onCompleted(boolean status, Object... objects) {
						nearbyProgress.setVisibility(View.GONE);
						gpsIcon.setVisibility(View.VISIBLE);
						setRefreshActionButtonState(false);
						isLoading = false;
						if (status) {
							nearbyPlaces = (ArrayList<Place>) objects[0];
							setUpListView(context, nearbyPlaces, refresh);
							addMarkerToMap(nearbyPlaces);
						}
					}
				}, skip);
		getNearby.execute(loc.getLongitude(), loc.getLatitude());
	}

	protected void addMarkerToMap(ArrayList<Place> places) {
		MarkerOptions options;
		Place place;
		LatLng loc;
		for (int i = 0; i < places.size(); i++) {
			place = places.get(i);
			loc = new LatLng(place.getLatitude(), place.getLongitude());
			options = new MarkerOptions();
			options.position(loc);
			nearbyMap.addMarker(options);
		}

	}

	private void setUpListView(final Context context, ArrayList<Place> places,
			boolean refresh) {
		
		if (adapter == null || refresh) {
			footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.load_more_view, null, false);
			if(!refresh) nearbyList.addFooterView(footerView);

			adapter = new NearbyListAdapter(context, nearbyPlaces);
			nearbyList.setAdapter(adapter);
			nearbyList.setOnItemClickListener(adapter);

			OnScrollListener loadMoreListener = new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView arg0, int arg1) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					Helper.log("total item count : " + totalItemCount);
					int lastInScreen = firstVisibleItem + visibleItemCount;
					if ((totalItemCount == lastInScreen) && !isLoading) {
						loadNearby(currLocation, context, false, totalItemCount-1);
					}
				}
			};

			PauseOnScrollListener pauseListenerWithLoadMore = new PauseOnScrollListener(
					ImageLoader.getInstance(), false, true, loadMoreListener);
			nearbyList.setOnScrollListener(pauseListenerWithLoadMore);
		} else {
			for (int i = 0; i < places.size(); i++) {
				adapter.add(places.get(i));
			}

			// server returning zero, remove footer view & load more listener
			if (places.size() < 10) {
				Helper.log("places size : "+places.size());				
				footerView.setVisibility(View.GONE);				
				PauseOnScrollListener pauseListener = new PauseOnScrollListener(
						ImageLoader.getInstance(), false, true);
				nearbyList.setOnScrollListener(pauseListener);
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Helper.log("Location Client : Connection Failed.");
	}

	@Override
	public void onConnected(Bundle bundle) {
		currLocation = locClient.getLastLocation();
		initMap(currLocation);
		if (nearbyList.getAdapter() == null
				|| nearbyList.getAdapter().isEmpty()) {
			loadNearby(currLocation, this, false, 0);
		}
		Helper.log("curr location : " + currLocation.getLongitude() + ", "
				+ currLocation.getLatitude());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nearby_refresh:
			if (locClient != null) {
				loadNearby(locClient.getLastLocation(), MainActivity.this,
						true, 0);
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.optionsMenu = menu;
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.nearby_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void setRefreshActionButtonState(final boolean refreshing) {
		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu
					.findItem(R.id.nearby_refresh);
			if (refreshItem != null) {
				if (refreshing) {
					refreshItem
							.setActionView(R.layout.actionbar_indeterminate_progress);
				} else {
					refreshItem.setActionView(null);
				}
			}
		}
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
		super.onStop();
		locClient.disconnect();
	}

}
