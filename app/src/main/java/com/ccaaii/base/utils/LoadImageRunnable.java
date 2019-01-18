package com.ccaaii.base.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.shenghuotong.CcaaiiApp;

public class LoadImageRunnable implements Runnable
{

	private static final String TAG = "[CCAAII]LoadImageRunnable";
	
	public static final int LOAD_HEAD_IMAGE             = 1001;
	
	private int mThreadId ;
	private Handler mHandler ;
	private String sUrl;
	
	public LoadImageRunnable(Handler h, int id, String str)
	{
		mHandler = h;
		mThreadId = id;
		sUrl = str;
	}
	
	@Override
	public void run()
	{
		Context context = CcaaiiApp.getCcaaiiContext();
		Message msg = new Message();
		msg.what = mThreadId;
		if(mThreadId == LOAD_HEAD_IMAGE){
			msg.obj = DownloadFilesUtils.getHeadImage(context, sUrl);
		}
		mHandler.sendMessage(msg);
		if(LogLevel.DEV){
			DevLog.d(TAG, "LoadImageRunnable : " + Thread.currentThread().getName());
		}
		
	}

}
