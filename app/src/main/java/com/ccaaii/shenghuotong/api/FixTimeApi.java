package com.ccaaii.shenghuotong.api;

import com.ga.sdk.api.Params;

public class FixTimeApi extends BaseApi {

    private static final String LEANCLOUD = "/classes/FixTime";
    private static final String LOGIN = "/login?username=" + "%1$s" + "&password=" + "%2$s";
    private static final String USER = "/users";

    public String getLeancloud() throws Exception {
        Params params = new Params();

        return this.doGet(LEANCLOUD, params.getMap());
    }

    public String Login(String s1, String s2) throws Exception {
        Params params = new Params();

        return this.doGet(String.format(LOGIN, s1, s2), params.getMap());
    }

    public String  Register(String s1, String s2) throws Exception {
        Params params = new Params();

        params.putRequired("username",s1);
        params.putRequired("password",s1);

        return this.doPost(USER, params.getMap());
    }
}
