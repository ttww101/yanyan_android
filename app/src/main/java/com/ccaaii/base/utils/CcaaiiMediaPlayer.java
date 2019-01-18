
package com.ccaaii.base.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.CcaaiiApp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.widget.SeekBar;


/**
 */
public class CcaaiiMediaPlayer {
	
    private static final String TAG = "[CCAAII] CcaaiiMediaPlayer";
    private static final boolean ADVANCED_AUDIO_MODE_ON = true;
    private static final String FILE_PROTOCOL = "file://";

    private static CcaaiiMediaPlayer instance;
    private static MediaPlayer mediaPlayer;
    private static AudioManager mAudioManager;
    private static SeekBar mSeekBar;

    // DK: current audio mode - restore when play is stopped
    private int mInitialAudioMode = AudioManager.MODE_CURRENT; // keeps phones audio manager mode
    private boolean mInitialSpeakerState = false; // keeps phone state - flag, is speaker on/off
    private static boolean mInternalSpeakerState = false; // our internal setting, toggle mode

    // IA: work around - Samsung Galaxy S GT-I9000
    // Android sdk 8 has a bug, it's necessary use coefficient 2.8 for media
    // player status/progress bar
    private static double coefficientForMediaPlayerProgressBar = 1;
    private static boolean mIsMotorolaMB860 = false;
     
    private int nowMaxDuration = 0;
    private boolean isFromUser = false;
    
    
	private CcaaiiMediaPlayer() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			String manuf = null, model = null;
			Class<?> execClass = Class.forName("android.os.Build");
			if (null != execClass) {
				Field manufacturerF = execClass.getField("MANUFACTURER");

				if (null != manufacturerF) {
					manuf = (String) manufacturerF.get(null);
				}
				Field modelF = execClass.getField("MODEL");
				if (null != modelF) {
					model = (String) modelF.get(null);
				}
			}
			
			if (LogLevel.DEV) {
                DevLog.i("player", "API Level =  " + Compatibility.getApiLevel());
                DevLog.i("player", "manuf =  " + manuf);
                DevLog.i("player", "coefficientForMediaPlayerProgressBar = " + coefficientForMediaPlayerProgressBar);
            }
			
			if (0 == manuf.compareToIgnoreCase("Motorola") && ( model.contains("MB860") || model.contains("mb860")) ) {
				mIsMotorolaMB860 = true;
			}
//			if (0 == manuf.compareToIgnoreCase("SAMSUNG") && 
//					(model.contains("I9300") || model.contains("i9300") || model.contains("I9308") || model.contains("i9308") || 
//					 model.contains("I939")  || model.contains("i939")  || model.contains("I535")  || model.contains("i535")  ||
//					 model.contains("R530")  || model.contains("r530")  || model.contains("L710")  || model.contains("l710"))   ) {
//				mIsMotorolaMB860 = true;
//			}
		} catch (Throwable th) {
		}
	}

    public static CcaaiiMediaPlayer getInstance() {
        if (instance == null) {
            instance = new CcaaiiMediaPlayer();
        }
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) CcaaiiApp.getCcaaiiContext().getSystemService(Context.AUDIO_SERVICE);
        }
        return instance;
    }

    public void setSource(Context context, String path) {
        mediaPlayer = MediaPlayer.create(context, Uri.parse(FILE_PROTOCOL + path));
    }

	public void backupAudioState() {		
		if (mAudioManager != null) {
			mInitialAudioMode = mAudioManager.getMode();
			mInitialSpeakerState = mAudioManager.isSpeakerphoneOn();
			setSpeakerState(mInternalSpeakerState);
			if (LogLevel.MARKET) {
				MarketLog.d(TAG, "backupAudioState(), mInitialAudioMode : " + mInitialAudioMode + ", mInitialSpeakerState : " + mInitialSpeakerState + ", mInternalSpeakerState : " + mInternalSpeakerState);
			}
		}
	}

	public void restoreAudioState() {

		if (mAudioManager != null) {
			mInternalSpeakerState = false;
			mAudioManager.setSpeakerphoneOn(mInitialSpeakerState);
			mAudioManager.setMode(mInitialAudioMode);			
			
			if (LogLevel.DEV) {
				DevLog.d(TAG, "restoreAudioState(), mInitialSpeakerState : " + mInitialSpeakerState + ", mInitialAudioMode : " + mInitialAudioMode);
			}
		}

	}
	
	public void restoreAudioState4S3() {
		if (mAudioManager != null) {
			mInternalSpeakerState = false;
			mAudioManager.setSpeakerphoneOn(mInitialSpeakerState);
			
			if (LogLevel.DEV) {
				DevLog.d(TAG, "restoreAudioState(), mInitialSpeakerState : " + mInitialSpeakerState + ", mInitialAudioMode : " + mInitialAudioMode);
			}
		}
	}
	
	public void restoreAudioManager() {
		if (mAudioManager != null) {
			mAudioManager.setSpeakerphoneOn(mInitialSpeakerState);
			mAudioManager.setMode(mInitialAudioMode);			
		}
	}
	
	public void restoreAudioManager4S3() {
		if (mAudioManager != null) {
			mAudioManager.setSpeakerphoneOn(mInitialSpeakerState);
		}
	}
    
	public void restoreAudioMode(){
		if (mAudioManager != null) {
			mAudioManager.setMode(mInitialAudioMode);
		}
	}
	
	public void setModeToCurrent(){
		if (mAudioManager != null) {
			mAudioManager.setMode(AudioManager.MODE_CURRENT);
		}
	}
	
    /**
     * Play media file via Path to file (cannot be used for resume after pause, use resume() for this).
     * You can specify onCompletionListener and SeekBar for feedback or just type null.
     *
     * @param path                 - to media file
     * @param onCompletionListener - instance of listener or null
     * @param onErrorListener
     * @param seekbar              - instance of SeekBar or null
     */
    public synchronized int play(String path, MediaPlayer.OnCompletionListener onCompletionListener, MediaPlayer.OnErrorListener onErrorListener, final SeekBar seekbar) {
    	
    	FileInputStream fis = null;
    	
        try {
            if (mediaPlayer != null) {
                stop();
            }
            
            mSeekBar = seekbar;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setWakeMode(CcaaiiApp.getCcaaiiContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            
            if (mediaPlayer == null) {
                if (LogLevel.DEV) {
                    DevLog.e(TAG, "cannot create mediaplayer via default constructor.");
                }
                return -1;
            }

            if (onErrorListener != null) {
                mediaPlayer.setOnErrorListener(onErrorListener);
            }

            // WORKAROUND for wav files
            if (path.contains("wav")) {
                coefficientForMediaPlayerProgressBar = 1.0;
            }
            
            if (mediaPlayer != null) {
            	mediaPlayer.reset();
            }
                     
            File file = new File(path); 
            fis = new FileInputStream(file); 
            if (mediaPlayer != null) {
            	mediaPlayer.setDataSource(fis.getFD());
            	mediaPlayer.prepare();
            }
            
            int duration = -1;
            if (mediaPlayer != null) {
            	duration = mediaPlayer.getDuration();
            }

            if (LogLevel.DEV) {
                DevLog.e("player", "duration = " + mediaPlayer.getDuration());
            }
            
            if (mediaPlayer != null) {
            	mediaPlayer.start();
            }

            if (onCompletionListener != null) {
                mediaPlayer.setOnCompletionListener(onCompletionListener);
            }
            if (mSeekBar != null) {
            	mSeekBar.setProgress(0);
            	mSeekBar.setMax(mediaPlayer.getDuration());
                isFromUser = false;
        		nowMaxDuration = 0;
                new Thread("CcaaiiMediaPlayer:play") {
                    public void run() {
                        while (true) {
                            try {
                            	TimeUnit.MILLISECONDS.sleep(300);
                                if (mediaPlayer != null) {
                                    if (isPlaying()) {
                                    	if(isFromUser){
                                    		mSeekBar.setProgress(getCurrentPosition());
                                    		isFromUser = false;
                                    		nowMaxDuration = 0;
                                    	} else {
                                    		if(getCurrentPosition() > nowMaxDuration){
                                    			nowMaxDuration = getCurrentPosition();
                                    		}
                                    		mSeekBar.setProgress(nowMaxDuration);
                                    	}
                                        if (LogLevel.DEV) {
                                            DevLog.e("player", "************************************************* ");
                                            DevLog.e("player", "getCurrentPosition() =  " + getCurrentPosition() );
                                            DevLog.e("player", "seekBar.getProgress(); =  " + mSeekBar.getProgress() );
                                        }
                                    }
                                } else {
                                	mSeekBar.setProgress(0);
                                    return;
                                }
                            } catch (Exception e) {
                                if (LogLevel.DEV) {
                                    DevLog.w(TAG, "at SeekBar setProgress", e);
                                }
                            }
                        }
                    }
                }.start();
            }
            return duration;
        } catch (IOException e) {
            if (mediaPlayer != null) {
                mediaPlayer.setOnErrorListener(null);
            }
            if (LogLevel.MARKET) {
                MarketLog.e(TAG, "cannot create mediaplayer. ", e);
            }
            return -1;
        } finally {
        	if (fis != null) {
        		try {
        			fis.close();
        		} catch (Exception e) {
        			if (LogLevel.MARKET) {
                        MarketLog.e(TAG, "Can't close FileInputStream. ", e);
                    }
        		}
        	}
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    
    public synchronized void setAudioManagerMode(int mode) {
    	if (mAudioManager != null) {
    		mAudioManager.setMode(mode);
    	}
    }
    
    public int getAudioManagerMode() {
    	if (mAudioManager != null) {
    		return mAudioManager.getMode();
    	}
    	return -500;
    }

    public synchronized void stop() {

        // IA: Conditional compilation 
        // if advanced playback feature using both speakers is turned on - before stop, will be necessary restore audio settings   
        if (ADVANCED_AUDIO_MODE_ON) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }

        }
        
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public synchronized boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }
    
    public synchronized void setSeekBar(SeekBar bar){
    	mSeekBar = bar;
    }

    public void setSpeakerState(boolean state) {
        
        if (mAudioManager != null) {
            // IA: Conditional compilation
            // if advanced playback feature is turned on - in case of silent
            // mode audio will go to the internal earpiece speaker,
            // in case of loud mode(speaker picture with big count of waves)
            // audio will be streamed on external speaker
            // in else case
            // audio will transmitted by the old way: thru external speaker,
            // with 2 modes: little bit silent/little bit louder
            if (ADVANCED_AUDIO_MODE_ON) {
                mAudioManager.setSpeakerphoneOn(state);
                setAudioMode(state);

                if (LogLevel.DEV) {
                    DevLog.d(TAG, "setSpeakerState. " + (state ? "Speaker on & Mode NORMAL" : " Speaker Off & Mode IN_CALL"));
                }
            } else {
                mAudioManager.setSpeakerphoneOn(!mAudioManager.isSpeakerphoneOn());
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    if (!mAudioManager.isSpeakerphoneOn()) {
                        mediaPlayer.setVolume(0.2f, 0.2f);
                    } else {
                        mediaPlayer.setVolume(1.0f, 1.0f);
                    }
                }
            }
        }
    }
    
    private void setAudioMode(boolean speakerState){
    	final int manufacturerCode = Compatibility.getManufacturerCode();
    	final int apiLevel = Compatibility.getApiLevel();
    	
    	if (LogLevel.DEV) {
    		DevLog.d(TAG, "setAudioMode(), manufacturerCode : " + manufacturerCode + ", apiLevel : " + apiLevel);    		    		
    	}
    	
    	// AB-3852 Feedback: Playback Issue on Razr, when device is MB860, use MODE_IN_COMMUNICATION as default.
		if (mIsMotorolaMB860) {
			try {
				if (mAudioManager.getMode() != AudioManager.MODE_IN_CALL) {
					mAudioManager.setMode(speakerState ? AudioManager.MODE_NORMAL: 
						AudioManager.class.getField("MODE_IN_COMMUNICATION").getInt(mAudioManager));
				}
			} catch (Throwable error) {
				if (LogLevel.MARKET) {
					MarketLog.e(TAG, "setAudioMode():" + error);
				}
			} 
		} else {
			mAudioManager.setMode(speakerState ? AudioManager.MODE_NORMAL : AudioManager.MODE_IN_CALL);
    	}
    }
    
    /**
     * turn off speaker, when the headset was removed, set the speaker state to false.
     * On LG-P97-V20n, the mAudioManager.isSpeakerphoneOn() always false when headset was removed, hence, cannot call toggleSpeakerState().
     * AB-3847 Voice details screen: audio source is shown as "Earpiece", but sound is lounder after disconnecting headset
     */
    public void turnOffSpeaker() {
        mInternalSpeakerState = false;
        setSpeakerState(false);
    }
    
    public void turnOnSpeaker() {
        mInternalSpeakerState = true;
        setSpeakerState(true);
    }
    
    public boolean isSpeakerOn() {
        return mInternalSpeakerState;
    }

    boolean isSystemSpeakerOn() {
        if (mAudioManager != null) {
            return mAudioManager.isSpeakerphoneOn();
        }
        return false;
    }

    public void seekTo(int position) {        
        if (mediaPlayer != null) {
            // INFO : division current position by coefficient is done because
            // of: in Android platform SDK 8 there is bug (coefficient is 2.8)
            // coefficient is calculating during EasiioMediaPlayer init
            mediaPlayer.seekTo((int) ((double) position / coefficientForMediaPlayerProgressBar));
        }
    }

    public int getCurrentPosition() {
    	int position = 0;
        if (mediaPlayer != null) {
            // INFO : multiplication current position by coefficient is done
            // because of: in Android platform SDK 8 there is bug (coefficient is 2.8)
            // coefficient is calculating during EasiioMediaPlayer init
			try {
				position = (int) ((double) mediaPlayer.getCurrentPosition() * coefficientForMediaPlayerProgressBar);
			} catch (Exception e) {
				MarketLog.w(TAG, "getCurrentPosition:"+e);
			}
        }
        return position;
    }

    public String getCurrentPositionAtMin() {
        if (mediaPlayer != null) {
            return formatDuration(getCurrentPosition());
        } else {
            return formatDuration(0);
        }
    }
    
    public static String formatDuration(int time){
        String smin = "00", ssec = "00";
        int min = time / 60000;
        int sec = (time - min * 60000) / 1000;
        if (min < 10) {
            smin = "0" + min;
        } else {
            smin = "" + min;
        }
        if (sec < 10) {
            ssec = "0" + sec;
        } else {
            ssec = "" + sec;
        }
        return smin + ":" + ssec;
    }


	public static int trimToSeconds(int totaltime) {
		int timeInSeconds = totaltime / 1000;
		if (timeInSeconds == 0){
			return 1;
		} else {
			return timeInSeconds;
		}
	}

	public int getDuration() {
		if (mediaPlayer != null) {
			return mediaPlayer.getDuration();
		}
		return -1;
	}
	
    public boolean isReady() {
        if (mediaPlayer != null) {
            return true;
        }
        return false;
    }
    
    public void setFromUser(){
    	isFromUser = true;
    	nowMaxDuration = 0;
    }
    
    public void initFromUser(){
    	isFromUser = false;
		nowMaxDuration = 0;
    }

}