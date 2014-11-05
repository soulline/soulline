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
   
public class FunctionPopupWindow extends PopupWindow {  
   
	public Button liangan1,liangan2,canganjiance;
 
    private View mMenuView;  
   
    public FunctionPopupWindow(Activity context,OnClickListener itemsOnClick) {  
        super(context);  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        
        mMenuView = inflater.inflate(R.layout.function_pop_tab, null);  
        liangan1 = (Button)mMenuView.findViewById(R.id.liangan1);
		liangan2 = (Button)mMenuView.findViewById(R.id.liangan2);
		canganjiance = (Button)mMenuView.findViewById(R.id.canganjiance);
        
		liangan1.setOnClickListener(itemsOnClick);  
		liangan2.setOnClickListener(itemsOnClick);  
		canganjiance.setOnClickListener(itemsOnClick);  
		

        this.setContentView(mMenuView);  
        this.setWidth(150);  
        this.setHeight(110);  
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