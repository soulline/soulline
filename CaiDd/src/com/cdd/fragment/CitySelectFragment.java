package com.cdd.fragment;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.minepage.CityListAdapter;
import com.cdd.mode.CityItemEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.GetCityListOp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CitySelectFragment extends DialogFragment implements OnClickListener{

	private View view;
	
	private ListView cityListview;
	
	private CityListAdapter adapter;
	
	private BaseFragmentListener listener;
	
	public CitySelectFragment() {
		super();
	}

	public void addFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.city_select_fragment, null);
		initView();
		getCityList();
		return view;
	}
	
	private void initView() {
		view.findViewById(R.id.city_select_bg_layout).setOnClickListener(this);
		cityListview = (ListView) view.findViewById(R.id.city_listview);
		view.findViewById(R.id.city_cancel).setOnClickListener(this);
		view.findViewById(R.id.city_ok).setOnClickListener(this);
		cityListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (adapter != null && listener != null) {
					CityItemEntry city = adapter.getItem(position);
					listener.onCallBack(city);
					dismissAllowingStateLoss();
				}
				
			}
		});
	}
	
	private void getCityList() {
		final GetCityListOp getCityOp = new GetCityListOp(getActivity());
		getCityOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				final ArrayList<CityItemEntry> list = getCityOp.getCityList();
				if (list.size() > 0 && getActivity() instanceof BaseActivity) {
					((BaseActivity) getActivity()).handler.post(new Runnable() {
						
						@Override
						public void run() {
							initCityAdapter(list);
						}
					});
				}
			}
		});
	}
	
	private void initCityAdapter(ArrayList<CityItemEntry> list) {
		if (adapter == null) {
			adapter = new CityListAdapter(getActivity());
			adapter.addData(list);
			cityListview.setAdapter(adapter);
		} else {
			adapter.clear();
			adapter.addData(list);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.city_select_bg_layout:
			dismissAllowingStateLoss();
			break;

		case R.id.city_cancel:
			dismissAllowingStateLoss();
			break;
			
		case R.id.city_ok:
			dismissAllowingStateLoss();
			break;

		default:
			break;
		}
		
	}
	
}
