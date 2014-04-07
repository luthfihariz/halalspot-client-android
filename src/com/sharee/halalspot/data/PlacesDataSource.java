package com.sharee.halalspot.data;

import android.content.Context;

public class PlacesDataSource extends AbstractDataSource {

	@SuppressWarnings("unused")
	private static final String[] coloumns = {
		SqliteHelper.PLACES_ID,
		SqliteHelper.PLACES_NAME,
		SqliteHelper.PLACES_LAT,
		SqliteHelper.PLACES_LNG,
		SqliteHelper.PLACES_ADDR,
		SqliteHelper.PLACES_CITY,
		SqliteHelper.PLACES_COUNTRY,
		SqliteHelper.PLACES_WEBSITE,
		SqliteHelper.PLACES_PHONE,
		SqliteHelper.PLACES_CAT_NAME,
		SqliteHelper.PLACES_PHOTOS,
		};
	
	public PlacesDataSource(Context context) {
		super(context);		
	}
	
	

}
