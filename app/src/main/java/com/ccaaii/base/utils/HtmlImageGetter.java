package com.ccaaii.base.utils;

/**
 */

import java.io.File;
import java.io.InputStream;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.TextView;


public class HtmlImageGetter implements ImageGetter {
    private TextView _htmlText;
    private Drawable _defaultDrawable;
    private String TAG = "[CCAAII]HtmlImageGetter";

    public HtmlImageGetter(TextView htmlText, Drawable defaultDrawable) {
        _htmlText = htmlText;
        _defaultDrawable = defaultDrawable;
    }

    @Override
    public Drawable getDrawable(String imgUrl) {

        String imgKey = String.valueOf(imgUrl.hashCode());

        String[] ss = imgUrl.split("\\.");
        String imgX = ss[ss.length - 1];
        imgKey = FileUtils.getCooldingImagePath() + imgKey + "." + imgX;

        File file = new File(imgKey);
        if (file.exists()) {
            Drawable drawable = FileUtils.getImageDrawable(imgKey);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            } else {
                Log.v(TAG, "load img:" + imgKey + ":null");
            }
        }

        URLDrawable urlDrawable = new URLDrawable(_defaultDrawable);
        new AsyncThread(urlDrawable).execute(imgKey, imgUrl);
        return urlDrawable;
    }

    private class AsyncThread extends AsyncTask<String, Integer, Drawable> {
        private String imgKey;
        private URLDrawable _drawable;

        public AsyncThread(URLDrawable drawable) {
            _drawable = drawable;
        }

        @Override
        protected Drawable doInBackground(String... strings) {
            imgKey = strings[0];
            InputStream inps = NetWork.getInputStream(strings[1]);
            if (inps == null) return _drawable;

            FileUtils.write2SDFromInput(imgKey, inps);
            Drawable drawable = Drawable.createFromPath(imgKey);
            return drawable;
        }

        public void onProgressUpdate(Integer... value) {

        }

        @Override
        protected void onPostExecute(Drawable result) {
            _drawable.setDrawable(result);
            _htmlText.setText(_htmlText.getText());
        }
    }

    public class URLDrawable extends BitmapDrawable {

        private Drawable drawable;

        public URLDrawable(Drawable defaultDraw) {
            setDrawable(defaultDraw);
        }

        private void setDrawable(Drawable ndrawable) {
            drawable = ndrawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());
            setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());
        }

        @Override
        public void draw(Canvas canvas) {
            drawable.draw(canvas);
        }
    }
}