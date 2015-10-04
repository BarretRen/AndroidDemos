package org.huge.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.huge.matertialdesign.R;
import org.huge.matertialdesign.R.id;
import org.huge.matertialdesign.R.layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 继承RecyclerView.Adapter实现RecyclerView要使用的adapter
 */
public class SampleRecyclerAdapter extends RecyclerView.Adapter<SampleRecyclerAdapter.ViewHolder> {
	private Context context;
	private List<String> data=new ArrayList<String>();
	private onItemCLickListener listener;
	
	//自定义的接口，用于activity实现点击和长按的事件处理
	public interface onItemCLickListener{
		void onItemClick(View view,int position);
		void onItemLongClick(View view,int position);
	}
	
	public SampleRecyclerAdapter(Context context,List<String> data) {
		super();
		this.context=context;
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
	public void onBindViewHolder(final ViewHolder arg0,int arg1) {
		//获取当前列表项中显示的数据
		final String rowData=data.get(arg1);
		//设置要显示的数据
		arg0.textView.setText(rowData);
		arg0.itemView.setTag(rowData);
		//在这里设置点击监听
		arg0.itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "点击位置:"+arg0.getPosition(), 1).show();
			}
		});
		arg0.itemView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(context, "长按位置:"+arg0.getPosition(), 1).show();
				return true;
			}
		});
	}
	
	//删除指定的列表项
	public void removeData(int position){
		data.remove(position);
		notifyItemRemoved(position);//通知RecyclerView某个位置的项被删除
	}
	
	//添加指定的列表项
	public void addItem(int position){
		data.add(position, "新的列表项 "+new Random().nextInt(1000));
		notifyItemInserted(position);//通知某位置添加项,这种方式列表项的索引是不会自动刷新的
	}
	
	@Override
	public int getItemCount() {
		return data.size();
	}
	
	public void setListener(onItemCLickListener listener) {
		this.listener = listener;
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
