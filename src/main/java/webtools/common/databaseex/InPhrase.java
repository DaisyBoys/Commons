package webtools.common.databaseex;

import javax.servlet.http.HttpServletRequest;

public class InPhrase extends BasePhrase {

	@Override
	public String get_phrase(webtools.common.URL.RequestConnection con) {
		// TODO Auto-generated method stub
		int idx = find_var(0);
		if(idx != -1)
		{
			String[] name = {null};
			boolean[] ischar = {false}; 
			get_var(idx,name,ischar);
			if(name[0] != null && !name[0].equals(""))
			{
				//String[] vals = con.getRequest().getParameterValues(name[0]);
				String[] vals = con.getParameters(name[0]);
				//if(vals != null && vals.length > 0)
				if(vals.length > 0)
				{
					String v = "";
					for(int i = 0; i < vals.length; i++)
					{
						String flag = ischar[0]?"'":"";
						v += flag + vals[i] + flag;
						if(i < vals.length -1) v += ",";
					}
					return left + " " + oper + " (" + v + ")"; 
				}
			}
			return new String("");
		}
		else
		{
			return left + " " + oper + " " + value;
		}
	}
	
	private String value;	
	public void put_value(String val) {
		value = val;
	}
	
	private int find_var(int index)
	{
		int pt = value.indexOf('$',index);
		if(pt >0 && value.charAt(pt - 1) == '\'')
		{
			return pt - 1;
		}
		return pt;
	}
	
	private int get_var(int index,String[] var_name,boolean[] ischar)
	{
		ischar[0] = false;
		StringBuffer var = new StringBuffer("");
		boolean start = false;
		int i = index;
		for(; i < value.length(); i++)
		{
			char ch = value.charAt(i);
			if(!ischar[0] && (ch == ' ' || ch == ')' || ch == ','))
			{
				i--;
				break;
			}
			else if(ischar[0] && ch == '\'')
			{
				break;
			}
			else if(ch == '\'' && !ischar[0])
			{
				ischar[0] = true;
			}
			else if(ch == '$' && !start)
			{
				start = true;
			}
			else if(ch == '$' && start)
			{
				start = false;
			}
			else if(start)
			{
				var.append(ch);
			}
		}
		var_name[0] = var.toString();
		return i - index;
	}
}
