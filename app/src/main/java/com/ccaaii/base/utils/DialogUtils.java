/**
 * Copyright (C) 2013, Easiio, Inc.
 * All Rights Reserved.
 */
package com.ccaaii.base.utils;

import com.ccaaii.base.utils.widgets.CcaaiiAlertDialog;
import com.ccaaii.shenghuotong.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogUtils {

	public static void showCooldingAlertDialog(Context context, String title, String message){
		
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(R.drawable.icon_dialog_title);
		builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.show();
			setDialogWidth(context, dialog);
		} catch (Exception e){
			
		}
		
	
	}
	public static void showCooldingAlertDialog(Context context, int title, int message){
		
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(R.drawable.icon_dialog_title);
		builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.show();
			setDialogWidth(context, dialog);
		} catch (Exception e){
			
		}
	}
	
	public interface OnClickConfirmButtonListener{
		public void onClick();
	}
	
	public interface OnClickCancelButtonListener{
		public void onClick();
	}
	
	public static void showCooldingAlertDialog(Context context, int title, int message, final OnClickConfirmButtonListener confirmListner){
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(R.drawable.icon_dialog_title);
		builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(confirmListner != null){
					confirmListner.onClick();
				}
			}
		});
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.show();
			dialog.setCancelable(false);
			setDialogWidth(context, dialog);
		} catch (Exception e){
			
		}
	}
	
	public static void showCooldingAlertDialog(Context context, int title, int message, final OnClickConfirmButtonListener confirmListner, final OnClickCancelButtonListener cancelListener){
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(R.drawable.icon_dialog_title);
		builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(confirmListner != null){
					confirmListner.onClick();
				}
				
			}
		});
		builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(cancelListener != null){
					cancelListener.onClick();
				}
				
			}
		});
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.show();
			setDialogWidth(context, dialog);
		} catch (Exception e){
			
		}
	}
	
	public static void showCooldingAlertDialog(Context context, int title, int message, boolean cancelable, final OnClickConfirmButtonListener confirmListner, final OnClickCancelButtonListener cancelListener){
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(R.drawable.icon_dialog_title);
		builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(confirmListner != null){
					confirmListner.onClick();
				}
				
			}
		});
		builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(cancelListener != null){
					cancelListener.onClick();
				}
				
			}
		});
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.setCancelable(cancelable);
			dialog.show();
			setDialogWidth(context, dialog);
		} catch (Exception e){
			
		}
	}
	
	public static void showCooldingAlertDialog(Context context, int title, int message, int posBtnStr, final OnClickConfirmButtonListener confirmListner, final OnClickCancelButtonListener cancelListener){
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(R.drawable.icon_dialog_title);
		builder.setPositiveButton(posBtnStr, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(confirmListner != null){
					confirmListner.onClick();
				}
			}
		});
		builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(cancelListener != null){
					cancelListener.onClick();
				}
			}
		});
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.show();
			setDialogWidth(context, dialog);
		} catch (Exception e){
			
		}
	}
	
	public static void showCooldingAlertDialog(Context context, int title, String message, int posBtnStr, final OnClickConfirmButtonListener confirmListner, final OnClickCancelButtonListener cancelListener){
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(R.drawable.icon_dialog_title);
		builder.setPositiveButton(posBtnStr, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(confirmListner != null){
					confirmListner.onClick();
				}
			}
		});
		builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(cancelListener != null){
					cancelListener.onClick();
				}
			}
		});
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.show();
			setDialogWidth(context, dialog);
		} catch (Exception e){
			
		}
	}
	
	public static void showCooldingAlertDialog(Context context, int title, String message, int posBtnStr, int negativeBtnStr, final OnClickConfirmButtonListener confirmListner, final OnClickCancelButtonListener cancelListener){
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(R.drawable.icon_dialog_title);
		builder.setPositiveButton(posBtnStr, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(confirmListner != null){
					confirmListner.onClick();
				}
			}
		});
		builder.setNegativeButton(negativeBtnStr, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(cancelListener != null){
					cancelListener.onClick();
				}
			}
		});
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.show();
			setDialogWidth(context, dialog);
		} catch (Exception e){
			
		}
	}
	
	public static void showCooldingAlertDialog(Context context, int title, String message, int posBtnStr, int negativeBtnStr, boolean cancelable, final OnClickConfirmButtonListener confirmListner, final OnClickCancelButtonListener cancelListener){
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(R.drawable.icon_dialog_title);
		builder.setPositiveButton(posBtnStr, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(confirmListner != null){
					confirmListner.onClick();
				}
			}
		});
		builder.setNegativeButton(negativeBtnStr, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(cancelListener != null){
					cancelListener.onClick();
				}
			}
		});
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.setCancelable(cancelable);
			dialog.show();
			setDialogWidth(context, dialog);
		} catch (Exception e){
			
		}
	}
	
	public static void showCooldingAlertDialog(Context context, Object title, Object message) {
		showCooldingAlertDialog(context, title, message, null, null, null, null);
	}
	
	public static void showCooldingAlertDialog(Context context, Object title, Object message,
			Object posBtnStr, final OnClickConfirmButtonListener confirmListner,
			Object negBtnStr, final OnClickCancelButtonListener cancelListener) {
		CcaaiiAlertDialog.Builder builder = new CcaaiiAlertDialog.Builder(context);
		if (title instanceof Integer) {
			builder.setTitle((Integer) title);
		} else if (title instanceof String) {
			builder.setTitle((String) title);
		} else {
			return;
		}
		if (message instanceof Integer) {
			builder.setMessage((Integer) message);
		} else if (message instanceof String) {
			builder.setMessage((String) message);
		} else {
			return;
		}
		int positiveStr;
		int negativeStr;
		if (posBtnStr instanceof Integer) {
			positiveStr = (Integer) posBtnStr;
		} else {
			positiveStr = R.string.dialog_ok;
		}
		if (negBtnStr instanceof Integer) {
			negativeStr = (Integer) negBtnStr;
		} else {
			negativeStr = R.string.dialog_cancel;
		}
		builder.setIcon(R.drawable.icon_dialog_title);
		if (posBtnStr != null || confirmListner != null) {
			builder.setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if(confirmListner != null){
						confirmListner.onClick();
					}
				}
			});
		}
		if (negBtnStr != null || cancelListener != null) {
			builder.setNegativeButton(negativeStr, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if(cancelListener != null){
						cancelListener.onClick();
					}
				}
			});
		}
		
		try {
			CcaaiiAlertDialog dialog = builder.create();
			dialog.show();
			setDialogWidth(context, dialog);
		} catch (Exception e){
		}
	}
	
	public static void setDialogWidth(Context context, Dialog dialog){
		Activity activity  = (Activity) context;
		if(activity == null){
			return;
		}
		WindowManager windowManager = activity.getWindowManager();  
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		int screenWidth = (int)(display.getWidth() * 0.9);
		if(screenWidth <= 0){
			screenWidth = DensityUtils.dp_px(250);
		}
		lp.width = screenWidth;
		lp.height = LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
	}
	
	public static void setDialogWidth(Activity activity, Dialog dialog){
		WindowManager windowManager = activity.getWindowManager();  
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		int screenWidth = (int)(display.getWidth() * 0.9);
		if(screenWidth <= 0){
			screenWidth = DensityUtils.dp_px(250);
		}
		lp.width = screenWidth;
		lp.height = LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
	}
	
	public static void setDialogWidthHeight(Activity activity, Dialog dialog){
		WindowManager windowManager = activity.getWindowManager();  
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		int screenWidth = (int)(display.getWidth() * 0.9);
		int screenHeight = (int)(display.getHeight() * 0.8);
		if(screenWidth <= 0){
			screenWidth = DensityUtils.dp_px(250);
		}
		if(screenHeight <= 0){
			screenHeight = DensityUtils.dp_px(400);
		}
		lp.width = screenWidth;
		lp.height = screenHeight;
		dialog.getWindow().setAttributes(lp);
	}
	
	public static void setDialogWidthHeight(Context context, Dialog dialog){
		Activity activity  = (Activity) context;
		if(activity == null){
			return;
		}
		WindowManager windowManager = activity.getWindowManager();  
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		int screenWidth = (int)(display.getWidth() * 0.9);
		int screenHeight = (int)(display.getHeight() * 0.8);
		if(screenWidth <= 0){
			screenWidth = DensityUtils.dp_px(250);
		}
		if(screenHeight <= 0){
			screenHeight = DensityUtils.dp_px(400);
		}
		lp.width = screenWidth;
		lp.height = screenHeight;
		dialog.getWindow().setAttributes(lp);
	}
	public static void setDialogWidthHeightMin(Context context, Dialog dialog){
		Activity activity  = (Activity) context;
		if(activity == null){
			return;
		}
		WindowManager windowManager = activity.getWindowManager();  
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		int screenWidth = (int)(display.getWidth() * 0.9);
		int screenHeight = (int)(display.getHeight() * 0.6);
		if(screenWidth <= 0){
			screenWidth = DensityUtils.dp_px(250);
		}
		if(screenHeight <= 0){
			screenHeight = DensityUtils.dp_px(400);
		}
		lp.width = screenWidth;
		lp.height = screenHeight;
		dialog.getWindow().setAttributes(lp);
	}
	public static void setDialogWidthBig(Context context, Dialog dialog){
		Activity activity  = (Activity) context;
		if(activity == null){
			return;
		}
		WindowManager windowManager = activity.getWindowManager();  
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		int screenWidth = (int)(display.getWidth() * 0.9);
		if(screenWidth <= 0){
			screenWidth = DensityUtils.dp_px(250);
		}
		lp.width = screenWidth;
		lp.height = LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
	}
	
	public static void setDialogWidthBig(Activity activity, Dialog dialog){
		WindowManager windowManager = activity.getWindowManager();  
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		int screenWidth = (int)(display.getWidth() * 0.9);
		if(screenWidth <= 0){
			screenWidth = DensityUtils.dp_px(250);
		}
		lp.width = screenWidth;
		lp.height = LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
	}
	
	public static Dialog showProgressDialog(Context context, int textId, boolean cancelable, final OnProgressDialogCancelListener listener){
		return showProgressDialog(context, textId, cancelable, listener, true);
	}
	
	public static Dialog showProgressDialog(Context context, int textId, boolean cancelable, final OnProgressDialogCancelListener listener, boolean isShow){
		LayoutInflater inflater = LayoutInflater.from(context);  
		View v = inflater.inflate(R.layout.dialog_progressing, null);
        TextView textView = (TextView) v.findViewById(R.id.loading_text_view);
        textView.setText(textId);
        Dialog progressdialog = new Dialog(context, R.style.loading_dialog);
        
        progressdialog.setCancelable(cancelable);
        progressdialog.setContentView(v, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));
        
        progressdialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

			@Override
			public void onCancel(DialogInterface dialog) {
				if(listener != null){
					listener.onProgressDialogCancel();
				}
				
			}
			
		});
        
        if(isShow){
        	progressdialog.show();
    		WindowManager.LayoutParams lp = progressdialog.getWindow().getAttributes();
    		int screenWidth = DensityUtils.dp_px(200);
    		lp.width = screenWidth;
    		lp.height = LayoutParams.WRAP_CONTENT;
    		progressdialog.getWindow().setAttributes(lp);
        }
        
		return progressdialog;
	}
	
	public static Dialog showProgressDialog(Context context, int textId, boolean cancelable,boolean isShow){
		LayoutInflater inflater = LayoutInflater.from(context);  
		View v = inflater.inflate(R.layout.dialog_progressing, null);
        TextView textView = (TextView) v.findViewById(R.id.loading_text_view);
        textView.setText(textId);
        Dialog progressdialog = new Dialog(context, R.style.loading_dialog);
        
        progressdialog.setCancelable(cancelable);
        progressdialog.setContentView(v, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));
        
        if(isShow){
        	progressdialog.show();
    		WindowManager.LayoutParams lp = progressdialog.getWindow().getAttributes();
    		int screenWidth = DensityUtils.dp_px(200);
    		lp.width = screenWidth;
    		lp.height = LayoutParams.WRAP_CONTENT;
    		progressdialog.getWindow().setAttributes(lp);
        }
        
		return progressdialog;
	}
	
}
