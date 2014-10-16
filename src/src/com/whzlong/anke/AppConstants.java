package com.whzlong.anke;

public class AppConstants {

	public static final int STOP = 0x10000;
	public static final int ERROR = 0x20000;
	
	//系统设置界面
	//报警炼钢厂选择
	public static final String SELECTED_WARNING_FACTORY_KEY = "selectedWarningFactoryCode";
	public static final String SELECTED_WARNING_FACTORY_NAME = "selectedWarningFactoryName";
	
	//选择的报警时间段
	public static final String SELECTED_TIME_AREA = "selectedTimeArea";
	//选择的轮询时间间隔
	public static final String SELECTED_POLLING_INTERVAL_INFO = "selectedPollingIntervalInfo";
	public static final int SELECTED_POLLING_INTERVAL = 10;
	
	public static final String MINUTE_UNIT = "分钟";
	
	public static final String URI_IP_PORT = "URI_IP_PORT";
	public static final String PROTOCOL = "http://";
	public static final String PATH1 = "/restservice.svc/factory";
}
