package org.huge.outlineprovider;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

/*
 * 轮廓提供者，提供轮廓的范围
 */
public class ClipOutlineProvider extends ViewOutlineProvider {
	//主要实现这个方法
	@Override
	public void getOutline(View view, Outline outline) {
		//准备计算轮廓距离View边缘的距离（margin）
		final int margin=Math.min(view.getWidth(), view.getHeight())/10;
		//设置轮廓的范围——是相对于View的，这里设置的圆角矩形
		outline.setRoundRect(margin, margin, view.getWidth()-margin, view.getHeight()-margin, margin/2);
		//设置轮廓为圆形
		//outline.setOval(margin, margin, 700, 700);
	}

}
