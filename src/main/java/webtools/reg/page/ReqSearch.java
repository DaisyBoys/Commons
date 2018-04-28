package webtools.reg.page;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import HTTPJSON.GsonMapMgr;

import webtools.reg.page.bean.search.BeanQueryPageLinkInfo;
import webtools.reg.page.bean.search.QuerySqlBean;

import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;
import webtools.common.database.JdbcAgentEx;

/**
 * 查询请求处理层
 * 1、得到页面请求参数，并进行转义
 * 		来自页面的数据库记录集合的当前页数信息
 *      来自页面的查询（WHERE）部分的查询条件参数
 *      来自页面的排序参数请求（order by ）
 * 
 * 
 *  useBodyEncodingForURI="true"
 * **/
public final class ReqSearch {
	@SuppressWarnings("unchecked")
	protected  void finalize() throws Throwable{             
        super.finalize();
        try {
        	if (mapQueryTerm!=null){
        		Iterator   itSearch   =  mapQueryTerm.entrySet().iterator();
    			while   (itSearch.hasNext())   { 
    				@SuppressWarnings("unused")
					Object   me=itSearch.next(); 
    				me=null;
    			}
    			mapQueryTerm.clear();
    			mapQueryTerm=null;
        		
        	}
        	querySQLBean=null;
        	
		} catch (Exception e) {
			// TODO: handle exception
		}
      
          
    } 
	private int nPageGroup=5;		//页面分组
	@SuppressWarnings("unchecked")
	public Map mapQueryTerm=null;
	private String  reqOrderByName="order";//排序请求名称
	private String  reqOrderByNameType="ordertype";//当前字段的排序方式名称
	private String  orderbyValue="";//排序字段置
	@SuppressWarnings("unchecked")
	private List  lstResult=null;
	
	@SuppressWarnings("unchecked")
	public List getLstResult() {
		return lstResult;
	}
	@SuppressWarnings("unchecked")
	public void setLstResult(List lstResult) {
		this.lstResult = lstResult;
	}
	public String getOrderbyValue() {
		return orderbyValue;
	}
	public void setOrderbyValue(String orderbyValue) {
		this.orderbyValue = orderbyValue;
	}
	public int getOrderbyType() {
		return orderbyType;
	}
	public void setOrderbyType(int orderbyType) {
		this.orderbyType = orderbyType;
	}

	private int     orderbyType=0;//当前排序方式
		
	
	public String getReqOrderByNameType() {
		return reqOrderByNameType;
	}
	public void setReqOrderByNameType(String reqOrderByNameType) {
		this.reqOrderByNameType = reqOrderByNameType;
	}
	public String getReqOrderByName() {
		return reqOrderByName;
	}
	public void setReqOrderByName(String reqOrderByName) {
		this.reqOrderByName = reqOrderByName;
	}

	private String  reqCurrPageName="currpage";//当前页请求的名称
    private  QuerySqlBean querySQLBean=new QuerySqlBean();//查询脚本BEAN
    private String querySQL=null;//查询使用的脚本
    private int currPage=0;
    private int pageSize=20;//翻页尺寸
    private ReqSearchResult pReqSearchResult=new ReqSearchResult();//记录结果集
	
	public ReqSearchResult getPReqSearchResult() {
		return pReqSearchResult;
	}
	public void setPReqSearchResult(ReqSearchResult reqSearchResult) {
		pReqSearchResult = reqSearchResult;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public String getQuerySQL() {
		return querySQL;
	}
	public void setQuerySQL(String querySQL) {
		this.querySQL = querySQL;
	}
	public QuerySqlBean getQuerySQLBean() {
		return querySQLBean;
	}
	public void setQuerySQLBean(QuerySqlBean querySQLBean) {
		this.querySQLBean = querySQLBean;
	}
	public String getReqCurrPageName() {
		return reqCurrPageName;
	}
	public void setReqCurrPageName(String reqCurrPageName) {
		this.reqCurrPageName = reqCurrPageName;
	}
	/**
	 * 初始化查询条件参数
	 * */
	@SuppressWarnings("unchecked")
	public void setQueryTermName(final String name){
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
	/**
	 * 得到查询条件值
	 * @param 输入参数 name = 查询条件的名称
	 * @return NULL 为查询条件未设置，否则为当前的查询条件
	 * */
	public String getQueryTermValue(final String name){
		try{
			Object obj=mapQueryTerm.get(name);
			if (obj==null) return null;
			String strRtn=(String)obj;
			try {
				//strRtn=URLDecoder.decode(strRtn,this.charFormat);
			} catch (Exception e) {}
			return strRtn;
			
		}catch(Exception e){
			
		}
		return "";
	}
	/**
	 * 查询条件参数地图
	 * */
	@SuppressWarnings("unchecked")
	public Map getQueryTermMap(){
		return mapQueryTerm;
	}
	/**
	 * 得到查询传参时所带查询条件参数
	 * */
	@SuppressWarnings("unchecked")
	private String getTermUrl(){
		String strRtn="";
		if (mapQueryTerm!=null){
			Iterator   itSearch   =  mapQueryTerm.entrySet().iterator();
			while   (itSearch.hasNext())   { 
				Map.Entry   me=(Map.Entry)itSearch.next(); 
				String   mapKey   =(String)   me.getKey();
				String   mapValue =(String) me.getValue();
				if (mapValue!=null){
					/*
					try {
						mapValue=URLDecoder.decode(mapValue,"utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
					
					}
					*/
					strRtn+=mapKey+"="+mapValue+"&";
				}
			} 
			int nPos=strRtn.lastIndexOf("&");
			if (nPos>0)strRtn=strRtn.substring(0,nPos);
		}		
		return strRtn;
	}
	/**
	 * 得到排序参数
	 * */
	private String getOrderByUrl(){
		String strRtn="";
		if(!this.getReqOrderByName().equals("")){
			if (!this.getOrderbyValue().equals("")){
				strRtn=this.getReqOrderByName()+"="+this.getOrderbyValue();
				strRtn+="&"+this.getReqOrderByNameType()+"="+this.getOrderbyType();
			}
		}
		
		return strRtn;
	}
	
	public void doRespose(final HttpServletRequest request,final HttpServletResponse response)throws  ServletException, IOException  {
		doRequst(request,response);
	}
	
	//转换机制 所有得到的值存储为加密 然后得到的时候进行解密
	private String charFormat="";
	
	@SuppressWarnings("unchecked")
	public void doRequst(final HttpServletRequest request,final HttpServletResponse response)throws  ServletException, IOException {
		this.charFormat=request.getCharacterEncoding();
		this.pReqSearchResult.charFormat=this.charFormat;
		//设置查询条件参数
		if (mapQueryTerm!=null){
			Iterator   itSearch   =  mapQueryTerm.entrySet().iterator();
			while   (itSearch.hasNext())   { 
				Map.Entry   me=(Map.Entry)itSearch.next(); 
				String   mapKey   =(String)   me.getKey();
				String reqVal=request.getParameter(mapKey);
				if(reqVal!=null){
				
					reqVal=URLEncoder.encode(reqVal,"utf-8");
					
					if (!"POST".equalsIgnoreCase(request.getMethod())){
						//reqVal=URLDecoder.decode(reqVal,this.charFormat);
						//reqVal=URLEncoder.encode(reqVal,"utf-8");
						//System.out.println(reqVal+"====  "+mapKey);
						//reqVal=URLEncoder.encode(reqVal,"utf-8");
					}else{
						
					//	reqVal=URLDecoder.decode(reqVal,"utf-8");
					}
					
				}
				if(reqVal!=null){
					mapQueryTerm.put(mapKey, reqVal);
				}
			}
			pReqSearchResult.setMapQueryTerm(mapQueryTerm);//设置查询条件
		}
		//得到排序使用的 名称
		if(!this.getReqOrderByName().equals("")){
			String strVal=request.getParameter(this.getReqOrderByName());
			if (strVal==null)strVal="";
			this.setOrderbyValue(strVal);
		}
		//当前的排序方式
		if(!this.getReqOrderByNameType().equals("")){
			String strVal=request.getParameter(this.getReqOrderByNameType());
			if (strVal==null)strVal="0";
			if (strVal.equals("1")){//当前的字段为降序
				this.setOrderbyType(1);
				pReqSearchResult.setNextOrderbyType(1);
			}else{
				this.setOrderbyType(0);
				pReqSearchResult.setNextOrderbyType(0);
			}
			
		}
		setOrderbyInfo();//设置查询的相关参数
		
		//得到当前页数
		String strCurrPage=request.getParameter(this.getReqCurrPageName());
		this.setCurrPage(GetCurrPageNum(strCurrPage));
	}
	//根据查询设置的字段得到条件值
	public String getQueryTermValueByName(final String name){
		if (mapQueryTerm==null) return "";
		String str=(String)mapQueryTerm.get(name);
		if (str==null){
			return "";
		}
		try {
			str=URLDecoder.decode(str,this.charFormat);
			if (str.indexOf("'")>=0){
				str=str.replaceAll("'", "\\\\'");
			}
		} catch (Exception e) {}
		return str.trim();
	}
	/**
	 * 得到当前页数值
	 * */
	public  int GetCurrPageNum(String strCurrPage){
		int nRtn=0;
		try{
			if (strCurrPage==null) return 0;
			strCurrPage=strCurrPage.trim();//需要进行数据交验
			boolean b=Pattern.compile("^(\\d{1,9})?$").matcher(strCurrPage).find();
			if (!b){
				return 0;
			}
			try{
				nRtn=Integer.parseInt(strCurrPage);
				
			}catch(Exception e){
				
			}
			
		}catch(Exception e){
			//System.out.println(e.toString());
			
		}
		return nRtn;
	}
	/**
	 * 设置查询语句
	 * @param selField=查询使用的自动，where=查询条件，orderby=排序条件，groupby=分组条件
	 * @return 
	 * */
	public void SetQuerySQL(final String selField,final String from,final String where,final String orderby,final String groupby){
		if (querySQLBean==null)querySQLBean= new QuerySqlBean();
		querySQLBean.setSelectField(selField);
		querySQLBean.setQueryFrom(from);
		querySQLBean.setQueryWhere(where);
		querySQLBean.setQueryOrderBy(orderby);
		querySQLBean.setQueryGroupBy(groupby);
		
	}
	
	public String[] getSQL(){
		String []strRtn=new String[2] ;
		String selField =querySQLBean.getSelectField();
		if (selField==null || selField.equals("")){
			return null;
		}
		String from =querySQLBean.getQueryFrom();
		if (from==null || from.equals("")){
			return null;
		}
		String strQuery="select "+selField+" from "+from;
		String strQueryCount="select count(*) as cnt from "+from;
		//处理查询条件
		String where =querySQLBean.getQueryWhere();
		if (!where.equals("")){
			where=" where "+where;
		}
		//排序条件
		String orderby=querySQLBean.getQueryOrderBy();
		if (!orderby.equals("")){
			orderby=" order by  "+orderby;
		}
		//分组条件
		String groupby=querySQLBean.getQueryGroupBy();
		if (!groupby.equals("")){
			groupby=" group by  "+groupby;
		}
		
		strRtn[0]=strQuery+where+groupby+orderby;
		strRtn[1]=strQueryCount+where+groupby;
		
		return strRtn;
	}
	
	@SuppressWarnings("unchecked")
	public final  List getDBQueryResult(){
		List rtnList=new ArrayList();
		final String[] sqls=getSQL();
		if (sqls==null){
			//System.out.println("输入的查询字段脚本为空");
			return rtnList;//脚本为空返回
		}
		final String sql=sqls[0];
		//System.out.println(sql);
		final String sqlCnt=sqls[1];
		final JdbcAgent jAgent=new JdbcAgent();
		//得到记录总是
		try{
			DBResult result=jAgent.query(sqlCnt);
			//System.out.println(sqlCnt);
			int nTotal=0;
			if (result.getRowCount()>0){
				if (result.getRowCount()>1){
					nTotal=result.getRowCount();
				}else{
					final String sTotal=result.getString(0,0);//记录集总数
					try{
						nTotal=Integer.parseInt(sTotal);
					}catch(Exception e){
						
					}
				}
			}
			result=null;
			pReqSearchResult.setNRSTotal(nTotal);
			int nCurrPage=this.getCurrPage();//得到当前的页数
			if (nCurrPage<=0)nCurrPage=1;
			pReqSearchResult.setNCurrPage(nCurrPage);
			nCurrPage--;//真实使用的当前页数
			if (nCurrPage<0)nCurrPage=0;
			
			final int nPageSize=this.getPageSize();//得到记录每页的尺寸
			pReqSearchResult.setNPageSize(nPageSize);
			int nPageCnt=GetTotalPageCnt(nTotal,nPageSize);
			if (nCurrPage>=nPageCnt)nPageCnt--;
			if (nPageCnt<=0)nPageCnt=1;
			pReqSearchResult.setNRSPageTotalCnt(nPageCnt);//记录总分页数据
			final int nPos1=nCurrPage*nPageSize;
			final int nPos2=(nCurrPage+1)*nPageSize-1;
			//System.out.println(sql);
			result=jAgent.query(sql,nPos1,nPos2);//得到查询结果数据
			if(nPos1==0){
				int n=result.getRowCount();
				if(n==1&&nPageSize>1 ){
					if(nTotal>1){
						pReqSearchResult.setNRSTotal(1);
						pReqSearchResult.setNRSPageTotalCnt(1);//记录总分页数据
					}
				}
			}
		  
			final int nCurrRSCnt=result.getRowCount();//当前的记录数
			for (int i=0;i<nCurrRSCnt;i++){
				final ConcurrentHashMap map=new ConcurrentHashMap();//存储记录值
				for (int j=0;j<result.getColCount();j++){
					final String colName=result.getColumAttribute(j).getColname();//得到列名
					final String colType=result.getColumAttribute(j).getType().toString();
					if("VARBINARY".equals(colType)){
						final Object s= result.getObject(i, j);
						
						System.out.println(s.toString());
					}
					
					String srtVal=result.getString(i, j);
					String str=(String)map.get(colName);
					if (colType.equalsIgnoreCase("DATETIME")){
						if (srtVal.length()>19) srtVal=srtVal.substring(0,19);
					}
					
				
					
					if (str==null){
						map.put(colName, srtVal);
					}else{
						map.put(colName+j, srtVal);
					}
				}
				rtnList.add(map);
			}
			result=null;
			pReqSearchResult.setRsList(rtnList);
			pReqSearchResult.setNRsCnt(rtnList.size());
			setPageLink(pReqSearchResult.getNCurrPage(),pReqSearchResult.getNRSPageTotalCnt());
			
		}catch(Exception e){
			//System.out.println(e.toString());
		}
		
		return  rtnList;
		
	}
	//得到所有的页数
	private int GetTotalPageCnt(final int nTotal,final int nPageSize){
		int nPage=nTotal/nPageSize;
		int nModPage=nTotal%nPageSize;
		if (nModPage>0)nPage++;
		return nPage;
	}
	//设置翻页地址
	public void setPageLink(final int nCurrPage,final int nTotalPage){
		String strLink=getTermUrl();
		if (!strLink.equals(""))strLink+="&";
		String strOrderBy=getOrderByUrl();
		if (!strOrderBy.equals("")){
			strLink+=strOrderBy+"&";
		}
		String pageName=this.getReqCurrPageName();
		
		String firsLink=strLink+pageName+"="+1;
		String lastLink=strLink+pageName+"="+nTotalPage;
		int nPre=nCurrPage-1;
		if (nPre<=0)nPre=1;
		int nNext=nCurrPage+1;
		if (nNext>nTotalPage)nNext=nTotalPage;
		String preLink=strLink+pageName+"="+nPre;
		String nextLink=strLink+pageName+"="+nNext;
		String currentPageLink=strLink+pageName+"="+nCurrPage;//jack.dong
		pReqSearchResult.setFirstPageLink(firsLink);
		pReqSearchResult.setPrePageLink(preLink);
		pReqSearchResult.setNextPageLink(nextLink);
		pReqSearchResult.setLastPageLink(lastLink);
		pReqSearchResult.setCurrentPageLink(currentPageLink);//jack.dong
		this.setListPageGroup(nCurrPage, nTotalPage, strLink, pageName);
	}
	//设置字段排序查询条件
	public void setOrderbyInfo(){
		String strLink=getTermUrl();
		if (!strLink.equals(""))strLink+="&";
	
		if(!this.getReqOrderByName().equals("")){
			
			strLink+=this.getReqOrderByName()+"=%s&"+this.getReqOrderByNameType()+"=%s";
			pReqSearchResult.setOrderbyInfo(strLink);
		}
	}
	public String getCurrOrderbyInfo(){
		String orderby= getOrderbyValue();
		int nOrdertype =getOrderbyType();
		if (!orderby.equals("")){
			String str= orderby ;
			if (nOrdertype==1){
				str+=" desc";
			}
			return str;
		}
		return "";
	}
	//执行
	public final ReqSearchResult runReq(){
		getDBQueryResult();
		return this.getPReqSearchResult();
	}
	
	@SuppressWarnings("unchecked")
	public List doQueryByJson(final int nPage,final int nPageSize){
		return getDBQueryResultByJson( nPage, nPageSize);
	}

	@SuppressWarnings("unchecked")
	public List getDBQueryResultByJson(final int nPage,final int nPageSize){
		this.setCurrPage(nPage);
		this.setPageSize(nPageSize);
		List rtnList=new ArrayList();
		String[] sqls=getSQL();
		if (sqls==null){
			//System.out.println("输入的查询字段脚本为空");
			return rtnList;//脚本为空返回
		}
		String sql=sqls[0];
		//System.out.println(sql);
		String sqlCnt=sqls[1];
		JdbcAgentEx jAgent=new JdbcAgentEx();
		//得到记录总是
		try{
			DBResult result=jAgent.query(sqlCnt);
			//System.out.println(sqlCnt);
			int nTotal=0;
			if (result.getRowCount()>0){
				if (result.getRowCount()>1){
					nTotal=result.getRowCount();
				}else{
					String sTotal=result.getString(0,0);//记录集总数
					try{
						nTotal=Integer.parseInt(sTotal);
					}catch(Exception e){
						
					}
				}
			}
			pReqSearchResult.setNRSTotal(nTotal);
			int nCurrPage=this.getCurrPage();//得到当前的页数
			if (nCurrPage<=0)nCurrPage=1;
			pReqSearchResult.setNCurrPage(nCurrPage);
			nCurrPage--;//真实使用的当前页数
			if (nCurrPage<0)nCurrPage=0;
			
			//int nPageSize=this.getPageSize();//得到记录每页的尺寸
			pReqSearchResult.setNPageSize(nPageSize);
			int nPageCnt=GetTotalPageCnt(nTotal,nPageSize);
			if (nCurrPage>=nPageCnt)nPageCnt--;
			if (nPageCnt<=0)nPageCnt=1;
			pReqSearchResult.setNRSPageTotalCnt(nPageCnt);//记录总分页数据
			
			ConcurrentHashMap mapParam=new ConcurrentHashMap();//存储记录值
			
			mapParam.put("nRSTotal", nTotal);
			mapParam.put("nCurrPage", nCurrPage);
			mapParam.put("nPageCnt", nPageCnt);
			mapParam.put("nPageSize", nPageSize);
			mapParam.put("nTotalPage", nPageCnt);
			rtnList.add(mapParam);
			
			
			int nPos1=nCurrPage*nPageSize;
			int nPos2=(nCurrPage+1)*nPageSize-1;
			//System.out.println(sql);
			result=jAgent.query(sql,nPos1,nPos2);//得到查询结果数据
		  
			int nCurrRSCnt=result.getRowCount();//当前的记录数
			for (int i=0;i<nCurrRSCnt;i++){
				ConcurrentHashMap map=new ConcurrentHashMap();//存储记录值
				for (int j=0;j<result.getColCount();j++){
					String colName=result.getColumAttribute(j).getColname();//得到列名
					String colType=result.getColumAttribute(j).getType().toString();
					
					String srtVal=result.getString(i, j);
					String str=(String)map.get(colName);
					if (colType.equalsIgnoreCase("DATETIME")){
						if (srtVal.length()>19) srtVal=srtVal.substring(0,19);
					}
				
					
					if (str==null){
						map.put(colName, srtVal);
					}else{
						map.put(colName+j, srtVal);
					}
				}
				rtnList.add(map);
			}
			//pReqSearchResult.setRsList(rtnList);
			//pReqSearchResult.setNRsCnt(rtnList.size());
			//setPageLink(pReqSearchResult.getNCurrPage(),pReqSearchResult.getNRSPageTotalCnt());
		}catch(Exception e){
			//System.out.println(e.toString());
		}
		
		return  rtnList;
		
	}
	//==页面分组处理
	private void setListPageGroup(final int nCurrPage,final int nTotalPage,final String strLink,final String pageName){
		try {
			int nCurrGroup=nCurrPage/this.nPageGroup;
			if(nCurrPage%this.nPageGroup>0)nCurrGroup++;
			int nTotalGroup=nTotalPage/this.nPageGroup;
			if(nTotalPage%this.nPageGroup>0)nTotalGroup++;
			int nStart=(nCurrGroup-1)*this.nPageGroup;
			int nEnd=nStart+this.nPageGroup;
			if(nEnd>nTotalPage)nEnd=nTotalPage;
			pReqSearchResult.pageListGroup.clear();
			for(int i=nStart;i<nEnd;i++){
				int iPos=i+1;
				String link=strLink+pageName+"="+iPos;
				BeanQueryPageLinkInfo pBeanQueryPageLinkInfo =new BeanQueryPageLinkInfo();
				pBeanQueryPageLinkInfo.urlInfo=link;
				pBeanQueryPageLinkInfo.nPos=iPos;
				
				pReqSearchResult.pageListGroup.add(pBeanQueryPageLinkInfo);
			}
			
			//
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	private int convertInt(String str){
		try {
			if (str==null || "".equals(str)){
				str="0";
			}
			return Integer.parseInt(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	private GsonMapMgr pGsonMapMgr =new GsonMapMgr();
	/**
	 * JSON数据处理
	 * */
	@SuppressWarnings("unchecked")
	public List getListJsonMap(final Map<String,Object> mapJson){
		///this.setCurrPage(nPage);
		//this.setPageSize(nPageSize);
		List rtnList=new ArrayList();
		//得到记录总是
		try{
			int nTotal=convertInt(pGsonMapMgr.getString(mapJson, "nTotal"));
			int nPageCnt=convertInt(pGsonMapMgr.getString(mapJson, "nPageCnt"));
			int nPageSize=convertInt(pGsonMapMgr.getString(mapJson, "nPageSize"));
			int nCurrPage=convertInt(pGsonMapMgr.getString(mapJson, "nCurrPage"));
			nCurrPage++;
			//int nPageSize=convertInt(pGsonMapMgr.getString(mapJson, "nPageSize"));
			
			pReqSearchResult.setNRSTotal(nTotal);
			//int nCurrPage=this.getCurrPage();//得到当前的页数
			if (nCurrPage<=0)nCurrPage=1;
			pReqSearchResult.setNCurrPage(nCurrPage);
			//nCurrPage--;//真实使用的当前页数
			//if (nCurrPage<0)nCurrPage=0;
			
			//int nPageSize=this.getPageSize();//得到记录每页的尺寸
			pReqSearchResult.setNPageSize(nPageSize);
			//int nPageCnt=GetTotalPageCnt(nTotal,nPageSize);
			//if (nCurrPage>=nPageCnt)nPageCnt--;
			//if (nPageCnt<=0)nPageCnt=1;
			pReqSearchResult.setNRSPageTotalCnt(nPageCnt);//记录总分页数据
			
//			ConcurrentHashMap mapParam=new ConcurrentHashMap();//存储记录值
//			
//			mapParam.put("nRSTotal", nTotal);
//			mapParam.put("nCurrPage", nCurrPage);
//			mapParam.put("nPageCnt", nPageCnt);
//			mapParam.put("nPageSize", nPageSize);
//			mapParam.put("nTotalPage", nPageCnt);
			//rtnList.add(mapParam);
			
			
			//int nPos1=nCurrPage*nPageSize;
			//int nPos2=(nCurrPage+1)*nPageSize-1;
			//System.out.println(sql);
			//result=jAgent.query(sql,nPos1,nPos2);//得到查询结果数据
		  
			List<Map<String, Object>> list= pGsonMapMgr.getListMapObj(mapJson, "dataList");
			for (int i=0;i<list.size();i++){
				ConcurrentHashMap map=new ConcurrentHashMap();//存储记录值
				Map<String, Object> mapData=list.get(i);
				Iterator   itSearch   =  mapData.entrySet().iterator();
				while   (itSearch.hasNext())   { 
					Map.Entry   me=(Map.Entry)itSearch.next(); 
					String colName=(String)   me.getKey();
					String srtVal=pGsonMapMgr.getString(mapData, colName);
					map.put(colName, srtVal);
				}
				rtnList.add(map);
			} 
		
			
			pReqSearchResult.setRsList(rtnList);
			pReqSearchResult.setNRsCnt(rtnList.size());
			setPageLink(pReqSearchResult.getNCurrPage(),pReqSearchResult.getNRSPageTotalCnt());
		}catch(Exception e){
			//System.out.println(e.toString());
		}
		
		return  rtnList;
		
	}
	//执行
	public ReqSearchResult runReqJsonMap(final Map<String,Object> mapJson){
		this.getListJsonMap(mapJson);
		return this.getPReqSearchResult();
	}
	//执行
	public final ReqSearchResult runReqLucene(int nTotal){
		
		pReqSearchResult.setNRSTotal(nTotal);
		int nCurrPage=this.getCurrPage();//得到当前的页数
		if (nCurrPage<=0)nCurrPage=1;
		pReqSearchResult.setNCurrPage(nCurrPage);
		nCurrPage--;//真实使用的当前页数
		if (nCurrPage<0)nCurrPage=0;
		
		final int nPageSize=this.getPageSize();//得到记录每页的尺寸
		pReqSearchResult.setNPageSize(nPageSize);
		int nPageCnt=GetTotalPageCnt(nTotal,nPageSize);
		if (nCurrPage>=nPageCnt)nPageCnt--;
		if (nPageCnt<=0)nPageCnt=1;
		pReqSearchResult.setNRSPageTotalCnt(nPageCnt);//记录总分页数据
		setPageLink(pReqSearchResult.getNCurrPage(),pReqSearchResult.getNRSPageTotalCnt());
		return this.getPReqSearchResult();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReqSearch pReqSearch =new ReqSearch();
		//pReqSearch.GetCurrPageNum("111");
		//pReqSearch.setQueryTermName("b");
		//String ss=pReqSearch.getTermUrl();
		//System.out.print(ss);
		//pReqSearch.getListPageGroup(11,20);
		

	}

}
