package com.jingbo.houtai.result;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liufeng
 * @date 2020年7月12日
 */
public class JsonResult {

	private int code = ResponseCode.SUCCESS;
	/** 业务异常消息 */
	private String errorMessage;
	/** 其他异常消息 */
	private String otherMessage;
	/** 重构之后的错误码，所有业务的错误提示都用错误码返回给前端*/
	private int errorCode = -1;
	private Object data;
	private int queryIndex;

	private JsonResult() {
	}

	private JsonResult(int code, Object data) {
		this(code, data, -1);
	}

	/**
	 * @param code
	 *            返回代码
	 * @param data
	 *            返回内容
	 * @param queryIndex
	 *            如果为查询部分，则需要带上queryIdex
	 */
	private JsonResult(int code, Object data, int queryIndex) {
		this.code = code;
		this.data = data;
		if (queryIndex != -1) {
			this.queryIndex = queryIndex;
		}
	}

	/**
	 * 返回成功结果
	 * 
	 * @return
	 */
	public static JsonResult success() {
		return new JsonResult();
	}

	/**
	 * 返回成功结果
	 * 
	 * @return
	 */
	public static JsonResult success(Object data) {
		return new JsonResult(ResponseCode.SUCCESS, data);
	}

	/**
	 * 返回成功结果
	 * 
	 * @return
	 */
	public static JsonResult success(Object data, int queryIndex) {
		return new JsonResult(ResponseCode.SUCCESS, data, queryIndex);
	}

	/**
	 * 返回失败结果
	 * 
	 * @return
	 */
	public static JsonResult fail() {
		return new JsonResult(ResponseCode.FAIL, null);
	}

	/**
	 * 返回失败结果
	 *
	 * @return
	 */
	public static JsonResult fail(int code,String errorMessage) {
		JsonResult jsonResult = new JsonResult(code, null);
		jsonResult.setErrorMessage(errorMessage);
		return jsonResult;
	}

	/**
	 * 返回失败结果
	 * 
	 * @return
	 */
	public static JsonResult fail(String errorMessage) {
		JsonResult rst = new JsonResult(ResponseCode.FAIL, null);
		rst.setErrorMessage(errorMessage);

		return rst;
	}

	/**
	 * 返回失败结果
	 * 
	 * @return
	 */
	public static JsonResult fail(Throwable e, Object data) {
		JsonResult rst = new JsonResult(ResponseCode.FAIL, data);
		rst.buildMessage(e);

		return rst;
	}

	/**
	 * 返回失败结果
	 *
	 * @return
	 */
	public static JsonResult fail(Throwable e) {
		JsonResult rst = new JsonResult(ResponseCode.FAIL, null);
		rst.buildMessage(e);

		return rst;
	}

	/**
	 * 返回失败结果
	 * 
	 * @return
	 */
	public static JsonResult fail(String errorMessage, int queryIndex) {
		JsonResult rst = new JsonResult(ResponseCode.FAIL, null, queryIndex);
		rst.setErrorMessage(errorMessage);

		return rst;
	}

	/**
	 * 返回失败结果
	 * 
	 * @return
	 */
	public static JsonResult fail(Throwable e, int queryIndex) {
		JsonResult rst = new JsonResult(ResponseCode.FAIL, null, queryIndex);
		rst.buildMessage(e);

		return rst;
	}

	/**
	 * 返回自定义结果，不是成功或失败的
	 * 
	 * @return
	 */
	public static JsonResult custom(int code, Object data, int queryIndex) {
		return new JsonResult(code, data, queryIndex);
	}

	/**
	 * 设置异常消息<P>
	 * 业务异常放到errorMessage中，其他异常放到otherMessage中
	 * @param e
	 */
	private void buildMessage(Throwable e) {
		this.setOtherMessage(e.getMessage());
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = formateException(errorMessage);
	}

	public int getQueryIndex() {
		return queryIndex;
	}

	public void setQueryIndex(int queryIndex) {
		this.queryIndex = queryIndex;
	}

	public String getOtherMessage() {
		return otherMessage;
	}

	public void setOtherMessage(String otherMessage) {
		this.otherMessage = otherMessage;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	/**
	 * 暂时保留，让客户端做国际化，以后会去掉，由服务端做国际化
	 * @param excption
	 * @return
	 */
	private String formateException(String excption){
		if(excption==null){
			return "NullPointer";
		}
		if(excption.toString().contains("Permission denied")){
			return "Permission denied";
		}else if(excption.toString().contains("timed out")){
			return "Timed out";
		}else if(excption.toString().contains("Duplicate entry")){
			return "Duplicate key";
		}else{
			return excption.toString();
		}
	}

	public static String formatMessagekey(String key, String... args) {
		Map result = new HashMap<String,String>();
		Gson gson = new Gson();
		result.put("code",key);
		result.put("params",args);
		return gson.toJson(result);
	}

}
