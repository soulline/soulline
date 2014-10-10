package com.yyl.download;

import java.io.File;

import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.download.YylDownloadManger.DownloadListener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DownLoadFragment extends DialogFragment {

	private Context context;
	
	private View view;
	
	private TextView downloadTitle, downloadPersent;
	
	private String urlString = "";
	
	public DownLoadFragment(Context context, Bundle b) {
		this.context = context;
		urlString = b.getString("download_url");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.download_fragment, null);
		initView();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				startDownload();
			}
		}).start();
		return view;
	}
	
	private void initView() {
		downloadTitle = (TextView) view.findViewById(R.id.download_title);
		downloadPersent = (TextView) view.findViewById(R.id.download_persent);
	}
	
	private void installAPK() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW); 
        String savePathString = Environment.getExternalStorageDirectory()+"/yyl/"+"yaoyl.apk";
        File file = new File(savePathString);
        Uri abc = Uri.fromFile(file);
        intent.setDataAndType(abc,  
                "application/vnd.android.package-archive");  
        startActivity(intent); 
	}

	private void startDownload() {
		if (!TextUtils.isEmpty(urlString)) {
			YylDownloadManger downManger = new YylDownloadManger(new DownloadListener() {
				
				@Override
				public void onChange(int state, int persent) {
					final int newPersent = persent;
					switch (state) {
					case 0:
						if (context instanceof BaseActivity) {
							((BaseActivity) context).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									downloadTitle.setText(getString(R.string.download_start));
									downloadPersent.setText("0%");
								}
							});
						}
						break;
					case 1:
						if (context instanceof BaseActivity) {
							((BaseActivity) context).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									downloadTitle.setText(getString(R.string.download_doing));
									downloadPersent.setText(newPersent + "%");
									
								}
							});
						}
						break;
					case 2:
						if (context instanceof BaseActivity) {
							((BaseActivity) context).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									downloadTitle.setText(getString(R.string.download_done));
									downloadPersent.setText("100%");
								}
							});
						}
						installAPK();
						dismissAllowingStateLoss();
						break;
					case 3:
						if (context instanceof BaseActivity) {
							((BaseActivity) context).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									downloadTitle.setText(getString(R.string.download_failed));
									downloadPersent.setText("0%");
								}
							});
						}
						dismissAllowingStateLoss();
						break;

					default:
						break;
					}
				}
			});
			downManger.onDownload(urlString);
		}
	}
}
