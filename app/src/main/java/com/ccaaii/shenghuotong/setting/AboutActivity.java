package com.ccaaii.shenghuotong.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.utils.DeviceUtils;
import com.ccaaii.shenghuotong.R;

public class AboutActivity extends BaseActivity {

	private static final String TAG = "[CCAAII]AboutActivity";

	private ImageButton mBackButton;
	private TextView mVersionView;

	private Dialog mProgressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (LogLevel.DEV) {
			DevLog.i(TAG, "onCreate...");
		}
		this.setContentView(R.layout.about_layout, true);

		buildLayout();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (LogLevel.DEV) {
			DevLog.i(TAG, "onResume...");
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (LogLevel.DEV) {
			DevLog.i(TAG, "onStart...");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (LogLevel.DEV) {
			DevLog.i(TAG, "onPause...");
		}

	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (LogLevel.DEV) {
			DevLog.i(TAG, "onDestroy...");
		}

	}

	private void buildLayout() {
		mBackButton = (ImageButton) this.findViewById(R.id.button_back);
		mBackButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mVersionView = (TextView) this.findViewById(R.id.version_textview);
		mVersionView.setText(DeviceUtils.getAppVersionName());
		this.findViewById(R.id.about_update_view).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

	}



}
