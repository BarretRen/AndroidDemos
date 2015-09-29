package org.huge.viewpagerweichat;

import java.util.ArrayList;
import java.util.List;

import org.huge.viewpagerweichat.R.id;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements OnClickListener{
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private List<View> views=new ArrayList<View>();
	private LinearLayout chat,pengyouquan,lianxiren,set;
	private ImageButton buttonChat,buttonPengyou,buttonLianxi,buttonSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//初始化组件
		initView();
		//初始化事件处理
		initListener();
	}
	
	private void initListener(){
		chat.setOnClickListener(this);
		pengyouquan.setOnClickListener(this);
		lianxiren.setOnClickListener(this);
		set.setOnClickListener(this);
		//设置viewpager滑动切换
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	private void initView(){
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		chat=(LinearLayout)findViewById(R.id.chat);
		pengyouquan=(LinearLayout)findViewById(R.id.pengyouquan);
		lianxiren=(LinearLayout)findViewById(R.id.tongxunlu);
		set=(LinearLayout)findViewById(R.id.set);
		buttonChat=(ImageButton)findViewById(R.id.btn_chat);
		buttonPengyou=(ImageButton)findViewById(R.id.btn_parent);
		buttonLianxi=(ImageButton)findViewById(R.id.btn_content);
		buttonSet=(ImageButton)findViewById(R.id.btn_set);
		//获取每个tab页布局
		LayoutInflater inflater=LayoutInflater.from(this);
		views.add(inflater.inflate(R.layout.tab_1, null));
		views.add(inflater.inflate(R.layout.tab_2, null));
		views.add(inflater.inflate(R.layout.tab_3, null));
		views.add(inflater.inflate(R.layout.tab_4, null));
		//设置viewpager适配器
		pagerAdapter=new PagerAdapter() {
			
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(views.get(position));
				return views.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return views.size();
			}
		};
		viewPager.setAdapter(pagerAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat:
			viewPager.setCurrentItem(0);
			break;
		case R.id.pengyouquan:
			viewPager.setCurrentItem(1);
			break;
		case R.id.tongxunlu:
			viewPager.setCurrentItem(2);
			break;
		case R.id.set:
			viewPager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}

}
