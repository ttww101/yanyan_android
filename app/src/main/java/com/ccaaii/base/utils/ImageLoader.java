package com.ccaaii.base.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.shenghuotong.R;

public class ImageLoader {

	public static final String TAG = "[CCAAII]ImageLoader";
	
	public static final int TYPE_HEAD_IMAGE           = 0;
	public static final int TYPE_ADD_IMAGE            = 2;
    public static final int TYPE_SHOW_IMAGE           = 3;
    public static final int TYPE_SHOW_BIG_IMAGE       = 4;
	public static final int TYPE_SLIDE_PICTURE_IMAGE  = 5;
	public static final int TYPE_DISCOVER_IMAGE       = 6;

	private Context mContext;
	private int mType;
	private MemoryCache mMemoryCache = new MemoryCache();
	private Map<ImageView, String> mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ExecutorService mExecutorService;
	
	private int mWidth;
	private int mHeight;

	private int default_image_res_id = R.drawable.icon_user_head_default;
	
	public ImageLoader(Context context, int type) {
		this.mContext = context;
		this.mType = type;
		init();
		mExecutorService = Executors.newFixedThreadPool(5);
	}
	
	public ImageLoader(Context context, int type, int defaultImageResId) {
		this.mContext = context;
		this.mType = type;
		init();
		this.default_image_res_id = defaultImageResId;
		mExecutorService = Executors.newFixedThreadPool(5);
	}
	
	public ImageLoader(Context context, int type, int width, int height) {
		this.mContext = context;
		this.mType = type;
		this.mWidth = width;
		this.mHeight = height;
		
		if(mWidth <= 0 || mHeight <= 0){
			init();
		}
		
		mExecutorService = Executors.newFixedThreadPool(5);
	}
	
	private void init(){
		if (mType == TYPE_HEAD_IMAGE){
			default_image_res_id = R.drawable.icon_user_head_default;
		} else if (mType == TYPE_SHOW_BIG_IMAGE) {
            default_image_res_id = R.drawable.bg_show_big_image;
		} else if (mType == TYPE_DISCOVER_IMAGE){
			default_image_res_id = R.drawable.icon_home_empty;
		}
		
	}

	public void displayImage(String fileUrl, ImageView imageView) {
		mImageViews.put(imageView, fileUrl);
		Bitmap bitmap = mMemoryCache.get(fileUrl);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(fileUrl, imageView);
			imageView.setImageResource(default_image_res_id);
			
		}
	}

	public void displayImage(String fileUrl, ImageView imageView, View loadingView) {
		mImageViews.put(imageView, fileUrl);
		Bitmap bitmap = mMemoryCache.get(fileUrl);
		if (bitmap != null){
			loadingView.setVisibility(View.GONE);
			imageView.setImageBitmap(bitmap);
		} else {
			loadingView.setVisibility(View.VISIBLE);
			queuePhoto(fileUrl, imageView, loadingView);
			imageView.setImageResource(default_image_res_id);

		}
	}
	
	private void queuePhoto(String fileUrl, ImageView imageView) {
		final PhotoToLoad p = new PhotoToLoad(fileUrl, imageView);
		mExecutorService.submit(new PhotosLoader(p));
	}

	private void queuePhoto(String fileUrl, ImageView imageView, View loadingView) {
		final PhotoToLoad p = new PhotoToLoad(fileUrl, imageView, loadingView);
		mExecutorService.submit(new PhotosLoader(p));
	}
	
	private Bitmap readBitmapFromServerOrLocal(String fileUrl) {
		if(LogLevel.DEV){
			DevLog.d(TAG, "readBitmapFromServerOrLocal, fileUrl : " + fileUrl);
		}
		if(TextUtils.isEmpty(fileUrl)){
			return null;
		}
		if (mType == TYPE_HEAD_IMAGE){
			Bitmap bitmap = DownloadFilesUtils.getHeadImage(mContext, fileUrl);
			if (bitmap != null){
				bitmap = BitmapUtils.toRoundBitmap(bitmap);
			}
			return bitmap;
		}  else if (mType == TYPE_DISCOVER_IMAGE){
			Bitmap bitmap = DownloadFilesUtils.getHeadImage(mContext, fileUrl);
			if (bitmap != null){
				bitmap = BitmapUtils.getBitmapThumbnail(bitmap, 120, 90);
			}
			return bitmap;
		} else if (mType == TYPE_ADD_IMAGE){
            if (fileUrl.startsWith("http")){
                Bitmap bitmap = DownloadFilesUtils.getCooldingImage(mContext, fileUrl);
                if (bitmap != null){
					bitmap = BitmapUtils.getBitmapThumbnail(bitmap, (float)0.5);
                    bitmap = BitmapUtils.zoomImgByWidth(bitmap, DensityUtils.dp_px(100));
                }
                return  bitmap;
            }
			return BitmapUtils.getSmallBitmap(fileUrl);
		} else if (mType == TYPE_SHOW_IMAGE){
            Bitmap bitmap = DownloadFilesUtils.getCooldingImage(mContext, fileUrl);
            if (bitmap != null){
				bitmap = BitmapUtils.getBitmapThumbnail(bitmap, (float)0.5);
                bitmap = BitmapUtils.zoomImgByWidth(bitmap, DensityUtils.dp_px(100));
            }
            return  bitmap;
        } else if (mType == TYPE_SHOW_BIG_IMAGE){
            Bitmap bitmap = DownloadFilesUtils.getCooldingImage(mContext, fileUrl);
            return bitmap;
        } else if (mType == TYPE_SLIDE_PICTURE_IMAGE){
			Bitmap bitmap = DownloadFilesUtils.getCooldingSlidePictureImage(mContext, fileUrl);
//			if (bitmap == null){
//				bitmap = BitmapUtils.readBitMap(mContext, R.drawable.banner1);
//			}
			if (bitmap != null){
				bitmap = BitmapUtils.getBitmapThumbnail(bitmap, (float)0.8);
			}
			return bitmap;
		}
		
		return null;
		
	}
	
	private class PhotoToLoad {
		public String fileUrl;
		public ImageView imageView;
		public View loadingView;

		public PhotoToLoad(String file_url, ImageView image_view) {
			this.fileUrl = file_url;
			this.imageView = image_view;
		}

		public PhotoToLoad(String file_url, ImageView image_view, View loading_view) {
			this.fileUrl = file_url;
			this.imageView = image_view;
			this.loadingView = loading_view;
		}

	}


	private class PhotosLoader implements ImageLoaderRunnable {
		PhotoToLoad photoToLoad;

		String mTag;
		
		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
			this.mTag = "PhotosLoader : " + photoToLoad.fileUrl;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad)){
				return;
			}
			Bitmap bmp = readBitmapFromServerOrLocal(photoToLoad.fileUrl);

			mMemoryCache.put(photoToLoad.fileUrl, bmp);
			if (imageViewReused(photoToLoad)){
				return;
			}
			final BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}

		@Override
		public String getTAG() {
			return mTag;
		}
	}
	
	private boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = mImageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.fileUrl)){
			return true;
		}
		return false;
	}
	
	private class BitmapDisplayer implements ImageLoaderRunnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;
		String mTag;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
			mTag = TAG + p.fileUrl;
		}

		public void run() {
			if (photoToLoad.loadingView != null){
				photoToLoad.loadingView.setVisibility(View.GONE);
			}
			if (imageViewReused(photoToLoad)){
				return;
			}
			if (bitmap != null){
				photoToLoad.imageView.setImageBitmap(bitmap);
			} else {
				photoToLoad.imageView.setImageResource(default_image_res_id);
			}
		}

		@Override
		public String getTAG() {
			return mTag;
		}
	}
	
	public Bitmap getBitmapFromCache(String keyName){
		return mMemoryCache.get(keyName);
	}

	private interface ImageLoaderRunnable extends Runnable{
		public String getTAG();
	}
	
	public void clearCache(List<String> keyList) {
		mMemoryCache.clear(keyList);
		List<Runnable> list = mExecutorService.shutdownNow();
		if(LogLevel.DEV){
			if(list != null && list.size() > 0){
				for(Runnable runnable : list){
					try {
						ImageLoaderRunnable imageLoaderRunnable = (ImageLoaderRunnable) runnable;
						DevLog.w(TAG, "shutdown runnable : " + imageLoaderRunnable.getTAG());
					} catch (Exception e){
						DevLog.w(TAG, "shutdown runnable : " + runnable.toString());
					}
					
				}
			}
		}
		
	}

}
