package com.facpp.picturedetect;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

public class MainActivity extends Activity {

	final private static String TAG = "MainActivity";
	final private int PICTURE_CHOOSE = 1;
	
	private ImageView imageView = null;
	private Bitmap img = null;//要识别的图片
	private Button buttonPick,buttonDetect = null;
	private TextView textView = null;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)this.findViewById(R.id.textView1);
        imageView = (ImageView)this.findViewById(R.id.imageView1);
        buttonPick = (Button)this.findViewById(R.id.button1);
        buttonPick.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//从相册选择照片
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		        photoPickerIntent.setType("image/*");
		        startActivityForResult(photoPickerIntent, PICTURE_CHOOSE);
			}
		});
        buttonDetect = (Button)this.findViewById(R.id.button2);
        buttonDetect.setVisibility(View.INVISIBLE);
        buttonDetect.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				textView.setText("正在识别 ...");
				FaceppDetect faceppDetect = new FaceppDetect();
				faceppDetect.setDetectCallback(new DetectCallback() {
					public void detectResult(JSONObject rst) {
						Paint paint = new Paint();
						paint.setColor(Color.RED);
						paint.setStrokeWidth(8);//设置人脸识别框的宽度
						//创建一个新的图片
						Bitmap bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());
						Canvas canvas = new Canvas(bitmap);
						canvas.drawBitmap(img, new Matrix(), null);
						try {
							//找出所有人脸
							final int count = rst.getJSONArray("face").length();
							for (int i = 0; i < count; ++i) {
								/*
								 * 在识别出的脸上画出轮廓
								 */
								float x, y, w, h;
								//获取识别后人脸的中心点坐标
								x = (float)rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getJSONObject("center").getDouble("x");
								y = (float)rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getJSONObject("center").getDouble("y");
								//获取脸的大小
								w = (float)rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getDouble("width");
								h = (float)rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getDouble("height");
								//还原到实际图片大小的坐标
								x = x / 100 * img.getWidth();
								w = w / 100 * img.getWidth() * 0.7f;
								y = y / 100 * img.getHeight();
								h = h / 100 * img.getHeight() * 0.7f;
								//画出脸型区域
								canvas.drawLine(x - w, y - h, x - w, y + h, paint);
								canvas.drawLine(x - w, y - h, x + w, y - h, paint);
								canvas.drawLine(x + w, y + h, x - w, y + h, paint);
								canvas.drawLine(x + w, y + h, x + w, y - h, paint);
								/*
								 * 识别照片人脸的年龄和性别
								 */
								int age=(int)rst.getJSONArray("face").getJSONObject(i).getJSONObject("attribute").getJSONObject("age").getInt("value");
								String sex=rst.getJSONArray("face").getJSONObject(i).getJSONObject("attribute").getJSONObject("gender").getString("value");
								sex=sex.equals("Female")?"女":"男";
								//绘制到图片中
								paint.setColor(Color.LTGRAY);
								paint.setTextSize(w*0.5f);
								canvas.drawText(sex+" "+String.valueOf(age), x-w, y+h*1.5f, paint);
							}
							img = bitmap;
							MainActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									//显示新的图像
									imageView.setImageBitmap(img);
									textView.setText("识别出 "+ count + "张脸");
								}
							});
						} catch (JSONException e) {
							e.printStackTrace();
							MainActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									textView.setText("识别失败");
								}
							});
						}
						
					}
				});
				faceppDetect.detect(img);
			}
		});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	if (requestCode == PICTURE_CHOOSE) {//选择照片后回调
    		if (intent != null) {
    			Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
    			cursor.moveToFirst();
    			int idx = cursor.getColumnIndex(ImageColumns.DATA);
    			String fileSrc = cursor.getString(idx); 
    			//重新设置尺寸
    			Options options = new Options();
    			options.inJustDecodeBounds = true;
    			options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)options.outWidth / 1024f, (double)options.outHeight / 1024f)));
    			options.inJustDecodeBounds = false;
    			img = BitmapFactory.decodeFile(fileSrc, options);
    			textView.setText("可以识别了==>");
    			
    			imageView.setImageBitmap(img);
    			buttonDetect.setVisibility(View.VISIBLE);
    		}
    	}
    }

    private class FaceppDetect {
    	DetectCallback callback = null;
    	
    	public void setDetectCallback(DetectCallback detectCallback) { 
    		callback = detectCallback;
    	}

    	public void detect(final Bitmap image) {
    		new Thread(new Runnable() {
				public void run() {
					HttpRequests httpRequests = new HttpRequests("4480afa9b8b364e30ba03819f3e9eff5", "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M", true, false);
		    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    		//缩小图片，便于网络传输
		    		float scale = Math.min(1, Math.min(600f / image.getWidth(), 600f / image.getHeight()));
		    		Matrix matrix = new Matrix();
		    		matrix.postScale(scale, scale);
		    		Bitmap imgSmall = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
		    		imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		    		byte[] array = stream.toByteArray();
		    		try {
						JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(array));
						if (callback != null) {
							callback.detectResult(result);
						}
					} catch (FaceppParseException e) {
						e.printStackTrace();
						MainActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								textView.setText("网络错误");
							}
						});
					}
				}
			}).start();
    	}
    }

    interface DetectCallback {
    	void detectResult(JSONObject rst);
	}
}
