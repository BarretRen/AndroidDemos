package org.huge.matertialdesign;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
/*
 * 继承ItemDecoration重新定义列表项的分隔条样式
 */
public class MyDefineDivider extends ItemDecoration {
	private static final int[] ATTRS={android.R.attr.listDivider};
	private Drawable divider;//分隔条样式
	
	public MyDefineDivider(Context context) {
		TypedArray array=context.obtainStyledAttributes(ATTRS);
		//获取系统提供的分隔条drawable对象
		divider=array.getDrawable(0);
		array.recycle();
	}
	
	//在该方法中绘制列表项的分隔条
	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, State state) {
		//获取列表项左边缘的距离
		int left=parent.getPaddingLeft();
		//获取列表项右边缘的距离
		int right=parent.getWidth()-parent.getPaddingRight();//组件宽度减去偏移距离
		//获取列表项总数
		int childCount=parent.getChildCount();
		//开始绘制所有列表项之间的分隔条
		for (int i = 0; i < childCount; i++) {
			//回去当前列表项
			View child=parent.getChildAt(i);
			//获取列表项布局参数信息
			RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)child.getLayoutParams();
			//计算分隔条左上角的纵坐标
			int top=child.getBottom()+params.bottomMargin;
			//计算分隔条右下角纵坐标
			int bottom=top+divider.getIntrinsicHeight();
			//设置分隔条位置
			divider.setBounds(left, top, right, bottom);
			//开始绘制列表项分隔条
			divider.draw(c);
		}
	}
	
}
