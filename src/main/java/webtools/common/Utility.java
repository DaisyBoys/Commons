package webtools.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: liyong
 * Date: 2006-12-7
 * Time: 14:10:46
 * ͨ�÷����࣬�ṩͨ�õĲ�����������.
 */
public class Utility {
   private static CharExchange page_char_exchange = null;
   
   /**
    * �÷�����DBManager��InitialConnection�б����ã���Ҫ�趨ҳ���ַ�����ISO8859-1�Ļ�ת��
    * ��Ҫ��getSafeParameter �� getParameters �б����á�
    * */
   public static void set_app_charcode(String appchar,String formchar)
   {
	   page_char_exchange = new CharExchange();
	   page_char_exchange.setCode1(appchar);
	   page_char_exchange.setCode2(formchar);
   }
   /**
    * ��ȡ�ַ����ת������
    * */
   public static CharExchange get_char_oper()
   {
	   return page_char_exchange;
   }
	/**
	 * ���ո��format��ʽȡ�õ�ǰʱ����ַ���ʽ��
	 *���ں�ʱ��ģʽ
	*���ں�ʱ���ʽ�����ں�ʱ��ģʽ �ַ�ָ���������ں�ʱ��ģʽ�ַ��У�δ����ŵ���ĸ 'A' �� 'Z' �� 'a' �� 'z' ������Ϊģʽ��ĸ��������ʾ���ڻ�ʱ���ַ�Ԫ�ء��ı�����ʹ�õ���� (') ��������������н��͡�"''" ��ʾ����š����������ַ����ͣ�ֻ���ڸ�ʽ��ʱ�����Ǽ򵥸��Ƶ�����ַ������ڽ���ʱ�������ַ����ƥ�䡣 
	*����������ģʽ��ĸ�����������ַ� 'A' �� 'Z' �� 'a' �� 'z' ������������<br> 
	*<br> 
	*��ĸ  ���ڻ�ʱ��Ԫ��  ��ʾ  ʾ��  <br> 
	*G  Era ��־��  Text  AD  <br> 
	*y  ��  Year  1996; 96  <br> 
	*M  ���е��·�  Month  July; Jul; 07  <br> 
	*w  ���е�����  Number  27  <br> 
	*W  �·��е�����  Number  2  <br> 
	*D  ���е�����  Number  189  <br> 
	*d  �·��е�����  Number  10  <br> 
	*F  �·��е�����  Number  2  <br> 
	*E  �����е�����  Text  Tuesday; Tue  <br> 
	*a  Am/pm ���  Text  PM  <br> 
	*H  һ���е�Сʱ��0-23��  Number  0  <br> 
	*k  һ���е�Сʱ��1-24��  Number  24  <br> 
	*K  am/pm �е�Сʱ��0-11��  Number  0  <br> 
	*h  am/pm �е�Сʱ��1-12��  Number  12  <br> 
	*m  Сʱ�еķ�����  Number  30  <br> 
	*s  �����е�����  Number  55  <br> 
	*S  ������  Number  978  <br> 
	*z  ʱ��  General time zone  Pacific Standard Time; PST; GMT-08:00  <br> 
	*Z  ʱ��  RFC 822 time zone  -0800  <br> 
	*
	*ģʽ��ĸͨ�����ظ��ģ�������ȷ���侫ȷ��ʾ�� <br> 
	*Text: ���ڸ�ʽ����˵�����ģʽ��ĸ���������ڵ��� 4����ʹ����ȫ��ʽ�������ڿ��õ������ʹ�ö���ʽ����д��ʽ�����ڽ�����˵��������ʽ���ǿɽ��ܵģ���ģʽ��ĸ�������޹ء� <br> 
	*Number: ���ڸ�ʽ����˵��ģʽ��ĸ����������С����λ�������λ���������� 0 ����Դﵽ�����������ڽ�����˵��ģʽ��ĸ�����������ԣ���Ǳ���ֿ����������ֶΡ� <br> 
	*Year: ����ʽ���� Calendar �Ǹ����������Ӧ�����¹���<br> 
	*<br> 
	*���ڸ�ʽ����˵�����ģʽ��ĸ������Ϊ 2������ݽ�ȡΪ 2 λ��,������ݽ���Ϊ number�� <br> 
	*���ڽ�����˵�����ģʽ��ĸ���������� 2�������������������н��ͣ������λ�Ƕ��١����ʹ��ģʽ "MM/dd/yyyy"���� "01/11/12" ����Ϊ��Ԫ 12 �� 1 �� 11 �ա� <br> 
	*�ڽ�����д���ģʽ��"y" �� "yy"��ʱ��SimpleDateFormat ���������ĳ��������������д����ݡ���ͨ�����ڵ���Ϊ SimpleDateFormat ʵ��֮ǰ�� 80 ���֮�� 20 �귶Χ������ɡ����磬�� "MM/dd/yy" ģʽ�£���� SimpleDateFormat ʵ������ 1997 �� 1 �� 1 �մ����ģ����ַ� "01/11/12" ��������Ϊ 2012 �� 1 �� 11 �գ����ַ� "05/04/64" ��������Ϊ 1964 �� 5 �� 4 �ա��ڽ���ʱ��ֻ��ǡ������λ������ɵ��ַ��� Character.isDigit(char) ����ģ�������ΪĬ�ϵ����͡������κ������ַ�������������н��ͣ����絥�����ַ�3 ������������ɵ��ַ����߲��������ֵ���λ�����ַ�����"-1"������ˣ�����ͬ��ģʽ�£� "01/02/3" �� "01/02/003" ����Ϊ��Ԫ 3 �� 1 �� 2 �ա�ͬ��"01/02/-3" ����Ϊ��Ԫǰ 4 �� 1 �� 2 �ա� 
	*������Ӧ������ϵͳ�ض�����ʽ�����ڸ�ʽ���ͽ��������ģʽ��ĸ������Ϊ 4 ����� 4����ʹ�������ض��� long form��������ʹ�������ض��� short or abbreviated form�� <br> 
	*Month: ���ģʽ��ĸ������Ϊ 3 ����� 3�����·ݽ���Ϊ text���������Ϊ number�� <br> 
	*General time zone: ���ʱ������ƣ������ǽ���Ϊ text�����ڱ�ʾ GMT ƫ��ֵ��ʱ��ʹ�������﷨�� <br> 
	*     GMTOffsetTimeZone:<br> 
	*             GMT Sign Hours : Minutes<br> 
	*     Sign: one of<br> 
	*             + -<br> 
	*     Hours:<br> 
	*             Digit<br> 
	*             Digit Digit<br> 
	*     Minutes:<br> 
	*             Digit Digit<br> 
	*     Digit: one of<br> 
	*             0 1 2 3 4 5 6 7 8 9Hours ������ 0 �� 23 ֮�䣬Minutes ������ 00 �� 59 ֮�䡣��ʽ�������Ի����޹صģ��������ֱ���ȡ�� Unicode ��׼�� Basic Latin �顣 <br> 
	*���ڽ�����˵��RFC 822 time zones Ҳ�ǿɽ��ܵġ� <br> 
	*
	*RFC 822 time zone: ���ڸ�ʽ����˵��ʹ�� RFC 822 4-digit ʱ���ʽ�� <br> 
	*     RFC822TimeZone:<br> 
	*             Sign TwoDigitHours Minutes<br> 
	*     TwoDigitHours:<br> 
	*             Digit DigitTwoDigitHours ������ 00 �� 23 ֮�䡣����������� general time zones�� <br> 
	*���ڽ�����˵��general time zones Ҳ�ǿɽ��ܵġ� <br> 
	*
	*SimpleDateFormat ��֧�ֱ��ػ����ں�ʱ��ģʽ �ַ�����Щ�ַ��У�����������ģʽ��ĸ���������������Ի����йص�ģʽ��ĸ���滻��SimpleDateFormat �������ģʽ��ĸ֮����ı����ػ���������Ŀͻ��������? <br> 
	*
	*ʾ��<br> 
	*����ʾ����ʾ��������������Ի����н������ں�ʱ��ģʽ��������ں�ʱ��Ϊ����̫ƽ��ʱ��ı���ʱ�� 2001-07-04 12:08:56�� <br> 
	*���ں�ʱ��ģʽ  ���  <br> 
	*"yyyy.MM.dd G 'at' HH:mm:ss z"  2001.07.04 AD at 12:08:56 PDT  <br> 
	*"EEE, MMM d, ''yy"  Wed, Jul 4, '01  <br> 
	*"h:mm a"  12:08 PM  <br> 
	*"hh 'o''clock' a, zzzz"  12 o'clock PM, Pacific Daylight Time  <br> 
	*"K:mm a, z"  0:08 PM, PDT  <br> 
	*"yyyyy.MMMMM.dd GGG hh:mm aaa"  02001.July.04 AD 12:08 PM  <br> 
	*"EEE, d MMM yyyy HH:mm:ss Z"  Wed, 4 Jul 2001 12:08:56 -0700  <br> 
	*"yyMMddHHmmssZ"  010704120856-0700  <br> 
	*"yyyy-MM-dd'T'HH:mm:ss.SSSZ"  2001-07-04T12:08:56.235-0700  <br> 
	 * @param format 
	 * */
   public static String getCurrentDateTime(String format){
        SimpleDateFormat sFmt = new SimpleDateFormat(format);
        return sFmt.format(new Date());
    }

   public static String formatDate(Date date,String format)
   {
	   SimpleDateFormat sFmt = new SimpleDateFormat(format);
	   return sFmt.format(date);
   }
   
   private static int serial = 0;
   /**
	 * ����������ʱ������������ݿ�����ؼ���
	 * */
   public static String getPrimaryKey(){
       if(serial == 999) serial = 0;
       return getCurrentDateTime("yyyyMMddHHmmssSSS") + String.valueOf(serial ++);
   }

    /**
    * ��ȫȡ��ҳ�����֤����ȡ��null
    * */
    public static String getSafeParameter(HttpServletRequest request,String name,String def_val){
        String value = request.getParameter(name);
        return value;
     //   if(value == null || value.equalsIgnoreCase("")) value = def_val;
     //   return Utility.get_char_oper().code2_2_code1(value);
    }
    
    /**
     * ��ȫȡ��ҳ�����ϣ���֤����ȡ��null
     * */
    public static String[] getParameters(HttpServletRequest request,String name)
    {
    	String[] values = request.getParameterValues(name);
    	for(int i = 0; values != null && i < values.length; i++)
    	{
    		values[i] = Utility.get_char_oper().code2_2_code1(values[i]);
    	}
    	return values;
    }
    
    /**
    * д�Ի�cookie
    * @param cookieName                    cookie�����
    * @param cookieValue                   cookie��ֵ
    * @param time                          cookie������ʱ�䣬����Ϊ��λ��0���ɾ�� cookie -1���cookie ������������ر�
    * @param path                          cookie��Ч·��
    * @param domain                        cookie��Ч��
    * @param response                       
    */
   public static void setSessionCookie(String cookieName, String cookieValue,int time,String path,String domain, HttpServletResponse response) {
	   if(cookieValue != null && !cookieValue.equals(""))
       {
		   Cookie mycookie = new Cookie(cookieName, cookieValue);
	       mycookie.setDomain(domain);
	       mycookie.setPath(path);
	       mycookie.setMaxAge(time);
	       response.addCookie(mycookie);
       }
   }

   /**
    * ɾ��cookie
    * @param cookieName                    cookie�����
    * @param response   
    * */
   public static void removeCookie(String cookieName,HttpServletResponse response){
         Cookie mycookie = new Cookie(cookieName,"");
         mycookie.setPath("/");
         mycookie.setMaxAge(0);
         response.addCookie(mycookie);
   }

   /**
    * д�־�cookie
    * @param cookieName                    cookie�����
    * @param cookieValue                     cookie��ֵ
    * @param response
    */
   public static void setPersistentCookie(String cookieName, String cookieValue, HttpServletResponse response) {
           Cookie mycookie = new Cookie(cookieName, cookieValue);
           mycookie.setPath("/");
           //mycookie.setMaxAge(cookieValue != null ? 24 * 60 * 60 : 0);
           mycookie.setMaxAge(cookieValue != null ? -1 : 0);
           response.addCookie(mycookie);
   }

   /**
    * ���ָ��cookie��ֵ
    * @param cookieName                            cookie�����
    * @param request
    * @return String                                          cookie��ֵ
    */
   public static String getSafeCookieValue(HttpServletRequest request,String cookieName,String def_val) {
           Cookie[] cookies = request.getCookies();
           Cookie mycookie = null;
           String cookieValue = null;
           for(int i = 0; cookies != null && i < cookies.length; i++) {
                   mycookie = cookies[i];
                   if(mycookie.getName().equalsIgnoreCase(cookieName)) {
                           cookieValue = mycookie.getValue();
                           break;
                   }
           }
           if(cookieValue == null || cookieValue.equals(""))
           {
        	   cookieValue = def_val;
           }
           return cookieValue;
   }
   private static String appurl = "";
   public static String getAppUrl()
   {
	 return appurl;   
   }
   public static void setAppUrl(String url)
   {
	   appurl = url;
   }
   
   private static String apppath = "";
   public static String getAppPath()
   {
	   return apppath;
   }
   
   public static void setAppPath(String path)
   {
	   apppath = path;
   }
}
