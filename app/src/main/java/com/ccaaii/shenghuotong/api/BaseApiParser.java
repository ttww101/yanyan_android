package com.ccaaii.shenghuotong.api;

import com.ga.sdk.parser.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseApiParser extends BaseParser {
    public static String getMessage(String jsonString) throws JSONException {
        return getString(jsonString, "message");
    }

    private static String getString(String jsonString, String key) throws JSONException {
        JSONObject json = new JSONObject(jsonString);

        return getString(json, key);
    }
}