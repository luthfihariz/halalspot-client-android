package com.sharee.halalspot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharee.halalspot.R;
import com.sharee.utilities.Helper;

public class PhotoPagerFragment extends Fragment {

	private ImageView photoView;
	private String photoUrl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initView();
	}

	private View initView() {

		Bundle bundle = getArguments();
		photoUrl = bundle.getString(Helper.KEY_PLACE_PHURL);

		photoView = new ImageView(getActivity());
		android.view.ViewGroup.LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		photoView.setLayoutParams(params);
		photoView.setScaleType(ScaleType.CENTER_CROP);
		photoView.setImageResource(R.drawable.placeholder);
		
		LinearLayout layout = new LinearLayout(getActivity());
		LayoutParams layParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(layParams);
		layout.setGravity(Gravity.CENTER);
		layout.addView(photoView);

		ImageLoader.getInstance().displayImage(photoUrl, photoView);

		Helper.log("photo url : " + photoUrl);

		return layout;
	}
}
