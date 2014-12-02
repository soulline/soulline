package com.asag.serial.utils;

public class CMDCode {

	public static final String STOP_CMD = "5A A5 04 00 FF FF";

	public static final String CD_LIANGAN_CHECK_1 = "5A A5 03 01 FF FF";

	public static final String CD_LIANGAN_CHECK_2 = "5A A5 03 02 01";
	
	public static final String CD_POINT_CHECK = "5A A5 03 02 FF FF";

	public static final String CD_CANGAN_CHECK = "5A A5 03 03 FF FF ";
 
	public static final String FF_LIANGAN_CHECK_1 = "5A A5 02 01 FF FF";

	public static final String FF_LIANGAN_CHECK_2 = "5A A5 02 02 FF FF";

	public static final String FF_CANGAN_CHECK = "5A A5 02 03 FF FF ";
	
	public static final String DATA_CANGHAO = "5A A5 02 01 01";
	
	public static final String DATA_LIANGZHONG = "5A A5 02 01 02";
	
	public static final String DATA_SHULIANG = "5A A5 02 01 03";
	
	public static final String DATA_SHUIFEN = "5A A5 02 01 04";
	
	public static final String DATA_RKSHIJIAN = "5A A5 02 01 05";
	
	public static final String DATA_CHANDI = "5A A5 02 01 06";
	
	public static final String RECEIVE_CO2 = "5A A5 09";
	
	public static final String RECEIVE_PH3 = "5A A5 0A";
	
	public static final String RECEIVE_O2 = "5A A5 0B";
	
	public static final String PAIKONG_HALF_TIME = "5A A5 03 01 01 FF FF";
	
	public static final String PAIKONG_END_TIME = "5A A5 03 01 02 FF FF";
	
	public static final String CHECK_TIME_HALF = "5A A5 03 01 03 FF FF";
	
	public static final String CHECK_TIME_END = "5A A5 03 01 04 FF FF";
	
	public static final String PREPARE_OK = "5A A5 02 01 07 FF FF";
	
	public static final String PREPARE_CANCLE = "5A A5 02 01 08 FF FF";
	
	public static final String PASSWAY_DATA = "5A A5 0C";
}
