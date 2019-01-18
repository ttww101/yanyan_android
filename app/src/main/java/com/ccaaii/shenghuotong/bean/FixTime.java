package com.ccaaii.shenghuotong.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FixTime implements Serializable {

    @SerializedName("results")
    private List<Results> results;

    public List<Results> getResults() { return results; }
}
