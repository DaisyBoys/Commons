package webtools.org.common.pfcy.util;

public class BuildNumber {


	public long buildResult(long mode){
		
		long result=(long)(Math.random()*mode);
		
		if (result<mode) {
			long b=mode/result;
			result*=b;
		}
		
		return result;
		
	}

	public long[] buildResults(int totalNum,long mode){
		
		long[] result=new long[totalNum];
		

		for (int i = 0; i < result.length;) {

			long r=buildResult(mode);
			boolean isHas=false;
			for (int j = 0; j < i; j++) {
				if (r==result[j]) {
					isHas=true;
				}
			}
			
			if (!isHas) {
				result[i]=r;
				i++;
			}
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	
}
