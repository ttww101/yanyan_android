package com.ccaaii.base.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.CcaaiiApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;

import org.apache.http.util.EncodingUtils;

public class FileUtils {

	private static final String TAG = "[CCAAII]FileUtils";

	private static final  int FILESIZE = 4 * 1024;

	private static final String PHOTO_FILE_NAME = "ccaaii_photo.jpg";
	private static final String UPLOAD_PHOTO_FILE_NAME = "ccaaii_upload_photo.jpg";
	private static final String COOLDING = "CCAAII";
	private static final String HEAD_IMAGE = "HeadImage";
	private static final String COOLDING_IMAGE = "CcaaiiImage";
	private static final String TAKE_PHOTO = "TakePhoto";
	private static final String COOLDING_LOG = "CcaaiiLog";
	private static final String COOLDING_FILE = "CcaaiiFile";
	private static final String COOLDING_MEDIA_FILE = "CcaaiiMediaFile";

	public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";

	public static final String LOCAL_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImmortalSpeech/";

	public static boolean isStorageAccessible() {
        File file = CcaaiiApp.getCcaaiiContext().getExternalFilesDir(null);
        if (file == null || !file.exists()){
            return false;
        }
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static String getTakePhotoCachePath() {
		return CcaaiiApp.getCcaaiiContext().getExternalFilesDir(null)
				+ File.separator + COOLDING + File.separator;
	}
	
	public static final String getHeadImagePath() {
		if (isStorageAccessible()) {
			return CcaaiiApp.getCcaaiiContext().getExternalFilesDir(null) + File.separator + HEAD_IMAGE + File.separator;
		} else {
			return CcaaiiApp.getCcaaiiContext().getFilesDir() + File.separator + HEAD_IMAGE + File.separator;
		}

	}

	public static final String getLocalFilePath() {
		if (isStorageAccessible()) {
			return CcaaiiApp.getCcaaiiContext().getExternalFilesDir(null) + File.separator + COOLDING_FILE + File.separator;
		} else {
			return CcaaiiApp.getCcaaiiContext().getFilesDir() + File.separator + COOLDING_FILE + File.separator;
		}

	}

	public static final String getCooldingMediaFilePath() {
		if (isStorageAccessible()) {
			return CcaaiiApp.getCcaaiiContext().getExternalFilesDir(null) + File.separator + COOLDING_MEDIA_FILE + File.separator;
		} else {
			return CcaaiiApp.getCcaaiiContext().getFilesDir() + File.separator + COOLDING_MEDIA_FILE + File.separator;
		}

	}

    public static final String getCooldingImagePath() {
        if (isStorageAccessible()) {
            return CcaaiiApp.getCcaaiiContext().getExternalFilesDir(null) + File.separator + COOLDING_IMAGE + File.separator;
        } else {
            return CcaaiiApp.getCcaaiiContext().getFilesDir() + File.separator + COOLDING_IMAGE + File.separator;
        }

    }

	public static final String getCooldingSlidePictureImagePath() {
		if (isStorageAccessible()) {
			return CcaaiiApp.getCcaaiiContext().getExternalFilesDir(null) + File.separator + COOLDING_IMAGE + File.separator;
		} else {
			return CcaaiiApp.getCcaaiiContext().getFilesDir() + File.separator + COOLDING_IMAGE + File.separator;
		}

	}
	
	public static String getCooldingTakePhotoImagePath() {
        if (isStorageAccessible()) {
            return CcaaiiApp.getCcaaiiContext().getExternalFilesDir(null) + File.separator + TAKE_PHOTO + File.separator;
        } else{
            return CcaaiiApp.getCcaaiiContext().getFilesDir() + File.separator + TAKE_PHOTO + File.separator;
        }
	}

	public static String getCooldingLogPath() {
		if (isStorageAccessible()) {
			return CcaaiiApp.getCcaaiiContext().getExternalFilesDir(null) + File.separator + COOLDING_LOG + File.separator;
		} else{
			return CcaaiiApp.getCcaaiiContext().getFilesDir() + File.separator + COOLDING_LOG + File.separator;
		}
	}


	public static boolean saveImageToSDCardByPath(Context context, Bitmap b, String filePath) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "saveImageToSDCardByPath start filePath = " + filePath);
		}
		if (b == null || TextUtils.isEmpty(filePath)) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "saveImageToSDCardByPath bitmap is null");
			}
			return false;
		}
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (LogLevel.DEV) {
				DevLog.d(TAG, "saveImageToSDCardByPath file path str : " + filePath);
			}

			fos = new FileOutputStream(filePath);
			if (null != fos) {
				if (filePath.endsWith("png") || filePath.endsWith("PNG")){
					b.compress(Bitmap.CompressFormat.PNG, 60, fos);
				} else {
					b.compress(Bitmap.CompressFormat.JPEG, 80, fos);

				}
				fos.flush();
				fos.close();
			}

			return true;

		} catch (Exception e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "saveImageToSDCardByPath failed : " + e.toString());
			}

			return false;
		}
	}

	public static boolean saveImageToSDCardByPath(Context context, Bitmap b, String filePath, int quality) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "saveImageToSDCardByPath start filePath = " + filePath);
		}
		if (b == null || TextUtils.isEmpty(filePath)) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "saveImageToSDCardByPath bitmap is null");
			}
			return false;
		}
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (LogLevel.DEV) {
				DevLog.d(TAG, "saveImageToSDCardByPath file path str : " + filePath);
			}

			fos = new FileOutputStream(filePath);
			if (null != fos) {
				if (filePath.endsWith("png") || filePath.endsWith("PNG")){
					b.compress(Bitmap.CompressFormat.PNG, quality, fos);
				} else {
					b.compress(Bitmap.CompressFormat.JPEG, quality, fos);

				}
				fos.flush();
				fos.close();
			}

			return true;

		} catch (Exception e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "saveImageToSDCardByPath failed : " + e.toString());
			}

			return false;
		}
	}

	public static String getFileNameByHttpUrl(String httpUrl){
		if(TextUtils.isEmpty(httpUrl)){
			return "";
		}

		String fileName = "";
		int index = httpUrl.lastIndexOf("/");
		if(index > 0){
			fileName = httpUrl.substring(index + 1);
		}

		if (fileName.indexOf(".") <= 0){
			return CharUtils.toBASE64Str(httpUrl) + ".jpg";
		}

		return fileName;
	}

	public static File getTakePhotoFile() {
		String parentFilePath = getTakePhotoCachePath();
		File parentFile = new File(parentFilePath);
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		File tempFile = new File(parentFilePath + PHOTO_FILE_NAME);
		return tempFile;
	}
	
	public static String getTakePhotoFilePath() {
		String parentFilePath = getTakePhotoCachePath();
		File parentFile = new File(parentFilePath);
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		return parentFilePath + PHOTO_FILE_NAME;
	}
	
	public static String getUploadTakePhotoFilePath() {
		String parentFilePath = getTakePhotoCachePath();
		File parentFile = new File(parentFilePath);
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		return parentFilePath + UPLOAD_PHOTO_FILE_NAME;
	}

	public static void deleteTakePhoto() {
		try{
			File file = getTakePhotoFile();
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		

	}
	
	public static void initFilePath(){
		
	}
	
	public static String getCooldingPhotoFilePath(int index) {
		String parentFilePath = getCooldingTakePhotoImagePath();
		File parentFile = new File(parentFilePath);
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		return parentFilePath + "photo_" + index + ".jpg";
	}

	public static File getCooldingPhotoFile(int index) {
		File file = new File(getCooldingPhotoFilePath(index));
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		return file;
	}
	
	public static boolean copySdcardFile(File fromFile, String toFilePath) {
		try {
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFilePath);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return true;

		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean copySdcardFile(String fromFilePath, String toFilePath) {
		try {
            File file = new File(toFilePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
			InputStream fosfrom = new FileInputStream(fromFilePath);
			OutputStream fosto = new FileOutputStream(toFilePath);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return true;

		} catch (Exception ex) {
			return false;
		}
	}
	
	public static File saveAndGetFileByBytesAndPath(Context context, String path, byte[] data) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(path);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}


    public static String getCooldingImagePathByUrl(String fileUrl){
        try {
			String fileName = getFileNameByHttpUrl(fileUrl);
            return FileUtils.getCooldingImagePath() + fileName;
        } catch (Exception ex){
            MarketLog.e(TAG, "getCoodingImagePathByUrl url = " + fileUrl + ", failed, ex : " + ex.getLocalizedMessage());
            return null;
        }

    }

	public static String getCooldingSlidePictureImagePathByUrl(String fileUrl){
		try {
			String fileName = getFileNameByHttpUrl(fileUrl);
			return FileUtils.getCooldingSlidePictureImagePath() + fileName;
		} catch (Exception ex){
			MarketLog.e(TAG, "getCooldingSlidePictureImagePathByUrl url = " + fileUrl + ", failed, ex : " + ex.getLocalizedMessage());
			return null;
		}

	}

	public static void copyAsserts(Context context, String assetDir, String dir) {
		String[] files;
		try {
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(dir);
		// if this directory does not exists, make one.
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {

			}
		}

		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				// we make sure file name not contains '.' to be a folder.
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						copyAsserts(context, fileName, dir + fileName + "/");
					} else {
						copyAsserts(context, assetDir + "/" + fileName, dir + fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists()){
					continue;
				}
				InputStream in = null;
				if (0 != assetDir.length())
					in = context.getAssets().open(assetDir + "/" + fileName);
				else
					in = context.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getFileStringWithCode(String filePath, String code){
		try {
			File file = new File(filePath);
			if (file != null && file.exists()) {
				FileInputStream fin = new FileInputStream(file);
				int length = fin.available();
				byte [] buffer = new byte[length];
				fin.read(buffer);
				fin.close();
				return EncodingUtils.getString(buffer, code);
			}

			return null;
		} catch (Exception ex){
			MarketLog.e(TAG, "getFileStringWithCode failed, ex : " + ex.getLocalizedMessage());
			return null;
		}

	}

	public static String getFileStringWithCode(File file, String code){
		try {
			if (file != null && file.exists()) {
				FileInputStream fin = new FileInputStream(file);
				int length = fin.available();
				byte [] buffer = new byte[length];
				fin.read(buffer);
				fin.close();
				return EncodingUtils.getString(buffer, code);
			}

			return null;
		} catch (Exception ex){
			MarketLog.e(TAG, "getFileStringWithCode failed, ex : " + ex.getLocalizedMessage());
			return null;
		}

	}

	public static String getFileString(String filePath){
		try {
			File file = new File(filePath);
			if (file != null && file.exists()) {
				if (LogLevel.DEV) {
					DevLog.d(TAG, "getFileString success, file is exist.");
				}
				StringBuffer sb = new StringBuffer();
				FileInputStream fis = new FileInputStream(file);
				int c;
				while ((c = fis.read()) != -1) {
					sb.append((char) c);
				}
				fis.close();
				return sb.toString();
			}

			return null;
		} catch (Exception ex){
			MarketLog.e(TAG, "getFileString failed, ex : " + ex.getLocalizedMessage());
			return null;
		}
	}

	public static File write2SDFromInput(String path, InputStream input){
		File file = null;
		OutputStream output = null;
		try {
			file = new File(path);
			if (!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];
			int length;
			while((length=(input.read(buffer))) >0){
				output.write(buffer,0,length);
			}
			output.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static Drawable getImageDrawable(String filePath){
		File file = new File(filePath);
		if(!file.exists()) return null;
		try{
			InputStream inp = new FileInputStream(new File(filePath));
			return BitmapDrawable.createFromStream(inp, "img");
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
