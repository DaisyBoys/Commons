package webtools.com.tools;



import webtools.common.FileMgr;
import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;

/**
 * 创建数据库对应的表
 * */
public class ToolBuildDBFile {
	private final FileMgr pFileMgr=new FileMgr();
	private final JdbcAgent agent=new JdbcAgent();;
	public void doBuild(String path,String databaseName){
		try {
			boolean isExist=pFileMgr.isFileExist(path);
			if(!isExist){
				pFileMgr.mkdir(path);
			}
			this.save(path, databaseName);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private String save(String path,String databaseName){
		String strRtn="";
		try {
			DBResult rs=agent.query("SHOW TABLES FROM "+databaseName);
			if (rs!=null) {
				for (int i = 0; i < rs.getRowCount(); i++) {
					String tableName=rs.getString(i, 0);
					DBResult rsT=agent.query("SHOW COLUMNS FROM "+tableName);
					String selField=tableName+"\r\n\r\n";
					String strResult="";
					for(int n=0;n<rsT.getRowCount();n++){
						String col=(String)rsT.getObject(n,0);
						if(n==0){
							selField+="\" t1."+col+" as "+col+"\" +";
						}else{
							selField+="\",t1."+col+" as "+col+"\" +";
						}
						selField+="\r\n";
						
						strResult+="final String "+col+"    =pResult.getRSValue(i,\""+col+"\");\r\n";
						
					}
					String file=path+"/"+tableName+".txt";
					String content=selField+"\r\n"+strResult;
					this.pFileMgr.saveContent2File(file, content, "utf-8");
					
					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return strRtn;
	}
}
