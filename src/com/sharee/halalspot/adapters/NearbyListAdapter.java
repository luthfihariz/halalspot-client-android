package com.sharee.halalspot.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharee.halalspot.R;
import com.sharee.halalspot.activities.PlaceDetailActivity;
import com.sharee.halalspot.beans.Place;
import com.sharee.utilities.Helper;

public class NearbyListAdapter extends BaseAdapter implements
		OnItemClickListener {

	private LayoutInflater inflater;
	private ArrayList<Place> places;
	private ViewHolder holder;
	private ImageLoader imgLoader;
	private Context context;

	public NearbyListAdapter(Context context, ArrayList<Place> places) {
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.places = places;
		this.imgLoader = ImageLoader.getInstance();
		this.context = context;
	}

	@Override
	public int getCount() {
		return places.size();
	}

	@Override
	public Object getItem(int arg0) {
		return places.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		Place place = (Place) getItem(position);
		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.row_main, null);
			holder.nearbyPlaceImg = (ImageView) v.findViewById(R.id.img_nearby);
			holder.nearbyPlaceName = (TextView) v
					.findViewById(R.id.txt_nearby_name);
			holder.nearbyPlaceCategory = (TextView) v
					.findViewById(R.id.txt_nearby_category);
			holder.nearbyPlaceDistance = (TextView) v
					.findViewById(R.id.txt_nearby_dist);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		holder.nearbyPlaceName.setText(place.getName());
		holder.nearbyPlaceCategory.setText(place.getCategory().getShortName());
		holder.nearbyPlaceDistance.setText(place.getFormattedDistance());

		if (place.getPhotos().size() > 0) {
			String photoUrl = place.getPhotos().get(0).getUrl();
			Helper.log("photo [" + position + "] url : " + photoUrl);
			imgLoader.displayImage(photoUrl, holder.nearbyPlaceImg);
		} else {
			imgLoader.displayImage(null, holder.nearbyPlaceImg);
		}

		return v;
	}

	static class ViewHolder {
		ImageView nearbyPlaceImg;
		TextView nearbyPlaceName;
		TextView nearbyPlaceCategory;
		TextView nearbyPlaceDistance;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Place place = (Place) getItem(position);
		Intent intent = new Intent(context, PlaceDetailActivity.class);
		intent.putExtra(Helper.KEY_PLACE_ID, place.getId());
		intent.putExtra(Helper.KEY_PLACE_NAME, place.getName());
		intent.putExtra(Helper.KEY_PLACE_ADDR, place.getAddress());
		intent.putExtra(Helper.KEY_PLACE_CATNAME, place.getCategory().getName());
		intent.putExtra(Helper.KEY_PLACE_CITY, place.getCity());
		intent.putExtra(Helper.KEY_PLACE_COUNTRY, place.getCountry());
		intent.putExtra(Helper.KEY_PLACE_WEB, place.getWebsite());
		intent.putExtra(Helper.KEY_PLACE_PHONE, place.getPhone());
		intent.putExtra(Helper.KEY_PLACE_LAT, place.getLatitude());
		intent.putExtra(Helper.KEY_PLACE_LNG, place.getLongitude());
		
		context.startActivity(intent);
	}

}
