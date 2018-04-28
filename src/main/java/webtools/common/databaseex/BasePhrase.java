package webtools.common.databaseex;

import javax.servlet.http.HttpServletRequest;
import webtools.common.*;

public abstract class BasePhrase 
{
	public static BasePhrase parse_phrase(String[] Query)
	{
		StrOper oper = new StrOper();
		String left = oper.cut_word(Query);
		BasePhrase phrase = null;
		if(left != null && !left.equals(""))
		{
			if(left.charAt(0) == '(')
			{
				phrase = new MultPhrase();
				phrase.put_value(left);
			}
			else
			{
				String operator = oper.cut_word(Query);
				String right = oper.cut_word(Query);
				if(operator.equalsIgnoreCase("not"))
				{
					operator += " " + oper.cut_word(Query);
					phrase = new InPhrase();
				}
				else if(operator.equalsIgnoreCase("in")){
					phrase = new InPhrase();
				}
				else{
					phrase = new CommonPhrase();
				}
				phrase.setLeft(left);
				phrase.setOper(operator);
				phrase.put_value(right);
			}
			String conj = oper.cut_word(Query);
			Query[1] = conj;
		}
		return phrase;
	}
	
	public abstract String  get_phrase(webtools.common.URL.RequestConnection con);
	public abstract void    put_value(String val);
	
	protected String left = null;
	protected String oper = null;

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}
	
}
