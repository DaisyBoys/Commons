package webtools.common;

import java.io.UnsupportedEncodingException;

public class CharExchange {
	String code1;
	String code2;
	
	
	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public String code2_2_code1(String source)
	{
		try {
			if((code1 != null && !code1.equals("")) && (code2 != null && !code2.equals("")) && !code1.equalsIgnoreCase(code2))
				return (new String(source.getBytes(code2), code1));
		} catch (UnsupportedEncodingException e) {
			
	    }
		return source;
	}
	
	public String code1_2_code2(String source)
	{
		try {
			if((code1 != null && !code1.equals("")) && (code2 != null && !code2.equals(""))&& !code1.equalsIgnoreCase(code2))
				return (new String(source.getBytes(code1), code2));
		} catch (UnsupportedEncodingException e) {
			
	    }
		return source;
	}
	
}
