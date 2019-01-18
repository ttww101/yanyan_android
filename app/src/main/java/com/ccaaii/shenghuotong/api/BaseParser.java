package com.ccaaii.shenghuotong.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseParser {
    protected static Gson gson() {
        return new GsonBuilder().create();
    }
}
