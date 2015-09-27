package org.huge.loadmorelistview;

import java.util.ArrayList;
import java.util.List;

import org.huge.loadmorelistview.LoadListView.ILoadListener;
import org.huge.loadmorelistview.LoadListView.RLoadListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {
	private LoadListView listView;
	private ArrayAdapter<String> adapter;
	private List<String> datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化组件
		listView=(LoadListView)findViewById(R.id.main);
		listView.setInterface(new ILoadListener() {
			@Override
			public void onLoad() {
				//添加延时效果
				Handler handler=new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						initMoreDatas();//加载更多数据
						adapter.notifyDataSetChanged();
						listView.loadCompleted();//隐藏加载提示
					}
				}, 2000);
			}
		});
		listView.setRefreshInterface(new RLoadListener() {
			@Override
			public void onRefresh() {
				//添加延时效果
				Handler handler=new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						initRefreshDatas();//更新更多数据
						adapter.notifyDataSetChanged();
						listView.reflashComplete();
					}
				}, 2000);
			}
		});
		datas=new ArrayList<String>();
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,datas);
		listView.setAdapter(adapter);
		//初始化数据
		for(int i=1;i<21;i++){
			datas.add("数据"+i+" ");
		}
	}

	private void initMoreDatas(){
		for(int i=1;i<21;i++){
			datas.add("新数据"+i+" ");
		}
	}
	
	private void initRefreshDatas(){
		datas.add(0, "下拉刷新数据" + 1 + "");//此方法插入表头  
	    datas.add(0, "下拉刷新数据" + 2 + ""); 

	}
}