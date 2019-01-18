package com.ccaaii.shenghuotong.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ccaaii.shenghuotong.CcaaiiApp;
import com.ccaaii.shenghuotong.bean.InitBean;
import com.ccaaii.shenghuotong.utils.SharedPreferencesHelper;
import com.google.gson.reflect.TypeToken;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.bean.City;
import com.ccaaii.shenghuotong.weather.WeatherDAO;
import com.ccaaii.base.config.Build;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class ApiUtils {

    private static final String TAG = "[CCAAII]ApiUtils";

    public static void initData(final Context context, final IResponseListener listener) {
        RequestQueue queue = CcaaiiApp.getQueue();
        try {
            queue.cancelAll(APIConstants.GUESS_INIT_TAG);
        } catch (Exception e) {

        }

        String url = APIConstants.GUESS_INIT_URL;
        Log.i(TAG, "initData URL = " + url);
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "initData response : " + response);
                try {
                    if (!TextUtils.isEmpty(response)) {
                        InitBean initBean = CcaaiiApp.getGson().fromJson(response, new TypeToken<InitBean>() {}.getType());
                        if (initBean != null) {
                            SharedPreferencesHelper.saveH5Page(CcaaiiApp.getCcaaiiContext(), initBean);
                            SharedPreferencesHelper.saveAndroidApkUrl(CcaaiiApp.getCcaaiiContext(), initBean);
                            SharedPreferencesHelper.saveTouzhuLink(CcaaiiApp.getCcaaiiContext(), initBean);
                        }
                        if (initBean != null){
                            CcaaiiApp.saveInitData(response);
                        }
                    }else{
                        InitBean initBean=new InitBean();
                        initBean.h5Page="";
                        initBean.androidApkUrl="";
                        initBean.touzhulink = "";
                        SharedPreferencesHelper.saveH5Page(CcaaiiApp.getCcaaiiContext(),initBean);
                        SharedPreferencesHelper.saveAndroidApkUrl(CcaaiiApp.getCcaaiiContext(),initBean);
                        SharedPreferencesHelper.saveTouzhuLink(CcaaiiApp.getCcaaiiContext(),initBean);
                    }
                    if (listener != null) {
                        listener.onResponseResult(true, 0, null);
                    }
                } catch (Exception ex) {
                    InitBean initBean=new InitBean();
                    initBean.h5Page="";
                    SharedPreferencesHelper.saveH5Page(CcaaiiApp.getCcaaiiContext(),initBean);
                    Log.e(TAG,ex.getMessage());
                    if (listener != null) {
                        listener.onResponseResult(false, 0, null);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "initData error : " + error.getMessage());
                InitBean initBean=new InitBean();
                initBean.h5Page="";
                SharedPreferencesHelper.saveH5Page(CcaaiiApp.getCcaaiiContext(),initBean);
                if (listener != null) {
                    listener.onResponseResult(false, 0, null);
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };
        req.setTag(APIConstants.GUESS_INIT_TAG);
        queue.add(req);
    }

    public static void getWeather(final Context context, final String cityId, final String cityStr, final String provinceStr, final boolean isCurrent, final IResponseListener listener) {
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getWeather cityId = " + cityId);
        }

        if (!NetworkUtils.isAvailable(context)) {
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_NOT_NETWORK, context.getString(R.string.no_network));
            }
            return;
        }


        if (TextUtils.isEmpty(cityId)){
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.no_network));
            }
            return;
        }

        String url = "https://free-api.heweather.com/s6/weather?location=" + cityId + "&key=04b3f448dfdf430ebd5fa45a5b8a4ad6";
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getWeather url=" + url);
        }

        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (LogLevel.DEV) {
                    DevLog.d(TAG, "getWeather onResponse, response = " + response);
                }
                try {

                    JSONObject json = new JSONObject(response);
                    if (json != null){
                        JSONArray array = json.getJSONArray("HeWeather6");
                        JSONObject object = array.getJSONObject(0);
                        if (object.getString("status").equals("ok")){
                            String weatherStr = object.toString();
                            City city = new City();
                            city.id = cityId;
                            city.city = cityStr;
                            city.prov = provinceStr;
                            WeatherDAO.saveWeather(context, city, weatherStr, isCurrent);
                        }

                    }
                    if (listener != null) {
                        listener.onResponseResult(true, 0, null);
                    }
                } catch (Exception ex) {
                    if (listener != null) {
                        listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (LogLevel.MARKET) {
                    MarketLog.e(TAG, "getWeather onErrorResponse, error = " + error.getMessage());
                }
                if (listener != null) {
                    listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                }
            }
        }) {

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("apikey", Build.API_STORE_KEY);
//                return headers;
//            }

        };
        getRequest.setTag(RequestTag.REQUEST_TAG_FOR_WEATHER);
        CcaaiiApp.getQueue().add(getRequest);
    }


    //https://www.nowapi.com/api
    public static void getMobileDetail(final Context context, final String mobile, final IResponseListener listener) {
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getMobileDetail mobile = " + mobile);
        }

        if (!NetworkUtils.isAvailable(context)) {
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_NOT_NETWORK, context.getString(R.string.no_network));
            }
            return;
        }


        if (TextUtils.isEmpty(mobile)){
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, "");
            }
            return;
        }

        String url = "http://api.k780.com/?app=phone.get&phone=" + mobile + "&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getMobileDetail url=" + url);
        }

        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (LogLevel.DEV) {
                    DevLog.d(TAG, "getMobileDetail onResponse, response = " + response);
                }
                try {
                    if (listener != null) {
                        listener.onResponseResult(true, 0, response);
                    }
                } catch (Exception ex) {
                    if (listener != null) {
                        listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (LogLevel.MARKET) {
                    MarketLog.e(TAG, "getMobileDetail onErrorResponse, error = " + error.getMessage());
                }
                if (listener != null) {
                    listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                }
            }
        }) {

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("apikey", Build.API_STORE_KEY);
//                return headers;
//            }

        };
        getRequest.setTag(RequestTag.REQUEST_TAG_FOR_SEARCH_MOBILE);
        CcaaiiApp.getQueue().add(getRequest);
    }

    //https://www.nowapi.com/api
    public static void getIdsDetail(final Context context, final String ids, final IResponseListener listener) {
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getIdsDetail ids = " + ids);
        }

        if (!NetworkUtils.isAvailable(context)) {
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_NOT_NETWORK, context.getString(R.string.no_network));
            }
            return;
        }


        if (TextUtils.isEmpty(ids)){
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, "");
            }
            return;
        }

        String url = "http://api.k780.com/?app=idcard.get&idcard=" + ids + "&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getIdsDetail url=" + url);
        }

        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (LogLevel.DEV) {
                    DevLog.d(TAG, "getIdsDetail onResponse, response = " + response);
                }
                try {
                    if (listener != null) {
                        listener.onResponseResult(true, 0, response);
                    }
                } catch (Exception ex) {
                    if (listener != null) {
                        listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (LogLevel.MARKET) {
                    MarketLog.e(TAG, "getIdsDetail onErrorResponse, error = " + error.getMessage());
                }
                if (listener != null) {
                    listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                }
            }
        }) {

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("apikey", Build.API_STORE_KEY);
//                return headers;
//            }

        };
        getRequest.setTag(RequestTag.REQUEST_TAG_FOR_SEARCH_IDS);
        CcaaiiApp.getQueue().add(getRequest);
    }

    //https://www.nowapi.com/api
    public static void getChangeMoney(final Context context, final String from, final String to, final String amount, final IResponseListener listener) {
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getChangeMoney from = " + from + ", to = " + to + ", amount = " + amount);
        }

        if (!NetworkUtils.isAvailable(context)) {
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_NOT_NETWORK, context.getString(R.string.no_network));
            }
            return;
        }


        if (TextUtils.isEmpty(from) || TextUtils.isEmpty(to) || TextUtils.isEmpty(amount)){
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, "");
            }
            return;
        }

        String url = "http://api.k780.com/?app=finance.rate&scur=" + from + "&tcur=" + to + "&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4";
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getChangeMoney url=" + url);
        }

        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (LogLevel.DEV) {
                    DevLog.d(TAG, "getChangeMoney onResponse, response = " + response);
                }
                try {
                    if (listener != null) {
                        listener.onResponseResult(true, 0, response);
                    }
                } catch (Exception ex) {
                    if (listener != null) {
                        listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (LogLevel.MARKET) {
                    MarketLog.e(TAG, "getChangeMoney onErrorResponse, error = " + error.getMessage());
                }
                if (listener != null) {
                    listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                }
            }
        }) {

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("apikey", Build.API_STORE_KEY);
//                return headers;
//            }

        };
        getRequest.setTag(RequestTag.REQUEST_TAG_FOR_SEARCH_MONEY);
        CcaaiiApp.getQueue().add(getRequest);
    }

    //https://www.nowapi.com/api
    public static void getIpsDetail(final Context context, final String ips, final IResponseListener listener) {
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getIpsDetail ids = " + ips);
        }

        if (!NetworkUtils.isAvailable(context)) {
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_NOT_NETWORK, context.getString(R.string.no_network));
            }
            return;
        }


        if (TextUtils.isEmpty(ips)){
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, "");
            }
            return;
        }

        String url = "http://api.k780.com/?app=ip.get&ip=" + ips + "&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getIpsDetail url=" + url);
        }

        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (LogLevel.DEV) {
                    DevLog.d(TAG, "getIpsDetail onResponse, response = " + response);
                }
                try {
                    if (listener != null) {
                        listener.onResponseResult(true, 0, response);
                    }
                } catch (Exception ex) {
                    if (listener != null) {
                        listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (LogLevel.MARKET) {
                    MarketLog.e(TAG, "getIpsDetail onErrorResponse, error = " + error.getMessage());
                }
                if (listener != null) {
                    listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                }
            }
        }) {

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("apikey", Build.API_STORE_KEY);
//                return headers;
//            }

        };
        getRequest.setTag(RequestTag.REQUEST_TAG_FOR_SEARCH_IPS);
        CcaaiiApp.getQueue().add(getRequest);
    }

    public static void getLocationByIp(final Context context, final IResponseListener listener) {
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getLocationByIp");
        }

        if (!NetworkUtils.isAvailable(context)) {
            if (listener != null) {
                listener.onResponseResult(false, APIConstants.RESPONSE_CODE_NOT_NETWORK, context.getString(R.string.no_network));
            }
            return;
        }


        String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getLocationByIp url=" + url);
        }

        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (LogLevel.DEV) {
                    DevLog.d(TAG, "getLocationByIp onResponse, response = " + response);
                }
                try {
                    if (listener != null) {
                        listener.onResponseResult(true, 0, response);
                    }
                } catch (Exception ex) {
                    if (listener != null) {
                        listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (LogLevel.MARKET) {
                    MarketLog.e(TAG, "getLocationByIp onErrorResponse, error = " + error.getMessage());
                }
                if (listener != null) {
                    listener.onResponseResult(false, APIConstants.RESPONSE_CODE_FAILED, context.getString(R.string.request_failed));
                }
            }
        }) {

        };
        getRequest.setTag(RequestTag.REQUEST_TAG_LOCATION);
        CcaaiiApp.getQueue().add(getRequest);
    }
}
