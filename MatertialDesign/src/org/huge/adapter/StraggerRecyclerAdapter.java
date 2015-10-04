package org.huge.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.huge.matertialdesign.R;
import org.huge.matertialdesign.R.id;
import org.huge.matertialdesign.R.layout;

import android.R.integer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

/*
 * 和SampleRecyclerAdapter基本一致，不同的是设置了列表项的高度是随机的，大小不一
 */
public class StraggerRecyclerAdapter extends RecyclerView.Adapter<StraggerRecyclerAdapter.StraggerViewHolder> {
	private List<String> data=new ArrayList<String>();
	private List<Integer> heights;
	
	public StraggerRecyclerAdapter(List<String> data) {
		super();
		this.data = data;
		heights=new ArrayList<Integer>();
		for(int i=0;i<data.size();i++){
			heights.add((int)(100+Math.random()*300));
		}
	}

	//用于创建组件,以ViewHolder返回
	@Override
	public StraggerViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		//获取列表项组件
		View item=LayoutInflater.from(arg0.getContext()).inflate(R.layout.list_item, arg0,false);
		return new StraggerViewHolder(item);
	}	
	
	//用于把数据绑定到组件中
	@Override
	public void onBindViewHolder(StraggerViewHolder arg0, int arg1) {
		//设置随机的高度
		LayoutParams params=arg0.textView.getLayoutParams();
		params.height=heights.get(arg1);
		//获取当前列表项中显示的数据
		final String rowData=data.get(arg1);
		//设置要显示的数据
		arg0.textView.setText(rowData);
		arg0.itemView.setTag(rowData);
	}
	
	@Override
	public int getItemCount() {
		return data.size();
	}
	
	/*
	 * 保存每个每个列表项中使用的组件
	 */
	public static class StraggerViewHolder extends RecyclerView.ViewHolder{
		private final TextView textView;

		public StraggerViewHolder(View itemView) {
			super(itemView);
			textView=(TextView)itemView.findViewById(R.id.textview);
		}
	}
}
