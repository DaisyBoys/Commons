package webtools.org.common.pfcy.database.dbvalidate.dbfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import webtools.org.common.pfcy.database.dbvalidate.validate.ValidateTable;
import webtools.org.common.pfcy.util.Constant;


/**
 * ��ȡ�ļ����
 * @author jack.dong
 *
 */
public class DBValidateCentral {
	
	//��ǰ��֤��
	private static ValidateTable vTable=new ValidateTable();
	

	/**
	 * ��ݿ����нṹ��֤
	 * @param tabField
	 * @param value
	 * @return
	 */
	public int columnValidateData(String tabField, String value){
		return vTable.validateM_vdata(tabField, value);
	}
	
	/**
	 * ����������֤����ݿ���е��еĽṹ��֤
	 * @param tabField
	 * @param value
	 * @param pConstant
	 * @return
	 */
	public int columnValidateMatcher(String tabField, String value,Constant pConstant){
		int result=vTable.otherValidate(pConstant.value(), value);//�Զ�����֤
		if (result==-1) {//����Զ�����֤û�д���
			result=columnValidateData(tabField, value);//��֤��ݿ�
		}
		return result;
	}
	
	/**
	 * ���Զ���������֤����ݿ���е��еĽṹ��֤
	 * @param tabField
	 * @param value
	 * @param matchStr �Զ�������
	 * @return
	 */
	public int columnValidateMatcher(String tabField, String value,String matchStr){
		int result=vTable.otherValidate(matchStr, value);//�Զ�����֤
		if (result==-1) {//����Զ�����֤û�д���
			result=columnValidateData(tabField, value);//��֤��ݿ�
		}
		return result;
	}
		
	//==============================
	
	/**
	 * ������ݽṹ
	 * @param filePath ������ݽṹ�ļ�·��
	 * @return �������
	 */
	public void initDBData(String filePath) {
		
		BufferedReader reader = null;
		
		try {
			
			Map<String,String[]> m_vdata=new ConcurrentHashMap<String, String[]>();
			
			reader = new BufferedReader(new FileReader(new File(filePath)));
			
			String str=null;
			
			while((str=reader.readLine())!=null){
				
				String[] result=null;
				String tableFild=null;
				String[] strs=str.split("\t");
				
				if (strs!=null) {
					tableFild=strs[0];
					result=new String[strs.length-1];
					for (int i = 1; i < strs.length; i++) {
						result[i-1]=strs[i];
					}
				}
				
				m_vdata.put(tableFild, result);
			}
			
			vTable.m_vdata=m_vdata;
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		try {
			if(reader!=null)reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
