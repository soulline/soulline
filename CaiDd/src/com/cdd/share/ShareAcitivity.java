package com.cdd.share;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cdd.R;
import com.cdd.base.BaseActivity;

public class ShareAcitivity extends BaseActivity implements OnItemClickListener{

	private GridView shareGrid;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.share_activity);
		initView();
		initContent();
	}
	
	private void initView() {
		shareGrid = (GridView) findViewById(R.id.share_grid);
		shareGrid.setOnItemClickListener(this);
	}
	
	private void initContent() {
		CddShareAdapter adapter = new CddShareAdapter(context);
		String[] shareTitles = getResources().getStringArray(R.array.share_array);
		adapter.addData(shareTitles);
		shareGrid.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

}
