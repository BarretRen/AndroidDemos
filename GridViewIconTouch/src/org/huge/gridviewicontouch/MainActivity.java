package org.huge.gridviewicontouch;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.attr;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

public class MainActivity extends Activity {
	private GridView gridView;
	private GridViewAdapter adapter;
	private boolean isShowDelete=false;
	private ArrayList<HashMap<String, Object>> myList;
	final String[] name=new String[]{"test","testa","testb","testc","testd","testf","testg"};
	final int[] pic=new int[]{
		R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,
		R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,
		R.drawable.ic_launcher
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gridView=(GridView)findViewById(R.id.gridview);
		myList=getMenuAdapter(pic,name);
		adapter=new GridViewAdapter(this, myList);
		gridView.setAdapter(adapter);
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (isShowDelete) {
					isShowDelete=false;
				}else {
					isShowDelete=true;
					adapter.setIsShowDelete(isShowDelete);
					gridView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							delete(arg2);//删除选中项
							adapter=new GridViewAdapter(MainActivity.this, myList);
							gridView.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
					});
				}
				adapter.setIsShowDelete(isShowDelete);
				return true;
			}
		});
	}
	
	//删除被点击的项
		private void delete(int position){
			ArrayList<HashMap<String, Object>> newList=new ArrayList<HashMap<String,Object>>();
			if (isShowDelete) {
				myList.remove(position);
				isShowDelete=false;
			}
			newList.addAll(myList);
			myList.clear();
			myList.addAll(newList);
		}
		
	//填充list数据
	private ArrayList<HashMap<String, Object>> getMenuAdapter(int[] image,String[] name){
		ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<image.length;i++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("image", image[i]);
			map.put("name", name[i]);
			data.add(map);
		}
		return data;
	}
	
}
