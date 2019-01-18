package com.ccaaii.shenghuotong.web;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.NetworkUtils;
import com.ccaaii.shenghuotong.R;
import com.flurry.android.FlurryAgent;

import java.io.File;

/**
 */
public class KeziWebViewActivity extends BaseActivity {

    private static final String TAG = KeziWebViewActivity.class.getSimpleName();
    private TextView mTitleView;
    private ViewGroup titleLayout;
    private WebView webView;
    private LinearLayout webViewPlaceHolder;
    private ProgressBar pb;
    private Toolbar toolbar;
    private ImageButton homeIB;
    private ImageButton forwardIB;
    private ImageButton backIB;
    private ImageButton refreshIB;
    private String homePage;
    private ViewGroup toobarLayout;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private final static int permission_request_camera = 101;
    private Uri mCapturedImageURI = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview_kezi, true);

        homePage = getIntent() != null ? getIntent().getStringExtra("url") : null;
        if (TextUtils.isEmpty(homePage)){
            MarketLog.e(TAG, "url is null.");
            finish();
            return;
        }

        this.homeIB = (ImageButton) findViewById(R.id.tool_home);
        if (this.homeIB != null) {
            this.homeIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (webView != null) {
                        //webView.loadUrl("http://baidu.com");
                        webView.loadUrl(homePage);
                    }
                }
            });

        }
        this.forwardIB = (ImageButton) findViewById(R.id.tool_forward);
        if (this.forwardIB != null) {
            this.forwardIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (webView != null && webView.canGoForward()) {
                        webView.goForward();
                    } else {
                        Toast.makeText(KeziWebViewActivity.this, "不能在前进了", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        this.backIB = (ImageButton) findViewById(R.id.tool_back);
        if (this.backIB != null) {
            this.backIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (webView != null && webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        Toast.makeText(KeziWebViewActivity.this, "不能在后退了", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        this.refreshIB = (ImageButton) findViewById(R.id.tool_refresh);
        if (this.refreshIB != null) {
            this.refreshIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (webView != null && !TextUtils.isEmpty(webView.getUrl())) {
                        if (webView.getUrl().contains("live800")) {

                        } else {
                            webView.reload();
                        }

                    }
                }
            });

        }

        this.pb = (ProgressBar) findViewById(R.id.webProgressBar);
        setWebView();

        if (!NetworkUtils.isAvailable(this)){
            String data = KeziWebViewActivity.this.getString(R.string.web_page_error);
            webView.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            return;
        }
        webView.loadUrl(homePage);

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
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();

    }


    private void setWebView() {

//        webViewPlaceHolder = (LinearLayout) this.findViewById(R.id.webViewPlaceholder);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        webView = new WebView(WebShowActivity.this);
//        webView.setLayoutParams(params);
//        webView.requestFocusFromTouch();
//        webViewPlaceHolder.addView(webView);
        webView = (WebView) this.findViewById(R.id.content_webview);
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);


//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //优先使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pb.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                pb.setVisibility(View.VISIBLE);
                Log.i(TAG,"===========url="+url);
                if (url.startsWith("http") || url.startsWith("https")) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
                if (url.startsWith("intent://")) {
                    try {
                        Context context = view.getContext();
                        Intent intent = new Intent().parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            view.stopLoading();
                            PackageManager packageManager = context.getPackageManager();
                            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (info != null) {
                                context.startActivity(intent);
                            } else {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                context.startActivity(browserIntent);
                            }

                            return true;
                        }
                    } catch (Exception e) {
                    }
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed();
                pb.setVisibility(View.GONE);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                pb.setVisibility(View.GONE);
            }


        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    pb.setProgress(newProgress);
                } else {

                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        });

        webView.loadUrl(homePage);
        webView.requestFocus();
        FlurryAgent.logEvent("url=" + homePage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if(resultCode==Activity.RESULT_OK) {
                if (null == uploadMessage && null == uploadMessageAboveL) return;
                if (data == null) {
                    data = new Intent();
                    data.setData(mCapturedImageURI);
                }
                if (uploadMessageAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, data);
                } else if (uploadMessage != null) {
                    try {
                        if (data.getData().getScheme().contains("content")) {
                            String fileUri = getFilePathFromContentUri(data.getData(), getContentResolver());
                            uploadMessage.onReceiveValue(Uri.parse(fileUri));
                        } else {
                            uploadMessage.onReceiveValue(data.getData());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }else{
                if(uploadMessage!=null){
                    uploadMessage.onReceiveValue(null);
                }
                if(uploadMessageAboveL!=null){
                    uploadMessageAboveL.onReceiveValue(null);
                }
            }
            uploadMessage=null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }else{

            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        if(filePath!=null&&!filePath.startsWith("file")){
            return "file://"+filePath;
        }
        return filePath;
    }
    private void openImageChooserActivity() {
        if (ContextCompat.checkSelfPermission(KeziWebViewActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

        }else {
            ActivityCompat.requestPermissions(KeziWebViewActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    permission_request_camera);
        }
        try{
            File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidExampleFolderShenghuotong");
            if (!imageStorageDir.exists()) {
                imageStorageDir.mkdirs();
            }
            File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
            mCapturedImageURI = Uri.fromFile(file); // save to the private variable

            final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            Intent chooserIntent = Intent.createChooser(i, "拍照或选择文件上传");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[] { captureIntent });

            startActivityForResult(chooserIntent, FILE_CHOOSER_RESULT_CODE);
        }
        catch(Exception e){
            Toast.makeText(getBaseContext(), "Camera Exception:"+e, Toast.LENGTH_LONG).show();
        }
    }

    private long mBackTime = 0;
    @Override
    public void onBackPressed() {
        if (mBackTime == 0){
            mBackTime = System.currentTimeMillis();
            showToast(R.string.toast_back_again_to_finish, Toast.LENGTH_SHORT);
        } else {
            if (System.currentTimeMillis() - mBackTime <= 1000){
                super.onBackPressed();
            } else {
                showToast(R.string.toast_back_again_to_finish, Toast.LENGTH_SHORT);
                mBackTime = System.currentTimeMillis();
            }
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
}
