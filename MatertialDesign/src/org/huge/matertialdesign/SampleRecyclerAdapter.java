package org.huge.matertialdesign;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*
 * 继承RecyclerView.Adapter实现RecyclerView要使用的adapter
 */
public class SampleRecyclerAdapter extends RecyclerView.Adapter<SampleRecyclerAdapter.ViewHolder> {
	private List<String> data=new ArrayList<String>();
	
	public SampleRecyclerAdapter(List<String> data) {
		super();
		this.data = data;
	}

	//用于创建组件,以ViewHolder返回
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		//获取列表项组件
		View item=LayoutInflater.from(arg0.getContext()).inflate(R.layout.list_item, arg0,false);
		return new ViewHolder(item);
	}	
	
	//用于把数据绑定到组件中
	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		//获取当前列表项中显示的数据
		final String rowData=data.get(arg1);
		//设置要显示的数据
		arg0.textView.setText(rowData);
		arg0.itemView.setTag(rowData);
	}
	
	//删除指定的列表项
	public void removeData(int position){
		data.remove(position);
		notifyItemRemoved(position);//通知RecyclerView某个位置的项被删除
	}
	
	//添加指定的列表项
	public void addItem(int position){
		data.add(position, "新的列表项 "+new Random().nextInt(1000));
		notifyItemInserted(position);//通知某位置添加项
	}
	
	@Override
	public int getItemCount() {
		return data.size();
	}
	
	/*
	 * 保存每个每个列表项中使用的组件
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder{
		private final TextView textView;

		public ViewHolder(View itemView) {
			super(itemView);
			textView=(TextView)itemView.findViewById(R.id.textview);
		}
	}
}
