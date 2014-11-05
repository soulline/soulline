package com.asag.serial;

import android.app.Activity;  
import android.content.Context;  
import android.graphics.drawable.ColorDrawable;  
import android.view.LayoutInflater;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.View.OnTouchListener;  
import android.view.ViewGroup.LayoutParams;  
import android.widget.Button;  
import android.widget.PopupWindow;  
import android.widget.TextView;
   
public class FilePopupWindow extends PopupWindow {  
   
   
	public TextView lujing,lianjie,tuichu;
 
    private View mMenuView;  
   
    public FilePopupWindow(Activity context,OnClickListener itemsOnClick) {  
        super(context);  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        
        mMenuView = inflater.inflate(R.layout.file_pop_tab, null);  
        lujing = (TextView)mMenuView.findViewById(R.id.lujing);
		lianjie = (TextView)mMenuView.findViewById(R.id.lianjie);
		tuichu = (TextView)mMenuView.findViewById(R.id.tuichu);
       
		
		lujing.setOnClickListener(itemsOnClick);  
		lianjie.setOnClickListener(itemsOnClick);  
		tuichu.setOnClickListener(itemsOnClick);  
		
        this.setContentView(mMenuView);  
        this.setWidth(150);  
        this.setHeight(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
//        this.setAnimationStyle(R.style.AnimBottom);  
//        ColorDrawable dw = new ColorDrawable(0xb0000000);  
 //        this.setBackgroundDrawable(dw);  
  //        mMenuView.setOnTouchListener(new OnTouchListener() {  
//               
//            public boolean onTouch(View v, MotionEvent event) {  
//                   
//                int height = mMenuView.findViewById(R.id.file_pop).getTop();  
//                int y=(int) event.getY();  
//                if(event.getAction()==MotionEvent.ACTION_UP){  
//                    if(y<height){  
//                        dismiss();  
//                    }  
//                }                 
//                return true;  
//            }  
//        });  
   
    }  
   
}  