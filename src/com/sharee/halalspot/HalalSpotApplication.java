package com.sharee.halalspot;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class HalalSpotApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(10))
				.showImageOnLoading(R.drawable.placeholder)
				.showImageForEmptyUri(R.drawable.placeholder)
				.showImageOnFail(R.drawable.placeholder)
				.resetViewBeforeLoading(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.build();
		ImageLoader.getInstance().init(config);
	}
}
