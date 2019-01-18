package com.ccaaii.base;


import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.ccaaii.base.utils.Compatibility;

public class BaseFragment extends Fragment{

	private Toast mToast;
	protected void showToast(int message, int time) {
		showToast(this.getString(message), time);
	}
	
	protected void showToast(String message, int time) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), message, time);
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
