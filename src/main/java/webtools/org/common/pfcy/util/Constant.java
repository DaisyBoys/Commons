package webtools.org.common.pfcy.util;

import javax.servlet.http.HttpServletResponse;

/**
 * ���������ʽ
 * @author jack.dong
 *
 */
public enum Constant {

	/**
	 * ����
	 */
	vdChineseE("^([a-zA-Z\\u0391-\\uFFE5|\\s*]+$)?$"),
	vdChinese("^([\\u0391-\\uFFE5|\\s*]+$)?$"),
	/**
	 * Ӣ��
	 */
	vdEnglish("^([a-zA-Z|\\s*]+)?$"),
	
	/**
	 * ����
	 */
	vdNumber	("^(\\d*)?$"),
	
	/**
	 * ����
	 */
	 //vdInteger("^([-\\+]?\\d{1,9})?$"),
	vdInteger("^-?\\d+$"),//整数   
	vdIntegerZ("^+?\\d+$"),//正整数   

	//vStrNum("^(?!_)(?!.*?_$)[0-9a-zA-z-_]+$"),//只有字母、数字和下划线且不能以下划线开头和结尾的正则表达式
	vStrNum("^[0-9a-zA-z-_]+$"),//只能是字母,数字,减号,下划线组成
	//
	/**
	 * �����ȸ�����
	 */
	 //vdFloat	("^((([-\\+]?\\d+)(\\.\\d+))|(\\.\\d+)|(\\d*))?$"),
	 vdFloat("^(-?\\d+)(\\.\\d+)?$"),//浮点数   

	
	/**
	 * ˫���ȸ���
	 */
	// vdDouble("^((([-\\+]?\\d+)(\\.\\d+))|(\\.\\d+)|(\\d*))?$"),
	 vdDouble("^(-?\\d+)(\\.\\d+)?$"),//浮点数   
	
	/**
	 * �ַ�
	 */
	 vdString("^([^'<>\\[\\]\"]+)?$"),	
	
	/**
	 * ������
	 */
	 //vdInt	("^(\\d{1,9})?$"),
	 vdInt	("^-?\\d+$"),//整数   
	
	/**
	 * ������(-123)
	 */
	 vdMinusint("^(\\-([1-9])(\\d*))?$"),       
	
	/**
	 * ��������(2004-08-12)
	 */
	 vdDate   ("^((([1-9]\\d{3})|([1-9]\\d{1}))-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0-1]))?$"),
	
	/**
	 * ʱ������(08:37:29)
	 */
	 vdTime    ("^((0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]))?$"),
	
	/**
	 * ����ʱ����(2004-08-12 08:37:29)
	 */
	 vdDatetime("^((([1-9]\\d{3})|([1-9]\\d{1}))-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0-1]) (0[0-9]|1[0-9]|2[0-4]):([0-5][0-9]):([0-5][0-9]))?$"),
	
	/**
	 * ����ʱ����(2004-08-12 12:25)
	 */
	 vdDatehm  ("^((([1-9]\\d{3})|([1-9]\\d{1}))-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0-1]) (0[0-9]|1[0-9]|2[0-4]):([0-5][0-9]))?$"),
	
	/**
	 * ���ڵ���
	 */
	 vdYear    ("^(\\d{4})?$"),
	
	/**
	 * ���ڵ���
	 */
	 vdMonth   ("^([1-9]|0[1-9]|1[0-2])?$"),
	
	/**
	 * ���ڵ���
	 */
	 vdDay     ("^([1-9]|0[1-9]|1[0-9]|2[0-9]|3[0-1])?$"),
	
	/**
	 * �ʱ�(100001)
	 */
	 vdPostcode("^(\\d{6})?$"),           
	
	/**
	 * ������֤(msm@hotmail.com)
	 */
	 vdEmail   ("^(.+\\@.+\\..+)?$"),
	
	/**
	 * �绰����(010-67891234)
	 */
	 vdPhone   ("^(\\(\\d{3}\\))?(\\(?(\\d{3}|\\d{4}|\\d{5})\\)?(-?)(\\d+))?((-?)(\\d+))?$"),
	
	/**
	 * �ƶ��绰����(13867891234)
	 */
	 vdMobiletel("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"),
	
	/**
	 * IP��ַ
	 */
	 vdIp("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-5][0-5])\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-5][0-5])\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-5][0-5])\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-5][0-5]))?$"),
	
	/**
	 * ���֤��֤
	 */
	 vdIdcard  ("^(\\d{15}|\\d{18}|\\d{17}X|\\d{17}x)?$"),
	
	/**
	 * ���ֺ���ĸ�ַ�
	 */
	 vdNumAndStr(" /^([0-9a-zA-Z]+)?$"),
	
	/**
	 * ����ĸ�ַ�
	 */
	 vdLetterStr(" /^([a-zA-Z]+)?$"),
	
	/**
	 * ���ַ�(""|"   ")
	 */
	vdEmpty("^(\\s*)?$");
		
	private String validateStr; //�����ַ�
	
	/**
	 * �����ַ�ֵ
	 * @return
	 */
	public String value() { 
		return validateStr;
	}
	
	Constant(String str) { 
		validateStr = str; 
	}
	
	public String toString(){
		return this.name().substring(2);
	}

	
	
	
}