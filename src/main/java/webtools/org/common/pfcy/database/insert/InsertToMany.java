package webtools.org.common.pfcy.database.insert;

import java.util.ArrayList;
import java.util.List;

import webtools.org.common.pfcy.database.dbvalidate.validate.ErrorMessage;

/**
 * ���������ݴ���
 * @author jack.dong
 *
 */
public class InsertToMany {

	private List<InsertToOne> properties=null;
	
	private String errorMes=null;
	
	/**
	 * ����һ������
	 * @param insertToOne ������ݷ�װ
	 */
	public void setProperties(InsertToOne insertToOne) {
		if (this.properties==null) {
			this.properties=new ArrayList();
		}
		this.properties.add(insertToOne);
	}
	
	/**
	 * ��ȡ�����������
	 * @return
	 * @throws Exception 
	 */
	public String[] getInsertSqls() throws Exception{
		String[] instr=null;
		if (properties!=null) {
			instr=new String[properties.size()];
			for (int i = 0; i < properties.size(); i++) {
				InsertToOne iOne=properties.get(i);
				String sql=iOne.getInsertSql();
				if (iOne.errorColumn!=null) {
					ErrorMessage eMessage=new ErrorMessage();
					
					throw new Exception(iOne.getTableName()+"��"+iOne.errorColumn+"�д���:"+eMessage.getErrorMessage(iOne.errorNum));
				}
				instr[i]=sql;
			}
		}
		return instr;
	}
	
}
