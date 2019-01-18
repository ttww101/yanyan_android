package com.ccaaii.shenghuotong.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.FileUtils;
import com.ccaaii.base.utils.NetworkUtils;
import com.ccaaii.shenghuotong.CcaaiiConstants;
import com.ccaaii.shenghuotong.R;

/**
 */
public class WebShowActivity extends BaseActivity{

    private static final String TAG = "[CCAAII]WebShowActivity";

    private String mUrl;
    private int mJSEnabled;

    private TextView mTitleView;
    private WebView mWebView;

    private ProgressBar mLoadingBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MarketLog.i(TAG, "onCreate");

        mUrl = getIntent() != null ? getIntent().getStringExtra(CcaaiiConstants.EXTRA_WEB_URL) : null;
        mJSEnabled = getIntent() != null ? getIntent().getIntExtra(CcaaiiConstants.EXTRA_JS_ENABLED, 1) : 1;
        if (TextUtils.isEmpty(mUrl)){
            MarketLog.e(TAG, "url is null.");
            finish();
            return;
        }

        boolean noTopBar = getIntent().getBooleanExtra(CcaaiiConstants.EXTRA_NO_TOP_BAR, false);

        setContentView(noTopBar ? R.layout.web_show_layout_no_top : R.layout.web_show_layout, true);



        String name = getIntent().getStringExtra(CcaaiiConstants.EXTRA_WEB_TITLE);

        MarketLog.i(TAG, "Load url : " + mUrl);
        mTitleView = (TextView) this.findViewById(R.id.title_view);
        if (!TextUtils.isEmpty(name)){
            mTitleView.setText(name);
        }

        this.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWebView = (WebView) this.findViewById(R.id.web_view);
        mWebView.requestFocusFromTouch();
        mLoadingBar = (ProgressBar)this.findViewById(R.id.loading_progressbar);
        mLoadingBar.setVisibility(View.VISIBLE);

        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setAllowContentAccess(true);
//        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(FileUtils.getCooldingLogPath());
//        webSettings.setCacheMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setNeedInitialFocus(true);
        webSettings.setJavaScriptEnabled(mJSEnabled == 1);
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                MarketLog.w(TAG, "onPageStarted");
                if (mLoadingHandler == null){
                    return;
                }
                mLoadingHandler.removeMessages(MSG_LOADING_ADD);
                Message msg = mLoadingHandler.obtainMessage();
                msg.what = MSG_LOADING_START;
                mLoadingHandler.sendMessage(msg);

            }



            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                MarketLog.w(TAG, "onPageFinished");
                if (mLoadingHandler == null){
                    return;
                }
                mLoadingHandler.removeMessages(MSG_LOADING_ADD);
                Message msg = mLoadingHandler.obtainMessage();
                msg.what = MSG_LOADING_COMPLETED;
                mLoadingHandler.sendMessage(msg);

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                MarketLog.w(TAG, "onReceivedSslError");
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                String data = WebShowActivity.this.getString(R.string.web_page_error);
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                String data = WebShowActivity.this.getString(R.string.web_page_error);
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            }

        });

        if (!NetworkUtils.isAvailable(this)){
            String data = WebShowActivity.this.getString(R.string.web_page_error);
            mWebView.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            return;
        }
        mWebView.loadUrl(mUrl);

    }

    private class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mLoadingHandler == null){
                return;
            }

            if (newProgress > 100){
                return;
            }
            mLoadingHandler.removeMessages(MSG_LOADING_ADD);
            Message msg = mLoadingHandler.obtainMessage();
            msg.what = MSG_LOADING_ADD;
            msg.arg1 = newProgress;
            mLoadingHandler.sendMessage(msg);
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MarketLog.i(TAG, "onDestroy");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private static final int MSG_LOADING_START = 0;
    private static final int MSG_LOADING_ADD = 1;
    private static final int MSG_LOADING_COMPLETED = 2;
    private static final int MSG_LOADING_HIDDEN = 3;

    private void hideLoadingProgress(){
        if (mLoadingHandler == null){
            return;
        }
        mLoadingHandler.removeMessages(MSG_LOADING_ADD);
        Message msg = mLoadingHandler.obtainMessage();
        msg.what = MSG_LOADING_HIDDEN;
        mLoadingHandler.sendMessageDelayed(msg, 500);
    }

    private Handler mLoadingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_LOADING_START){
                mLoadingBar.setMax(100);
                mLoadingBar.setProgress(0);
                mLoadingBar.setVisibility(View.VISIBLE);
            } else if (msg.what == MSG_LOADING_ADD){
                int progress = msg.arg1;
                mLoadingBar.setProgress(progress);
            } else if (msg.what == MSG_LOADING_COMPLETED){
                mLoadingBar.setProgress(100);
                hideLoadingProgress();
            } else if (msg.what == MSG_LOADING_HIDDEN){
                mLoadingBar.setVisibility(View.GONE);
            }
        }
    };
}
