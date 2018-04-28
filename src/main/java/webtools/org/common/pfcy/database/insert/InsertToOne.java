package webtools.org.common.pfcy.database.insert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import webtools.org.common.pfcy.database.dbvalidate.dbfile.DBValidateCentral;

/**
 * ����뵥����¼
 * @author jack.dong
 *
 */
public class InsertToOne {
	
	private String tableName=null;
		
	private Map propertie=null;
	
	public int errorNum=-1;

	public String errorColumn=null;
	
	/**
	 * ���ò������
	 * @param columName ����
	 * @param value �ж�Ӧ��ֵ
	 * @throws Exception 
	 */
	public void setPropertie(String columName,String value) throws Exception {
		if (this.errorNum!=-1) return;
		if (propertie==null) {
			this.propertie=new ConcurrentHashMap();
		}
		this.propertie.put(columName, value);
		DBValidateCentral dCentral=new DBValidateCentral();
		this.errorNum=dCentral.columnValidateData(tableName+"."+columName, value);
		if (this.errorNum!=-1) {
			this.errorColumn=columName;
		}
	}

	/**
	 * ��ȡ��������
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * ���ò�������
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	} 
	
	/**
	 * ��ȡ�����������
	 * @return
	 */
	public String getInsertSql(){
		String insertSql=null;
		if (this.errorNum!=-1) {//������
			return insertSql;
		}
		if (this.propertie!=null) {
			insertSql="insert into "+this.tableName;
			String keyString="",vaString="";
			Iterator iterator=this.propertie.keySet().iterator();
			while(iterator.hasNext()){
				String key=(String) iterator.next();
				if (key!=null) {
					String value=(String)this.propertie.get(key);
					if (key!=null) {
						keyString+=key;
						vaString+="'"+value+"'";
						if (iterator.hasNext()) {
							keyString+=",";
							vaString+=",";
						}
					}
				}
			}
			insertSql+=" ("+keyString+") value("+vaString+");";
		}
		
		return insertSql;
	}
	
}
