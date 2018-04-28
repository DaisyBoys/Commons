package webtools.org.common.pfcy.util;

/**
 * ������֤����
 * @author jack.dong
 */
public interface ValidateUtil {

	/**
	 * ��������֤�ַ�
	 * @param compileStr ����ƥ���ַ�
	 * @param value ����Ҫ��֤�����
	 * @return true|false �Ƿ�ƥ��
	 */
	public boolean matcher(String compileStr,String value);
	
	/**
	 * �ж��Ƿ�Ϊnull
	 * @param value ����Ҫ��֤�����
	 * @return true|false �Ƿ�ƥ��
	 */
	public boolean isNull(String value);
	
	/**
	 * ������֤
	 * @param sizeNum ����
	 * @param value ��֤���ַ�
	 * @return
	 */
	public boolean isSize(int sizeNum,String value);
	
	/**
	 * ��󳤶���֤
	 * @param sizeNum ��󳤶�
	 * @param value ��֤���ַ�
	 * @return
	 */
	public boolean maxSize(int sizeNum,String value);
	
}
