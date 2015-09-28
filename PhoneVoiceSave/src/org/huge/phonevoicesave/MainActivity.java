package org.huge.phonevoicesave;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import cn.bmob.v3.Bmob;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//启动监听服务
		startService(new Intent(this,PhoneService.class));
	}

}
