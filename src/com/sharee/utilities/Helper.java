package com.sharee.utilities;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

public class Helper {

	public static String TAG = "ShareeHalal";

	public static final void toastShort(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	public static final void toastLong(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}

	public static final void log(String msg) {
		Log.d(Helper.TAG, msg);
	}

	public static ProgressDialog buildProgressDialog(Context ctx,
			String message, boolean cancelable) {
		ProgressDialog pd = new ProgressDialog(ctx);
		pd.setMessage(message);
		pd.setCancelable(cancelable);

		return pd;
	}

	public static boolean isValidEmail(String email) {
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(email).matches();
	}

	public static boolean isConnectingToInternet(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String setThousandSeparator(long number) {
		BigDecimal bd = new BigDecimal(number);
		NumberFormat formatter = NumberFormat.getInstance();
		return formatter.format(bd.longValue());
	}

	public static float calculateDistance(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		Helper.log("distance in mil : " + dist);
		double kmConversion = 1.609;
		return (float) (dist * kmConversion);
	}

	public static String convertUnixToString(long unixTimeInMillis) {
		Date time = new Date(unixTimeInMillis * 1000);
		String timeString = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(time);
		return timeString;
	}
}
