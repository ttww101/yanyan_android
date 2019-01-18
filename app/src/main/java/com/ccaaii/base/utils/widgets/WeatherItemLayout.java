package com.ccaaii.base.utils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccaaii.base.utils.WeatherUtils;
import com.ccaaii.shenghuotong.R;

public class WeatherItemLayout extends RelativeLayout {

	private static final String TAG = "[CCAAII]WeatherItemLayout";

	private TextView topTextView;
	private TextView bottomTextView;
	private ImageView middleImageView;

	public WeatherItemLayout(Context context) {
		super(context);
	}

	public WeatherItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(attrs, context);
	}

	protected void initView(AttributeSet attrs, Context context) {
		inflate(context, R.layout.weather_item_layout, this);
		topTextView = (TextView) this.findViewById(R.id.top_textview);
		bottomTextView = (TextView) this.findViewById(R.id.bottom_textview);
		middleImageView = (ImageView) this.findViewById(R.id.middle_imageview);
	}

	public void setData(String top, String bottom, String code){
		topTextView.setText(top);
		bottomTextView.setText(bottom);
		middleImageView.setImageResource(WeatherUtils.WEATHER_RES.getWeatherResByCode(code).getWeatherImgId());
	}

	public void setData(int top, String bottom, String code){
		topTextView.setText(top);
		bottomTextView.setText(bottom);
		middleImageView.setImageResource(WeatherUtils.WEATHER_RES.getWeatherResByCode(code).getWeatherImgId());
	}

	public void setData(int top, String bottom, int imgId){
		topTextView.setText(top);
		bottomTextView.setText(bottom);
		middleImageView.setImageResource(imgId);
	}
}
