package org.huge.matertialdesign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
/*
 * 通过Elevation和translationZ设置组件阴影效果
 * 进行阴影拖动效果的实现
 */
public class ShadowLayoutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shadow_layout);
		final View shape2=(View)findViewById(R.id.shape2);
		shape2.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://按下时增大阴影
					shape2.setTranslationZ(120);
					break;
				case MotionEvent.ACTION_UP:
					shape2.setTranslationZ(0);
				default:
					break;
				}
				return true;
			}
		});
	}
}
