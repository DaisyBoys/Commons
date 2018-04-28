package webtools.req.edit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;

/**
 * 删除处理
 * */
public final class ReqDelete {
	private ConcurrentHashMap mapQueryTerm=null;
	private EditInfo pEditInfo=new EditInfo();
	private List lstDelScript=null;
	private List lstLimtSelectScript=null;
	private ConcurrentHashMap mapLimtClew=null;
	public List lstLimtClew=new ArrayList();
	private List lstDeleteClew=new ArrayList();

	public List getLstDeleteClew() {
		return lstDeleteClew;
	}

	public void setLstDeleteClew(List lstDeleteClew) {
		this.lstDeleteClew = lstDeleteClew;
	}

	public List getLstLimtClew() {
		return lstLimtClew;
	}

	public void setLstLimtClew(List lstLimtClew) {
		this.lstLimtClew = lstLimtClew;
	}

	public Map getMapLimtClew() {
		return mapLimtClew;
	}

	public void setMapLimtClew(ConcurrentHashMap mapLimtClew) {
		this.mapLimtClew = mapLimtClew;
	}

	public List getLstLimtSelectScript() {
		return lstLimtSelectScript;
	}

	public void setLstLimtSelectScript(List lstLimtSelectScript) {
		this.lstLimtSelectScript = lstLimtSelectScript;
	}

	public List getLstDelScript() {
		return lstDelScript;
	}

	public void setLstDelScript(List lstDelScript) {
		this.lstDelScript = lstDelScript;
	}


	public EditInfo getPEditInfo() {
		return pEditInfo;
	}

	public void setPEditInfo(EditInfo editInfo) {
		pEditInfo = editInfo;
	}

	public Map getMapQueryTerm() {
		return mapQueryTerm;
	}

	public void setMapQueryTerm(ConcurrentHashMap mapQueryTerm) {
		this.mapQueryTerm = mapQueryTerm;
	}
	
	public  void doRespose(HttpServletRequest request,HttpServletResponse response)throws  ServletException, IOException  {
		doRequst(request,response);
	}
	public  void doRequst(HttpServletRequest request,HttpServletResponse response)throws  ServletException, IOException {
		//设置数据提交的参数
		if (mapQueryTerm!=null){
			Iterator   itSearch   =  mapQueryTerm.entrySet().iterator();
			while   (itSearch.hasNext())   { 
				Map.Entry   me=(Map.Entry)itSearch.next(); 
				String   mapKey   =(String)   me.getKey();
				String[]  sVal=request.getParameterValues(mapKey);
				if(sVal!=null){
					mapQueryTerm.put(mapKey, sVal);
				}
			}
			pEditInfo.setMapQueryTerm(mapQueryTerm);
		}
	}
	/**
	 * 初始化查询条件参数
	 * */
	public void setQueryTermName(String name){
		try{
			if (name==null){
				return ;
			}
			if (mapQueryTerm==null) mapQueryTerm=new ConcurrentHashMap();//初始化MAP
			Object obj=mapQueryTerm.get(name);
			if (obj==null){
				mapQueryTerm.put(name, "");
			}
			
			
		}catch(Exception e){
			//System.out.println(e.toString());
			
		}
	}
	//根据设定的名称得到相关的数据
	public String[] getPageParamValues(String name){
		if (mapQueryTerm==null){
			return null;
		}
		String []s=null;
		try{
			s=(String [])mapQueryTerm.get(name);
		}catch(Exception e){
			//System.out.println(e.toString());
		}
		return s;
	}
	
	
	//设置删除条件
	public void setDeleteInfo(String tablename,String deleteTermField,String[] values,String clew){
		//setDeleteTable(tablename );
		String delFrm="delete from %s where %s in (%s) ";//删除处理插入失败用
		String sql="";
		//sql=sql.format(delFrm, tablename);
		
		if (values!=null){
			String strVal="";
			for (int i=0;i<values.length;i++){
				String str=values[i];
				strVal+="'"+str+"'";
				if (i<values.length-1){
					strVal+=",";
				}
			}
			sql=sql.format(delFrm, tablename,deleteTermField,strVal);
			if (!sql.equals("")){
				if (lstDelScript==null) lstDelScript=new ArrayList();
				lstDelScript.add(sql);
				if (mapLimtClew==null)mapLimtClew=new ConcurrentHashMap();
				mapLimtClew.put("del"+lstDelScript.size(), clew);
				
			}
		}
	}
	
	
	//设置约束表信息
	@SuppressWarnings("static-access")
	public void setLimitTable(String tablename,String selTermField,String[] values,String clew ){
		String Frm="select count(*) as cnt from %s where %s in (%s) ";//删除处理插入失败用
		String sql="";
		if (values!=null){
			String strVal="";
			for (int i=0;i<values.length;i++){
				String str=values[i];
				strVal+="'"+str+"'";
				if (i<values.length-1){
					strVal+=",";
				}
			}
			sql=sql.format(Frm, tablename,selTermField,strVal);
			if (!sql.equals("")){
				if (lstLimtSelectScript==null) lstLimtSelectScript=new ArrayList();
				lstLimtSelectScript.add(sql);
				if (mapLimtClew==null)mapLimtClew=new ConcurrentHashMap();
				mapLimtClew.put("clew"+lstLimtSelectScript.size(), clew);
			}
		}
	}
	//删除处理
	public void doDelete(){
		try{
			JdbcAgent jAgent=new JdbcAgent();
			if (lstLimtSelectScript!=null){
				int nSelSize=lstLimtSelectScript.size();
				if (nSelSize>0){
					DBResult result;
					for (int i=0;i<nSelSize;i++){
						String sqlCnt=(String)lstLimtSelectScript.get(i);
						result=jAgent.query(sqlCnt);
						int nTotal=0;
						if (result.getRowCount()>0){
							String sTotal=result.getString(0,0);//记录集总数
							try{
								nTotal=Integer.parseInt(sTotal);
							}catch(Exception e){
								
							}
						}
						if (nTotal>0){
							int nPos=i+1;
							if (mapLimtClew!=null){
								String strClew=(String)mapLimtClew.get("clew"+nPos);
								if (strClew!=null){
									lstLimtClew.add(strClew+" 共："+nTotal+"条。");
								}
							}
						}
					}
				}
			}
			//限制条件存在记录不能删除
			if (lstLimtClew.size()>0){
				return ;
			}
			if (lstDelScript!=null){
				
				for (int i=0;i<lstDelScript.size();i++){
					String strDel=(String )lstDelScript.get(i);
					int n=jAgent.update(strDel);
					if (mapLimtClew!=null){
						int nPos=i+1;
						String strClew=(String)mapLimtClew.get("del"+nPos);
						if (strClew!=null){
							if (n<0){
								lstDeleteClew.add(strClew+" 删除失败请联系管理员！");
							}else{
								lstDeleteClew.add(strClew+" 共："+n+"条。");
							}
						}
					}
					
				}
			}//if (lstDelScript!=null)
			
			
		}catch(Exception e){
			
		}
	}
	public void runDelete(){
		doDelete();
	}
	public String getDeleteInfo(){
		String rtnStr="";
		String rtn="";
		String strFrm="<div class=\"list_error\">"+
					" <span><b>删除提示</b></span>"+
					  "<ul>"+
					  "  %s"+
					 " </ul>"+
					"</div>";
		if(lstLimtClew!=null){
			if (lstLimtClew.size()>0){
				for (int i=0;i<lstLimtClew.size();i++){
					Object obj=lstLimtClew.get(i);
					String str=obj.toString();
					rtn+="<li><font color=green>"+str+"</font></li>";
				}
			}
		}
		if(lstDeleteClew!=null){
			if (lstDeleteClew.size()>0){
				for (int i=0;i<lstDeleteClew.size();i++){
					Object obj=lstDeleteClew.get(i);
					String str=obj.toString();
					rtn+="<li>"+str+"</li>";
				}
			}
		}
		if (!rtn.equals("")){
			rtnStr=rtnStr.format(strFrm, rtn);
		}
		return rtnStr;
	}
	
	

}
