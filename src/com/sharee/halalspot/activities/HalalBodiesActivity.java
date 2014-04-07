package com.sharee.halalspot.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharee.halalspot.R;
import com.sharee.halalspot.beans.HalalBodies;
import com.sharee.utilities.Api;
import com.sharee.utilities.Helper;

public class HalalBodiesActivity extends SherlockActivity {

	private String email, webUrl, phone;
	private Intent intent;
	private ImageView bodiesHalalLogo;
	private TextView bodiesName;
	private TextView bodiesAddr;
	private TextView bodiesOverview;
	private TextView bodiesEmail;
	private TextView bodiesPhone;
	private TextView bodiesWeb;
	private RelativeLayout bodiesEmailCon;
	private RelativeLayout bodiesWebCon;
	private RelativeLayout bodiesPhoneCon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		initView();
		initAction();
	}

	private void initView() {
		setContentView(R.layout.activity_halal_bodies);
		bodiesHalalLogo = (ImageView) findViewById(R.id.img_bodies_halallogo);
		bodiesName = (TextView) findViewById(R.id.txt_bodies_name);
		bodiesAddr = (TextView) findViewById(R.id.txt_bodies_addr);
		bodiesOverview = (TextView) findViewById(R.id.txt_bodies_overview);
		bodiesWeb = (TextView) findViewById(R.id.txt_bodies_website);
		bodiesPhone = (TextView) findViewById(R.id.txt_bodies_phone);
		bodiesEmail = (TextView) findViewById(R.id.txt_bodies_email);
		bodiesEmailCon = (RelativeLayout) findViewById(R.id.rl_bodies_email);
		bodiesPhoneCon = (RelativeLayout) findViewById(R.id.rl_bodies_phone);
		bodiesWebCon = (RelativeLayout) findViewById(R.id.rl_bodies_website);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(intent.getStringExtra(HalalBodies.KEY_BODY_NAME));

		Helper.log("overview: "
				+ intent.getStringExtra(HalalBodies.KEY_BODY_OVERVIEW));

		bodiesName.setText(intent.getStringExtra(HalalBodies.KEY_BODY_NAME));
		bodiesAddr.setText(intent.getStringExtra(HalalBodies.KEY_BODY_ADDRESS));
		bodiesOverview.setText(Html.fromHtml(intent
				.getStringExtra(HalalBodies.KEY_BODY_OVERVIEW)));
		bodiesEmail.setText(email = intent
				.getStringExtra(HalalBodies.KEY_BODY_EMAIL));
		bodiesPhone.setText(phone = intent
				.getStringExtra(HalalBodies.KEY_BODY_PHONE));
		bodiesWeb.setText(webUrl = intent
				.getStringExtra(HalalBodies.KEY_BODY_WEBSITE));

		String halalLogoUrl = Api.getHalalLogoUrl(intent
				.getStringExtra(HalalBodies.KEY_BODY_LOGOURL));
		ImageLoader.getInstance().displayImage(halalLogoUrl, bodiesHalalLogo);
	}

	private void initAction() {
		bodiesEmailCon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (email != null) {
					try {
						Intent emailIntent = new Intent(
								android.content.Intent.ACTION_SEND);
						emailIntent.setType("plain/text");
						emailIntent.putExtra(
								android.content.Intent.EXTRA_EMAIL,
								new String[] { email });
						startActivity(Intent.createChooser(emailIntent,
								"Send mail..."));
					} catch (ActivityNotFoundException e) {
						Helper.toastShort(HalalBodiesActivity.this,
								getString(R.string.action_fail));
					}
				}
			}
		});

		bodiesPhoneCon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (phone != null) {
					try {
						Intent callIntent = new Intent(Intent.ACTION_DIAL);
						callIntent.setData(Uri.parse("tel:" + phone));
						startActivity(callIntent);
					} catch (ActivityNotFoundException ex) {
						Helper.log("err : " + ex.getMessage());
						Helper.toastShort(HalalBodiesActivity.this,
								getString(R.string.action_fail));
					}
				}
			}
		});

		bodiesWebCon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (webUrl != null) {
					if (!webUrl.startsWith("http://")
							&& !webUrl.startsWith("https://")) {
						webUrl = "http://" + webUrl;
					}
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(webUrl));
					startActivity(intent);
				}
			}
		});
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
