package com.ccaaii.base.utils;

import android.text.TextUtils;

import com.ccaaii.shenghuotong.R;

/**
 */
public class WeatherUtils {

    public enum WEATHER_RES {
        WEATHER_100("100", R.drawable.weather_img_100, R.drawable.bg_weather_01, R.drawable.bg_weather_big_01),
        WEATHER_101("101", R.drawable.weather_img_101, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_102("102", R.drawable.weather_img_102, R.drawable.bg_weather_01, R.drawable.bg_weather_big_01),
        WEATHER_103("103", R.drawable.weather_img_103, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_104("104", R.drawable.weather_img_104, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_200("200", R.drawable.weather_img_200, R.drawable.bg_weather_01, R.drawable.bg_weather_big_01),
        WEATHER_201("201", R.drawable.weather_img_201, R.drawable.bg_weather_01, R.drawable.bg_weather_big_01),
        WEATHER_202("202", R.drawable.weather_img_202, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_203("203", R.drawable.weather_img_203, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_204("204", R.drawable.weather_img_204, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_205("205", R.drawable.weather_img_205, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_206("206", R.drawable.weather_img_206, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_207("207", R.drawable.weather_img_207, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_208("208", R.drawable.weather_img_208, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_209("209", R.drawable.weather_img_209, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_210("210", R.drawable.weather_img_210, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_211("211", R.drawable.weather_img_211, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_212("212", R.drawable.weather_img_212, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_213("213", R.drawable.weather_img_213, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_300("300", R.drawable.weather_img_300, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_301("301", R.drawable.weather_img_301, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_302("302", R.drawable.weather_img_302, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_303("303", R.drawable.weather_img_303, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_304("304", R.drawable.weather_img_304, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_305("305", R.drawable.weather_img_305, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_306("306", R.drawable.weather_img_306, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_307("307", R.drawable.weather_img_307, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_308("308", R.drawable.weather_img_308, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_309("309", R.drawable.weather_img_309, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_310("310", R.drawable.weather_img_310, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_311("311", R.drawable.weather_img_311, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_312("312", R.drawable.weather_img_312, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_313("313", R.drawable.weather_img_313, R.drawable.bg_weather_04, R.drawable.bg_weather_big_04),
        WEATHER_400("400", R.drawable.weather_img_400, R.drawable.bg_weather_05, R.drawable.bg_weather_big_05),
        WEATHER_401("401", R.drawable.weather_img_401, R.drawable.bg_weather_05, R.drawable.bg_weather_big_05),
        WEATHER_402("402", R.drawable.weather_img_402, R.drawable.bg_weather_05, R.drawable.bg_weather_big_05),
        WEATHER_403("403", R.drawable.weather_img_403, R.drawable.bg_weather_05, R.drawable.bg_weather_big_05),
        WEATHER_404("404", R.drawable.weather_img_404, R.drawable.bg_weather_05, R.drawable.bg_weather_big_05),
        WEATHER_405("405", R.drawable.weather_img_405, R.drawable.bg_weather_05, R.drawable.bg_weather_big_05),
        WEATHER_406("406", R.drawable.weather_img_406, R.drawable.bg_weather_05, R.drawable.bg_weather_big_05),
        WEATHER_407("407", R.drawable.weather_img_407, R.drawable.bg_weather_05, R.drawable.bg_weather_big_05),
        WEATHER_500("500", R.drawable.weather_img_500, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_501("501", R.drawable.weather_img_501, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_502("502", R.drawable.weather_img_502, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_503("503", R.drawable.weather_img_503, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_504("504", R.drawable.weather_img_504, R.drawable.bg_weather_02, R.drawable.bg_weather_big_02),
        WEATHER_507("507", R.drawable.weather_img_507, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_508("508", R.drawable.weather_img_508, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_900("900", R.drawable.weather_img_900, R.drawable.bg_weather_01, R.drawable.bg_weather_big_01),
        WEATHER_901("901", R.drawable.weather_img_901, R.drawable.bg_weather_03, R.drawable.bg_weather_big_03),
        WEATHER_999("999", R.drawable.weather_img_999, R.drawable.bg_weather_01, R.drawable.bg_weather_big_01);

        String code;
        int weahter_img;
        int weather_bg;
        int weather_big_bg;

        public String getCode(){
            return this.code;
        }

        public int getWeatherImgId(){
            return this.weahter_img;
        }

        public int getWeatherBgId(){
//            return this.weather_bg;
            return R.drawable.bg_weather_big;
        }

        public int getWeatherBigBgId(){
            return this.weather_big_bg;
        }

        WEATHER_RES(String code, int img, int bg, int bigBg){
            this.code = code;
            this.weahter_img = img;
            this.weather_bg = bg;
            this.weather_big_bg = bigBg;
        }

        public static WEATHER_RES getWeatherResByCode(String code){
            if (TextUtils.isEmpty(code)) {
                return WEATHER_999;
            }

            for (WEATHER_RES weather : WEATHER_RES.values()){
                if (code.equals(weather.getCode())){
                    return weather;
                }
            }

            return WEATHER_999;
        }

    }
}
