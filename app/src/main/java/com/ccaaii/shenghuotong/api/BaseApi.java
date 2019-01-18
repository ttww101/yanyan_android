package com.ccaaii.shenghuotong.api;

import android.content.Context;

import com.ccaaii.shenghuotong.BuildConfig;
import com.ccaaii.shenghuotong.utils.ExtraKey;
import com.ga.sdk.api.AbstractBaseApi;

import java.util.HashMap;
import java.util.Map;

public class BaseApi  extends AbstractBaseApi {
    @Override
    protected Map<String, String> getHeader() {
        Map<String, String> headers = null;

        headers = new HashMap<>();
        headers.put("X-LC-Id", ExtraKey.APPID);
        headers.put("X-LC-Key", ExtraKey.APPKEY);

        return headers;
    }

    @Override
    protected String getAccessToken(Context context) { return ""; }

    @Override
    protected String toUrl(String path) { return BuildConfig.API_URL_V2 + path; }

}