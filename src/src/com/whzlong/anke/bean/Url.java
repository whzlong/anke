package com.whzlong.anke.bean;

import java.io.Serializable;

public class Url implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";

	public static final String PROTOCOL = "http://";
	public static final String DEFAULT_URI_IP_PORT = "116.228.199.86" + ":"
			+ "8082";

	// 身份验证
	public static String URL_VERIFY_IDENTIFY = "/restservice.svc/ChkTelCode/[%1]";
	public static final String KEY_WORD_AUTHENTICATION = "authenticationResult";
	// 请求版本信息
	public static final String APP_VERSION = "/RESTService.svc/Ver";
	// 节能数据查询
	public static String URL_ENERGY_SAVING_DATA = "/RESTService.svc/JNData/[%1]/[%2]/[%3]";
	// 钢厂信息
	public static String URL_FACTORY_INFO = "/RESTService.svc/Factory";
	// 实时数据
	public static String URL_REAL_TIME_DATA = "/RESTService.svc/CData/[%1]";
	// 历史警告数据查询
	public static String URL_HISTORY_WARNING_INFO = "/RESTService.svc/Warn/[%1]/[%2]/[%3]";
	// 获取轮查时间段内需要提醒的报警信息
	public static String URL_HISTORY_WARNING_REMIND = "/RESTService.svc/NewWarn";
	//public static String URL_HISTORY_WARNING_REMIND = "/RESTService.svc/NewWarnNoData";
	//项目信息
	public static String URL_PROJECTS = "/RESTService.svc/Project/[%1]";

}
