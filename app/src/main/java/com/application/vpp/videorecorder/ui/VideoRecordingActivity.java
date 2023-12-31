package com.application.vpp.videorecorder.ui;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.vpp.R;

import java.io.IOException;

import com.application.vpp.videorecorder.CameraHelper;
import com.application.vpp.videorecorder.CameraWrapper;
import com.application.vpp.videorecorder.Util;
import com.application.vpp.videorecorder.core.BaseRecorder;
import com.application.vpp.videorecorder.core.DefaultVideoRecorder;

import static com.application.vpp.activity.PhotoVideoSignatureActivity.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE;

public class VideoRecordingActivity extends Activity implements SurfaceHolder.Callback{
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private CameraWrapper mCameraPair;
	private DefaultVideoRecorder mRecorder;
	
	private ImageView btStartStop, ivSwitchCamera;
	private TextView tvDuration;
	private long seconds = 0;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_recording_video);
		
		if(!getCameraObject(true, CameraInfo.CAMERA_FACING_BACK)){
			Toast.makeText(VideoRecordingActivity.this,
					R.string.not_support_camera, Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		btStartStop = (ImageView) findViewById(R.id.bt_start_stop);
		btStartStop.setOnClickListener(onClick);
		btStartStop.setVisibility(View.INVISIBLE);
		btStartStop.setTag(new String("start"));
		
		ivSwitchCamera = (ImageView)findViewById(R.id.switch_camera);
		ivSwitchCamera.setVisibility(Camera.getNumberOfCameras() > 1 ? View.VISIBLE
				: View.GONE);
		ivSwitchCamera.setOnClickListener(onClick);
		
		tvDuration = (TextView)findViewById(R.id.duration);
		
		initRecorder();
	}
	
	private void initRecorder(){
		if(mRecorder != null){
			mRecorder.release();
		}
		//Toast.makeText(VideoRecordingActivity.this,"1",Toast.LENGTH_SHORT).show();
		mRecorder = new DefaultVideoRecorder();
		mRecorder.bindCamera(mCameraPair);
		mRecorder.setOnRecordingListener(new BaseRecorder.OnRecordingListener() {

			@Override
			public void onPrepared() {
				runOnUiThread(new Runnable() {
					public void run() {
						//Toast.makeText(VideoRecordingActivity.this,"2",Toast.LENGTH_SHORT).show();
						btStartStop.setVisibility(View.VISIBLE);
						btStartStop.setClickable(true);
						setUIState(false);

						Log.e( "run: ", "onPrepared");
					}
				});
			}
			
			@Override
			public void onStart() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						setUIState(true);
						Log.e( "run: ", "onStart");
					}
				});
			}

			@Override
			public void onError(final String error) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Log.e( "run: ", "onError");
						try{
							int errorCode = Integer.valueOf(error);
							if(errorCode == -1007){
								Toast.makeText(getApplicationContext(), 
										R.string.stop_recording_video_err, Toast.LENGTH_SHORT).show();
							}
							setUIState(false);
							return;
						}catch(NumberFormatException e){
							Log.e( "run: ", e.getMessage());
						}
					}
				});
			}

			@Override
			public void onFailure() {
				onError(null);
				Log.e( "run: ", "onFailure");
			}

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						setUIState(false);
						Log.e( "run: ", "onSuccess");
						showPreviewPage(mRecorder.getOutputFile().getAbsolutePath());
					}
				});
			}

			@Override
			public void onCancel() {
				onFailure();
				Log.e( "run: ", "onCancel");
			}
		});	
	}
	
	private void prepareRecorder(){
		btStartStop.setClickable(false);
		
		new Thread(){
			public void run() {
				mRecorder.prepare();
				Log.e( "run: ", "onPrepareRecorder");
			};
		}.start();
	}

	private void setUIState(boolean recording){
		if(! recording){
			btStartStop.setImageResource(R.drawable.video_record);
			btStartStop.setTag(new String("start"));
			seconds = 0;
			mUIHandler.removeCallbacks(mUIRunnable);
			tvDuration.setVisibility(View.GONE);
			ivSwitchCamera.setVisibility(View.VISIBLE);
		}else{
			btStartStop.setImageResource(R.drawable.video_stop);
			btStartStop.setTag(new String("stop"));
			seconds = 0;
			mUIHandler.postDelayed(mUIRunnable, 0);
			tvDuration.setVisibility(View.VISIBLE);
			ivSwitchCamera.setVisibility(View.INVISIBLE);
		}
	}
	
	private void showPreviewPage(String videoPath){
//		Intent intent = new Intent(VideoRecordingActivity.this, VideoPreviewActivity.class);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, videoPath);
//		startActivity(intent);

		Log.e("videoPath", videoPath);
		Intent intent=new Intent();
		intent.putExtra("mVideoPath", videoPath);
		setResult(CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE,intent);
		finish();//finishing activity
	}
	
	private Handler mUIHandler = new Handler();
	private Runnable mUIRunnable = new Runnable() {

		@Override
		public void run() {
			tvDuration.setText(Util.formatDuration(seconds++));
			if(mRecorder != null && seconds >= mRecorder.getMaxDurationInSeconds() &&
					mRecorder.reachRecorderMaxDuration()){
				mRecorder.stopRecording(false);

				showPreviewPage(mRecorder.getOutputFile().getAbsolutePath());
			}else mUIHandler.postDelayed(this, 1000);
		}
	};
	
	private void switchCamera() {
		//Close current camera firstly
		Camera mCamera = mCameraPair.getCamera();
		mCamera.lock();
		mCamera.stopPreview();
		mCamera.release();
		
		int camId = mCameraPair.getCameraInfo().facing;
		int toggleCamId = (camId == CameraInfo.CAMERA_FACING_BACK ? 
				CameraInfo.CAMERA_FACING_FRONT : CameraInfo.CAMERA_FACING_BACK);
		if(! getCameraObject(false, toggleCamId)){
			return;
		}
		
		initRecorder();
		startCameraPreview();
	}
	
	public View.OnClickListener onClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.bt_start_stop){
				String tag = (String)v.getTag();
				if(mRecorder.isRecording() && tag.toString().equals("stop")){
					mRecorder.stopRecording(false);
				}else if(tag.toString().equals("start")){
					mRecorder.startRecording();
				}
			}else if(v.getId() == R.id.switch_camera){
				switchCamera();
			}
		}
	};
	
	
	protected void onStart() {
		super.onStart();
		
		mSurfaceHolder.addCallback(this);
	};
	
	@Override
	protected void onRestart() {
		super.onRestart();
		int cameraId;
		if(mCameraPair != null){
			cameraId = mCameraPair.getCameraInfo().facing;
		}else cameraId = CameraInfo.CAMERA_FACING_BACK;
		
		if(!getCameraObject(false, cameraId)){
			Toast.makeText(getBaseContext(),
					R.string.not_support_camera, Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		
		initRecorder();

		if(mSurfaceView.isShown()){
			//After screen OFF >  screen ON
			startCameraPreview();
		}else{
			//After press HOME
			Log.e("run", "camera not shown");
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!TextUtils.isEmpty(recordedVideoPath)){
			showPreviewPage(recordedVideoPath);
			recordedVideoPath = null;
		}
	}
	
	private static String recordedVideoPath;
	
	protected void onPause() {
		if(mRecorder.isRecording()){
			recordedVideoPath = mRecorder.getOutputFile().getAbsolutePath();

			mRecorder.stopRecording(true);
		}
		if(mRecorder != null){
			mRecorder.release();
		}
		super.onPause();
	};
	
	protected void onStop() {
		if(mCameraPair != null){
			mCameraPair.getCamera().lock();
			mCameraPair.release();
		}
		super.onStop();
	};

	@Override
	public void onBackPressed() {
		if(! mRecorder.isRecording()){
			super.onBackPressed();
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		holder.setKeepScreenOn(true);
		startCameraPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	private boolean getCameraObject(boolean first, int cameraId){
		if (first && !CameraHelper.isCameraSupported(this)) return false;
		
		mCameraPair = CameraHelper.getCamera(cameraId);
		if (mCameraPair == null && Camera.getNumberOfCameras() > 1) {
			int toggleCamId = (cameraId == CameraInfo.CAMERA_FACING_BACK ? 
					CameraInfo.CAMERA_FACING_FRONT : CameraInfo.CAMERA_FACING_BACK);
			mCameraPair = CameraHelper.getCamera(toggleCamId);
		}
		if(mCameraPair != null){
			CameraHelper.addCameraAttributes(this, mCameraPair);
			return true;
		}
		return false;
	}
	
	private void startCameraPreview(){
		Camera mCamera = mCameraPair.getCamera();
		if (mCamera != null) {
			mCamera.lock();
			try {
				mCamera.setPreviewDisplay(mSurfaceHolder);
				mCamera.startPreview();
				Log.e("run", "mSurfaceHolder");

//				CameraHelper.addCameraAutoFocusFeature(mCamera);
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("run", e.getMessage());
			}
			
			prepareRecorder();
		}
	}
	
}
