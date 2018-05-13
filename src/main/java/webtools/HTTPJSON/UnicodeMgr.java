package webtools.HTTPJSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class UnicodeMgr {


    /**
     * unicode 转字符串
     */
    public String unicode2String(String unicode) {

        return unicode2String2(unicode);
        /*
	    StringBuffer string = new StringBuffer();
	    String[] jsonKey = unicode.split("\"");
	    if(jsonKey!=null){
	    	for(int i=0;i<jsonKey.length;i++){
	    		String temp=jsonKey[i];
	    		
	    		
	    		if(temp.indexOf("\\\\u")>=0){
	    			String ss=unicode2String2(temp);
	    			string.append(ss+"\"");
	    				
	    			
	    			
	    		}else{
	    			string.append(temp+"\"");
	    		}
	    	}
	    }
	    */


        // return string.toString();
    }

    private final int CONCURRENCIES = 6; // 上传Part的并发线程数。

    public String unicode2String2(String unicode) {

        StringBuffer string = new StringBuffer();
        try {
            String[] hex = unicode.split("\\\\u");
            if (hex.length >= 10000) {

                ExecutorService pool = Executors.newFixedThreadPool(CONCURRENCIES);
                int nLen = hex.length;
                int nGroup = nLen / 5000;
                if (nGroup % 5000 != 0) nGroup++;
                List<String> listStr = new ArrayList<String>();
                for (int i = 0; i < nGroup; i++) {
                    listStr.add("");
                }
                for (int i = 0; i < nGroup; i++) {
                    int iStart = i * 5000;
                    int iEnd = iStart + 5000;
                    if (iEnd > nLen) iEnd = nLen;
                    pool.execute(new myThread(iStart, iEnd, hex, listStr, i));
                }

                pool.shutdown();
                while (!pool.isTerminated()) {
                    pool.awaitTermination(5, TimeUnit.SECONDS);
                }
                String strRtn = "";
                for (int i = 0; i < listStr.size(); i++) {
                    String str = listStr.get(i);
                    strRtn += str;
                }
                return strRtn;
            } else {
                for (int i = 1; i < hex.length; i++) {

                    // 转换出每一个代码点
                    int data = Integer.parseInt(hex[i], 16);

                    // 追加成string
                    string.append((char) data);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        return string.toString();
    }


    private class myThread implements Runnable {
        private int iStart;
        private int iEnd;
        private int iPos;
        private String[] hex;
        private List<String> listStr;

        public String strRtn = "";

        myThread(int iStart, int iEnd, String[] hex, List<String> listStr, int iPos) {
            this.iStart = iStart;
            this.iEnd = iEnd;
            this.hex = hex;
            this.listStr = listStr;
            this.iPos = iPos;

        }

        @Override
        public void run() {

            StringBuffer string = new StringBuffer();
            try {

                for (int i = iStart; i < iEnd; i++) {
                    if ("".equals(hex[i])) continue;
                    // 转换出每一个代码点
                    int data = Integer.parseInt(hex[i], 16);

                    // 追加成string
                    string.append((char) data);
                }
                strRtn = string.toString();
                listStr.set(iPos, strRtn);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }
}
