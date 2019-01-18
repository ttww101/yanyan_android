package com.ccaaii.shenghuotong;

import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.Loader.GetFixTimeLoader;
import com.ccaaii.shenghuotong.api.ApiLoaderCallback;
import com.ccaaii.shenghuotong.api.ApiUtils;
import com.ccaaii.shenghuotong.api.IResponseListener;
import com.ccaaii.shenghuotong.bean.FixTime;
import com.ccaaii.shenghuotong.utils.SharedPreferencesHelper;
import com.ccaaii.shenghuotong.web.KeziWebViewActivity;
import com.ga.sdk.api.ApiResult;

import java.util.List;


/**
 */
public class SplashScreenActivity extends BaseActivity {

    private static final String TAG = "[CCAAII]SplashScreenActivity";

    private static final int SPLASH_SHOW_TIME_OUT = 1000;

    public boolean canJump = false;

    private boolean hasShowAds = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onCreate");
        }

        setContentView(R.layout.splash_screen_layout, false);

        ApiLoaderCallback.startApiLoader(this, FixTimeLoaderCallback);
        /*
        ApiUtils.initData(this, new IResponseListener() {
            @Override
            public void onResponseResult(boolean success, int code, String msg) {

                String h5page = SharedPreferencesHelper.getH5page(SplashScreenActivity.this);
                if(!TextUtils.isEmpty(h5page)){
                    Intent intent  = new Intent(SplashScreenActivity.this, KeziWebViewActivity.class);
                    intent.putExtra("url",h5page);
                    startActivity(intent);
                    finish();
                    return;
                }
                Message msgMessage = mHandler.obtainMessage();
                msgMessage.what = WHAT_LOAD_TIMEOUT;
                mHandler.sendMessageDelayed(msgMessage, SPLASH_SHOW_TIME_OUT);
            }
        });
*/
    }

    @Override
    public void onResume() {
        super.onResume();

        if (canJump) {
            Message msg = mHandler.obtainMessage();
            msg.what = WHAT_AD_DISSMISSED;
            mHandler.sendMessage(msg);
        }
        canJump = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        canJump = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onDestroy");
        }

        if (mHandler != null) {
            mHandler.removeMessages(WHAT_LOAD_TIMEOUT);
            mHandler.removeMessages(WHAT_REFRESH_AD);
            mHandler.removeMessages(WHAT_AD_DISSMISSED);
            mHandler = null;
        }
    }

    private static final int WHAT_LOAD_TIMEOUT = 0;
    private static final int WHAT_REFRESH_AD = 1;
    private static final int WHAT_AD_DISSMISSED = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isFinishing()) {
                return;
            }
            if (msg.what == WHAT_LOAD_TIMEOUT) {
                MarketLog.i(TAG, "mHandler WHAT_LOAD_TIMEOUT");
                if (hasShowAds) {
                    return;
                }
                canJump = true;
            } else if (msg.what == WHAT_AD_DISSMISSED) {
                MarketLog.i(TAG, "mHandler WHAT_AD_DISSMISSED");
            } else if (msg.what == WHAT_REFRESH_AD) {
                MarketLog.w(TAG, "mHandler WHAT_REFRESH_AD");
                return;
            }

            if (canJump) {
//              Intent intent = new Intent(SplashScreenActivity.this, GuideScreenActivity.class);
//              SplashScreenActivity.this.startActivity(intent);
                SharedPreferences setting = SplashScreenActivity.this.getSharedPreferences(CcaaiiConstants.CCAAII_SHARED_PREFERENCES_CONSTANTS, 0);
                boolean isFirstInstall = setting.getBoolean(CcaaiiConstants.FIRST_INSTALL_FLAG, true);
                if (isFirstInstall) {
                    Intent intent = new Intent(SplashScreenActivity.this, GuideScreenActivity.class);
                    SplashScreenActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, HomeFragmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SplashScreenActivity.this.startActivity(intent);
                }

                finish();
            } else {
                canJump = true;
            }

        }
    };

    ApiLoaderCallback<FixTime> FixTimeLoaderCallback = new ApiLoaderCallback<FixTime>() {
        @Override
        public Loader<ApiResult<FixTime>> onCreateLoader(int id, Bundle args) {
            return new GetFixTimeLoader(SplashScreenActivity.this);
        }

        @Override
        protected void onStatusOk(FixTime data) {

            if(!TextUtils.isEmpty( data.getResults().get(0).getUrl())){
                Intent intent  = new Intent(SplashScreenActivity.this, KeziWebViewActivity.class);
                intent.putExtra("url", data.getResults().get(0).getUrl());
                startActivity(intent);
                finish();
                return;
            }

            Message msgMessage = mHandler.obtainMessage();
            msgMessage.what = WHAT_LOAD_TIMEOUT;
            mHandler.sendMessageDelayed(msgMessage, SPLASH_SHOW_TIME_OUT);
        }
    };
}
