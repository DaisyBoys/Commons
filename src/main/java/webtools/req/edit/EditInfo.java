package webtools.req.edit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 结果集合信息
 * */
public final class EditInfo {
	private Map mapQueryTerm=null;
	private Map mapErr=null; 
	private ConcurrentHashMap mapPageErrInfo=null;

	public Map getMapPageErrInfo() {
		return mapPageErrInfo;
	}

	public void setMapPageErrInfo(ConcurrentHashMap mapPageErrInfo) {
		this.mapPageErrInfo = mapPageErrInfo;
	}

	public Map getMapErr() {
		return mapErr;
	}

	public void setMapErr(Map mapErr) {
		this.mapErr = mapErr;
	}

	public Map getMapQueryTerm() {
		return mapQueryTerm;
	}

	public void setMapQueryTerm(Map mapQueryTerm) {
		this.mapQueryTerm = mapQueryTerm;
	}
	public String getStrInputValue(String name){
		if (mapQueryTerm==null) return "";
		if (!mapQueryTerm.containsKey(name)){
			return "";
		}
	try {
		String[]ss=(String[]) mapQueryTerm.get(name);
		if (ss==null) return "";
		if (ss.length>0){
			
			return ss[0];
		}
	} catch (Exception e) {
	}
		
		return "";
	}
	
	public String[] getStrInputValueList(String name){
		if (mapQueryTerm==null) return null;
		Object oo=mapQueryTerm.get(name);
		if(oo!=null){
			try {
				String[]ss=(String[]) mapQueryTerm.get(name);
				if (ss==null) return null;
				return ss;
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		return null;
	}
	
	public void setFiledErr(final String oldKey,final String field){
		if (mapErr!=null){
			String str=(String)mapErr.get(oldKey);
			if (str!=null){
				if (mapPageErrInfo==null) mapPageErrInfo=new ConcurrentHashMap();
				mapPageErrInfo.put(field, str);
			}
		}
	}
	
	public String getStrInputErr(String name){
		if (mapPageErrInfo==null) return "";
		String ss=(String) mapPageErrInfo.get(name);
		if (ss==null) return "";
		return ss;
	
	}

}
