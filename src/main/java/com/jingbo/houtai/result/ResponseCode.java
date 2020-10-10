package com.jingbo.houtai.result;

public class ResponseCode {

	// 成功
	public static final int SUCCESS = 200;
	// 失败
	public static final int FAIL = 0;
	// 未登录
	public static final int NO_LOGIN = -1;
	// 没有权限
	public static final int NO_PRIVILEGE = -2;
	// 非法字符
	public static final int ILLEGAL_CHARS = -3;
	// 非法参数
	public static final int ILLEGAL_PARAMS = -5;
	//重复添加，请确认后添加
	public static final int DUPLICATEKEY = -7;
	//结束
	public static final int FINISHED = 2;
	//过期
	public static final int EXPIRED=3;
	//文件名过长
	public static final int FILENAME_TOO_LONG=5;
	//任务进行中
	public static final int TASK_RUNNING=6;

	public static final int PROXY_USER_NULL=7;
	//notebook-session为空
	public static final int NOTE_SESSION_NULL=8;
}