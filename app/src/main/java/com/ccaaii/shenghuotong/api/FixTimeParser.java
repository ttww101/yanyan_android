package com.ccaaii.shenghuotong.api;

import com.ccaaii.shenghuotong.bean.FixTime;

public class FixTimeParser extends DataParser<FixTime> {
    @Override
    protected Class<FixTime> getType() {
        return FixTime.class;
    }
}