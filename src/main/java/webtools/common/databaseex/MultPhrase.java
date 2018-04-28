package webtools.common.databaseex;

import javax.servlet.http.HttpServletRequest;

public class MultPhrase extends BasePhrase {

	
	@Override
	public String get_phrase(webtools.common.URL.RequestConnection con) 
	{
		int begin = value.indexOf('(');
		int end   = value.lastIndexOf(')');
		String core = value.substring(begin + 1,end);
		
		String strret = "";
		String tmp[] = new String[2];
		tmp[0] = core;
		String conj = " (";
		while(true)
		{
			BasePhrase phrase = BasePhrase.parse_phrase(tmp);
			if(phrase == null)
			{
				break;
			}
			String str = phrase.get_phrase(con);
			if(!str.equals("")){
				strret += conj + str;
				conj = " " + tmp[1] + " ";
			}
		}
		if(!strret.equals("")) strret += ")";
		return strret;
	}
	String value;
	public void put_value(String value) 
	{
		this.value = value;
	}
}
