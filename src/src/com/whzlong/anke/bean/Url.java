package com.whzlong.anke.bean;

import java.io.Serializable;

public class Url implements Serializable {
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";

	private final static String URL_SPLITTER = "/";
	private final static String URL_UNDERLINE = "_";

	public static final String PROTOCOL = "http://";
	public static final String DEFAULT_URI_IP_PORT = "101.231.219.254" + ":"
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
	public static String URL_HISTORY_WARNING_INFO = "/RESTService.svc/JNData/[%1]/[%2]";

}
