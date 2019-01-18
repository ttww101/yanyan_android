package com.ccaaii.shenghuotong.document;

import android.content.Context;
import android.util.Xml;


import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.FileUtils;
import com.ccaaii.shenghuotong.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 */
public class DocumentUtils {

    private static final String TAG = "[CCAAII]DocumentUtils";

    public static final int[] STAR_LEVEL_RES_IDS = new int[]{
            R.drawable.image_star_0,
            R.drawable.image_star_1,
            R.drawable.image_star_2,
            R.drawable.image_star_3,
            R.drawable.image_star_4,
            R.drawable.image_star_5,
            R.drawable.image_star_6,
            R.drawable.image_star_7,
            R.drawable.image_star_8,
            R.drawable.image_star_9,
            R.drawable.image_star_10,
    };

    public static final int[] CATEGORY_NAME_IDS = new int[]{
            R.string.category_0,
            R.string.category_1,
            R.string.category_2,
            R.string.category_3,
            R.string.category_4,
            R.string.category_5,
            R.string.category_6,
            R.string.category_7
    };

    public static final int[] CATEGORY_COLOR_IMG_IDS = new int[]{
            R.drawable.icon_category_color_0,
            R.drawable.icon_category_color_1,
            R.drawable.icon_category_color_2,
            R.drawable.icon_category_color_3,
            R.drawable.icon_category_color_4,
            R.drawable.icon_category_color_5,
            R.drawable.icon_category_color_6,
            R.drawable.icon_category_color_7

    };

    public static int getStarLevelImageResId(long starTotal, long replyCount){
        try {
            int level = (int)(starTotal * 2 / replyCount);
            if (level > 10){
                level = 10;
            }
            return STAR_LEVEL_RES_IDS[level];
        } catch (Exception ex){
            return STAR_LEVEL_RES_IDS[8];
        }

    }

    public static String getCategoryStr(Context context, int category){
        try {
            return context.getString(CATEGORY_NAME_IDS[category]);
        } catch (Exception ex){

        }
        return "";
    }

    public static int getCategoryColorImg(int category){
        try {
            return CATEGORY_COLOR_IMG_IDS[category];
        } catch (Exception ex){

        }
        return R.drawable.icon_category_color_0;
    }

    public static String getLengthStr(long length){
        if (length <= 0){
            return "0";
        }
        if (length < 60){
            return length + "S";
        } else {
            int min = (int)(length / 60);
            int sec = (int)(length % 60);
            return min + "m" + sec + "s";
        }
    }

    public static String getPlayCountStr(Context context, long count){
        if (count < 10000){
            return "" + count;
        } else {
            int w = (int)(count / 10000);
            int k = (int)(count % 10000 / 1000);
            return w + "." + k + context.getString(R.string.unit_w);
        }
    }

    public static void testParseData(Context context){

        String[] files;
        try {
            files = context.getResources().getAssets().list("documents");
        } catch (IOException e1) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                parseData(context, fileName);
            } catch (Exception ex){

            }
        }

    }

    public static void parseData(Context context, String fileName){

        MarketLog.w("CCAAIIParseData", "parseData start fileName = " + fileName);
        List<String> cnList = new ArrayList<String>();
        List<String> enList = new ArrayList<String>();
        String cnName = "";
        String enName = "";
        String cnAuthor = "";
        String enAuthor = "";
        try {
            InputStream input = context.getAssets().open("documents/" + fileName);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(input, "utf-8");
            int types = parser.getEventType();
            while(types != XmlPullParser.END_DOCUMENT){
                switch(types){
                    case XmlPullParser.START_DOCUMENT:
                        cnList = new ArrayList<String>();
                        enList = new ArrayList<String>();
                        break;
                    case XmlPullParser.START_TAG:
                        if("speech".equals(parser.getName())){

                        }else if("titlecn".equals(parser.getName())){
                            cnName = parser.nextText();
                        }else if("authorcn".equals(parser.getName())){
                            cnAuthor = parser.nextText();
                        }else if("titleen".equals(parser.getName())){
                            enName = parser.nextText();
                        }else if("authoren".equals(parser.getName())){
                            enAuthor = parser.nextText();
                        }else if("pcn".equals(parser.getName())){
                            cnList.add(parser.nextText());
                        }else if("pen".equals(parser.getName())){
                            enList.add(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        break;

                }
                types = parser.next();

            }
            input.close();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        MarketLog.w("CCAAIIParseData", "Create json start............");
        try {
            JSONObject json = new JSONObject();

            JSONArray contentArray = new JSONArray();
            for (int i = 0; i < enList.size(); i ++){
                JSONObject contentJson = new JSONObject();
                String cnContent = cnList.get(i);
                String enContent = enList.get(i);
                contentJson.put("time",0);
                contentJson.put("line",i);
                contentJson.put("en_content", enContent);
                contentJson.put("cn_content", cnContent);
                contentArray.put(contentJson);
            }

            json.put("content", contentArray);
            json.put("cn_title", cnName);
            json.put("en_title", enName);
            json.put("cn_author", cnAuthor);
            json.put("en_author", enAuthor);

            File dirFile = new File(FileUtils.LOCAL_FILE_PATH);
            if (!dirFile.exists()){
                dirFile.mkdirs();
            }

            FileOutputStream fout = new FileOutputStream(FileUtils.LOCAL_FILE_PATH + enName.replaceAll(" ", "_") + ".json");
            byte [] bytes = json.toString().getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception ex){
            MarketLog.e("CCAAIIParseData", "failed ex : " + ex.getLocalizedMessage());
        }
        MarketLog.w("CCAAIIParseData", "parseData end............");

    }


}
