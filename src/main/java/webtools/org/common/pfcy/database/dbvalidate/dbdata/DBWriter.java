package webtools.org.common.pfcy.database.dbvalidate.dbdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import webtools.common.database.DBManager;
import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;

/**
 * ��ݿ���ݲ���
 * @author jack.dong
 *
 */
public class DBWriter {
	
	/**
	 * ��ȡ��ݿ���֤���
	 * @throws Exception 
	 */
	public List<List<String>> getValdiateCentral(String databaseName) throws Exception{
		
		List<List<String>> lists=null;
		
		JdbcAgent agent=null;
		
		try {			
			agent=new JdbcAgent();
		} catch (Exception e) {
			throw new Exception("common.database.DBManagerException:dataSource is null");
		}	
		DBResult rs=agent.query("SHOW TABLES FROM "+databaseName);
		
		//��ʼ����ݿ������б�
		if (rs!=null) {
			lists=new ArrayList<List<String>>();
			for (int i = 0; i < rs.getRowCount(); i++) {
				String tableName=rs.getString(i, 0);
				DBResult rsT=agent.query("SHOW COLUMNS FROM "+tableName);
				
				Pattern pattern=null;
				Matcher matcher=null;
				for(int n=0;n<rsT.getRowCount();n++){
					List lst=new ArrayList();
					String typeStr=(String) rsT.getObject(n,1);
					int length=typeStr.indexOf("(");
					
					if (length!=-1) {
						String str=typeStr.substring(length+1,typeStr.length()-1);
						if (str.indexOf(",")>0){
							pattern=Pattern.compile("[(]\\d*.\\d*[)]");
							matcher=pattern.matcher(typeStr);
							typeStr=matcher.replaceFirst("");
							if (typeStr.equalsIgnoreCase("float")){
								length=9;
							}
						}else{
						
							length=Integer.parseInt(typeStr.substring(length+1,typeStr.length()-1));
							pattern=Pattern.compile("[(]\\d*[)]");
							matcher=pattern.matcher(typeStr);
							typeStr=matcher.replaceFirst("");
						}
					}
					lst.add(tableName.toLowerCase()+"."+rsT.getObject(n,0).toString().toLowerCase());//����+���� 0
					
					lst.add(typeStr);//���� 1
					lst.add(length);//���� 2
					lst.add(rsT.getObject(n,2));//�Ƿ�Ϊ�� 3
					lst.add(rsT.getObject(n,5));//�Ƿ�������
					
					lists.add(lst);
					
				}
			}
		}
		
		return lists;
		
	}
	
	//=============================

	public int writerFile(String filePath,String chartFormat,List<List<String>> columns) {
		if(columns==null)return 1;//û�м�⵽д�����
		if(columns.size()<1)return 1;
		FileOutputStream out = null;
		
		try {
			out=new FileOutputStream(new File(filePath));
			
			for (int i = 0; i < columns.size(); i++) {//�?
				
				List lsts=columns.get(i);
				
				if(lsts!=null){//���±�������Լ������
					
					for (int j = 0; j < lsts.size(); j++) {
						
						out.write((lsts.get(j)+"\t").getBytes(chartFormat));
						
					}
					out.write("\r\n".getBytes(chartFormat));
				}
				
			}
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					return 2;//�ļ��쳣�˳��رմ���
				}
			}
			return 0;//�ļ���ȡʧ��
		}
        
		return -1;
	}
	
	/**
	 * ��ݿ�ṹ����
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		//��ʼ����ݿ�����
		Map map=new ConcurrentHashMap();
		map.put("driver","com.mysql.jdbc.Driver");
		map.put("url","jdbc:mysql://192.168.0.100:3306/erp-db");
		map.put("user","root");
		map.put("password","root");
		map.put("database.charset","ISO8859-1");
		map.put("application.charset","GBK");
		map.put("database.type","MySQL");
		try {
			DBManager.initConnection(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//================����Ϊ��ʼ����ݿ�����
		
		
		DBWriter dbWriter=new DBWriter();
		
		String databaseName="test";
		dbWriter.getValdiateCentral(databaseName);
		
		
		//dbWriter.writerFile(filePath, chartFormat, columns);
		
		
		
		//�����ı�����ȫ·��
		String saveFilePath="C:\\Documents and Settings\\10\\����\\erp-db.txt";
		
		//build.save(saveFilePath, "utf-8");
		
	}
	
}
