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

	/**
	 * 系统设置界面
	 */
	public static final String SELECTED_WARNING_FACTORY_KEY = "selectedWarningFactoryCode";
	/**
	 * 报警炼钢厂选择
	 */
	public static final String SELECTED_WARNING_FACTORY_NAME = "selectedWarningFactoryName";

	/**
	 * 选择的报警时间段
	 */
	public static final String SELECTED_TIME_AREA = "selectedTimeArea";
	
	/**
	 * 选择的轮询时间间隔
	 */
	public static final String SELECTED_POLLING_INTERVAL_INFO = "selectedPollingIntervalInfo";
	
	/**
	 * 选择的轮询时间间隔
	 */
	public static final int SELECTED_POLLING_INTERVAL = 3;
	
	/**
	 * 时间单位(分)
	 */
	public static final String MINUTE_UNIT = "分钟";
	
	/**
	 * 空字符串
	 */
	public static final String EMPTY = "";

	/**
	 * 网络处理相关(处理正常的情况)
	 */
	public static final int OK = 0x10000;
	
	/**
	 * 网络处理相关(处理不正常的情况)
	 */
	public static final int NG = 0x10001;
	//
	/**
	 * 网络异常错误(网络连接有误)
	 */
	public static final int ERROR1 = 0x20000;

	/**
	 * 网络异常错误(转换为JSON时的错误)
	 */
	public static final int ERROR2 = 0x20001;
	
	/**
	 * 常量-0
	 */
	public static final String ZERO = "0";
	
	/**
	 * 常量-1
	 */
	public static final String ONE = "1";
	
	//网络请求时需要使用的固定文字
	
	/**
	 * 主机地址
	 */
	public static final String URI_IP_PORT = "URI_IP_PORT";
	
	/**
	 * 版本号
	 */
	public static final String KEY_WORD_VERSION_NO = "versionNo";
	
	/**
	 * 版本名称
	 */
	public static final String KEY_WORD_VERSION_NAME = "versionName";
	
	/**
	 * apk下载地址
	 */
	public static final String KEY_WORD_DOWNLOAD_URL = "downloadUrl";
	
	/**
	 * 使用钢厂信息选择界面时，各个主界面的区分(节能数据)
	 */
	public static final int ENERGY_SAVING = 0;
	
	/**
	 * 使用钢厂信息选择界面时，各个主界面的区分(实时数据)
	 */
	public static final int REAL_TIME = 1;
	
	/**
	 * 使用钢厂信息选择界面时，各个主界面的区分(警告信息)
	 */
	public static final int WARNING_INFO = 2;
	
	/**
	 * 当天产生警告信息的工厂的代码
	 */
	public static final String WARNING_FACTORY_CODES = "warningFactoryCodes";
	
	//网络认证
	public static final String AUTHENTICATION_RESULT = "authenticationResult";
	
	//代表通知界面
	public static final String NOTIFICATION = "previousPage";
	
	//获取警告信息时间段
	public static final int TIME_AREA_ZERO = 0;
	public static final int TIME_AREA_ONE = 1;
	public static final int TIME_AREA_TWO = 2;
	public static final int TIME_AREA_THREE = 3;
	
	// 显示在界面上的报警时间段
	public static final String[] TIME_AREA_NAME = { "00:00 ~ 08:00",
			"08:00 ~ 17:00", "17:00 ~ 24:00", "关闭" };
	public static final int[] TIME_AREA_POINT = { 0, 8, 17, 24 };
	
	//项目信息中被选中项目的代码
	public static final String SELECTED_PROJECT_CODE = "selectedProjectCode";
	//项目信息中被选中项目的名称
	public static final String SELECTED_PROJECT_NAME = "selectedProjectName";
	//钢厂信息中被选中钢厂的代码
	public static final String SELECTED_FACTORY_CODE = "selectedFactoryCode";
	//钢厂信息中被选中钢厂的名称
	public static final String SELECTED_FACTORY_NAME = "selectedFactoryName";
	//之前被选中的钢厂代码
	public static final String PREVIOUS_FACTORY_CODE = "previousFactoryCode";
}
