package com.ccaaii.base.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.CcaaiiApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogcatFileManager {
	private static LogcatFileManager INSTANCE = null;
	private static String PATH_LOGCAT;
	private LogDumper mLogDumper = null;
	private int mPId;
	private static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final long TWO_DAY_MS = 172800000;

	private static final long RESTART_TIME_STAMP = 60 * 60 * 1000;

	public static LogcatFileManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LogcatFileManager();
		}
		return INSTANCE;
	}

	private LogcatFileManager() {
		mPId = android.os.Process.myPid();
	}

	private void setFolderPath(String folderPath) {
		try {
			File folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			if (!folder.isDirectory())
				throw new IllegalArgumentException("The logcat folder path is not a directory: " + folderPath);
			PATH_LOGCAT = folderPath.endsWith("/") ? folderPath : folderPath + "/";
		} catch (Exception ex){

		}

	}
	
	public void restart(){
		stop();
		try {
			File log_dir = new File(PATH_LOGCAT);
			if(log_dir.exists()){
				clearFiles(log_dir);
			}
			if (!log_dir.exists()) {
				log_dir.mkdirs();
			}
		} catch (Exception ex){
			
		}
		start();
	}

	public void start() {
		setFolderPath(FileUtils.getCooldingLogPath());
		if (mLogDumper == null)
			mLogDumper = new LogDumper(String.valueOf(mPId), PATH_LOGCAT);
		mLogDumper.start();
	}

	public void stop() {
		if (mLogDumper != null) {
			mLogDumper.stopLogs();
			mLogDumper = null;
		}
	}

	private class LogDumper extends Thread {
		private Process logcatProc;
		private BufferedReader mReader = null;
		private boolean mRunning = true;
		String cmds = null;
		private String mPID;
		private FileOutputStream out = null;
		private String mDirPath = null;

		public LogDumper(String pid, String dir) {
			mPID = pid;
			mDirPath = dir;
			try {
				File log_dir = new File(mDirPath);
				if(log_dir.exists()){
					clearFiles(log_dir);
				}
				if (!log_dir.exists()) {
					log_dir.mkdirs();
				}
				String fileName = "coolding-logcat-" + simpleDateFormat1.format(new Date()) + ".log";
				out = new FileOutputStream(new File(mDirPath, fileName), true);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			/**
			 * 
			 * log level：*:v , *:d , *:w , *:e , *:f , *:s
			 * 
			 * Show the current mPID process level of E and W log.
			 * 
			 * */
			// cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
			// cmds = "logcat  | grep \"(" + mPID + ")\"";//show log of all
			// level
			// cmds = "logcat -s way";//Print label filtering information
			cmds = "logcat *:e *:w *:i | grep \"(" + mPID + ")\"";
			MarketLog.w("EASIIOLOG", "cmds = " + cmds);
		}

		public void stopLogs() {
			mRunning = false;
		}

		@Override
		public void run() {
			try {


				if (out != null ) {
					out.write(("\n\n\n#####################################################################\n").getBytes());
					out.write(("############################## Start Log ############################\n").getBytes());
					out.write(("#####################################################################\n").getBytes());
					out.write(("# Device Name : " + DeviceUtils.getDeviceName() + "\n").getBytes());
					out.write(("# Android Release Version : " + DeviceUtils.getAndroidReleaseVersion() + "\n").getBytes());
					PackageInfo info;
					try {
						info = CcaaiiApp.getCcaaiiContext().getPackageManager().getPackageInfo(CcaaiiApp.getCcaaiiContext().getPackageName(), 0);
						out.write(("# App Package Name : " + info.packageName + "\n").getBytes());
						out.write(("# App Version : " + info.versionName + "\n").getBytes());
					} catch (PackageManager.NameNotFoundException e) {
						e.printStackTrace();
					}

					out.write(("# Available Internal Memory Size : " + DeviceUtils.getAvailableInternalMemorySize() + "\n").getBytes());
					out.write(("# Available External Memory Size : " + DeviceUtils.getAvailableExternalMemorySize() + "\n").getBytes());
					out.write(("#####################################################################\n").getBytes());
					out.write(("#####################################################################\n").getBytes());
				}

				logcatProc = Runtime.getRuntime().exec(cmds);
				mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
				String line = null;
				int index = 0;
				while (mRunning && (line = mReader.readLine()) != null) {
					if (!mRunning) {
						break;
					}
					if (line.length() == 0) {
						continue;
					}
					if (out != null && line.contains(mPID)) {
						out.write((simpleDateFormat2.format(new Date()) + "  " + line + "\n").getBytes());
						if (index >= 1000){
							index = 0;
							try {
								File log_dir = new File(PATH_LOGCAT);
								if(log_dir.exists()){
									clearFiles(log_dir);
								}
								if (!log_dir.exists()) {
									log_dir.mkdirs();
								}
							} catch (Exception ex){
								
							}
						}
						index ++;
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (logcatProc != null) {
					logcatProc.destroy();
					logcatProc = null;
				}
				if (mReader != null) {
					try {
						mReader.close();
						mReader = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					out = null;
				}
			}
		}
	}

	private long getFileSize(File f) {
		try {
			long size = 0;
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
			
			return size;
		} catch (Exception ex){
			return 0;
		}
		
	}


	private Date getDateByStr(String str){
		Date date;
		try {
			date = simpleDateFormat1.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	private void clearFiles(File f){
		try {
			long currentTime = new Date().getTime();
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if(flist[i].exists() && !flist[i].isDirectory()){
					try {
						String fName = flist[i].getName();
						if (!fName.startsWith("easiio-sdk-logcat-")){
							continue;
						}
						String dateStr = fName.substring(fName.lastIndexOf("-") + 1, fName.lastIndexOf("."));
						Date date = getDateByStr(dateStr);
						long fTime = date.getTime();
						if (fTime > 0 && currentTime - fTime >= TWO_DAY_MS){
							flist[i].delete();
						}
					} catch (Exception ex){

					}

				}
			}
		} catch (Exception ex){
			
		}
		
	}
	
	
}
