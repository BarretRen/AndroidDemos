package org.huge.chartenginedemo;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private String[] titles=new String[]{"济南","西安","广州","青岛"};//四个城市,图表中四组数据
	private List<double[]> x=new ArrayList<double[]>();//坐标值
	private List<double[]> values=new ArrayList<double[]>();//要显示的值
	private int[] colors=new int[]{Color.BLUE,Color.GREEN,Color.CYAN,Color.YELLOW};//每组数据的颜色
	private PointStyle[] styles=new PointStyle[]{PointStyle.CIRCLE,PointStyle.DIAMOND,PointStyle.TRIANGLE,PointStyle.SQUARE};//每组数据的显示样式

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化数据
		for(int i=0;i<titles.length;i++){
			x.add(new double[]{1,2,3,4,5,6,7,8,9,10,11,12});
		}
		values.add(new double[]{12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2, 13.9});
		values.add(new double[]{10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11});
		values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });
		values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10 });
		
		Button button=(Button)findViewById(R.id.start);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try{
					//设置渲染器，调整表格样式
					XYMultipleSeriesRenderer renderer=setChartSettings(colors,styles,"平均气温","月份","温度",0.5,12.5,-10,40,Color.LTGRAY,Color.LTGRAY);
					//设置数据集，填充表格
					XYMultipleSeriesDataset dataset=addXYSeries(titles,x,values,0);
					//通过图表，获取到view
					View view=ChartFactory.getLineChartView(MainActivity.this,dataset, renderer);
					MainActivity.this.setContentView(view);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	private XYMultipleSeriesRenderer setChartSettings(int[] colors,PointStyle[] styles,String title,String xTitle,String yTitle,double xMin,double xMax,double yMin,double yMax,int axesColor,int labelsColor){
		XYMultipleSeriesRenderer renderer=new XYMultipleSeriesRenderer();
		//添加四组数据的渲染
		for(int i=0;i<colors.length;i++){
			XYSeriesRenderer r=new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			r.setFillPoints(true);
			renderer.addSeriesRenderer(r);
		}
		//设置表格标题,和xy坐标轴的值
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[]{20,30,15,20});
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMax(xMax);
		renderer.setXAxisMin(xMin);
		renderer.setYAxisMax(yMax);
		renderer.setYAxisMin(yMin);
		renderer.setAxesColor(axesColor);
		renderer.setGridColor(Color.BLACK);//设置单元格颜色
		renderer.setLabelsColor(labelsColor);
		//设置xy格数，可以通过最大最小值自动形成分隔
		renderer.setXLabels(12);//设置X轴格数
		renderer.setYLabels(10);//设置Y轴格数
		renderer.setShowGrid(true);//显示网格
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		
		renderer.setZoomButtonsVisible(true);//缩放按钮
		renderer.setPanLimits(new double[]{-10,20,-10,40});
		renderer.setZoomLimits(new double[]{-10,20,-10,40});
		return renderer;
	}
	
	private XYMultipleSeriesDataset addXYSeries(String[] titles,List<double[]> xValues,List<double[]> yValue,int scale){
		XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
		for(int i=0;i<titles.length;i++){
			XYSeries series=new XYSeries(titles[i],scale);
			double[] x=xValues.get(i);
			double[] y=yValue.get(i);
			for(int k=0;k<x.length;k++){
				series.add(x[k], y[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}
}
