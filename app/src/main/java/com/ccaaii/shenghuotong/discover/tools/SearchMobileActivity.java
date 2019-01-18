package com.ccaaii.shenghuotong.discover.tools;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.utils.DialogUtils;
import com.ccaaii.base.utils.OnProgressDialogCancelListener;
import com.ccaaii.shenghuotong.CcaaiiApp;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.api.ApiUtils;
import com.ccaaii.shenghuotong.api.IResponseListener;
import com.ccaaii.shenghuotong.bean.Finance;
import com.ccaaii.shenghuotong.bean.Mobile;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

/**
 */
public class SearchMobileActivity extends BaseActivity {

    private static final String TAG = "[CCAAII]SearchMobileActivity";

    private EditText mEditText;
    private Button mSearchButton;
    private ImageButton mClearButton;
    private TextView mResultView;

    private Dialog mProcessDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onCreate...");
        }
        this.setContentView(R.layout.tools_mobile_layout, true);

        buildLayout();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onResume...");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onStart...");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onPause...");
        }

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onDestroy...");
        }
    }

    private void buildLayout() {
        this.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mEditText = (EditText) this.findViewById(R.id.search_edit_text);
        mClearButton = (ImageButton) this.findViewById(R.id.button_clear);
        mSearchButton = (Button) this.findViewById(R.id.button_search);
        mResultView = (TextView) this.findViewById(R.id.search_result_textview);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edit = editable.toString();
                if (TextUtils.isEmpty(edit)){
                    mClearButton.setVisibility(View.GONE);
                    mSearchButton.setEnabled(false);
                } else {
                    mClearButton.setVisibility(View.VISIBLE);
                    mSearchButton.setEnabled(true);
                }
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.getEditableText().clear();
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

    }

    private void search(){
        final String mobile = mEditText.getEditableText().toString();
        if (TextUtils.isEmpty(mobile)){
            return;
        }
        mProcessDialog = DialogUtils.showProgressDialog(this, R.string.search_points, true, new OnProgressDialogCancelListener() {
            @Override
            public void onProgressDialogCancel() {

            }
        });

        ApiUtils.getMobileDetail(this, mobile, new IResponseListener() {
            @Override
            public void onResponseResult(boolean success, int code, String msg) {
                try {
                    mProcessDialog.dismiss();
                } catch (Exception ex){

                }

                if (success){
                    Mobile mobileBean = CcaaiiApp.getGson().fromJson(msg,  new TypeToken<Mobile>() {}.getType());
                    if (mobileBean != null && mobileBean.getSuccess().equals("1") && mobileBean.getResult() != null && mobileBean.getResult().getStatus().equals("ALREADY_ATT")){
                        mResultView.setText(SearchMobileActivity.this.getString(R.string.search_result_mobile, mobile, mobileBean.getResult().getOperators(), mobileBean.getResult().getAtt(), mobileBean.getResult().getCtype()));
                    } else {
                        mResultView.setText(R.string.toast_search_failed);
                    }
                } else {
                    mResultView.setText(R.string.toast_search_failed);
                    showToast(R.string.toast_search_failed, Toast.LENGTH_SHORT);
                }
            }
        });
    }


}