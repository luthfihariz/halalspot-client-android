package com.sharee.halalspot.sync;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.sharee.halalspot.R;
import com.sharee.halalspot.beans.Category;
import com.sharee.halalspot.beans.Photo;
import com.sharee.halalspot.beans.Place;
import com.sharee.utilities.Api;
import com.sharee.utilities.Helper;
import com.sharee.utilities.OnAsyncTaskCompleted;

public class GetPlaceDetailTask extends AsyncTask<String, Void, JSONObject> {

	private OnAsyncTaskCompleted listener;
	private Context context;

	public GetPlaceDetailTask(Context context, OnAsyncTaskCompleted listener) {
		this.listener = listener;
		this.context = context;
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String placeId = params[0];
		try {
			return Api.getPlacesDetail(placeId);
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
					listener.onCompleted(true, parseJson(response));
					return;
				}
			} catch (JSONException e) {
				Helper.log("err : "+e.getMessage());				
			}
		}		
		listener.onCompleted(false,
				context.getString(R.string.fail_to_connect));
	}

	private Place parseJson(JSONObject response) throws JSONException {
		Place place = new Place();
		JSONObject placeJson = response.getJSONObject("result").getJSONObject(
				"place");
		place.setName(placeJson.getString("name"));		

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

		return place;
	}

}
