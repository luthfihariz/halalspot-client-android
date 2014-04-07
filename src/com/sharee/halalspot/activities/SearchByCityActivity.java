package com.sharee.halalspot.activities;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.sharee.halalspot.R;
import com.sharee.halalspot.adapters.NearbyListAdapter;
import com.sharee.halalspot.beans.Place;
import com.sharee.halalspot.sync.GetNearbyTask;
import com.sharee.utilities.Api;
import com.sharee.utilities.Helper;
import com.sharee.utilities.OnAsyncTaskCompleted;

public class SearchByCityActivity extends SherlockActivity {

	private AutoCompleteTextView autoCompleteView;
	private MyAdapter<String> adapter;
	private ListView placesList;
	private ProgressBar progress;
	private ProgressBar actionBarProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_city);
		placesList = (ListView) findViewById(R.id.list_places);
		progress = (ProgressBar) findViewById(R.id.progressBar1);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_search_city_view, null);
		actionBar.setCustomView(v);
		actionBarProgress = (ProgressBar) v
				.findViewById(R.id.action_bar_progress);
		autoCompleteView = (AutoCompleteTextView) v
				.findViewById(R.id.city_auto_complete);
		adapter = new MyAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item);
		autoCompleteView.setAdapter(adapter);
		autoCompleteView.setOnItemClickListener(adapter);
		autoCompleteView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence charSequence, int arg1,
					int arg2, int arg3) {
				if (autoCompleteView.isPerformingCompletion()) {
					return;
				}
				if (charSequence.length() < 3) {
					return;
				}

				String query = charSequence.toString();
				adapter.clear();

				AutoCompleteCityTask autoCompleteTask = new AutoCompleteCityTask();
				autoCompleteTask.execute(query);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	public class MyAdapter<T> extends ArrayAdapter<String> implements
			Filterable, OnItemClickListener {

		public MyAdapter(Context context, int resource) {
			super(context, resource);
		}

		@Override
		public Filter getFilter() {
			return filter;
		}

		private Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					filterResults.count = getCount();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence contraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				}
			}
		};

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(autoCompleteView.getWindowToken(), 0);
			String city = getItem(arg2).split(",")[0];
			getNearbyListByCity(city);
		}
	}

	private class AutoCompleteCityTask extends
			AsyncTask<String, Void, JSONArray> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBarProgress.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONArray doInBackground(String... arg0) {
			try {
				return Api.autoCompleteCity(arg0[0]);
			} catch (IOException e) {
				e.printStackTrace();

			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			actionBarProgress.setVisibility(View.GONE);
			if (result != null) {
				for (int i = 0; i < result.length(); i++) {
					try {						
						adapter.add(result.getString(i));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else {
				Helper.toastShort(SearchByCityActivity.this,
						getString(R.string.fail_to_connect));
			}
		}

	}

	public void getNearbyListByCity(String city) {
		progress.setVisibility(View.VISIBLE);
		GetNearbyTask getNearbyTask = new GetNearbyTask(this,
				new OnAsyncTaskCompleted() {

					@Override
					public void onCompleted(boolean status, Object... objects) {
						progress.setVisibility(View.GONE);
						if (status) {
							ArrayList<Place> nearbyPlaces = (ArrayList<Place>) objects[0];
							setUpListView(SearchByCityActivity.this,
									nearbyPlaces);
						}
					}
				}, 0, city);
		getNearbyTask.execute();
	}

	private void setUpListView(final Context context, ArrayList<Place> places) {
		NearbyListAdapter adapter = new NearbyListAdapter(context, places);
		placesList.setAdapter(adapter);
		placesList.setOnItemClickListener(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
