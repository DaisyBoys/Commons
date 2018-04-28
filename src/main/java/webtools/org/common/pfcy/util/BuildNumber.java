package webtools.org.common.pfcy.util;

/**
 * ��������
 * 
 * public long build(long mode)��ɹ̶����ȵ����
 * ���� ����10 �����10��99��2λ���
 * 
 * @author jack.dong
 */
public class BuildNumber {

	/**
	 * ����봫�����λ��һ�µ����
	 * @param mode λ�����
	 * @return �봫�����λ��һ��long�������
	 */
	public long buildResult(long mode){
		
		long result=(long)(Math.random()*mode);
		
		if (result<mode) {
			long b=mode/result;
			result*=b;
		}
		
		return result;
		
	}
	
	/**
	 * ���һ����ݵ�����
	 * @param totalNum ��ɽ����
	 * @param mode ���λ��ģ�����
	 * @return ������ɼ�
	 */
	public long[] buildResults(int totalNum,long mode){
		
		long[] result=new long[totalNum];
		
		//��ʼ��������
		for (int i = 0; i < result.length;) {
			
			//��������ظ����
			long r=buildResult(mode);
			boolean isHas=false;//�Ƿ�����ظ�
			for (int j = 0; j < i; j++) {
				if (r==result[j]) {
					isHas=true;//����ҵ������ظ�����
					System.err.println("�����ظ�");
				}
			}
			
			if (!isHas) {//����ظ�����
				result[i]=r;
				i++;
			}
			try {
				Thread.sleep(1L);//ͣ��һ����
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	
}
