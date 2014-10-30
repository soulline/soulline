package com.cdd.sqpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.mode.ForumItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountingHotAdapter extends ArrayAdapter<ForumItem> {

	private Context context;
	
	public AccountingHotAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}
	
	public void addData(ArrayList<ForumItem> list) {
		synchronized (list) {
			for (ForumItem item : list) {
				add(item);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.accounting_listview_item, null);
			holder.accountingTx = (TextView) convertView.findViewById(R.id.accounting_tx);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.accountingTx.setText(getItem(position).name);
		return convertView;
	}

	
	public class ViewHolder{
		public TextView accountingTx;
	}
	
}
