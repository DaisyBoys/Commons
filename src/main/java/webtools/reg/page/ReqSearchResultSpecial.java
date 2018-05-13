package webtools.reg.page;


import webtools.reg.page.bean.search.BeanQueryPageLinkInfo;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * */
public final class ReqSearchResultSpecial {
	@Override
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
        	
        	
		} catch (Exception e) {
			// TODO: handle exception
		}
      
          
    } 
	public String charFormat="";
	private int nPageSize=5000;//页面尺寸
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
	public void setOrderbyInfo(String orderbyInfo) {
		this.orderbyInfo = orderbyInfo;
	}
	public Map getMapQueryTerm() {
		return mapQueryTerm;
	}
	public void setMapQueryTerm(Map mapQueryTerm) {
		this.mapQueryTerm = mapQueryTerm;
	}
	public String getFirstPageLink() {
		return firstPageLink;
	}
	public void setFirstPageLink(String firstPageLink) {
		this.firstPageLink = firstPageLink;
	}
	public String getPrePageLink() {
		return prePageLink;
	}
	public void setPrePageLink(String prePageLink) {
		this.prePageLink = prePageLink;
	}
	public String getNextPageLink() {
		return nextPageLink;
	}
	public void setNextPageLink(String nextPageLink) {
		this.nextPageLink = nextPageLink;
	}
	public String getLastPageLink() {
		return lastPageLink;
	}
	public void setLastPageLink(String lastPageLink) {
		this.lastPageLink = lastPageLink;
	}
	private String firstPageLink="";//首页
	private String prePageLink="";//上一页
	private String nextPageLink="";//下一页
	private String lastPageLink="";//末页
	private String currentPageLink="";//当前页 jack.dong
	public List<BeanQueryPageLinkInfo> pageListGroup=new ArrayList<BeanQueryPageLinkInfo>();
	
	public String getCurrentPageLink() {
		return currentPageLink;
	}
	public void setCurrentPageLink(String currentPageLink) {
		this.currentPageLink = currentPageLink;
	}
	public int getNRsCnt() {
		return nRsCnt;
	}
	public void setNRsCnt(int rsCnt) {
		nRsCnt = rsCnt;
	}
	private List rsList=null;
	public int getNPageSize() {
		return nPageSize;
	}
	public void setNPageSize(int pageSize) {
		nPageSize = pageSize;
	}
	public int getNRSTotal() {
		return nRSTotal;
	}
	public void setNRSTotal(int total) {
		nRSTotal = total;
	}
	public int getNRSPageTotalCnt() {
		return nRSPageTotalCnt;
	}
	public void setNRSPageTotalCnt(int pageTotalCnt) {
		nRSPageTotalCnt = pageTotalCnt;
	}
	public int getNCurrPage() {
		return nCurrPage;
	}
	public void setNCurrPage(int currPage) {
		nCurrPage = currPage;
	}
	public List getRsList() {
		return rsList;
	}
	public void setRsList(List rsList) {
		this.rsList = rsList;
	}
	
	public String getRSValue(int nRow,String fieldName){
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
	
	public List<BeanQueryPageLinkInfo> getListPagePos(){
		return this.pageListGroup;
	}
	
	
	public void setRSValue(int nRow,String fieldName,String val){
		if (rsList==null) return ;
		if (nRow<rsList.size()){
			ConcurrentHashMap map=(ConcurrentHashMap)rsList.get(nRow);
			if (map==null){
			  return ;	
			}
			map.put(fieldName, val);
		}
		
	}
	public String getQueryTermValueByName(String name){
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
	public String getOrderbyInfo(String orderName,int nType){
		String strRtn="";
		String frm=this.getOrderbyInfo();
		strRtn= String.format(frm, orderName,nType+"");
		return strRtn;
	}
	
	

}
