package org.huge.progressdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationProgressDialog extends ProgressDialog {
	private AnimationDrawable animation;
	private Context context;
	private ImageView imageView;
	private String loadingTip;
	private TextView loadingTV;
	private int count=0;
	private String oldLoadingTip;
	private int resid;
	
	public AnimationProgressDialog(Context context,String content,int id){
		super(context);
		this.context=context;
		this.loadingTip=content;
		this.resid=id;
		setCanceledOnTouchOutside(true);//点击对话框之外取消作用
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化组件
		setContentView(R.layout.progress_dialog);//对话框显示样式
		loadingTV=(TextView)findViewById(R.id.loadingTv);
		imageView=(ImageView)findViewById(R.id.loadingIv);
		loadingTV.setText(loadingTip);
		imageView.setBackgroundResource(resid);
		//通过背景获取animation对象
		animation=(AnimationDrawable)imageView.getBackground();
		imageView.post(new Runnable() {
			public void run() {
				animation.start();//执行动画
			}
		});
	}
}
