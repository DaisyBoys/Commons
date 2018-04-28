package webtools.myjson;

import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import webtools.org.common.pfcy.util.MD5Utils;


import BO.tools.InGeneralParam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
public class InJSONParamList {
	private String utf8CoverStr(String str){
		if(str==null)return "";
		String strRtn="";
		try {
			strRtn=URLDecoder.decode(str,"utf-8");
		} catch (Exception e) {
			try {
				return str;
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		
		return strRtn;
		
	}
	/**
	 * 返回入库参数对象
	 * */
	public Map<String,Object> getGsonMap(final String strJsonBASE64){
		Map<String, Object> retMap=new ConcurrentHashMap<String, Object>();
		try {
			Gson gson=new Gson();
			String jsonStr=this.getCovertBase64(strJsonBASE64);
			retMap = gson.fromJson(jsonStr,  
	                new TypeToken<Map<String, Object>>(){
					}.getType());
			return retMap;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return retMap;
	}
	private String getCovertBase64(String strJsonBASE64){
		strJsonBASE64=strJsonBASE64.replaceAll(" ", "+");
		String str = new String(Base64.decode(strJsonBASE64));//解析BASE64
		if(str!=null){
			//UNIT
			str= unicode2String(str);
			str=str.replaceAll("'", "''");
			str=utf8CoverStr(str);
		}
		return str;
	}
	public Map<String,Object> getGsonMapNoUnicode(final String strJsonBASE64){
		Map<String, Object> retMap=new ConcurrentHashMap<String, Object>();
		try {
			Gson gson=new Gson();
			String jsonStr=this.getCovertBase64NoUnicode(strJsonBASE64);
			retMap = gson.fromJson(jsonStr,  
	                new TypeToken<Map<String, Object>>(){
					}.getType());
			return retMap;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return retMap;
	}
	private String getCovertBase64NoUnicode(String strJsonBASE64){
		strJsonBASE64=strJsonBASE64.replaceAll(" ", "+");
		String str = new String(Base64.decode(strJsonBASE64));//解析BASE64
		if(str!=null){
			//UNIT
			//str= unicode2String(str);
			str=str.replaceAll("'", "''");
			str=utf8CoverStr(str);
		}
		return str;
	}
	/**
	 * 字符串转换unicode
	 */
	public  String string2Unicode(String string) {
	 
	    StringBuffer unicode = new StringBuffer();
	 
	    for (int i = 0; i < string.length(); i++) {
	 
	        // 取出每一个字符
	        char c = string.charAt(i);
	 
	        // 转换为unicode
	        unicode.append("\\u" + Integer.toHexString(c));
	    }
	 
	    return unicode.toString();
	}

	/**
	 * unicode 转字符串
	 */
	public  String unicode2String(String unicode) {
	 
	    StringBuffer string = new StringBuffer();
	 
	    String[] hex = unicode.split("\\\\u");
	 
	    for (int i = 1; i < hex.length; i++) {
	 
	        // 转换出每一个代码点
	        int data = Integer.parseInt(hex[i], 16);
	 
	        // 追加成string
	        string.append((char) data);
	    }
	 
	    return string.toString();
	}
	public static void main(String[] args) {
		String strJsonBASE64="eyJwYXJhbSI6W3sidHhuQW10IjoiMTAiLCJyZXNwQ29kZSI6IjAwIiwicGF5VHlwZSI6IjIiLCJiaWxsSWQiOiIxNzA5MjM1MTQ1OTg0MDEzODczOTkiLCJvcmRlcklkIjoiMjMxNjk0NDM4ODMyMDIxMDgwMzIxMTE5NjMiLCJmaW5EYXRlIjoiMjAxNy0wOS0yMyAxMToyNTozOSIsInJlc3BNc2ciOiJUUkFERV9TVUNDRVNTIiwicXVlcnlJZCI6IjIwMTcwOTIzMjEwMDEwMDQxNTA1Mjk3ODI3NTgifV19";
		InJSONParamList pInJSONParamList=new InJSONParamList();
		pInJSONParamList.getGsonMapNoUnicode(strJsonBASE64);
	}
}
