package com.sharee.halalspot.adapters;

import java.util.ArrayList;

import com.sharee.halalspot.beans.Photo;
import com.sharee.halalspot.fragments.PhotoPagerFragment;
import com.sharee.utilities.Helper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Photo> photos;
	
	public PhotoPagerAdapter(FragmentManager fm, ArrayList<Photo> photos) {
		super(fm);
		this.photos = photos;
	}

	@Override
	public Fragment getItem(int position) {
		Photo photo = photos.get(position);
		
		Bundle bundle = new Bundle();
		bundle.putString(Helper.KEY_PLACE_PHURL, photo.getUrl());
		
		Fragment fragment = new PhotoPagerFragment();
		fragment.setArguments(bundle);
		
		return fragment;		
	}

	@Override
	public int getCount() {
		return photos.size();
	}

}
