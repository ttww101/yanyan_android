package com.ccaaii.shenghuotong.api;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class DataParser<T> extends BaseParser {
    protected Gson gson;

    public DataParser() {
        this.gson = gson();
    }

    public T toData(String json) throws JsonParseException {
        return this.gson.fromJson(json, getType());
    }

    public List<T> toList(String json) throws JsonParseException {
        List<T> list = new ArrayList<>();


        if (!TextUtils.isEmpty(json)) {
            Object[] array = (Object[]) Array.newInstance(getType(), 1);
            array = this.gson.fromJson(json, array.getClass());

            for (Object object : array) {
                @SuppressWarnings("unchecked")
                T t = (T) object;
                list.add(t);
            }
        }

        return list;
    }

    public String toJson(T t) throws JsonParseException {
        return this.gson.toJson(t);
    }

    protected abstract Class<T> getType();
}