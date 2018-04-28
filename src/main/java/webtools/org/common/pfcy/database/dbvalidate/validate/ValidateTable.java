package webtools.org.common.pfcy.database.dbvalidate.validate;

import java.util.Map;
import java.util.regex.Pattern;

import webtools.org.common.pfcy.util.Constant;
import webtools.org.common.pfcy.util.ValidateUtil;
import webtools.org.common.pfcy.util.ValidateUtilImpl;


/**
 * �����е������֤
 * @author jack.dong
 */
public class ValidateTable implements TableValidate {
	
	public Map<String,String[]> m_vdata=null;
	
    private ValidateUtil m_pValidateUtil=null;
    
    /**
     * Ĭ����֤
     * @param tabField ���ʼ�
     * @param value Ҫ��֤��ֵ
     * @return ���ش�����
     */
	public int validateM_vdata(String tabField, String value) {
		
		int result=-1;
		
		if (m_vdata==null){
			return 0;//δ�ҵ�У��
		}
		
		String[] lstInfo=getValidateListInfo(tabField);
		String validateType=null,validateNull=null;
		Integer validateLen=-1;
		if (lstInfo!=null){
			validateType=getTypeInfo(lstInfo,0);
			String le=getTypeInfo(lstInfo,1);
			if ("double".equalsIgnoreCase(validateType)||"double unsigned".equalsIgnoreCase(validateType)){
				le="15";
			}
			if ("float".equalsIgnoreCase(validateType)){
				le="9";
			}
			if(le!=null){
				validateLen=Integer.valueOf(le);//
			}
			validateNull=getTypeInfo(lstInfo,2);//
		}else{
			return 1;//
		}
		
	if (isNull(validateNull)){
			
			if (value==null) {
				return 2;//
			}
			
		}		
		
		if (validateLen.intValue()!=-1) {
			int nLenValLen=getValueLen(value);
			int nValiadateLen=validateLen.intValue();
			if (getValueLen(value)>validateLen.intValue()){
				
				return 3;//
				
			}
			
		}
		
	
		if (validateType!=null){
			
			int nVCode= validateType(validateType,value);
			
		    if (nVCode!=-1){
		    	
		    	return nVCode;
		    	
		    }
		    
		   
		    
		}
		
		return result;//�ɹ�
	}
	
	private String[] getValidateListInfo(String key) {
		return m_vdata.get(key);
	}
	
	private String getTypeInfo(String[] lst,int i) {
		return lst[i];
	}
	
	private boolean isNull(String validateNull){
		if (validateNull==null) return false;
		if (validateNull.equalsIgnoreCase("yes")) return true;
		return false;
	}
	
	private int getValueLen(String val){
		int l=0;
		if(val!=null){
			l=val.length();
		}
		return l;
	}
		
	private int validateType(String validateType,String value){
		
		if (m_pValidateUtil==null) m_pValidateUtil=new ValidateUtilImpl();
		
		validateType=convertDBType(validateType);
		
		//
		if (validateType.equalsIgnoreCase("Int")|| validateType.equalsIgnoreCase("int unsigned")|| validateType.equalsIgnoreCase("tinyint unsigned")){
		
			if (!m_pValidateUtil.matcher(Constant.vdInt.value(),value)){
				return 6;//
			}
			
		}else if(validateType.equalsIgnoreCase("Double")||validateType.equalsIgnoreCase("double unsigned")){
			
			if (!m_pValidateUtil.matcher(Constant.vdDouble.value(),value)){
				return 7;//
			}
		
		}else if(validateType.equalsIgnoreCase("Float")){
			
			if (!m_pValidateUtil.matcher(Constant.vdFloat.value(),value)){
				return 8;//
			}
						
		}else if(validateType.equalsIgnoreCase("Datetime")){
			
			if (!m_pValidateUtil.matcher(Constant.vdDatetime.value(),value)){
				return 9;//
			}
			
		}else if(validateType.equalsIgnoreCase("Date")){
			
			if (!m_pValidateUtil.matcher(Constant.vdDate.value(),value)){
				return 10;//
			}
						
		}else if(validateType.equalsIgnoreCase("String")){
		}else{
			return 11;//
		}
		
		return -1;//
		
	}
	
	/**
	 * �Զ�����֤���
	 * @param matchStr
	 * @param value
	 * @return
	 */
	public int otherValidate(String matchStr,String value){
		
		if (value!=null) {
			if (m_pValidateUtil==null) m_pValidateUtil=new ValidateUtilImpl();
			if (matchStr==null) {
				
				return 4;//
				
			}else if(!m_pValidateUtil.matcher(matchStr, value)){
				
				return 5;//
				
			}
			
		}
		
		
		return -1;//�ɹ�
	}
	
	/**
	 * ����ݿ�����ת��Ϊͳһ��ʶ�������
	 * @param str Ҫת�����ַ�
	 * @return ת�����ͳһ��ʶ������
	 */
	private String convertDBType(String str){
		if (str==null)return str;
		
		if ("int".equalsIgnoreCase(str.trim())||"bigint".equalsIgnoreCase(str.trim())
				||"tinyint".equalsIgnoreCase(str.trim())
				||"SMALLINT".equalsIgnoreCase(str.trim())||"MEDIUMINT".equalsIgnoreCase(str.trim())) {
			str="Int";
		}else if ("DOUBLE".equalsIgnoreCase(str.trim())||"real".equalsIgnoreCase(str.trim())) {
			str="Double";
		}else if ("FLOAT".equalsIgnoreCase(str.trim())||"DECIMAL".equalsIgnoreCase(str.trim())
				||"NUMERIC".equalsIgnoreCase(str.trim())) {
			str="Float";
		}else if ("DATETIME".equalsIgnoreCase(str.trim())) {
			str="Datetime";
		}else if ("DATE".equalsIgnoreCase(str.trim())) {
			str="Date";
		}else if("VARCHAR".equalsIgnoreCase(str.trim())||"TEXT".equalsIgnoreCase(str.trim())||"char".equalsIgnoreCase(str.trim())
				||"LONGTEXT".equalsIgnoreCase(str.trim())||"LONGBLOB".equalsIgnoreCase(str.trim())||"mediumtext".equalsIgnoreCase(str.trim())){
			str="String";
		}
		
		return str;
	}
	
}
