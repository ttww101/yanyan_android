package com.ccaaii.shenghuotong.Loader;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ccaaii.shenghuotong.api.FixTimeApi;
import com.ccaaii.shenghuotong.api.FixTimeParser;
import com.ccaaii.shenghuotong.bean.FixTime;
import com.ga.sdk.loader.BaseAsyncTaskLoader;

import java.util.List;

public class GetFixTimeLoader extends BaseAsyncTaskLoader<FixTime> {

    public GetFixTimeLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected String action() throws Exception {
        FixTimeApi otherApi = new FixTimeApi();
        return otherApi.getLeancloud();
    }

    @Override
    protected FixTime parseData(String s) throws Exception {
        FixTimeParser parser = new FixTimeParser();
        return parser.toData(s);
    }
}