package com.ccaaii.shenghuotong.api;

import com.ccaaii.shenghuotong.BuildConfig;
import com.ccaaii.shenghuotong.CcaaiiApp;
import com.ccaaii.shenghuotong.R;

/**
 */
public class APIConstants {


	public static final class HTTP_STATUS_CODE{
		public static final int STATUS_SUCCESS = 200;
		public static final int STATUS_SUCCESS_201 = 201;
		public static final int STATUS_BAD_REQUEST = 400;
		public static final int STATUS_NOT_FOUND = 404;
		public static final int STATUS_FORBIDDEN = 403;
		public static final int STATUS_TIMEOUT = 408;
		public static final int STATUS_INTERNAL_SERVER_ERROR = 500;
	}

	public static final int RESPONSE_CODE_FAILED = -1;
	public static final int RESPONSE_CODE_NOT_NETWORK = -2;

	public static String getWeatherImageUrl(String code){
		return "http://files.heweather.com/cond_icon/" + code + ".png";
	}

	public static final String BASE_LINE="http://www.ddaaweb.com:8080";
	public static final String SERVER_URL= BuildConfig.SERVER_URL;

	public static final String GUESS_INIT_URL = "http://"+SERVER_URL+"/"+ BuildConfig.WEB_PROJECT_NAME+"/app/api/init"
			+"/" + getBrandName()
			+"/android"
			+"/" + getMarketName()
			+"/"+BuildConfig.VERSION_CODE;

	public static final String GUESS_INIT_TAG = "com.ccaaii.ooaaiipp.GUESS_INIT_TAG";
	public static final String GUESS_INIT_RESULT_TAG = "com.ccaaii.ooaaiipp.GUESS_INIT_RESULT_TAG";

	public static String getMarketName(){
		return CcaaiiApp.getCcaaiiContext().getString(R.string.market_name);
	}

	public static String getBrandName(){
		return CcaaiiApp.getCcaaiiContext().getString(R.string.brand_name);
	}
}
