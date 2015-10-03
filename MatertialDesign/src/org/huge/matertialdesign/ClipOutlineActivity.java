package org.huge.matertialdesign;

import org.huge.outlineprovider.ClipOutlineProvider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/*
 * 使用个关闭轮廓裁剪
 */
public class ClipOutlineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outline_layout);
		final TextView textView=(TextView)findViewById(R.id.clip_outline);
		final Button button=(Button)findViewById(R.id.setoutline);
		//为组件设置轮廓提供者
		ClipOutlineProvider provider=new ClipOutlineProvider();
		textView.setOutlineProvider(provider);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//如果当前view处于裁剪状态
				if (textView.getClipToOutline()) {
					//取消轮廓裁剪
					textView.setClipToOutline(false);
					button.setText("打开轮廓裁剪");
				}else {
					textView.setClipToOutline(true);
					button.setText("关闭轮廓裁剪");
				}
			}
		});
	}
	
}
