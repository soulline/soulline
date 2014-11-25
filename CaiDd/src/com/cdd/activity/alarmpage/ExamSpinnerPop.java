package com.cdd.activity.alarmpage;



import java.util.ArrayList;

import com.cdd.R;
import com.cdd.mode.ForumItem;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

public class ExamSpinnerPop extends PopupWindow {
	
	private Context context;

	private View view;
	
	private OnExamClickListener listener;
	
	private ListView spinnerList;
	
	
	public interface OnExamClickListener {
		public void onClick(ForumItem item);
	}
	
	private ExamSpinnerAdapter adapter;
	
	public ExamSpinnerPop(Context context, OnExamClickListener listener, ArrayList<ForumItem> list) {

		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.spinner_pop_menu, null);
		this.listener = listener;
		this.setContentView(view);
		this.setWidth(380);
		this.setHeight(500);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
        this.update();  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview);
        initView();
        adapter = new ExamSpinnerAdapter(context);
        adapter.addData(list);
        spinnerList.setAdapter(adapter);
	
	}
	
	private void initView() {
		spinnerList = (ListView) view.findViewById(R.id.spinner_list);
		spinnerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ForumItem item = adapter.getItem(position);
				if (listener != null) {
					listener.onClick(item);
					dismiss();
				}
			}
		});
	}
	
	public void showPopupWindow(View parent) {  
        if (!this.isShowing()) {  
            this.showAsDropDown(parent, 10, 0);  
        } else {  
            this.dismiss();  
        }  
    }
}
