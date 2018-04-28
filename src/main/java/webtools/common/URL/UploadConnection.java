package webtools.common.URL;

import java.util.Enumeration;
import javax.servlet.http.HttpServletResponse;
import webtools.common.Utility;
import webtools.common.upload.*;

public class UploadConnection implements RequestConnection {
	//private Request request = null;
	private Upload upload = null;
	private HttpServletResponse response = null;
	
	public UploadConnection()
	{
		
	}
	
	public UploadConnection(Upload upload,HttpServletResponse response)
	{
		this.upload = upload;
		this.response = response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void sendbackDirect(String url,String ectrlname,String emsg)throws Exception
	{
		String backurl = url;
		String joint = "?";
		if(upload != null && response != null)
		{
			
			Enumeration para_names = getParameterNames();
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
		   	//�����������ƺʹ�����Ϣ����Ϊ�գ����뷵�ش�����Ϣ����
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
    	if(upload != null)
    	{
	        value = upload.getParameter(name);
    	}
        if(value == null || value.equalsIgnoreCase("")) value = def_val;
        //System.out.println(value);
        //String nval =  Utility.get_char_oper().code2_2_code1(value);
        //System.out.println(nval);
        return value;
    }
    
    /**
     * ��ȫȡ��ҳ�����ϣ���֤����ȡ��null
     * */
    public String[] getParameters(String name)
    {
    	String[] values = null;
    	if(upload != null)
    	{
    		values = upload.getParameterValues(name);
    	}
    	if(values == null)
    	{
    		values = new String[0];
    	}
    	for(int i = 0; i < values.length; i++)
    	{
    		values[i] = Utility.get_char_oper().code2_2_code1(values[i]);
    	}
    	return values;
    }
    
    /**
     * 
     * */
    public Enumeration getParameterNames()
    {
    	return upload.getParameterNames();
    }
}
