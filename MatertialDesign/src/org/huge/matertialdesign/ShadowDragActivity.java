package org.huge.matertialdesign;

import org.huge.LayoutView.DragFrameLayout;
import org.huge.LayoutView.DragFrameLayout.DragFrameLayoutController;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ShadowDragActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DragFrameLayout layout=(DragFrameLayout)findViewById(R.id.draglayout);
		final View view=(View)findViewById(R.id.shapedrag);
		//设置拖动里面的组件时的控制器
		layout.setDragFrameController(new DragFrameLayoutController() {
			@Override
			public void onDragDrop(boolean captured) {
				view.animate().translationZ(captured?100:20).setDuration(100);
			}
		});
	}
}
