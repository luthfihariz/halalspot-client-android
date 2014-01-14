package com.sharee.halalspot.sync;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sharee.halalspot.R;
import com.sharee.halalspot.beans.Category;
import com.sharee.halalspot.beans.Photo;
import com.sharee.halalspot.beans.Place;
import com.sharee.utilities.Api;
import com.sharee.utilities.Helper;
import com.sharee.utilities.OnAsyncTaskCompleted;

import android.content.Context;
import android.os.AsyncTask;

public class GetNearbyTask extends AsyncTask<Double, Void, JSONObject> {

	private Context context;
	private OnAsyncTaskCompleted listener;

	public GetNearbyTask(Context context, OnAsyncTaskCompleted listener) {
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected JSONObject doInBackground(Double... params) {
		try {
			return Api.getNearbyPlaces(params[1], params[0]);
		} catch (IOException e) {
		}
		return null;
	}

	@Override
	protected void onPostExecute(JSONObject response) {
		if (response != null) {
			try {
				boolean status = response.getBoolean("status");
				if (status) {
					listener.onTaskCompleted(true, parseJson(response));
				} else {
					listener.onTaskCompleted(false,
							context.getString(R.string.fail_to_connect));
				}
			} catch (JSONException e) {
				Helper.log("json error : "+e.getMessage());
				listener.onTaskCompleted(false,
						context.getString(R.string.fail_to_connect));
			}
		}
	}

	public ArrayList<Place> parseJson(JSONObject response) throws JSONException {
		ArrayList<Place> places = new ArrayList<Place>();

		JSONArray placesJson = response.getJSONObject("result").getJSONArray(
				"places");
		for (int i = 0; i < placesJson.length(); i++) {
			JSONObject placeJson = placesJson.getJSONObject(i);
			Place place = new Place();
			place.setName(placeJson.getString("name"));
			place.setDistance(placeJson.getDouble("distance"));

			JSONObject locJson = placeJson.getJSONObject("location");
			place.setAddress(locJson.getString("address"));
			place.setCity(locJson.getString("city"));
			place.setCountry(locJson.getString("country"));
			place.setCc(locJson.getString("cc"));
			place.setZipCode(locJson.getString("zipCode"));
			place.setLatitude((Double) locJson.getJSONObject("geocode")
					.getJSONArray("coordinates").get(1));
			place.setLongitude((Double) locJson.getJSONObject("geocode")
					.getJSONArray("coordinates").get(0));

			JSONObject contactJson = placeJson.getJSONObject("contact");
			place.setWebsite(contactJson.getString("website"));
			place.setTwitter(contactJson.getString("twitter"));
			place.setFacebook(contactJson.getString("facebook"));
			place.setTwitter(contactJson.getString("email"));
			place.setPhone(contactJson.getString("phone"));

			JSONObject catJson = placeJson.getJSONObject("category");
			Category category = new Category();
			category.setName(catJson.getString("name"));
			category.setShortName(catJson.getString("short_name"));
			place.setCategory(category);
			
			JSONArray photosJson = placeJson.getJSONArray("photos");
			ArrayList<Photo> photos = new ArrayList<Photo>();
			for (int j = 0; j < photosJson.length(); j++) {
				JSONObject photoJson = photosJson.getJSONObject(j);
				Photo photo = new Photo();
				photo.setUrl(photoJson.getString("url"));
				photo.setSource(photoJson.getString("source"));
				photos.add(photo);
			}
			place.setPhotos(photos);
			
			places.add(place);
		}

		return places;
	}

}
