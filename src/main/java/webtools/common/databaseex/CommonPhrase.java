package webtools.common.databaseex;

import javax.servlet.http.HttpServletRequest;

public class CommonPhrase extends BasePhrase
{
	private String right;
	protected boolean var = false;
	protected String  var_name = new String("");
	
	public String getRight() {
		return right;
	}

	public void put_value(String right) {
		this.right = right;
		int len = right.length();
		int i = right.indexOf('$');
		if(i != -1)
		{
			i++;
			StringBuffer strvar = new StringBuffer("");
			for(;i < len; i++){
				char ch = right.charAt(i);
				if(ch != '$')
				{
					strvar.append(ch);
					continue;
				}
				break;
			}
			if(!strvar.equals(""))
			{
				var = true;
				var_name = strvar.toString();
			}
		}
	}

	public String  get_phrase(webtools.common.URL.RequestConnection con)
	{
		String ret = "";
		String val = con.getSafeParameter(var_name, "");
		if(!var){
			ret = left + " " + oper + " " + right;
		}
		else if(var && !val.equals("") ){
			ret = left + " " + oper + " " + right.replace("$"+ var_name + "$", val);
		}
		return ret;
	}
}
