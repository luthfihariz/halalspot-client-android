package com.sharee.halalspot.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractDataSource {

	protected SQLiteDatabase db;
	protected Context context;
	private SqliteHelper dbHelper;
    	
	public AbstractDataSource(Context context) {
		dbHelper = new SqliteHelper(context);
	}
	
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		if (db.isOpen()) {
			dbHelper.close();
		}
	}
}
