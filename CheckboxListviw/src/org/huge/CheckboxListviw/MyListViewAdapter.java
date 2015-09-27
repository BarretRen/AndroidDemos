package org.huge.CheckboxListviw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tiger on 2015/9/27.
 */
public class MyListViewAdapter extends BaseAdapter{
    //������ݵ�list
    private ArrayList<Food> foodList;
    //����checkbox��ѡ��״̬
    private static HashMap<Integer,Boolean> isSelected;
    private Context context;
    private boolean isShowed=false;
    private LayoutInflater inflater=null;

    public MyListViewAdapter(ArrayList<Food> foodList, Context context,Boolean isShow) {
        this.foodList = foodList;
        this.context = context;
        this.isShowed=isShow;
        inflater=LayoutInflater.from(context);
        isSelected=new HashMap<Integer, Boolean>();
        //��ʼ������
        for (int i=0;i<foodList.size();i++){
            getIsSelected().put(i,false);
        }
    }
    
    
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        MyListViewAdapter.isSelected = isSelected;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            //���벼���ļ�
            convertView=inflater.inflate(R.layout.item,null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.food_img);
            holder.txt1=(TextView)convertView.findViewById(R.id.food_name);
            holder.txt2=(TextView)convertView.findViewById(R.id.price);
            holder.cb=(CheckBox)convertView.findViewById(R.id.check_box);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        //��������䵽holder
        Food food=foodList.get(position);
        if (isShowed) {
			holder.cb.setVisibility(View.VISIBLE);
		}else{
			holder.cb.setVisibility(View.GONE);
		}
        holder.imageView.setImageResource(food.food_img);
        holder.txt1.setText(food.food_name);
        holder.txt2.setText(food.food_price);
        holder.cb.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static class ViewHolder{
        public TextView txt1;
        public TextView txt2;
        public ImageView imageView;
        public CheckBox cb;
    }
}
