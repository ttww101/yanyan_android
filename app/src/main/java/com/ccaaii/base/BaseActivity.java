package com.ccaaii.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.ccaaii.base.utils.Compatibility;
import com.ccaaii.base.utils.StatusBarUtils;
import com.ccaaii.shenghuotong.R;

public class BaseActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

	}

	protected void setContentView(int resId, boolean enableStatusBarColor){
		this.setContentView(resId);
		if (enableStatusBarColor){
			StatusBarUtils.setColor(this, this.getResources().getColor(R.color.style_color));
		}
	}

	@Override
	public void onResume(){
		
		super.onResume();
		if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
	}

	private Toast mToast;
	protected void showToast(int message, int time) {
		showToast(this.getString(message), time);
	}
	
	protected void showToast(String message, int time) {
		if (mToast == null) {
			mToast = Toast.makeText(this, message, time);
		} else {
			mToast.setText(message);
			mToast.setDuration(time);
		}
		if (Compatibility.getApiLevel() < 14) {
			mToast.cancel();
		}
		mToast.show();
	}

}
