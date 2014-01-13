package com.sharee.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public class Api {

	private static String HOST_NAME = "http://127.0.0.1/sharee";
	private static String HOST_NAME_API = HOST_NAME + "/api";
	private static String NEARBY_PLACES_URL = HOST_NAME_API + "/places/nearby";
	private static String PLACES_URL = HOST_NAME_API + "/places";

	public static JSONObject getNearbyPlaces(double latitude, double longitude,
			int type) throws IOException {
		Map<String, String> params = new java.util.HashMap<String, String>();
		params.put("latitude", String.valueOf(latitude));
		params.put("longitude", String.valueOf(longitude));
		return Api.postHttp(NEARBY_PLACES_URL, params);
	}

	public static JSONObject getPlacesDetail(String placesId)
			throws IOException {
		return Api.getHttp(PLACES_URL, placesId);
	}

	public static JSONObject getHttp(String urlString, String paramVal)
			throws IOException {

		// always close connection
		System.setProperty("http.keepAlive", "false");// to fix EOFException

		URL url = null;
		HttpURLConnection conn = null;
		JSONObject jsonObject = null;

		try {

			if (paramVal != null) {
				url = new URL(urlString + "/" + paramVal);
			} else {
				url = new URL(urlString);
			}

			Helper.log("get url: " + url);

		} catch (MalformedURLException e) {
			Helper.log("invalid url " + e.getLocalizedMessage());
		}

		try {

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");

			// handle the response
			int status = conn.getResponseCode();

			if (status == 200)// status OK
			{
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				String line = null;
				StringBuilder sb = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

				String json = sb.toString();

				Helper.log("json response: " + json);

				// convert String to JSON object
				jsonObject = new JSONObject(json);
			} else {
				InputStream es = conn.getErrorStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(es));
				String line = null;

				while ((line = reader.readLine()) != null) {
					Helper.log("error: " + line);
				}
				es.close();

				throw new IOException(
						"[Server Util] Request failed with error code: "
								+ status);
			}

		} catch (JSONException e) {
			Helper.log("JSON Exception: " + e.getLocalizedMessage());
		} finally {
			conn.disconnect();
			Helper.log("connection disconnect");
		}

		return jsonObject;
	}

	public static JSONObject postHttp(String urlString,
			Map<String, String> params) throws IOException {

		// always close connection
		System.setProperty("http.keepAlive", "false");// to fix EOFException

		URL url = null;
		HttpURLConnection conn = null;
		JSONObject jsonObject = null;

		// Constructing parameters
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			stringBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				stringBuilder.append('&');
			}
		}

		// data to be sent
		String paramBody = stringBuilder.toString();
		byte[] paramBytes = paramBody.getBytes();

		Helper.log("body to send: " + paramBody);

		try {
			// Get URL object
			url = new URL(urlString);

			Helper.log("post url: " + url);

		} catch (MalformedURLException e) {
			// TODO: handle exception
			Helper.log("invalid url " + e.getLocalizedMessage());
		}

		try {

			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(paramBytes.length);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("Connection", "close");// to prevent
															// EOFException

			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(paramBytes);
			out.flush();
			out.close();

			// handle the response
			int status = conn.getResponseCode();

			if (status == 200)// status OK
			{
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				String line = null;
				StringBuilder sb = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

				String json = sb.toString();

				Helper.log("json response: " + json);

				// convert String to JSON object
				jsonObject = new JSONObject(json);
			} else {
				InputStream es = conn.getErrorStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(es));
				String line = null;

				while ((line = reader.readLine()) != null) {
					Helper.log("error: " + line);
				}
				es.close();

				throw new IOException(
						"[Server Util] Request failed with error code: "
								+ status);
			}

		} catch (JSONException e) {
			Helper.log("JSON Exception: " + e.getLocalizedMessage());
		} catch (Exception e) {
			Helper.log("Exception: " + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			conn.disconnect();
			Helper.log("connection disconnect");
		}

		return jsonObject;
	}

}