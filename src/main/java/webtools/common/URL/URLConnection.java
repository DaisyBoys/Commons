package webtools.common.URL;

import javax.servlet.http.*;

import webtools.common.Utility;

import java.io.UnsupportedEncodingException;
import java.util.*;


public class URLConnection  implements RequestConnection {
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	
	public URLConnection()
	{
		
	}
	
	public URLConnection(HttpServletRequest request,HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void sendbackDirect(String url,String ectrlname,String emsg)throws Exception
	{
		String backurl = url;
		String joint = "?";
		if(request != null && response != null)
		{
			Enumeration para_names = request.getParameterNames();
		   	while(para_names.hasMoreElements())
		   	{
		   		String para_name = (String)para_names.nextElement(); 		
		   		String[] para_vals = getParameters(para_name);
		   		for(int i = 0; para_vals != null && i < para_vals.length; i++)
		   		{	
		   			backurl += joint + para_name + "=" + Escape.escape(para_vals[i]);
		   			joint = "&";
		   		}
		   	}
		   	//
		   	backurl += joint + "err_name=" + ((ectrlname == null)? "":ectrlname) + 
		   			"&err_msg=" + Escape.escape(((emsg == null)? "":emsg));
		   	response.sendRedirect(backurl);
		}
	}
	
    /**
    * ��ȫȡ��ҳ�����֤����ȡ��null
    * */
    public String getSafeParameter(String name,String def_val){
    	String value = null;
    	if(request != null)
    	{
	        value = request.getParameter(name);
    	}
        if(value == null || value.equalsIgnoreCase("")) value = def_val;
        return Utility.get_char_oper().code2_2_code1(value);
    }
    
    /**
     * ��ȫȡ��ҳ�����ϣ���֤����ȡ��null
     * */
    public String[] getParameters(String name)
    {
    	int count = 0;
    	String[] values = null;
    	if(request != null)
    	{
    		values = request.getParameterValues(name);
    	}
    	if(values != null)
    	{
    		count = values.length;
    	}
    	String[] vallist = new String[count];
    	for(int i = 0; i < count; i++)
    	{
    		vallist[i] = Utility.get_char_oper().code2_2_code1(values[i]);
    		
    	}
    	return vallist;
    }
    
    /**
     * 
     * */
    public Enumeration getParameterNames()
    {
    	return request.getParameterNames();
    }
    
    /**
     * д�Ի�cookie
     * @param cookieName                    cookie�����
     * @param cookieValue                   cookie��ֵ
     * @param time                          cookie������ʱ�䣬����Ϊ��λ��0���ɾ�� cookie -1���cookie ������������ر�
     * @param path                          cookie��Ч·��
     * @param domain                        cookie��Ч��                      
     */
    public void setSessionCookie(String cookieName, String cookieValue,int time,String path,String domain) {
    	if(cookieValue != null && !cookieValue.equals("") && response != null)
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
     * */
    public void removeCookie(String cookieName){
    	if(response != null)
    	{
          Cookie mycookie = new Cookie(cookieName,"");
          mycookie.setPath("/");
          mycookie.setMaxAge(0);
          response.addCookie(mycookie);
    	}
    }

    /**
     * ɾ��cookie
     * @param cookieName                    cookie����� 
     * @param domain                        cookie����
     * */
    public void removeCookie(String cookieName,String domain)
    {
    	if(response != null)
    	{
          Cookie mycookie = new Cookie(cookieName,"");
          mycookie.setPath("/");
          mycookie.setDomain(domain);
          mycookie.setMaxAge(0);
          response.addCookie(mycookie);
    	}
    }
    
    /**
     * д�־�cookie
     * @param cookieName                    cookie�����
     * @param cookieValue                     cookie��ֵ
     */
    public void setPersistentCookie(String cookieName, String cookieValue) {
    	if(response != null)
    	{
            Cookie mycookie = new Cookie(cookieName, cookieValue);
            mycookie.setPath("/");
            //mycookie.setMaxAge(cookieValue != null ? 24 * 60 * 60 : 0);
            mycookie.setMaxAge(cookieValue != null ? -1 : 0);
            response.addCookie(mycookie);
    	}
    }
    
    /**
     * д�־�cookie
     * @param cookieName                    cookie�����
     * @param cookieValue                     cookie��ֵ
     * @param domain                        cookie��
     */
    public void setPersistentCookie(String cookieName, String cookieValue,String domain) {
    	if(response != null)
    	{
            Cookie mycookie = new Cookie(cookieName, cookieValue);
            mycookie.setPath("/");
            //mycookie.setMaxAge(cookieValue != null ? 24 * 60 * 60 : 0);
            mycookie.setMaxAge(cookieValue != null ? -1 : 0);
            mycookie.setDomain(domain);
            response.addCookie(mycookie);
    	}
    }

    /**
     * ���ָ��cookie��ֵ
     * @param cookieName                            cookie�����
     * @return String                                          cookie��ֵ
     */
    public String getSafeCookieValue(String cookieName,String def_val) {
    	String cookieValue = def_val;
    	if(request != null)
    	{
            Cookie[] cookies = request.getCookies();
            Cookie mycookie = null;
            for(int i = 0; cookies != null && i < cookies.length; i++) {
                    mycookie = cookies[i];
                    if(mycookie.getName().equalsIgnoreCase(cookieName)) {
                            cookieValue = mycookie.getValue();
                            break;
                    }
            }
    	}
        return cookieValue;
    }
    
    public String getBaseHref()
    {
    	String path = request.getContextPath();
    	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
    	return basePath;
    }
    
    public String getCurrentPath()
    {
    	String srvpath = request.getServletPath();
    	String[] list = srvpath.split("/");
    	String retpath = "";
    	for(int i = 1; i < list.length - 1; i++)
    	{
    		retpath += list[i] + "/";
    	}
    	return retpath;
    }
}
