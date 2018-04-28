package webtools.reg.page;


import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import webtools.reg.page.bean.search.BeanQueryPageLinkInfo;

/**
 * 
 * */
public final class ReqSearchResult {
	@SuppressWarnings("unchecked")
	/*
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
        	
        	
		} catch (Exception e) {
			// TODO: handle exception
		}
    } */
	public String charFormat="";
	private int nPageSize=20;//页面尺寸
	private int nRSTotal=0;//全部记录
	private int nRSPageTotalCnt=0;//
	private int nCurrPage=1;//
	private int nRsCnt=0;	
	private Map mapQueryTerm=null;
	private String orderbyInfo="";
	private int nextOrderbyType=0;//��һ״̬��Ϣ
	
	public int getNextOrderbyType() {
		if (nextOrderbyType==0){
			nextOrderbyType=1;
		}else{
			nextOrderbyType=0;
		}
		return nextOrderbyType;
	}
	public void setNextOrderbyType(int nextOrderbyType) {
		this.nextOrderbyType = nextOrderbyType;
	}
	public String getOrderbyInfo() {
		return orderbyInfo;
	}
	public void setOrderbyInfo(final String orderbyInfo) {
		this.orderbyInfo = orderbyInfo;
	}
	public Map getMapQueryTerm() {
		return mapQueryTerm;
	}
	public void setMapQueryTerm(final Map mapQueryTerm) {
		this.mapQueryTerm = mapQueryTerm;
	}
	public String getFirstPageLink() {
		return firstPageLink;
	}
	public void setFirstPageLink(final String firstPageLink) {
		this.firstPageLink = firstPageLink;
	}
	public final String getPrePageLink() {
		return prePageLink;
	}
	public void setPrePageLink(final String prePageLink) {
		this.prePageLink = prePageLink;
	}
	public final String getNextPageLink() {
		return nextPageLink;
	}
	public void setNextPageLink(final String nextPageLink) {
		this.nextPageLink = nextPageLink;
	}
	public String getLastPageLink() {
		return lastPageLink;
	}
	public void setLastPageLink(final String lastPageLink) {
		this.lastPageLink = lastPageLink;
	}
	private String firstPageLink="";//首页
	private String prePageLink="";//上一页
	private String nextPageLink="";//下一页
	private String lastPageLink="";//末页
	private String currentPageLink="";//当前页 jack.dong
	public final List<BeanQueryPageLinkInfo> pageListGroup=new ArrayList<BeanQueryPageLinkInfo>();
	
	public String getCurrentPageLink() {
		return currentPageLink;
	}
	public void setCurrentPageLink(final String currentPageLink) {
		this.currentPageLink = currentPageLink;
	}
	public int getNRsCnt() {
		return nRsCnt;
	}
	public void setNRsCnt(final int rsCnt) {
		nRsCnt = rsCnt;
	}
	private List rsList=null;//��¼����
	public int getNPageSize() {
		return nPageSize;
	}
	public void setNPageSize(final int pageSize) {
		nPageSize = pageSize;
	}
	public int getNRSTotal() {
		return nRSTotal;
	}
	public void setNRSTotal(final int total) {
		nRSTotal = total;
	}
	public int getNRSPageTotalCnt() {
		return nRSPageTotalCnt;
	}
	public void setNRSPageTotalCnt(final int pageTotalCnt) {
		nRSPageTotalCnt = pageTotalCnt;
	}
	public int getNCurrPage() {
		return nCurrPage;
	}
	public void setNCurrPage(final int currPage) {
		nCurrPage = currPage;
	}
	public List getRsList() {
		return rsList;
	}
	public void setRsList(List rsList) {
		this.rsList = rsList;
	}
	
	public String getRSValue(final int nRow,final String fieldName){
		if (rsList==null) return "";
		if (nRow<rsList.size()){
			ConcurrentHashMap map=(ConcurrentHashMap)rsList.get(nRow);
			String strRtn=(String)map.get(fieldName);
			if (strRtn==null){
				strRtn="";
			}
			return strRtn;
			
		}
		return "";
		
	}
	
	public final List<BeanQueryPageLinkInfo> getListPagePos(){
		return this.pageListGroup;
	}
	
	
	public void setRSValue(final int nRow,final String fieldName,final String val){
		if (rsList==null) return ;
		if (nRow<rsList.size()){
			ConcurrentHashMap map=(ConcurrentHashMap)rsList.get(nRow);
			if (map==null){
			  return ;	
			}
			map.put(fieldName, val);
		}
		
	}
	public String getQueryTermValueByName(final String name){
		if (mapQueryTerm==null) return "";
		String str=(String)mapQueryTerm.get(name);
		if (str==null){
			return "";
		}
		try {
			str=URLDecoder.decode(str,"utf-8");
			
			//str=URLDecoder.decode(str,this.charFormat);
			//System.out.println(str+"=this.charFormat"+this.charFormat);
		} catch (Exception e) {}
		return str.trim();
	}
	public String getOrderbyInfo(final String orderName,final int nType){
		String strRtn="";
		String frm=this.getOrderbyInfo();
		strRtn=strRtn.format(frm, orderName,nType+"");
		return strRtn;
	}
	
	

}
