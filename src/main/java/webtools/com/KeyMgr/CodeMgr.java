package webtools.com.KeyMgr;

public class CodeMgr {

	/**
	 * 生成与传入数据位数一致的数据
	 * @param mode 位数数据
	 * @return 
	 */
	public long buildResult(int length){
		
		long result=0;
		
		long d=System.currentTimeMillis();
		String str=d+"";
		int n1=str.length();
		int x=n1/4;
		int xm=n1%4;
	    String []s=new String [4];
		for (int i=0;i<x;i++){
			s[i]=str.substring(i*4,i*4+3);
		}
		if (xm>0){
			s[x]=str.substring((x-1)*4);
		}
		if (x<3){
			for (int j=x;j<4;j++){
				s[j]=str.substring(j*4,j*4+3);
			}
		}
		
		for (int i=0;i<4;i++){
			String str1=s[i];
			int n=Integer.parseInt(str1);
			int r =((int)(Math.random()*(10000-1)))+1;
			int xxx=n-r;
			if (xxx<0)xxx=-xxx;
			if (i==0){
				if (xxx<999){
					xxx=1000+xxx;
				}
			}
			 s[i]=String.format("%04d",xxx);
		}
		String str1=s[0]+s[1]+s[2]+s[3];
		result=Long.parseLong(str1);
		str1=result+"";
		if (str1.length()>length)str1=str1.substring(0,length);
		if (str1.length()<length){
			String st="";
			for (int i=str1.length();i<length;i++){
				st+="1";
			}
			str1+=st;
		}
		result=Long.parseLong(str1);
		return result;
		
	}
}
