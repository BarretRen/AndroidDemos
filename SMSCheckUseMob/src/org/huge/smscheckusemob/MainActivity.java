package org.huge.smscheckusemob;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends Activity implements OnClickListener{
	private EditText phoneNum,checkNum;
	private Button Btn_getCheck,Btn_next;
	private String checkNumBeGetten;//通过SMSSDK获取的验证码
	//SDK的应用识别key
	private static String APPKEY="ada9c26313c4";
	private static String APPSECRET="12063f012d85c7848613af2f8b6690bc";
	//设置处理SMSSDK的相关事件
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int event=msg.arg1;
			int result=msg.arg2;
			Object data=msg.obj;
			//根据不同事件结果进行相应处理
			if (result==SMSSDK.RESULT_COMPLETE) {
				if (event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					//验证码验证成功
					Toast.makeText(MainActivity.this, "验证成功", 1).show();
				}else if (event==SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					//验证码已发送
					Toast.makeText(MainActivity.this, "验证码已发送", 1).show();
				}
			}else {
				((Throwable)data).printStackTrace();
				Toast.makeText(MainActivity.this, "验证码错误", 1).show();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化组件
		phoneNum=(EditText)findViewById(R.id.phonenum);
		checkNum=(EditText)findViewById(R.id.checknum);
		Btn_getCheck=(Button)findViewById(R.id.getchecknum);
		Btn_next=(Button)findViewById(R.id.nextstep);
		//设置button监听
		Btn_getCheck.setOnClickListener(this);
		Btn_next.setOnClickListener(this);
		//初始化Mob SMSSDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		EventHandler eHandler=new EventHandler(){
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message message=new Message();
				message.arg1=event;
				message.arg2=result;
				message.obj=data;
				handler.sendMessage(message);//获取事件后提交handler处理
			}
		};
		SMSSDK.registerEventHandler(eHandler);
	}
	
	//销毁时取消注册
	@Override
	protected void onDestroy() {
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getchecknum:
			if (TextUtils.isEmpty(phoneNum.getText().toString())) {
				Toast.makeText(this, "电话不能为空", 1).show();
			}else {
				//提交电话号码用于获取验证码
				SMSSDK.getVerificationCode("86", phoneNum.getText().toString());
			}
			break;
		case R.id.nextstep:
			if (TextUtils.isEmpty(checkNum.getText().toString())) {
				Toast.makeText(this, "验证码不能为空", 1).show();
			}else {
				//验证填写的验证码和手机号是否一致
				SMSSDK.submitVerificationCode("86", phoneNum.getText().toString(), checkNum.getText().toString());
			}
			break;
		default:
			break;
		}
	}
}
