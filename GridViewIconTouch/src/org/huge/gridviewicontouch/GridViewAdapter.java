package org.huge.gridviewicontouch;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> myList;
	private Context mContext;
	private TextView name_tv;
	private ImageView img;
	private View deleteView;
	private boolean isShowDelete=false;//根据变量判断是否显示删除图标

	public GridViewAdapter(Context context,ArrayList<HashMap<String, Object>> list) {
		this.mContext=context;
		this.myList=list;
	}

	public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return myList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		arg1=LayoutInflater.from(mContext).inflate(R.layout.grid_item, null);
		img=(ImageView)arg1.findViewById(R.id.img);
		name_tv=(TextView)arg1.findViewById(R.id.name_tv);
		deleteView=(ImageView)arg1.findViewById(R.id.delete_markView);
		deleteView.setVisibility(isShowDelete?View.VISIBLE:View.GONE);
		img.setBackgroundResource(myList.get(arg0).get("image").hashCode());
		name_tv.setText(myList.get(arg0).get("name").toString());
		return arg1;
	}
}
