package com.example.lockpatterview;

import com.example.lockpatterview.LockPatterView.OnPatterChangeLister;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends Activity implements OnPatterChangeLister {

	LockPatterView lock;
	TextView text;
	String p = "14789";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.text);
		lock = (LockPatterView) findViewById(R.id.lock);
		lock.SetOnPatterChangeLister(this);
	}

	@Override
	public void onPatterChange(String passwordStr) {
		if (!TextUtils.isEmpty(passwordStr)) {
			if (passwordStr.equals(p)) {
				text.setText(passwordStr);
			} else {
				text.setText("密码错误");
				lock.errorPoint();
			}
		}else {
			Toast.makeText(MainActivity.this, "至少连接5点", 0).show();
		}

	}

	@Override
	public void onPatterStart(boolean isStart) {
		if (isStart) {
			text.setText("请绘制图案");
		}
	}

}
