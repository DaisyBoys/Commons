package webtools.HTTPJSON;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpCallback {
    //返回的结果集合
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private int nErr = 0;

    public int getnErr() {
        return nErr;
    }

    public void setnErr(int nErr) {
        this.nErr = nErr;
    }

    /*
     * 请求串为json格式。公用信息如下：
	*softVer: 软件版本号，由机器自行定义，为后续排除问题提供依据。
	*systemVer：系统版本号。
	*machId: 机器id，由富雷设备出厂定义。
	*paramList: 请求参数集，使用标准JSON格式
     * */
    //Convert16 pConvert16 =null;
    //	softVer 软件版本号，systemVer 系统版本号，机器id machId
    public String httpSet(final String url, String jsonStr, String urlToken, String businessNo) {
        this.setnErr(0);

        //连接地址
        String urlTerm = jsonStr;
        try {
            urlTerm = "jsonstr=" + this.string2Unicode(jsonStr);
            urlTerm += "&urlToken=" + urlToken;
            urlTerm += "&businessNo=" + businessNo;
            //urlTerm+="&act="+act;//业务接口标识
            //urlTerm+="&paramOpt="+OperationType;
            //urlTerm+="&machId="+ URLEncoder.encode(machId,"utf-8");
            //jsonStr=this.string2Unicode(jsonStr);
            //String base64Json=Base64.encode(jsonStr.getBytes());

            //urlTerm+="&paramList="+base64Json;

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        HttpURLConnection conn = null;
        //OutputStream outStream=null;
        BufferedReader in = null;
        String responseData = "";
        byte[] sendData = urlTerm.getBytes();
        try {
            final URL u = new URL(url);

            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            // 如果通过post提交数据，必须设置允许对外输出数据
            conn.setDoOutput(true);

            conn.setDoInput(true);
            conn.setUseCaches(false);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(sendData.length));
            OutputStream outStream = conn.getOutputStream();
            outStream.write(sendData);
            outStream.flush();
            outStream.close();
            //outStream=null;
            if (conn.getResponseCode() == 200) {
                // 获得服务器响应的数据
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                // 数据
                String retData = null;

                try {
                    while ((retData = in.readLine()) != null) {
                        responseData += retData;
                    }
                } catch (Exception e) {
                    this.setnErr(-1002);
                }

                in.close();
                in = null;

            } else {
                //连接错误
                nErr = -1001;
                this.setnErr(-1001);
            }
        } catch (Exception e) {
            // TODO: handle exception
            //地址错误
            nErr = -1000;
            this.setnErr(-1000);
        }
        try {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
            //if (outStream!=null){
            //	outStream.close();
            //	outStream=null;
            //}
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            this.setnErr(-1000);
        }

        return responseData;

    }

    /**
     * 字符串转换unicode
     */
    public String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }
}
