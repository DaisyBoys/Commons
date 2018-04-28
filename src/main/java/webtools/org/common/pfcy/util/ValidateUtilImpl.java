package webtools.org.common.pfcy.util;

import java.util.regex.Pattern;



/**
 * ��������֤����
 * @author jack.dong
 */
public class ValidateUtilImpl implements ValidateUtil {
		
	/**
	 *
	 * @param compileStr ����ƥ���ַ�
	 * @param value ����Ҫ��֤�����
	 * @return true|false �Ƿ�ƥ��
	 */
	public boolean matcher(String compileStr,String value){
		if (isNull(value)||isNull(compileStr)) {
			return false;
		}
		return Pattern.compile(compileStr).matcher(value).find();
	}
	
	/**
	 * �ж��Ƿ�Ϊnull
	 * @param value ����Ҫ��֤�����
	 * @return true|false �Ƿ�ƥ��
	 */
	public boolean isNull(String value) {
		return value==null?true:false;
	}
	
	/**
	 * ������֤
	 * @param sizeNum ����
	 * @param value ��֤���ַ�
	 * @return
	 */
	public boolean isSize(int sizeNum,String value){
		String compileStr="^.{"+sizeNum+"}$";
		return matcher(compileStr, value);
	}
	
	/**
	 * ��󳤶���֤
	 * @param sizeNum ��󳤶�
	 * @param value ��֤���ַ�
	 * @return
	 */
	public boolean maxSize(int sizeNum,String value){
		String compileStr="^.{0,"+sizeNum+"}$";
		return matcher(compileStr, value);
	}
	
}
