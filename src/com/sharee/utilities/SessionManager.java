package com.sharee.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	private SharedPreferences pref;
	private Editor editor;
	private Context context;
	
	private static final int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "HalalSpotPref";
	
	public SessionManager(Context context) {
		this.context = context;
		this.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
	}		
	
}
