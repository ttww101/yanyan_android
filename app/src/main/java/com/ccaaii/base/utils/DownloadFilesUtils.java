package com.ccaaii.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.MalformedInputException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.ccaaii.base.http.HttpClientFactory;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;

public class DownloadFilesUtils {

	private static final String TAG = "[CCAAII]DownloadFilesUtils";

	private static final int CONNECTION_TIMEOUT = 30 * 1000; // 30 sec

	public static Bitmap getHeadImage(Context context, String fileUrl) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "getHeadImage fileUrl = " + fileUrl);
		}
		try {
			if (TextUtils.isEmpty(fileUrl)) {
				return null;
			}

			String fileName = FileUtils.getFileNameByHttpUrl(fileUrl);

			String path = FileUtils.getHeadImagePath() + fileName;
			if (LogLevel.DEV) {
				DevLog.d(TAG, "getHeadImage path = " + path);
			}
			File file = new File(path);
			if (file != null && file.exists()) {
				if (LogLevel.DEV) {
					DevLog.d(TAG, "getHeadImage success, file is exist.");
				}
				return BitmapFactory.decodeFile(path);
			}
			Bitmap bitmap = getBitmapByHttpGetSimply(fileUrl);
            FileUtils.saveImageToSDCardByPath(context, bitmap, path);
			return bitmap;
		} catch (Exception e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "getHeadImage failed, e : " + e.toString());
			}
			return null;
		}

	}

    public static Bitmap getCooldingImage(Context context, String fileUrl) {
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getCooldingImage fileUrl = " + fileUrl);
        }
        try {
            if (TextUtils.isEmpty(fileUrl)) {
                return null;
            }

            String path = FileUtils.getCooldingImagePathByUrl(fileUrl);
            if (LogLevel.DEV) {
                DevLog.d(TAG, "getCooldingImage path = " + path);
            }
            File file = new File(path);
            if (file != null && file.exists()) {
                if (LogLevel.DEV) {
                    DevLog.d(TAG, "getCooldingImage success, file is exist.");
                }
                return BitmapFactory.decodeFile(path);
            }
            Bitmap bitmap = null;
            if (fileUrl.startsWith("http")){
                bitmap = getBitmapByHttpGetSimply(fileUrl);
				FileUtils.saveImageToSDCardByPath(context, bitmap, path);
            } else {
                if (FileUtils.copySdcardFile(fileUrl, path)){
                    bitmap = BitmapFactory.decodeFile(path);
                }
            }
            return bitmap;
        } catch (Exception e) {
            if (LogLevel.MARKET) {
                MarketLog.e(TAG, "getCooldingImage failed, e : " + e.toString());
            }
            return null;
        }

    }

	public static Bitmap getCooldingSlidePictureImage(Context context, String fileUrl) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "getCooldingSlidePictureImage fileUrl = " + fileUrl);
		}
		try {
			if (TextUtils.isEmpty(fileUrl)) {
				return null;
			}

			String path = FileUtils.getCooldingSlidePictureImagePathByUrl(fileUrl);
			if (LogLevel.DEV) {
				DevLog.d(TAG, "getCooldingSlidePictureImage path = " + path);
			}
			File file = new File(path);
			if (file != null && file.exists()) {
				if (LogLevel.DEV) {
					DevLog.d(TAG, "getCooldingSlidePictureImage success, file is exist.");
				}
				return BitmapFactory.decodeFile(path);
			}
			Bitmap bitmap = null;
			if (fileUrl.startsWith("http")){
				bitmap = getBitmapByHttpGetSimply(fileUrl);
				FileUtils.saveImageToSDCardByPath(context, bitmap, path);
			} else {
				if (FileUtils.copySdcardFile(fileUrl, path)){
					bitmap = BitmapFactory.decodeFile(path);
				}
			}
			return bitmap;
		} catch (Exception e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "getCooldingSlidePictureImage failed, e : " + e.toString());
			}
			return null;
		}

	}
	
	/**
	 * Download image by url and save
	 */
	public static Bitmap readImageFromUrl(Context context, String url, File file) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "readImageFromUrl URL = " + url);
		}
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			conn.setReadTimeout(CONNECTION_TIMEOUT);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(file);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(file);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * get bitmap by HttpGet
	 */
	public static Bitmap getBitmapByHttpGetSimply(String urlString) {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "getBitmapByHttpGet URL = " + urlString);
		}
		Bitmap bitmap = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			int length = conn.getContentLength();
			if (length != -1) {
				byte[] imgData = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, imgData, destPos, readLen);
					destPos += readLen;
				}
				bitmap = BitmapFactory.decodeByteArray(imgData, 0,imgData.length);
			}
			is.close();
		} catch (IOException e) {
		}

		return bitmap;

	}

    /**
     * get bitmap by HttpGet
     */
    public static boolean getBitmapAndSaveByHttpGetSimply(String urlString, String filePath) {
        if (LogLevel.DEV) {
            DevLog.d(TAG, "getBitmapAndSaveByHttpGetSimply URL = " + urlString + ", filePath = " + filePath);
        }
        HttpClient httpClient = HttpClientFactory.getHttpClient();
        final HttpGet httpGet = new HttpGet(urlString);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            InputStream is = response.getEntity().getContent();
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream os = new FileOutputStream(file);
            CopyStream(is, os);
            os.close();
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

	/***
	 * Download file
	 *
	 */
	public static boolean downFile(String urlStr, String path){
		InputStream inputStream = null;
		try {
			File file = new File(path);
			if (file.exists()){
				return true;
			} else {
				inputStream = getInputStreamFromURL(urlStr);
				File resultFile = FileUtils.write2SDFromInput(path, inputStream);
				if(resultFile == null){
					return false;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static String downloadFileAsString(String urlStr, String path){
		InputStream inputStream = null;
		try {
			File file = new File(path);
			if (file != null && file.exists()){
				return FileUtils.getFileStringWithCode(file, "UTF-8");
			} else {
				inputStream = getInputStreamFromURL(urlStr);
				File resultFile = FileUtils.write2SDFromInput(path, inputStream);
				if(resultFile == null){
					return null;
				}
				return FileUtils.getFileStringWithCode(resultFile, "UTF-8");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			try {
				if (inputStream != null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String downloadAsString(String urlStr){
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while( (line = buffer.readLine()) != null){
				sb.append(line);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static InputStream getInputStreamFromURL(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection)url.openConnection();
			inputStream = urlConn.getInputStream();

		} catch (MalformedInputException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			while (true) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1) {
					break;
				}
				os.write(bytes, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap decodeFile(File f) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE){
					break;
				}
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
