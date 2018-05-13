package webtools.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utility {
    private static CharExchange page_char_exchange = null;


    public static void set_app_charcode(String appchar, String formchar) {
        page_char_exchange = new CharExchange();
        page_char_exchange.setCode1(appchar);
        page_char_exchange.setCode2(formchar);
    }


    public static CharExchange get_char_oper() {
        return page_char_exchange;
    }


    public static String getCurrentDateTime(String format) {
        SimpleDateFormat sFmt = new SimpleDateFormat(format);
        return sFmt.format(new Date());
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sFmt = new SimpleDateFormat(format);
        return sFmt.format(date);
    }

    private static int serial = 0;

    /**
     * ����������ʱ������������ݿ�����ؼ���
     */
    public static String getPrimaryKey() {
        if (serial == 999) serial = 0;
        return getCurrentDateTime("yyyyMMddHHmmssSSS") + String.valueOf(serial++);
    }

    /**
     * ��ȫȡ��ҳ�����֤����ȡ��null
     */
    public static String getSafeParameter(HttpServletRequest request, String name, String def_val) {
        String value = request.getParameter(name);
        return value;
        //   if(value == null || value.equalsIgnoreCase("")) value = def_val;
        //   return Utility.get_char_oper().code2_2_code1(value);
    }

    /**
     * ��ȫȡ��ҳ�����ϣ���֤����ȡ��null
     */
    public static String[] getParameters(HttpServletRequest request, String name) {
        String[] values = request.getParameterValues(name);
        for (int i = 0; values != null && i < values.length; i++) {
            values[i] = Utility.get_char_oper().code2_2_code1(values[i]);
        }
        return values;
    }

    /**
     * д�Ի�cookie
     *
     * @param cookieName  cookie�����
     * @param cookieValue cookie��ֵ
     * @param time        cookie������ʱ�䣬����Ϊ��λ��0���ɾ�� cookie -1���cookie ������������ر�
     * @param path        cookie��Ч·��
     * @param domain      cookie��Ч��
     * @param response
     */
    public static void setSessionCookie(String cookieName, String cookieValue, int time, String path, String domain, HttpServletResponse response) {
        if (cookieValue != null && !cookieValue.equals("")) {
            Cookie mycookie = new Cookie(cookieName, cookieValue);
            mycookie.setDomain(domain);
            mycookie.setPath(path);
            mycookie.setMaxAge(time);
            response.addCookie(mycookie);
        }
    }

    /**
     * ɾ��cookie
     *
     * @param cookieName cookie�����
     * @param response
     */
    public static void removeCookie(String cookieName, HttpServletResponse response) {
        Cookie mycookie = new Cookie(cookieName, "");
        mycookie.setPath("/");
        mycookie.setMaxAge(0);
        response.addCookie(mycookie);
    }

    /**
     * д�־�cookie
     *
     * @param cookieName  cookie�����
     * @param cookieValue cookie��ֵ
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
     *
     * @param cookieName cookie�����
     * @param request
     * @return String                                          cookie��ֵ
     */
    public static String getSafeCookieValue(HttpServletRequest request, String cookieName, String def_val) {
        Cookie[] cookies = request.getCookies();
        Cookie mycookie = null;
        String cookieValue = null;
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            mycookie = cookies[i];
            if (mycookie.getName().equalsIgnoreCase(cookieName)) {
                cookieValue = mycookie.getValue();
                break;
            }
        }
        if (cookieValue == null || cookieValue.equals("")) {
            cookieValue = def_val;
        }
        return cookieValue;
    }

    private static String appurl = "";

    public static String getAppUrl() {
        return appurl;
    }

    public static void setAppUrl(String url) {
        appurl = url;
    }

    private static String apppath = "";

    public static String getAppPath() {
        return apppath;
    }

    public static void setAppPath(String path) {
        apppath = path;
    }
}
