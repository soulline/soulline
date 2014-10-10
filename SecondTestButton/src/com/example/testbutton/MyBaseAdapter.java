package com.example.testbutton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class MyBaseAdapter extends BaseAdapter{
 //иообнд 
    Context  context; 
    List<String> list=new ArrayList<String>();
    public MyBaseAdapter(Context context,List<String> list){ 
        this.context=context; 
        this.list = list;
    } 
	@Override
	public int getCount() {
	// TODO Auto-generated method stub
	return list.size();
	}
	
	
	@Override
	public Object getItem(int arg0) {
	// TODO Auto-generated method stub
	return list.get(arg0);
	}
	
	
	@Override
	public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
	}
	
	
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	LinearLayout layout=new LinearLayout(context); 
	         LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 
	                 ViewGroup.LayoutParams.FILL_PARENT); 
	         layout.setOrientation(LinearLayout.HORIZONTAL); 
	         LinearLayout.LayoutParams params2=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
	                 ViewGroup.LayoutParams.WRAP_CONTENT); 
	         
	         TextView  textView=new TextView(context); 
	         textView.setText(this.getItem(position).toString()); 
	         textView.setTextSize(25);
				textView.setTextColor(R.color.select);
	         
	         layout.addView(textView, params2); 
	         
	         
	return layout;
}
 

}