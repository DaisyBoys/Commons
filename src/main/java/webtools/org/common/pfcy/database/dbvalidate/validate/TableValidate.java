package webtools.org.common.pfcy.database.dbvalidate.validate;

/**
 * ��ݿ��ṹ�����µ��û�������ϢУ��ӿڣ�
 * �ӿ�1:
 *    public int validateM_vdata(String tabField,String value) throws Exception;
 *    ʵ�������ݿ��+�ֶ�����������ݽ���Լ����У�飬�������ͣ��ɹ��������룩��
 *    ������ݿ�Ķ�����Ϣ��Դ��MAP����ݿ��ṹ��ͼ��ͨ��"TABLE.FIELD"�����ֶν��ж�λ�����Ӷ�õ������ֶ��е����͡����ȡ��Ƿ�����Ϊ��
 * @author jack.dong
 *
 */
public interface TableValidate {
	
	/**
	 * ��֤ĳ��ĳ�е����
	 * @param tabField ����.����
	 * @param value Ҫ��֤�����
	 */
	public int validateM_vdata(String tabField,String value);
	
	
}
