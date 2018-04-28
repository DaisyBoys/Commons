package webtools.com.epad.tools;



import java.io.IOException;

import webtools.common.Utility;
import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;


/*
 * 
 * */
public class RtnString {
	private final String FieldSplit="\t@_@";//
	private final String ListRowEnd="\t@\trow";//

	//����б��е��ֶ�
	private String encField(String strSrc){
		String str="";
		str=strSrc+this.FieldSplit;
		return str;
	}
	private String encRowEnd(String strSrc){
		String str="";
		str=strSrc+this.ListRowEnd;
		return str;
	}
	public String setRowInfo(Object... obj){
		if (obj==null){
			return "";
		}
		int n=obj.length;
		String str="";
		for (int i=0;i<n;i++){
			String strTemp=(String)obj[i];
			str+=this.encField(strTemp);
		}
		str+=this.ListRowEnd;
	//	System.out.println(str);
		return str;
	}
	//==================得到数据库信息=======================
	public String getDBRSInfo(String sqlCnt,String sql,int nPage,int nPageSize ){
		
		try{
			String strRtn="";
			JdbcAgent jAgent=new JdbcAgent();
			//System.out.println(System.currentTimeMillis());
			//synchronized(this)
			{
				DBResult rs = jAgent.query(sqlCnt);
				//记录总数
				int nTotalRS=0;
				if (rs!=null){
					if (rs.getRowCount()>0){
						String cnt=rs.getString(0,0);
						try{
							nTotalRS=Integer.parseInt(cnt);
						}catch(Exception e){}
					}
				}
				strRtn+=nTotalRS+this.ListRowEnd;
				nPage--;
				if (nPage<0)nPage=0;
				int nStart=nPage*nPageSize;
				int nEnd=(nPage+1)*nPageSize-1;
				rs = jAgent.query(sql,nStart,nEnd);
				if (rs!=null){
					String strColInfo="";
					for (int iCol=0;iCol<rs.getColCount();iCol++){
						strColInfo+=rs.getColumAttribute(iCol).getColname()+this.FieldSplit;
					}
					strRtn+=strColInfo+this.ListRowEnd;
					for (int i=0;i<rs.getRowCount();i++){
						String strFields="";
						for (int iCol=0;iCol<rs.getColCount();iCol++){
							strFields+=encField(rs.getString(i, iCol));
						}
						strRtn+=encRowEnd(strFields);
					}
					
				}
				
				//System.out.println(sql);
				//System.out.println(strRtn);
				return strRtn;
			}
			
			
			
		}catch(Exception e){
			
		}
		return null;
	}
	//==================得到数据库信息 结束�========================
	
	 public static void main(String[] args) throws IOException {
		 //RtnString pRtnString=new RtnString();
		 //pRtnString.setRowInfo("1","2");
		 String strNow=Utility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		 System.out.println(strNow);
     }
}

