package webtools.com.KeyMgr;


import java.text.SimpleDateFormat;
import java.util.Date;

import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;

public final class MyKey {
	private JdbcAgent jAgent=new JdbcAgent();
//	public MyKey(JdbcAgent jAgent){
//		this.jAgent=jAgent;
//	}
	protected  void finalize() throws Throwable{             
        super.finalize();
        try {
        	jAgent=null;
       } catch (Exception e) {
			// TODO: handle exception
		}
          
    } 
	public String getKey(final String[] paramList){
		
		//String now=MyDataFormat.currentDate()+" "+MyDataFormat.currentTime();//当前时间
		//String nowUsed= MyDataFormat.dateAdd("d", -2, now);
		
		String strRtn="";
		try {
			if (paramList != null)
			{
				String KeyLen=paramList[0];
				String TableName=paramList[1];
				String id=paramList[2];
				
				int nL=Integer.parseInt(KeyLen);
				CodeMgr pCodeMgr =new CodeMgr();
				long lKey=pCodeMgr.buildResult(nL);
				
				int nCnt=0;
				try {
					String sql="select count(*) as cnt from "+TableName +" where "+id+"='"+lKey+"'";
					
					
					DBResult rs=jAgent.query(sql);
					if (rs!=null){
						if (rs.getRowCount()>0){
							String strCnt=rs.getString(0,"cnt");
							nCnt=Integer.parseInt(strCnt);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				while(nCnt>0){
					try {
						String sql="select count(*) as cnt from "+TableName +" where "+id+"='"+lKey+"'";
						
						
						DBResult rs=jAgent.query(sql);
						if (rs!=null){
							if (rs.getRowCount()>0){
								String strCnt=rs.getString(0,"cnt");
								nCnt=Integer.parseInt(strCnt);
								lKey=pCodeMgr.buildResult(nL);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
				
				return ""+lKey;
							
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return strRtn;
	}
	
	@SuppressWarnings("static-access")
	public String getKeyByDate(final String[] paramList){
		
		//String now=MyDataFormat.currentDate()+" "+MyDataFormat.currentTime();//当前时间
		//String nowUsed= MyDataFormat.dateAdd("d", -2, now);
		
		String strRtn="";
		try {
			if (paramList != null)
			{
				Date d=new Date();
				SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
				String str_date=df.format(d);
				
			
				SimpleDateFormat dNow=new SimpleDateFormat("yyyy-MM-dd");
				String nowDay=dNow.format(d);
				
				String TableName=paramList[0];
				String create_date=paramList[1];
				String id=paramList[2];
				String strFirst=paramList[3];
			
				
				int nCnt=0;
				try {
					String sql="select count(*) as cnt from "+TableName+" where "+create_date+"='"+nowDay+"'";
					
					
					DBResult rs=jAgent.query(sql);
					if (rs!=null){
						if (rs.getRowCount()>0){
							String strCnt=rs.getString(0,"cnt");
							nCnt=Integer.parseInt(strCnt);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				nCnt++;
				String strCnt="";
				strCnt=strCnt.format("-%06d", nCnt);
				strRtn=strFirst+str_date+strCnt;
				int nCntChk=1;
				while(nCntChk>0){
					try {
						String sql="select count(*) as cnt from "+TableName +" where "+id+"='"+strRtn+"'";
						
						
						DBResult rs=jAgent.query(sql);
						if (rs!=null){
							if (rs.getRowCount()>0){
								strCnt=rs.getString(0,"cnt");
								nCntChk=Integer.parseInt(strCnt);
								if (nCntChk==0){
									break;
								}
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					nCnt++;
					strCnt=strCnt.format("-%06d", nCnt);
					strRtn=strFirst+str_date+strCnt;
					
				}
							
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return strRtn;
	}
	
	public String getKeyByFrist(final String strFrist,final String[] paramList){
		
		//String now=MyDataFormat.currentDate()+" "+MyDataFormat.currentTime();//当前时间
		//String nowUsed= MyDataFormat.dateAdd("d", -2, now);
		
		String strRtn="";
		try {
			if (paramList != null)
			{
				String KeyLen=paramList[0];
				String TableName=paramList[1];
				String id=paramList[2];
				
				int nL=Integer.parseInt(KeyLen);
				CodeMgr pCodeMgr =new CodeMgr();
				long lKey=pCodeMgr.buildResult(nL);
				
				int nCnt=0;
				String strKey=strFrist+lKey;
				try {
					String sql="select count(*) as cnt from "+TableName +" where "+id+"='"+strKey+"'";
					
					
					DBResult rs=jAgent.query(sql);
					if (rs!=null){
						if (rs.getRowCount()>0){
							String strCnt=rs.getString(0,"cnt");
							nCnt=Integer.parseInt(strCnt);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				while(nCnt>0){
					try {
						String sql="select count(*) as cnt from "+TableName +" where "+id+"='"+strKey+"'";
						
						
						DBResult rs=jAgent.query(sql);
						if (rs!=null){
							if (rs.getRowCount()>0){
								String strCnt=rs.getString(0,"cnt");
								nCnt=Integer.parseInt(strCnt);
								lKey=pCodeMgr.buildResult(nL);
								strKey=strFrist+lKey;
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
				
				return ""+strKey;
							
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return strRtn;
	}
	public String getKeyByDateBillID(final String[] paramList){
		
		//String now=MyDataFormat.currentDate()+" "+MyDataFormat.currentTime();//当前时间
		//String nowUsed= MyDataFormat.dateAdd("d", -2, now);
		
		String strRtn="";
		try {
			if (paramList != null)
			{
				Date d=new Date();
				SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
				String str_date=df.format(d);
				
			
				SimpleDateFormat dNow=new SimpleDateFormat("yyyy-MM-dd");
				String nowDay=dNow.format(d);
				
				String TableName=paramList[0];
				String create_date=paramList[1];
				String id=paramList[2];
				String strFirst=paramList[3];
				int nL=6;
				CodeMgr pCodeMgr =new CodeMgr();
				long lKey=pCodeMgr.buildResult(nL);
				int nCnt=0;
				try {
					String sql="select count(*) as cnt from "+TableName+" where "+create_date+"='"+nowDay+"'";
					
					
					DBResult rs=jAgent.query(sql);
					if (rs!=null){
						if (rs.getRowCount()>0){
							String strCnt=rs.getString(0,"cnt");
							nCnt=Integer.parseInt(strCnt);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				nCnt++;
				String strCnt="";
				strCnt=strCnt.format("%06d", lKey);
				strRtn=strFirst+str_date+strCnt;
				int nCntChk=1;
				while(nCntChk>0){
					try {
						String sql="select count(*) as cnt from "+TableName +" where "+id+"='"+strRtn+"'";
						
						
						DBResult rs=jAgent.query(sql);
						if (rs!=null){
							if (rs.getRowCount()>0){
								strCnt=rs.getString(0,"cnt");
								nCntChk=Integer.parseInt(strCnt);
								if (nCntChk==0){
									break;
								}
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					nCnt++;
					strCnt=strCnt.format("%06d", lKey);
					strRtn=strFirst+str_date+strCnt;
					
				}
							
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return strRtn;
	}
}
