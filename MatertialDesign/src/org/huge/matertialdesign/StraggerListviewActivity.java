package org.huge.matertialdesign;

import java.util.ArrayList;

import org.huge.adapter.StraggerRecyclerAdapter;

import android.app.Activity;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;

/*
 * 类似WP的磁贴，可以大小不一的显示列表项
 */
public class StraggerListviewActivity extends Activity{
	private RecyclerView recyclerView;//新版Listview
	private View deleteBar;
	private ImageButton fabView;
	private ArrayList<String> data;//要显示的数据源
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData(20);
		initView();
	}
	
	private void initView() {
		deleteBar=(View)findViewById(R.id.deletebar);
		//创建右下角圆形按钮,将其他形状剪切成圆形
		ViewOutlineProvider provider=new ViewOutlineProvider() {
			@Override
			public void getOutline(View view, Outline outline) {
				//获取按钮尺寸
				int fabSize=getResources().getDimensionPixelSize(R.dimen.fab_size);
				//设置轮廓的尺寸
				outline.setOval(-4,-4,fabSize+2,fabSize+2);
			}
		};
		fabView=(ImageButton)findViewById(R.id.fab_add);//右下角添加按钮
		fabView.setOutlineProvider(provider);//绑定view和轮廓提供者，自动裁剪为圆形
		
		//获取RecyclerView,使用布局管理器设置布局方向
		recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
		final StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(manager);
		//设置adapter
		final StraggerRecyclerAdapter adapter=new StraggerRecyclerAdapter(data);
		recyclerView.setAdapter(adapter);
	}

	//初始化数据填充
	public void initData(int size){
		data=new ArrayList<String>();
		for(int i=0;i<size;i++){
			data.add("新的列表项<"+i+">");
		}
	}
}
