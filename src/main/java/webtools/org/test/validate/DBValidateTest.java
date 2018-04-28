package webtools.org.test.validate;

import webtools.org.common.pfcy.database.dbvalidate.dbfile.DBValidateCentral;
import webtools.org.common.pfcy.database.dbvalidate.dbdata.DBWriter;
import webtools.org.common.pfcy.database.insert.InsertFactory;
import webtools.org.common.pfcy.database.insert.InsertToMany;
import webtools.org.common.pfcy.database.insert.InsertToOne;
import webtools.common.database.DBManager;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

public class DBValidateTest {

	/**
	 * �����ݿ���֤�ļ�
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		/**
		 * ����ļ���ʼ
		 */
		
		//��ݿ����� ��ʼ����ݿ�����
		Map map=new ConcurrentHashMap();
		map.put("driver","com.mysql.jdbc.Driver");
		map.put(
		//"url","jdbc:mysql://192.168.0.100:3306/erp_head_db");//
		//map.put("url","jdbc:mysql://192.168.0.100:3306/erp_head_db");			
		//"url","jdbc:mysql://192.168.0.100:3306/dishes_db_vjin");//
		//"url","jdbc:mysql://192.168.0.100:3306/property_mgr");//
		//"url","jdbc:mysql://192.168.0.100:3306/dishes_db_v201");
		//"url","jdbc:mysql://192.168.0.200:3306/erp_head_db_gfzy");
		"url","jdbc:mysql://192.168.0.200:3306/fieldmgr_db");//
		
		map.put("user","test");//
		map.put("password","test123");//
		map.put("database.charset","ISO8859-1");
		map.put("application.charset","GBK");
		map.put("database.type","MySQL");
		DBManager.initConnection(map);
		//==================
		
		//================
		
		//д���ı�
		DBWriter dWriter=new DBWriter();
		
		//List<List<String>> results=dWriter.getValdiateCentral("erp_head_db");
		//List<List<String>> results=dWriter.getValdiateCentral("dishes_db_vjin");//
		//List<List<String>> results=dWriter.getValdiateCentral("property_mgr");//
		//List<List<String>> results=dWriter.getValdiateCentral("dishes_db_v201");
		//List<List<String>> results=dWriter.getValdiateCentral("erp_head_db_gfzy");
		List<List<String>> results=dWriter.getValdiateCentral("fieldmgr_db");//
		
		//dWriter.writerFile("E:/dys/java_work/dbvalidate/dishes_db.txt", "utf-8",results);//
		dWriter.writerFile("E:/dys/java_work/田间管理/fieldmgr/WebRoot/WEB-INF/config/db.validate", "utf-8",results);
		//dWriter.writerFile("E:/dys/java_work/生产线/erpcenter/WebRoot/WEB-INF/config/db.validate", "utf-8",results);
		/**
		 * ��ֹ����һ�� ����ļ�����
		 */
		
		if(true)return;
		//��������֤�ı��е���ݲ���
		DBValidateCentral dbvCentral=new DBValidateCentral();
		dbvCentral.initDBData("C:\\Documents and Settings\\10\\����\\�����ݿ�ṹ.txt");
		
		if(true)return;
		
		//��֤ʱ��
		//int mes=dbvCentral.columnValidateMatcher("names.bb","2011-05-05 11:63:12",Constant.vdDay);
		
		//ErrorMessage eMessage=new ErrorMessage();
		
		//String s=eMessage.getErrorMessage(mes);
		
		//System.out.println(s);
		
		//����
		
		InsertFactory iFactory=new InsertFactory();
		InsertToMany many=new InsertToMany();
		InsertToOne one=new InsertToOne();
		one.setTableName("names");
		one.setPropertie("id","21");
		one.setPropertie("name","aa");
		one.setPropertie("bb","2011-05-05 11:03:12");
		InsertToOne one1=new InsertToOne();
		one1.setTableName("names");
		one1.setPropertie("id","22");
		one1.setPropertie("name","bb");
		one1.setPropertie("bb","2011-05-05 11:03:12");
		
		many.setProperties(one);
		many.setProperties(one1);
		
		String[] sqls=many.getInsertSqls();
		
		iFactory.execute(sqls);
		
	}

}
