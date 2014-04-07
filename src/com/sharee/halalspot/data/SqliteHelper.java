package com.sharee.halalspot.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

	public static final String SQLITE_DATABASE_NAME = "halalspot.db";
	private static final int SQLITE_DATABASE_VERSION = 1;

	public static final String PLACES_TABLE = "places";
	public static final String PLACES_ID = "_id"; // 1
	public static final String PLACES_NAME = "name"; // 2
	public static final String PLACES_LAT = "lat"; // 3
	public static final String PLACES_LNG = "lng"; // 4
	public static final String PLACES_ADDR = "addr"; // 5
	public static final String PLACES_CITY = "city"; // 6
	public static final String PLACES_COUNTRY = "country"; // 7
	public static final String PLACES_WEBSITE = "web"; // 8
	public static final String PLACES_PHONE = "phone"; // 9
	public static final String PLACES_CAT_NAME = "cat_name"; // 10
	public static final String PLACES_PHOTOS = "photos"; // 11

	private static final String CREATE_TABLE_PLACES = String
			.format("create table %s(%s text not null primary key, %s text, %s text, %s text"
					+ "%s text, %s text, %s text, %s text, %s text, %s text, %s text);",
					PLACES_TABLE, PLACES_ID, PLACES_NAME, PLACES_LAT,
					PLACES_LNG, PLACES_ADDR, PLACES_CITY, PLACES_COUNTRY,
					PLACES_WEBSITE, PLACES_PHONE, PLACES_CAT_NAME,
					PLACES_PHOTOS);

	public SqliteHelper(Context context) {
		super(context, SQLITE_DATABASE_NAME, null, SQLITE_DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PLACES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists "+SqliteHelper.PLACES_TABLE);
		onCreate(db);
	}

}
