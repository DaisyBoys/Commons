package webtools.HTTPJSON.tools;

import com.google.gson.Gson;
import webtools.common.database.JdbcAgent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ListMgr {
    private final FunTools pFunTools = new FunTools();
    private final JdbcAgent jAgent = new JdbcAgent();
    private final Gson pGson = new Gson();

    public String toJson(final Object src) {
        return pGson.toJson(src);
    }

    public String utf8Str(final String str) {
        if (str == null) return "";
        String strRtn = "";
        try {
            strRtn = URLEncoder.encode(str, "utf-8");
            ;
        } catch (Exception e) {

        }


        return strRtn;

    }

    public String utf8CoverStr(String str) {
        if (str == null) return "";
        String strRtn = "";
        try {
            strRtn = URLDecoder.decode(str, "utf-8");
        } catch (Exception e) {

        }


        return strRtn;

    }

    public double convertDouble(String str) {
        try {
            if (str == null || "".equals(str)) {
                str = "0";
            }
            double dd = (double) Math.round(Double.parseDouble(str) * 100) / 100;
            return getDoubbleFrm2(dd);
        } catch (Exception e) {

        }
        return 0;

    }

    /**
     * @param nLen 是10的倍数，如1 ，10， 100，1000，小数点后多少位用10代表1位，100代表2位等
     */
    public double convertDouble(String str, long nLen) {
        try {
            if (str == null || "".equals(str)) {
                str = "0";
            }


            double dd = (double) Math.round(Double.parseDouble(str) * nLen) / nLen;
            return dd;
        } catch (Exception e) {

        }
        return 0;

    }

    public double convertDoubleSrc(String str) {
        try {
            if (str == null || "".equals(str)) {
                str = "0";
            }
            double dd = Double.parseDouble(str);
            return dd;
        } catch (Exception e) {

        }
        return 0;

    }

    public double getDoubbleFrm2(double dd) {
        dd = (double) Math.round(dd * 100) / 100;
        return dd;

    }

    public BigDecimal getDoubble2(double dd) {
        dd = (double) Math.round(dd * 100) / 100;
        BigDecimal b1 = new BigDecimal(Double.toString(dd));
        return b1;

    }

    public long convertLong(final String str) {
        try {
            long l = (long) (convertDouble(str));
            return l;
        } catch (Exception e) {

        }
        return 0;
    }

    public int convertInt(String str) {
        try {
            if (str == null || "".equals(str)) {
                str = "0";
            }
            return Integer.parseInt(str);
        } catch (Exception e) {

        }
        return 0;
    }

    //======================
    public String format_tk_no(final String tk_no, final String tc_no_len) {
        String strRtn = tk_no;
        try {
            int n_len = this.convertInt(tc_no_len);
            int n_tk_no_len = tk_no.length();
            if (n_tk_no_len < n_len) {
                //处理补位
                int nL = n_len - n_tk_no_len;
                String str = "";
                for (int i = 0; i < nL; i++) {
                    str += "0";
                }
                strRtn = str + tk_no;
            }

        } catch (Exception e) {

        }
        return strRtn;
    }

    public long formatTimeLong(final String hour, final String minute) {
        try {
            String str_hour = this.format_tk_no(hour, "2");
            String str_minute = this.format_tk_no(minute, "2");
            String strTime = "1" + str_hour + str_minute;
            return this.convertLong(strTime);
        } catch (Exception e) {

        }
        return 0;
    }

    public String formatLong2Time(final String str_time) {
        try {

            String str_hour = str_time.substring(1, 3);
            String str_minute = str_time.substring(3);

            return str_hour + ":" + str_minute;
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 得到时间
     */
    public String getTime_DateAndMM(final String str_time) {
        try {
            if (str_time == null) return "";
            if (str_time.length() > 16) return str_time.substring(0, 16);
        } catch (Exception e) {

        }
        return "";
    }

    public boolean getCheckedFun(final String[] funs, final String funID) {
        if (funs == null) return false;
        for (int i = 0; i < funs.length; i++) {
            if (funs[i].equalsIgnoreCase(funID)) {
                return true;
            }
        }
        return false;

    }

    /**
     * 字符传转MAP
     */
    public Map<String, String> convertStrings2Map(final String[] strs) {
        Map<String, String> mapRtn = new ConcurrentHashMap<String, String>();
        try {
            if (strs == null) return mapRtn;
            for (int i = 0; i < strs.length; i++) {
                String str = strs[i].trim();
                if ("".equals(str)) continue;
                mapRtn.put(strs[i], "1");

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return mapRtn;

    }

    //======全局处理===========
    public String getTermOR(final String fieldName, final String[] strList) {
        String tmpTerm = "";
        /*
		try {
			if (strList==null)return "";
			for (int i=0;i<strList.length;i++){
				if ("".equals(strList[i]))continue;
				tmpTerm+=" or "+fieldName+"='"+strList[i]+"'";
			}
		} catch (Exception e) {
			
		}
		*/
        try {
            if (strList == null) return "";
            for (int i = 0; i < strList.length; i++) {
                if ("".equals(strList[i])) continue;
                tmpTerm += " , " + "'" + strList[i] + "'";
            }
            tmpTerm = getTermOR(tmpTerm);
            if (!"".equals(tmpTerm)) {
                tmpTerm = " " + fieldName + " in(" + tmpTerm + ")";
            }
        } catch (Exception e) {

        }
        return tmpTerm;
    }

    private String getTermOR(String tmpTerm) {
		/*
		try {
			if (!"".equals(tmpTerm)){
				int n1=tmpTerm.indexOf("or");
				if (n1>0){
					tmpTerm=tmpTerm.substring(n1+2);
				}
				
				
			}else{
				
			}
		} catch (Exception e) {
			
		}
		*/
        try {
            if (!"".equals(tmpTerm)) {
                int n1 = tmpTerm.indexOf(",");
                if (n1 > 0) {
                    tmpTerm = tmpTerm.substring(n1 + 1);
                }

            } else {

            }

        } catch (Exception e) {

        }
        return tmpTerm;

    }

    /*
     * 动态数组处理
     * */
    public String getTermORByList(final String fieldName, final List<String> strList) {
        String tmpTerm = getTermOR(fieldName, (String[]) strList.toArray(new String[strList.size()]));

        return tmpTerm;
    }

    public String getTermNotInByList(final String fieldName, final List<String> strList) {
        String tmpTerm = getTermNotOR(fieldName, (String[]) strList.toArray(new String[strList.size()]));

        return tmpTerm;
    }

    public String getTermNotOR(final String fieldName, final String[] strList) {
        String tmpTerm = "";
		/*
		try {
			if (strList==null)return "";
			for (int i=0;i<strList.length;i++){
				if ("".equals(strList[i]))continue;
				tmpTerm+=" or "+fieldName+"='"+strList[i]+"'";
			}
		} catch (Exception e) {
			
		}
		*/
        try {
            if (strList == null) return "";
            for (int i = 0; i < strList.length; i++) {
                if ("".equals(strList[i])) continue;
                tmpTerm += " , " + "'" + strList[i] + "'";
            }
            tmpTerm = getTermOR(tmpTerm);
            if (!"".equals(tmpTerm)) {
                tmpTerm = " " + fieldName + " not in(" + tmpTerm + ")";
            }
        } catch (Exception e) {

        }
        return tmpTerm;
    }

    public String html_dui_gou(final String str) {
        if ("1".equals(str)) return "<font color=red>√</font>";
        return "×";

    }

    //是否显示
    public String html_yes_or_no(final String is_yes) {
        try {
            if ("1".equals(is_yes)) return "<font color=red>是</font>";
            if ("0".equals(is_yes)) return "<font color=green>否</font>";

        } catch (Exception e) {

        }
        return "";
    }

    public String html_no_or_yes(final String is_yes) {
        try {
            if ("1".equals(is_yes)) return "<font color=green>是</font>";
            if ("0".equals(is_yes)) return "<font color=red>否</font>";

        } catch (Exception e) {

        }
        return "";
    }

    public String txt_no_or_yes(final String is_yes) {
        try {
            if ("1".equals(is_yes)) return "是";
            if ("0".equals(is_yes)) return "否";

        } catch (Exception e) {

        }
        return "";
    }


    public String getTrimStr(final String str) {
        if (str == null) return "";
        String strRtn = "";
        try {
            strRtn = str.trim();
        } catch (Exception e) {

        }
        return strRtn;
    }

    /**
     * 字符串转换unicode
     */
    public String string2Unicode(final String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * unicode 转字符串
     */
    public String unicode2String(final String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    /*
     * 英文字母
     * **/
    public List<String> getListA_Z_0_9() {
        List<String> listRtn = new ArrayList<String>();
        listRtn.add("A");
        listRtn.add("B");
        listRtn.add("C");
        listRtn.add("D");
        listRtn.add("E");
        listRtn.add("F");
        listRtn.add("G");
        listRtn.add("H");
        listRtn.add("I");
        listRtn.add("J");
        listRtn.add("K");
        listRtn.add("L");
        listRtn.add("M");
        listRtn.add("N");
        listRtn.add("O");
        listRtn.add("P");
        listRtn.add("Q");
        listRtn.add("R");
        listRtn.add("S");
        listRtn.add("T");
        listRtn.add("U");
        listRtn.add("V");
        listRtn.add("W");
        listRtn.add("X");
        listRtn.add("Y");
        listRtn.add("Z");

        listRtn.add("0");
        listRtn.add("1");
        listRtn.add("2");
        listRtn.add("3");
        listRtn.add("4");
        listRtn.add("5");
        listRtn.add("6");
        listRtn.add("7");
        listRtn.add("8");
        listRtn.add("9");

        return listRtn;
    }

    /**
     * 时间处理
     */
    public String getTimeBy_DD_HH_MM(final String day, final String hour, final String minute) {
        String str = "";
        try {
            int n_hour = this.convertInt(hour);
            if (n_hour < 0 || n_hour > 24) {
                return "";
            }
            int n_minute = this.convertInt(minute);
            if (n_minute < 0 || n_minute > 60) {
                return "";
            }
            str = day + " " + this.format_tk_no(hour, "2") + ":" + this.format_tk_no(minute, "2") + ":00";
        } catch (Exception e) {
            // TODO: handle exception
        }
        return str;
    }

    /**
     * 性别
     */
    public String html_sex_type(final String sex_type) {
        try {
            if ("1".equals(sex_type)) return "男";
            if ("2".equals(sex_type)) return "女";
        } catch (Exception e) {

        }
        return "";
    }






    public String getLinuxPath(String srcFileName) {
        try {
            srcFileName = srcFileName.replaceAll("\\\\", "/");
            while (srcFileName.indexOf("//") >= 0) {
                srcFileName = srcFileName.replaceAll("//", "/");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return srcFileName;
    }

    public String getHttpPath(String httpAddr) {
        try {
            httpAddr = getLinuxPath(httpAddr);
            if (httpAddr.indexOf(":/") >= 0) {
                httpAddr = httpAddr.replaceAll(":/", "://");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return httpAddr;
    }

    public String gzip(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return primStr;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new sun.misc.BASE64Encoder().encode(out.toByteArray());
    }

    /**
     * <p>Description:使用gzip进行解压缩</p>
     *
     * @param compressedStr
     * @return
     */
    public String gunzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[8192];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return decompressed;
    }


}
