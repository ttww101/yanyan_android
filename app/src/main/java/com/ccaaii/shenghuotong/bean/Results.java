package com.ccaaii.shenghuotong.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Results implements Serializable {

    @SerializedName("url")
    private String url;

    public String getUrl() { return url; }
}