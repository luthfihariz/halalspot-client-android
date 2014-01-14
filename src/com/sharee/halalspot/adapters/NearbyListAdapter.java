package com.sharee.halalspot.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharee.halalspot.R;
import com.sharee.halalspot.beans.Place;
import com.sharee.utilities.Helper;

public class NearbyListAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private ArrayList<Place> places;
	private ViewHolder holder;
	private ImageLoader imgLoader;
		
	public NearbyListAdapter(Context context, ArrayList<Place> places){
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.places = places;
		this.imgLoader = ImageLoader.getInstance();		
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
		if(v == null){
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.row_main, null);			
			holder.nearbyPlaceImg = (ImageView) v.findViewById(R.id.img_nearby);
			holder.nearbyPlaceName = (TextView) v.findViewById(R.id.txt_nearby_name);
			holder.nearbyPlaceCategory = (TextView) v.findViewById(R.id.txt_nearby_category);
			holder.nearbyPlaceDistance = (TextView) v.findViewById(R.id.txt_nearby_dist);
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		
		holder.nearbyPlaceName.setText(place.getName());
		holder.nearbyPlaceCategory.setText(place.getCategory().getShortName());
		holder.nearbyPlaceDistance.setText(place.getFormattedDistance());
		
		if(place.getPhotos().size() > 0){
			String photoUrl = place.getPhotos().get(0).getUrl();
			//UrlImageViewHelper.setUrlDrawable(holder.nearbyPlaceImg, photoUrl);			
			Helper.log("photo ["+position+"] url : "+photoUrl);
			imgLoader.displayImage(photoUrl, holder.nearbyPlaceImg);			
		}else{
			imgLoader.displayImage(null, holder.nearbyPlaceImg);
		}
		
		return v;
	}
	
	static class ViewHolder{
		ImageView nearbyPlaceImg;
		TextView nearbyPlaceName;
		TextView nearbyPlaceCategory;
		TextView nearbyPlaceDistance;
	}

}
