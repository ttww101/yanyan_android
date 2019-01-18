package com.ccaaii.base.utils.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.shenghuotong.R;

public class HomeBottomButton extends RelativeLayout {
	private static final String TAG = "[CCAAII]HomeBottomButton";
	
	public HomeBottomButton(Context context) {
		super(context);
	}
	
	public HomeBottomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(attrs, context);
	}

	protected void initView(AttributeSet attrs, Context context) {
		inflate(context, R.layout.home_bottom_button_layout, this);
		setClickable(true);
		setFocusable(true);
		Drawable	drawable	= null;
		String		textValue	= null;
		if (attrs != null) {
			final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HomeBottomButton);
			drawable	= a.getDrawable(R.styleable.HomeBottomButton_home_bottom_button_icon);
			textValue	= a.getString(R.styleable.HomeBottomButton_home_bottom_button_text);
			a.recycle();

			if (LogLevel.DEV) {
				DevLog.d(TAG, "Init, textValue " + textValue);
			}
		}

		((ImageView) findViewById(R.id.home_bottom_button_image)).setImageDrawable(drawable);;
		((TextView) findViewById(R.id.home_bottom_button_text)).setText(textValue);;
	}
}
