package com.ccaaii.base.utils;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 */
public class AnimationUtils {
	
	public static TranslateAnimation mLeftInAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, 
			Animation.RELATIVE_TO_SELF, 0.0f, 	Animation.RELATIVE_TO_SELF, 0.0f, 
			Animation.RELATIVE_TO_SELF,0.0f);
	public static TranslateAnimation mLeftOutAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, 	
			Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
			Animation.RELATIVE_TO_SELF, 0.0f);
	public static TranslateAnimation mRightInAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, 
			Animation.RELATIVE_TO_SELF, 0.0f, 	Animation.RELATIVE_TO_SELF, 0.0f, 
			Animation.RELATIVE_TO_SELF,0.0f);
	public static TranslateAnimation mRightOutAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, 	
			Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
			Animation.RELATIVE_TO_SELF, 0.0f);
	public static TranslateAnimation mTopInAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
			Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
			-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
	public static TranslateAnimation mBottomOutAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
			Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
			0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
	public static TranslateAnimation mTopOutAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
			Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
			0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
	public static TranslateAnimation mBottomInAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
			Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
			1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
	
	public static AlphaAnimation mAlphaHideAllAction = new AlphaAnimation(1, 0);
	public static AlphaAnimation mAlphaShowAllAction = new AlphaAnimation(0, 1);
	public static AlphaAnimation mAlphaHideHalfAction = new AlphaAnimation(1, (float)0.5);
	public static AlphaAnimation mAlphaShowHalfAction = new AlphaAnimation((float)0.5, 1);
	
	public static ScaleAnimation mScaleZoomInAction;
	public static ScaleAnimation mScaleZoomOutAction; 
	
	public static AccelerateInterpolator mAccelerateInterpolator = new AccelerateInterpolator();
	public static DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();
	public static AccelerateDecelerateInterpolator mADInterpolator = new AccelerateDecelerateInterpolator();
	
	
	public static void leftInOrLeftOutAnimation(View view, long duration, boolean isShow){
		if(isShow){
			mLeftInAction.setDuration(duration);
			mLeftInAction.setInterpolator(mDecelerateInterpolator);
			view.startAnimation(mLeftInAction);
			view.setVisibility(View.VISIBLE);
		} else {
			mLeftOutAction.setDuration(duration);
			mLeftOutAction.setInterpolator(mDecelerateInterpolator);
			view.startAnimation(mLeftOutAction);
			view.setVisibility(View.GONE);
		}
		
	}
	
	
	public static void rightInOrRightOutAnimation(View view, long duration, boolean isShow){
		TranslateAnimation rightInAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f, 	Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF,0.0f);
		TranslateAnimation rightOutAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, 	
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f);
		if(isShow){
			rightInAction.setDuration(duration);
			rightInAction.setInterpolator(mDecelerateInterpolator);
			view.startAnimation(rightInAction);
			view.setVisibility(View.VISIBLE);
		} else {
			rightOutAction.setDuration(duration);
			rightOutAction.setInterpolator(mDecelerateInterpolator);
			view.startAnimation(rightOutAction);
			view.setVisibility(View.GONE);
		}
		
	}
	
	public static void rightInOrRightOutTwoAnimation(View view, final View mainView, long duration, boolean isShow){
		TranslateAnimation rightInAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f, 	Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF,0.0f);
		TranslateAnimation rightOutAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, 	
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f);
		if(isShow){
			mainView.setVisibility(View.VISIBLE);
			rightInAction.setDuration(duration);
			rightInAction.setInterpolator(mDecelerateInterpolator);
			view.startAnimation(rightInAction);
			view.setVisibility(View.VISIBLE);
		} else {
			rightOutAction.setDuration(duration);
			rightOutAction.setInterpolator(mDecelerateInterpolator);
			rightOutAction.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					mainView.setVisibility(View.GONE);
				}
			});
			view.startAnimation(rightOutAction);
			view.setVisibility(View.GONE);
		}
		
	}
	
	
	public static void topInOrTopOutAniamtion(View view, long duration, boolean isShow){
		if(view == null){
			return;
		}
		if(isShow){
			mTopInAction.setDuration(duration);
			view.startAnimation(mTopInAction);
			view.setVisibility(View.VISIBLE);
		} else {
			mTopOutAction.setDuration(duration);
			view.startAnimation(mTopOutAction);
			view.setVisibility(View.GONE);
		}
	}
	
	public static void bottomInOrBottomOutAniamtion(View view, long duration, boolean isBottomIn){
		if(view == null){
			return;
		}
		if(isBottomIn){
			mBottomInAction.setDuration(duration);
			view.startAnimation(mBottomInAction);
			view.setVisibility(View.VISIBLE);
		} else {
			mBottomOutAction.setDuration(duration);
			view.startAnimation(mBottomOutAction);
			view.setVisibility(View.GONE);
		}
	}
	
	public static void bottomInAndAlphaIn(View view, long duration) {
		mBottomInAction.setDuration(duration);
		mBottomInAction.setInterpolator(mDecelerateInterpolator);
		mAlphaShowAllAction.setDuration(duration);
		mAlphaShowAllAction.setInterpolator(mDecelerateInterpolator);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(mBottomInAction);
		set.addAnimation(mAlphaShowAllAction);
		view.startAnimation(set);
		view.setVisibility(View.VISIBLE);
	}
	
	public static void leftInAndAlphaIn(View view, long duration){
		mLeftInAction.setDuration(duration);
		mLeftInAction.setInterpolator(mDecelerateInterpolator);
		mAlphaShowAllAction.setDuration(duration);
		mAlphaShowAllAction.setInterpolator(mDecelerateInterpolator);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(mLeftInAction);
		set.addAnimation(mAlphaShowAllAction);
		view.startAnimation(set);
		view.setVisibility(View.VISIBLE);
	}
	
	public static void rightInAndAlphaIn(View view, long duration){
		mRightInAction.setDuration(duration);
		mRightInAction.setInterpolator(mDecelerateInterpolator);
		mAlphaShowAllAction.setDuration(duration);
		mAlphaShowAllAction.setInterpolator(mDecelerateInterpolator);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(mRightInAction);
		set.addAnimation(mAlphaShowAllAction);
		view.startAnimation(set);
		view.setVisibility(View.VISIBLE);
	}
	
	public static void downHideAnimation(View view, long duration, int position){
		float fValue;
		switch(position){
		case 0:
			fValue = 1.0f;
			break;
		case 1:
			fValue = 2.0f;
			break;
		default:
			fValue = 1.0f;
			break;
		}
		TranslateAnimation bottomOutAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, fValue);
		bottomOutAction.setDuration(duration);
		mAlphaHideAllAction.setDuration(duration);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(mAlphaHideAllAction);
		set.addAnimation(bottomOutAction);
		set.setDuration(duration);
		view.startAnimation(set);
	}
	
	public static void upShowAnimation(final View view, long duration, int position){
		float fValue;
		switch(position){
		case 0:
			fValue = 1.0f;
			break;
		case 1:
			fValue = 2.0f;
			break;
		default:
			fValue = 1.0f;
			break;
		}
		TranslateAnimation topOutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				fValue, Animation.RELATIVE_TO_SELF, 0);
		topOutAnimation.setDuration(duration);
		mAlphaShowAllAction.setDuration(duration);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(mAlphaShowAllAction);
		set.addAnimation(topOutAnimation);
		set.setDuration(duration);
		set.setFillAfter(true);
		set.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				view.clearAnimation();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				
			}
			
		});
		view.startAnimation(set);
		view.setVisibility(View.VISIBLE);
	}
	
	public static void hideAndShowHalfVoipOnHoldAnimation(final View view, long duration){
		mAlphaHideHalfAction.setDuration(duration);
		mAlphaHideHalfAction.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				view.startAnimation(mAlphaShowHalfAction);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}});
		mAlphaShowHalfAction.setDuration(duration);
		view.startAnimation(mAlphaHideHalfAction);
	}
	
	public static void scaleZoomInAndShowAnimation(View view){
		mScaleZoomInAction = new ScaleAnimation(0, 1.2f, 0, 1.2f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
		mScaleZoomInAction.setDuration(500);
		mScaleZoomInAction.setFillAfter(false);
		mAlphaShowAllAction.setDuration(500);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(mScaleZoomInAction);
		set.addAnimation(mAlphaShowAllAction);
		set.setDuration(500);
		set.setInterpolator(mAccelerateInterpolator);
		view.startAnimation(set);
		view.setVisibility(View.VISIBLE);
	}
	
	public static void scaleZoomOutAndHideAnimation(View view){
		mScaleZoomOutAction = new ScaleAnimation(1.2f, 0, 1.2f, 0, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
		mScaleZoomOutAction.setDuration(800);
		mScaleZoomOutAction.setFillAfter(false);
		mAlphaHideAllAction.setDuration(800);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(mScaleZoomOutAction);
		set.addAnimation(mAlphaHideAllAction);
		set.setDuration(800);
		set.setInterpolator(mDecelerateInterpolator);
		view.startAnimation(set);
		view.setVisibility(View.INVISIBLE);
	}
	
	public static void scaleZoomInAndExpandAnimation(View view, final LinearLayout expandView, final int height){
		float expandValue = (float)height/ expandView.getMeasuredHeight();
		final TranslateAnimation bottomOutAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, expandValue);
		mScaleZoomInAction = new ScaleAnimation(0, 1.2f, 0, 1.2f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
		mScaleZoomInAction.setDuration(500);
		mAlphaShowAllAction.setDuration(500);
		bottomOutAction.setDuration(1000);
		bottomOutAction.setStartOffset(500);
		bottomOutAction.setInterpolator(mDecelerateInterpolator);
		bottomOutAction.setFillAfter(false);
		bottomOutAction.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation arg0) {
				expandView.clearAnimation();
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			    params.topMargin = height;
			    expandView.setLayoutParams(params);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				
			}
			
		});
		
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(mScaleZoomInAction);
		set.addAnimation(mAlphaShowAllAction);
		set.setDuration(500);
		set.setInterpolator(mAccelerateInterpolator);
		set.setFillAfter(false);
		set.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				expandView.startAnimation(bottomOutAction);
				expandView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
		});
		view.startAnimation(set);
		view.setVisibility(View.VISIBLE);
	}
	
	public static void alphaInOrOut(View view, boolean in, long duration){
		if (in){
			mAlphaShowAllAction.setDuration(duration);
			view.startAnimation(mAlphaShowAllAction);
			view.setVisibility(View.VISIBLE);
		} else {
			mAlphaHideAllAction.setDuration(duration);
			view.startAnimation(mAlphaHideAllAction);
			view.setVisibility(View.GONE);
		}
		
	}
	

}
