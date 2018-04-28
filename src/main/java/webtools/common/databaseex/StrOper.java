package webtools.common.databaseex;

public class StrOper {
	public String cut_word(String[] sentence)
	{
		sentence[0] = sentence[0].trim();
		int len = sentence[0].length();
		StringBuffer word = new StringBuffer("");
		int i = 0;
		boolean v = false;
		boolean o = false;
		int count = 0;
		String pattern = new String(",;=<>!");
		for(; i < len; i++)
		{
			char ch = sentence[0].charAt(i);
			if( o && (pattern.indexOf(ch) == -1))
			{
				break;
			}
			else if(ch == '\'')
			{
				word.append(ch);
				v = !v;
				if(!v && count == 0) {i++;break;}
			}
			else if(!v && (ch == '(' || ch == ')'))
			{
				word.append(ch);
				if(ch == '('){
					count++;
				}
				else{
					if(--count == 0){ i++; break;}
				}
			}
			else if(count > 0 || v)
			{
				word.append(ch);
			}
			else if(ch == ' ')
			{
				break;
			}
			else if(i == 0)
			{
				word.append(ch);
				o = pattern.indexOf(ch) != -1;
			}
			else if(!(o ^ (pattern.indexOf(ch) != -1))){
				word.append(ch);
			}
			else
			{
				break;
			}
		}
		sentence[0] = sentence[0].substring(i);
		return word.toString();
	}
}
