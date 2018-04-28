package webtools.org.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Random;

import webtools.org.common.pfcy.util.Constant;

public abstract class test {

	/**
	 * @param args
	 */
	public static void main() {
		
		Map map =new ConcurrentHashMap();
		
		for (int i=0;i<100000;i++){
			map.put("a."+i, "sss"+i);
		}
		
		Long lS=System.currentTimeMillis();
		System.out.println(lS);
		System.out.println(map.get("a."+99999));
		Long lE=System.currentTimeMillis();
		System.out.println(lE);
		System.out.println(lE-lS);
		
		String s="table.id\tint\t4\tnot null\r\n";
		
		String []ss=s.split("\t");
		
		for (int j=0;j<ss.length;j++){
			System.out.println(ss[j]);
		}
		
		System.out.println(a(Constant.vdChinese));
		
		String s1=System.currentTimeMillis()+"";
		
		System.out.println(System.currentTimeMillis());
		System.out.println(s1.length());
	}

	public static String a(Constant c){
		return c.value();
	}
	
	public static void main(String[] args) {
		Random d=new Random();
		int a=d.nextInt(2);
		System.out.println(a);
	}
}
