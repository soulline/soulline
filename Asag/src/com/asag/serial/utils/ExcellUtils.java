package com.asag.serial.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.util.Log;

import com.asag.serial.mode.CheckDetailItem;
import com.asag.serial.mode.PointItemRecord;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcellUtils {
	
	
	public static final  String sdcardfilePath = "/storage/sdcard1/";
	
	public static String upanfilePath = "/storage/udisk1/disk-1/";

	public static void writeExcell(List list, String dirName, String fileName, String sheetName) throws Exception {
		File fileSd = new File(sdcardfilePath);
		String path = "";
		Log.d("zhao", "sdcard isDirectory : " + fileSd.isDirectory());
		if (fileSd.isDirectory()) {
			path = sdcardfilePath;
		} else {
			File upanFile = new File(upanfilePath);
			Log.d("zhao", "upan isDirectory : " + upanFile.isDirectory());
			if (upanFile.isDirectory()) {
				path = upanfilePath;
			}
		}
		File newFile = new File(path, dirName);
		if (!newFile.isDirectory()) {
			newFile.mkdir();
		}
		WritableWorkbook book= Workbook.createWorkbook(new File(newFile.getAbsolutePath() + "/" + fileName + ".xls")); 
        WritableSheet sheet=book.createSheet(sheetName, 0); 
        writeDataToSheet(sheet, list);
        book.write(); 
        book.close();
	}
	
	public static List getPointCheckList(CheckDetailItem checkDetail) {
		List list = new ArrayList();
		List<String> row1 = new ArrayList<String>();
		row1.add("一、基础信息");
		List<String> row2 = new ArrayList<String>();
		row2.add("仓号");
		row2.add("粮种");
		row2.add("数量");
		row2.add("水分%");
		row2.add("产地");
		row2.add("入库时间");
		row2.add("检测时间");
		List<String> row3 = new ArrayList<String>();
		row3.add(checkDetail.canghao);
		row3.add(checkDetail.liangzhong);
		row3.add(checkDetail.shuliang);
		row3.add(checkDetail.shuifen);
		row3.add(checkDetail.chandi);
		row3.add(checkDetail.rukuDate);
		row3.add(checkDetail.checkDate);
		List<String> row4 = new ArrayList<String>();
		row4.add("二、检测结果");
		List<String> row5 = new ArrayList<String>();
		row5.add("编号");
		row5.add("CO₂/ppm");
		row5.add("RH/%");
		row5.add("T/℃");
		row5.add("SSI");
		row5.add("MMI");
//		row5.add("安全状况");
		list.add(row1);
		list.add(row2);
		list.add(row3);
		list.add(row4);
		list.add(row5);
		for (PointItemRecord point : checkDetail.pointList) {
			List<String> row = new ArrayList<String>();
			row.add(point.wayNum);
			row.add(point.co2);
			row.add(point.rhValue);
			row.add(point.tValue);
			row.add(getSSIStatus(point.status));
			row.add(getMMiStatus(point.mmi));
//			row.add(point.status + "");
			list.add(row);
		}
		List<String> row6 = new ArrayList<String>();
		row6.add("三、储粮状况");
		List<String> row7 = new ArrayList<String>();
		row7.add("疑似点（CP）");
		row7.add(checkDetail.chuliangState);
		List<String> row8 = new ArrayList<String>();
		row8.add("四、水分迁移");
		List<String> row9 = new ArrayList<String>();
		row9.add("潜在点（PP）");
		row9.add(checkDetail.shuifenState);
		list.add(row6);
		list.add(row7);
		list.add(row8);
		list.add(row9);
		return list;
	}
	
	private static String getMMiStatus(String mmi) {
		String status = "I级";
		try {
			float mmiF = Float.valueOf(mmi);
			if (mmiF > 0.0f && mmiF <= 6.0f) {
				status = "I级";
			} else if (mmiF > 6.0f && mmiF <= 10.0f) {
				status = "II级";
			} else if (mmiF > 10.0f && mmiF <= 20.0f) {
				status = "III级";
			} else if (mmiF > 20.0f) {
				status = "IV级";
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return "I级";
		}
		return status;
	}
	
	private static String getSSIStatus(int status) {
		String statusStr = "I级";
		switch (status) {
		case 1:
			statusStr = "I级";
			break;
			
		case 2:
			statusStr = "II级";
			break;
			
		case 3:
			statusStr = "III级";
			break;
			
		case 4:
			statusStr = "IV级";
			break;

		default:
			break;
		}
		return statusStr;
	}
	
	public static List getCanganCheckList(CheckDetailItem checkDetail) {
		List list = new ArrayList();
		List<String> row1 = new ArrayList<String>();
		row1.add("日期");
		row1.add("安全状况");
		row1.add("PH₃/ppm");
		row1.add("O₂/%");
		row1.add("CO₂/ppm");
		list.add(row1);
		if (checkDetail.pointList.size() > 0) {
			List<String> row2 = new ArrayList<String>();
			row2.add(checkDetail.checkDate);
			row2.add(checkDetail.pointList.get(0).status + "");
			row2.add(checkDetail.pointList.get(0).ph3Value);
			row2.add(checkDetail.pointList.get(0).o2Value);
			row2.add(checkDetail.pointList.get(0).co2);
			list.add(row2);
		}
		return list;
	}
	
	public static void writeDataToSheet(WritableSheet sheet,List<List<String>> list) throws Exception{
        int columnBestWidth[]=new  int[list.get(0).size()];    //保存最佳列宽数据的数组
        
        for(int i=0;i<list.size();i++){
            List<String> row=list.get(i);
            for(int j=0;j<row.size();j++){
                 sheet.addCell(new Label(j,i,row.get(j)));
                 
                 /*int width=row.get(j).length()+getChineseNum(row.get(j));    ///汉字占2个单位长度
                 if(columnBestWidth[j]<width)    ///求取到目前为止的最佳列宽
                     columnBestWidth[j]=width;*/
            }
        }
        
        for(int i=0;i<columnBestWidth.length;i++){    ///设置每列宽
            sheet.setColumnView(i, 10);
        }
    }
    
    public static int getChineseNum(String context){    ///统计context中是汉字的个数
        int lenOfChinese=0;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");    //汉字的Unicode编码范围
        Matcher m = p.matcher(context);
        while(m.find()){
            lenOfChinese++;
        }
        return lenOfChinese;
    }
}
