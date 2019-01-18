package com.ccaaii.shenghuotong.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.DialogUtils;
import com.ccaaii.base.utils.NetworkUtils;
import com.ccaaii.base.utils.OnProgressDialogCancelListener;
import com.ccaaii.shenghuotong.CcaaiiConstants;
import com.ccaaii.shenghuotong.R;

public class SuggestionsActivity extends BaseActivity {
	
	private static final String TAG = "[CCAAII]SuggestionsActivity";
	
	private ImageButton mBackButton;
	private EditText mReplyContentText;
	private EditText mReplyContactText;
	private Button mSubmitButton;
	
	private Dialog mProcessDialog;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(LogLevel.DEV){
			DevLog.i(TAG, "onCreate...");
		}
		this.setContentView(R.layout.suggestions_layout, true);
		buildLayout();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(LogLevel.DEV){
			DevLog.i(TAG, "onResume...");
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
		if(LogLevel.DEV){
			DevLog.i(TAG, "onStart...");
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(LogLevel.DEV){
			DevLog.i(TAG, "onPause...");
		}

	}
	
	@Override
	public void finish(){
		super.finish();
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(LogLevel.DEV){
			DevLog.i(TAG, "onDestroy...");
		}
		String content = mReplyContentText.getEditableText().toString();
		String contact = mReplyContactText.getEditableText().toString();
		SharedPreferences setting = getSharedPreferences(CcaaiiConstants.CCAAII_SHARED_PREFERENCES_CONSTANTS, 0);
		setting.edit().
			putString(CcaaiiConstants.FEEDBACK_DRAFT_CONTENT, TextUtils.isEmpty(content) ? "" : content).
			putString(CcaaiiConstants.FEEDBACK_DRAFT_CONTACT, TextUtils.isEmpty(contact) ? "" : contact).commit();
		
	}
	
	private void buildLayout(){
		mBackButton = (ImageButton) this.findViewById(R.id.button_back);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		mReplyContentText = (EditText) this.findViewById(R.id.reply_content);
		mReplyContactText = (EditText) this.findViewById(R.id.reply_contacts);
		
		SharedPreferences setting = getSharedPreferences(CcaaiiConstants.CCAAII_SHARED_PREFERENCES_CONSTANTS, 0);
		String content = setting.getString(CcaaiiConstants.FEEDBACK_DRAFT_CONTENT, "");
		String contact = setting.getString(CcaaiiConstants.FEEDBACK_DRAFT_CONTACT, "");
		mReplyContentText.setText(content);
		mReplyContactText.setText(contact);
		
		mSubmitButton = (Button) this.findViewById(R.id.confirm_button);
		mSubmitButton.setEnabled(!TextUtils.isEmpty(content));
		mReplyContentText.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				mSubmitButton.setEnabled(!TextUtils.isEmpty(s.toString()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
		});
		
		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!NetworkUtils.isAvailable(SuggestionsActivity.this)){
					DialogUtils.showCooldingAlertDialog(SuggestionsActivity.this, R.string.cannot_submit, R.string.call_error_message_network_error);
					return;
				}
				submitSuggestions(SuggestionsActivity.this, mReplyContentText.getText().toString(), mReplyContactText.getText().toString());
			}
		});
	}
	
	private void submitSuggestions(Context context, String content, String contact){
		if(LogLevel.MARKET){
			MarketLog.d(TAG, "start to submit suggestions");
		}

		mProcessDialog = DialogUtils.showProgressDialog(context, R.string.submit_points, true, new OnProgressDialogCancelListener(){

			@Override
			public void onProgressDialogCancel() {
			}
			
		});

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mProcessDialog.dismiss();
				mReplyContactText.setText("");
				mReplyContentText.setText("");
				showToast(R.string.submit_suggestion_success, Toast.LENGTH_SHORT);
				finish();
			}
		}, 1000);
		
	}
	
	
}
