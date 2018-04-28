package webtools.common.URL;

import java.util.*;
public interface RequestConnection {
	public String[] getParameters(String name);
	public String getSafeParameter(String name,String def_val);
	public void sendbackDirect(String url,String ectrlname,String emsg)throws Exception;
	public Enumeration getParameterNames();
}
