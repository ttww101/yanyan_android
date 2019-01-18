package com.ccaaii.shenghuotong.document;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.CharUtils;
import com.ccaaii.base.utils.FileUtils;
import com.ccaaii.shenghuotong.CcaaiiApp;
import com.ccaaii.shenghuotong.CcaaiiConstants;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.bean.DocumentBean;

import java.io.File;
import java.util.ArrayList;

/**
 */
public class DocumentDetailActivity extends BaseActivity {

    private static final String TAG = "[CCAAII]DocumentDetailActivity";

    private static final int DEFAULT_LIMIT_COUNT = 10;

    public static final int FONT_MAX_SIZE = 40;
    public static final int FONT_MIN_SIZE = 10;
    private int mCurrentFontSize = 14;

    private int MSG_LOAD_COMPLETED = 0;
    private int MSG_DOWNLOAD_FAILED = 1;

    private DocumentBean mDocument;
    private String mDocumentID;

    private ScrollView mSpeechDetailScrollView;
    private TextView mNameView;
    private ImageView mStarLevelImageView;
    private TextView mCategoryView;
    private TextView mPlayCountView;
    private TextView mContentView;
    private View mQueryView;

    private ImageButton mFontMinBtn;
    private ImageButton mFontMaxBtn;

    private LoadHandler mLoadHandler;

    private LayoutInflater mInflater;

    private int mCurrentReplyIndex = 0;

    private TextView mTitleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MarketLog.i(TAG, "onCreate");

        setContentView(R.layout.document_detail_layout, true);
        if (getIntent() == null){
            MarketLog.e(TAG, "getIntent is null.");
            finish();
            return;
        }

        mDocumentID = getIntent().getStringExtra(CcaaiiConstants.EXTRA_DOCUMENT_ID);
        if (TextUtils.isEmpty(mDocumentID)){
            MarketLog.e(TAG, "mDocumentID is null.");
            finish();
            return;
        }

        mDocument = DocumentDAO.getDocument(CcaaiiApp.getCcaaiiContext(), mDocumentID);

        mLoadHandler = new LoadHandler();
        mInflater = LayoutInflater.from(this);

        mCurrentReplyIndex = 0;
        mCurrentFontSize = 14;

        buildLayout();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MarketLog.i(TAG, "onDestroy");

        if (mLoadHandler != null){
            mLoadHandler.removeMessages(MSG_LOAD_COMPLETED);
            mLoadHandler = null;
        }

    }

    private void buildLayout(){
        this.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSpeechDetailScrollView = (ScrollView) this.findViewById(R.id.speech_detail_scrollview);
        mNameView = (TextView) this.findViewById(R.id.name_textview);
        mStarLevelImageView = (ImageView) this.findViewById(R.id.star_level_imageview);
        mCategoryView = (TextView) this.findViewById(R.id.category_textview);
        mPlayCountView = (TextView) this.findViewById(R.id.play_count_textview);
        mContentView = (TextView) this.findViewById(R.id.content_textview);
        mQueryView = this.findViewById(R.id.query_content_view);

        mFontMinBtn = (ImageButton) this.findViewById(R.id.control_button_font_min);
        mFontMaxBtn = (ImageButton) this.findViewById(R.id.control_button_font_max);

        mContentView.setTextSize(mCurrentFontSize);

        mFontMinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFontSize(false);
            }
        });

        mFontMaxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFontSize(true);
            }
        });
    }



    private void changeFontSize(boolean add){

        mFontMinBtn.setEnabled(true);
        mFontMaxBtn.setEnabled(true);
        if (add){
            mCurrentFontSize += 2;
        } else {
            mCurrentFontSize -= 2;
        }
        if (mCurrentFontSize <= FONT_MIN_SIZE){
            mFontMinBtn.setEnabled(false);
        }
        if (mCurrentFontSize >= FONT_MAX_SIZE){
            mFontMaxBtn.setEnabled(false);
        }

        mContentView.setTextSize(mCurrentFontSize);

    }


    private class LoadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null || isFinishing()){
                return;
            }
            if (msg.what == MSG_LOAD_COMPLETED){
                mQueryView.setVisibility(View.GONE);
                try {
                    String content = (String) msg.obj;
                    mContentView.setText(content);
                } catch (Exception ex){

                }
            } else if (msg.what == MSG_DOWNLOAD_FAILED){
                mQueryView.setVisibility(View.GONE);
                showToast(R.string.loading_failed, Toast.LENGTH_SHORT);
            }
        }
    }

    public void startLoadingContent(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mDocument == null){
                        return;
                    }
                    String content = CharUtils.getFromAssetsWithLine(CcaaiiApp.getCcaaiiContext(), "documents/" + mDocumentID + ".txt");
                    if(mLoadHandler == null || isFinishing()){
                        return;
                    }
                    Message msg = mLoadHandler.obtainMessage();
                    msg.what = MSG_LOAD_COMPLETED;
                    msg.obj = content;
                    mLoadHandler.sendMessage(msg);
                } catch (Exception ex){
                    MarketLog.e(TAG, "startLoadingContent failed, ex : " + ex.getLocalizedMessage());
                    if(mLoadHandler == null || isFinishing()){
                        return;
                    }
                    Message msg = mLoadHandler.obtainMessage();
                    msg.what = MSG_DOWNLOAD_FAILED;
                    mLoadHandler.sendMessage(msg);
                }
            }
        });

        thread.start();
    }

    public void refreshView(){
        if (mDocument == null){
            return;
        }

        mNameView.setText(mDocument.getName());
        mStarLevelImageView.setImageResource(R.drawable.image_star_10);
        mCategoryView.setText(this.getString(R.string.speech_detail_category, DocumentUtils.getCategoryStr(this, mDocument.getCategory())));
        mPlayCountView.setText(this.getString(R.string.speech_detail_playcount, DocumentUtils.getPlayCountStr(this, mDocument.getPlaycount())));

        startLoadingContent();

    }

}
