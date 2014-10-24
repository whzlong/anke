package com.whzlong.anke;

public class AppConstants {

	public static final int STOP = 0x10000;
	public static final int ERROR = 0x20000;
	 /**
     * 消息参数引导
     */
	public static final String MESSAGE_PARAM_INDUCTOR = "%";

    /**
     * 参数开始
     */
	public static final String PARAM_BEGIN = "[";

    /**
     * 参数结束
     */
	public static final String PARAM_END = "]";
	
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
	
	public static final String EMPTY = "";
	
	
	//网络处理相关
	public static final int OK = 0x10000;
	public static final int NG = 0x10001;
	//网络异常错误
	public static final int ERROR1 = 0x20000;
	//转换为JSON时的错误
	public static final int ERROR2 = 0x20001;
	public static final int ERROR3 = 0x20002;
	
	public static final String ZERO = "0";
	public static final String ONE = "1";
	
	//网络请求时需要使用的固定文字
	public static final String URI_IP_PORT = "URI_IP_PORT";
	public static final String KEY_WORD_VERSION_NO = "versionNo";
	public static final String KEY_WORD_VERSION_NAME = "versionName";
	public static final String KEY_WORD_DOWNLOAD_URL = "downloadUrl";
	
	
}
