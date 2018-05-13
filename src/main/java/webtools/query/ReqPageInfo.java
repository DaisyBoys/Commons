package webtools.query;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import webtools.req.edit.ReqEditForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public final class ReqPageInfo {

    @Override
    protected void finalize() {
        try {
            this.pReqEditForm = null;

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private ReqEditForm pReqEditForm = new ReqEditForm();

    public ReqEditForm getPReqEditForm() {
        return pReqEditForm;
    }

    public void setPReqEditForm(ReqEditForm reqEditForm) {
        pReqEditForm = reqEditForm;
    }

    public void doRespose(HttpServletRequest request, HttpServletResponse response, String rtnName) throws ServletException, IOException {
        doRequst(request, response, rtnName);
    }

    public String lastPageUrlTempData = "";

    public String getLastPageUrlTempData() {
        return lastPageUrlTempData;
    }

    public void setLastPageUrlTempData(String lastPageUrlTempData) {
        this.lastPageUrlTempData = lastPageUrlTempData;
    }

    //页面缓存rtnList="页面返回时的参数列表"
    public void doRequst(HttpServletRequest request, HttpServletResponse response, String rtnName) throws ServletException, IOException {
        this.lastPageUrlTempData = "";
        //页面缓存
        //	String pagedata=request.getParameter("pfcypagetempdata");
        try {
            String reqName = rtnName;
            String tempS = request.getParameter(reqName);
            if (tempS == null) {
                tempS = "";
            }
            if (tempS.trim().equals("")) {
                this.lastPageUrlTempData += reqName + "=" + getUrlInfo(request);
            } else {
                String strTmp = request.getParameter(reqName);
                if (strTmp != null) {
                    strTmp = URLEncoder.encode(strTmp, "utf-8");

                    this.lastPageUrlTempData += reqName + "=" + strTmp;
                    //System.out.println(strTmp);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


    }

    private String getUrlInfo(HttpServletRequest request) throws ServletException, IOException {

        String pagedata = "";
        Map ps = request.getParameterMap();
        if (ps != null) {
            try {
                String con = "";
                Iterator iterator = ps.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String key = (String) entry.getKey();

                    String[] values = request.getParameterValues(key);
                    if (values == null || values.length == 0) {
                        continue;
                    } else {

                        for (int i = 0; i < values.length; i++) {
                            String value = values[i];
                            if (value == null) value = "";
                            //value=URLDecoder.decode(value,"utf-8");

                            value = URLEncoder.encode(value, "utf-8");

                            pagedata += con + key + "=" + value;
                        }
                    }

                    if (con.equals("")) {
                        con = "&";
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }

            pagedata = Base64.encode(pagedata.getBytes());
        }
        return pagedata;
    }

    public String doDecodeRtn1(HttpServletRequest request, HttpServletResponse response, String rtnName) {

        String rtn = "";

        String reqName = rtnName;
        String tempS = request.getParameter(reqName);
        if (tempS == null) {
            return "";
        }
        try {
            if (!tempS.trim().equals("")) {
                if (tempS.indexOf(" ") >= 0) {
                    tempS = tempS.replaceAll(" ", "+");
                }
                String str2 = new String(Base64.decode(tempS.trim()));
                rtn += str2;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        //String rtn1=request.getParameter("rtn1");
        //if (rtn1==null) return "";
        //String str2 = new String(Base64.decode(rtn1));

        return rtn;
    }

    public String doDecodeRtn1(String rtnName) {

        String rtn = "";

        String reqName = rtnName;

        try {

            String str2 = new String(Base64.decode(rtnName.trim()));
            rtn += str2;

        } catch (Exception e) {
            //e.printStackTrace();
        }
        //String rtn1=request.getParameter("rtn1");
        //if (rtn1==null) return "";
        //String str2 = new String(Base64.decode(rtn1));

        return rtn;
    }

    public String doDecodesendRedirect(HttpServletRequest request, HttpServletResponse response, String rtnName) {

        String temp = doDecodeRtn1(request, response, rtnName);
        if (temp == null) temp = "";
        //	try {
        //	temp=URLEncoder.encode(temp,"utf-8");
        //	} catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        //		e.printStackTrace();
        //	}
        //	temp=temp.replaceAll("%26","&");
        //	temp=temp.replaceAll("%3D","=");

        return temp;
    }

}
