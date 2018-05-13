package webtools.req.edit;

import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;
import webtools.org.common.pfcy.database.dbvalidate.dbfile.DBValidateCentral;
import webtools.org.common.pfcy.database.dbvalidate.validate.ErrorMessage;
import webtools.org.common.pfcy.util.ValidateUtilImpl;
import webtools.reg.page.ReqSearch;
import webtools.reg.page.ReqSearchResult;
import webtools.reg.page.bean.search.QuerySqlBean;
import webtools.req.edit.bean.FieldValues;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import java.net.URLEncoder;


/**
 * 页面请求数据增加
 **/
public final class ReqEditForm {
    @Override
    @SuppressWarnings("unchecked")
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            if (mapQueryTerm != null) {
                Iterator itSearch = mapQueryTerm.entrySet().iterator();
                while (itSearch.hasNext()) {
                    @SuppressWarnings("unused")
                    Object me = itSearch.next();
                    me = null;
                }
                mapQueryTerm.clear();
                mapQueryTerm = null;

            }
            if (mapDBInfo != null) {
                Iterator itSearch = mapDBInfo.entrySet().iterator();
                while (itSearch.hasNext()) {
                    @SuppressWarnings("unused")
                    Object me = itSearch.next();
                    me = null;
                }
                mapDBInfo.clear();
                mapDBInfo = null;

            }
            if (mapErr != null) {
                Iterator itSearch = mapErr.entrySet().iterator();
                while (itSearch.hasNext()) {
                    @SuppressWarnings("unused")
                    Object me = itSearch.next();
                    me = null;
                }
                mapErr.clear();
                mapErr = null;

            }
            if (mapQuery != null) {
                Iterator itSearch = mapQuery.entrySet().iterator();
                while (itSearch.hasNext()) {
                    @SuppressWarnings("unused")
                    Object me = itSearch.next();
                    me = null;
                }
                mapQuery.clear();
                mapQuery = null;

            }
            if (lstInsErrDel != null) {
                for (int i = 0; i < lstInsErrDel.size(); i++) {
                    Object obj = lstInsErrDel.get(i);
                    obj = null;

                }
                lstInsErrDel.clear();
                lstInsErrDel = null;

            }
            if (lstInsertScript != null) {
                for (int i = 0; i < lstInsertScript.size(); i++) {
                    Object obj = lstInsertScript.get(i);
                    obj = null;

                }
                lstInsertScript.clear();
                lstInsertScript = null;

            }
            if (pMyValidateInfo != null) {
                pMyValidateInfo = null;
            }


        } catch (Exception e) {
            // TODO: handle exception
        }


    }

    @SuppressWarnings("unchecked")
    public Map mapQueryTerm = null;
    @SuppressWarnings("unchecked")
    public Map mapDBInfo = null;
    @SuppressWarnings("unchecked")
    private List lstInsertScript = new ArrayList();
    @SuppressWarnings("unchecked")
    private List lstInsErrDel = new ArrayList();
    @SuppressWarnings("unchecked")
    private Map mapErr = null;
    private ErrorMessage pErrorMessage = null;
    private MyValidateInfo pMyValidateInfo = null;
    public Map<String, String> mapQuery = null;
    private EditInfo pEditInfo = new EditInfo();

    public EditInfo getPEditInfo() {
        return pEditInfo;
    }

    public void setPEditInfo(EditInfo editInfo) {
        pEditInfo = editInfo;
    }

    @SuppressWarnings("unchecked")
    public Map getMapQuery() {
        return mapQuery;
    }

    @SuppressWarnings("unchecked")
    public void setMapQuery(ConcurrentHashMap mapQuery) {
        this.mapQuery = mapQuery;
    }

    @SuppressWarnings("unchecked")
    public Map getMapErr() {
        return mapErr;
    }

    @SuppressWarnings("unchecked")
    public void setMapErr(Map mapErr) {
        this.mapErr = mapErr;
    }

    @SuppressWarnings("unchecked")
    public List getLstInsErrDel() {
        return lstInsErrDel;
    }

    @SuppressWarnings("unchecked")
    public void setLstInsErrDel(List lstInsErrDel) {
        this.lstInsErrDel = lstInsErrDel;
    }

    @SuppressWarnings("unchecked")
    public List getLstInsertScript() {
        return lstInsertScript;
    }

    @SuppressWarnings("unchecked")
    public void setLstInsertScript(List lstInsertScript) {
        this.lstInsertScript = lstInsertScript;
    }

    @SuppressWarnings("unchecked")
    public Map getMapDBInfo() {
        return mapDBInfo;
    }

    @SuppressWarnings("unchecked")
    public void setMapDBInfo(ConcurrentHashMap mapDBInfo) {
        this.mapDBInfo = mapDBInfo;
    }

    @SuppressWarnings("unchecked")
    public Map getMapQueryTerm() {
        return mapQueryTerm;
    }

    @SuppressWarnings("unchecked")
    public void setMapQueryTerm(ConcurrentHashMap mapQueryTerm) {
        this.mapQueryTerm = mapQueryTerm;
    }

    private String lastPageUrlTempData = "";//前一页面Url参数存储数据

    public String getLastPageUrlTempData() {
        return this.lastPageUrlTempData;
    }

    @SuppressWarnings("unchecked")
    public void doRespose(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //页面缓存
        String pagedata = request.getParameter("pfcypagetempdata");
        if (pagedata == null) {
            pagedata = "";
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
                                value = URLEncoder.encode(value, "utf-8");
                                value = URLDecoder.decode(value, "utf-8");
                                //value=URLEncoder.encode(value,request.getCharacterEncoding());

                                pagedata += con + key + "=" + value;
                            }
                        }

                        if (con.equals("")) {
                            con = "&";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.lastPageUrlTempData = pagedata;
            }
        } else {
            this.lastPageUrlTempData = pagedata;
        }

        doRequst(request, response);
    }

    @SuppressWarnings("unchecked")
    public void doRequst(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置数据提交的参数
        if (mapQueryTerm != null) {
            Iterator itSearch = mapQueryTerm.entrySet().iterator();
            while (itSearch.hasNext()) {
                Map.Entry me = (Map.Entry) itSearch.next();
                String mapKey = (String) me.getKey();
                String[] sVal = request.getParameterValues(mapKey);
                if (sVal != null) {
                    mapQueryTerm.put(mapKey, sVal);
                }
            }
            pEditInfo.setMapQueryTerm(mapQueryTerm);
        }
    }

    /**
     * 初始化查询条件参数
     */
    @SuppressWarnings("unchecked")
    public void setQueryTermName(String name) {
        try {
            if (name == null) {
                return;
            }
            if (mapQueryTerm == null) mapQueryTerm = new ConcurrentHashMap();//初始化MAP
            Object obj = mapQueryTerm.get(name);
            if (obj == null) {
                mapQueryTerm.put(name, "");
            }


        } catch (Exception e) {
            //System.out.println(e.toString());

        }
    }

    public void setQueryTermNameSList(String chkReqName, String nameHead, HttpServletRequest request) {
        try {
            String[] sList = request.getParameterValues(chkReqName);
            if (sList != null) {
                for (int i = 0; i < sList.length; i++) {
                    String val = sList[i];
                    this.setQueryTermName(nameHead + val);
                }
            }

        } catch (Exception e) {
            //System.out.println(e.toString());

        }
    }

    //根据设定的名称得到相关的数据
    public String[] getPageParamValues(String name) {
        if (mapQueryTerm == null) {
            return null;
        }
        String[] s = null;
        try {
            s = (String[]) mapQueryTerm.get(name);
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
        return s;
    }

    //得到单条字符串
    public String getPageParamValueString(String name) {
        if (mapQueryTerm == null) {
            return "";
        }
        String[] s = null;
        try {
            s = (String[]) mapQueryTerm.get(name);
            if (s != null) {
                if (s.length > 0) {
                    String str = s[0];
                    //return str.trim();//原有去掉空格处理
                    return str;
                }
            }
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
        return "";
    }

    //设置插入的表名
    @SuppressWarnings("unchecked")
    public void setInsertTable(String tablename) {
        if (mapDBInfo == null) mapDBInfo = new ConcurrentHashMap();
        Object obj = mapDBInfo.get(tablename);
        if (obj == null) {
            ConcurrentHashMap mapFiled = new ConcurrentHashMap();//字段使用
            mapDBInfo.put(tablename, mapFiled);
        }

    }

    //插入语句脚本
    @SuppressWarnings("unchecked")
    public void setInsertInfo(final String tablename, final String insField, final String[] values, final String selValidate) {

        setInsertTable(tablename);

        ConcurrentHashMap mapFiled = (ConcurrentHashMap) mapDBInfo.get(tablename);//得到当前表的字段信息
        FieldValues pFieldValues = new FieldValues();

        pFieldValues.setSelValidate(selValidate);
        pFieldValues.setValuses(values);

        mapFiled.put(insField, pFieldValues);
    }

    //得到插入的脚本数组
    @SuppressWarnings({"unchecked", "static-access"})
    public List getInsertScript() {
        String frm = "insert into %s (%s) values(%s)";
        String delFrm = "delete from %s where ";//删除处理插入失败用
        List lstRtn = new ArrayList();
        DBValidateCentral dCentral = new DBValidateCentral();//数据库格式验证
        if (mapDBInfo != null) {
            Iterator itSearch = mapDBInfo.entrySet().iterator();
            while (itSearch.hasNext()) {
                Map.Entry me = (Map.Entry) itSearch.next();
                String mapKey = (String) me.getKey();
                ConcurrentHashMap mapFiled = (ConcurrentHashMap) me.getValue();
                String delSql = "";
                delSql = delSql.format(delFrm, mapKey);
                if (mapFiled != null) {//有字段
                    String strField = "";
                    Iterator it = mapFiled.entrySet().iterator();
                    List listCol = new ArrayList();
                    int nValCnt = 0;
                    List delListFrm = new ArrayList();
                    List ValidateField = new ArrayList();
                    List myListValiadte = new ArrayList();
                    while (it.hasNext()) {
                        Map.Entry meField = (Map.Entry) it.next();
                        String strCurrField = (String) meField.getKey();
                        ValidateField.add(strCurrField);
                        strField += strCurrField + ",";
                        FieldValues pFieldValues = (FieldValues) meField.getValue();
                        String[] sV = (String[]) pFieldValues.getValuses();//得到数据置

                        listCol.add(sV);
                        myListValiadte.add((String) pFieldValues.getSelValidate());//自定义判断表达士
                        if (sV != null) {
                            nValCnt = sV.length;
                        }
                        delListFrm.add(strCurrField + "=%s");

                    }
                    if (!strField.equals("")) {
                        int nPos = strField.lastIndexOf(",");
                        if (nPos >= 0) strField = strField.substring(0, nPos);
                        String strValues = "";
                        for (int n = 0; n < nValCnt; n++) {
                            strValues = "";
                            for (int i = 0; i < listCol.size(); i++) {
                                String[] temp = (String[]) listCol.get(i);

                                String strVal = null;//得到当前列的第N行数据
                                if (temp != null) {
                                    if (n < temp.length)
                                        strVal = temp[n];
                                }

                                //数据库类型格式交验
                                String valiadateFiled = mapKey + "." + (String) ValidateField.get(i);
                                String myValiadate = (String) myListValiadte.get(i);
                                int nErr = -1;
                                if (myValiadate != null) {//需要交验自定义格式
                                    if (!myValiadate.trim().equals("")) {
                                        nErr = dCentral.columnValidateMatcher(valiadateFiled, strVal, myValiadate);
                                        if (nErr > -1) {
                                            if (mapErr == null) mapErr = new ConcurrentHashMap();
                                            if (pMyValidateInfo == null) pMyValidateInfo = new MyValidateInfo();
                                            if (mapErr.get(valiadateFiled) == null) {
                                                String errMsg = pMyValidateInfo.getValidate(myValiadate);
                                                mapErr.put(valiadateFiled, errMsg);
                                            }

                                        }
                                    }
                                }
                                if (nErr == -1) {
                                    nErr = dCentral.columnValidateData(valiadateFiled, strVal);
                                    if (strVal != null) {
                                        if (nErr > -1) {
                                            if (mapErr == null) mapErr = new ConcurrentHashMap();
                                            if (pErrorMessage == null) pErrorMessage = new ErrorMessage();
                                            if (mapErr.get(valiadateFiled) == null) {
                                                String errMsg = pErrorMessage.getErrorMessage(nErr);
                                                mapErr.put(valiadateFiled, errMsg);
                                            }

                                        }
                                    }
                                }

                                if (strVal == null) {
                                    strValues += strVal;
                                } else {
                                    strVal = strVal.replaceAll("'", "''");
                                    strVal = "'" + strVal + "'";
                                    strValues += strVal;
                                }

                                String delTermFrm = (String) delListFrm.get(i);
                                String strDel = "";
                                strDel = String.format(delTermFrm, strVal);
                                delListFrm.set(i, strDel);
                                if (i < listCol.size() - 1) strValues += ",";
                            }//for (int i=0;i<listCol.size();i++){
                            String delScript = delSql;
                            for (int k = 0; k < delListFrm.size(); k++) {
                                delScript += (String) delListFrm.get(k);
                                if (k < delListFrm.size() - 1) {
                                    delScript += " and ";
                                }
                            }
                            lstInsErrDel.add(delScript);//增加失败时的脚本
                            String sql = "";
                            sql = String.format(frm, mapKey, strField, strValues);
                            lstRtn.add(sql);
                        }//for (int n=0;n<nValCnt;n++)

                    }//if (!strField.isEmpty()){

                }//if (mapFiled!=null)

            }//while   (itSearch.hasNext())

        }//if (mapDBInfo!=null)
        this.setLstInsertScript(lstRtn);//设置增加使用的脚本语句
        return lstRtn;
    }

    @SuppressWarnings("unchecked")
    public void doInsert() {
        try {

            getInsertScript();//得到插入脚本
            if (mapErr != null) {//有错误不能进行插入
                Iterator itSearch = mapErr.entrySet().iterator();
                while (itSearch.hasNext()) {
                    Map.Entry me = (Map.Entry) itSearch.next();
                    @SuppressWarnings("unused")
                    String mapKey = (String) me.getKey();
                    @SuppressWarnings("unused")
                    String strErr = (String) me.getValue();
                    //System.out.println(mapKey+strErr);
                }

                pEditInfo.setMapErr(mapErr);
                return;
            }

            JdbcAgent jAgent = new JdbcAgent();
            boolean bErrQuery = false;
            if (mapQuery != null) {
                Iterator itSearch = mapQuery.entrySet().iterator();
                while (itSearch.hasNext()) {
                    Map.Entry me = (Map.Entry) itSearch.next();
                    String mapKey = (String) me.getKey();
                    String sql = (String) me.getValue();
                    DBResult rs = jAgent.query(sql);
                    if (rs != null)
                        if (rs.getRowCount() > 0) {
                            String strCnt = rs.getString(0, 0);
                            if (strCnt == null) strCnt = "0";
                            try {
                                int n = Integer.parseInt(strCnt);
                                if (n > 0) {
                                    bErrQuery = true;
                                    if (mapErr == null) mapErr = new ConcurrentHashMap();
                                    if (pMyValidateInfo == null) pMyValidateInfo = new MyValidateInfo();
                                    mapErr.put(mapKey, "存在相同的记录");
                                    break;
                                }
                            } catch (Exception e) {
                            }

                        }

                }
            }
            pEditInfo.setMapErr(mapErr);
            if (bErrQuery) return;//验证错误
            List lstIns = this.getInsertScript();
            List lstDel = this.getLstInsErrDel();
            int nPos = 0;
            boolean bErr = false;
            for (int i = 0; i < lstIns.size(); i++) {
                nPos = i;
                String strIns = (String) lstIns.get(i);
                //System.out.println(strIns);
                int n = jAgent.update(strIns);
                if (n < 0) {//数据库插入失败
                    bErr = true;
                    break;
                }
                if (n == 1) {
                    continue;
                }
                if (n == 0) {
                    bErr = true;
                    break;
                }
            }
            if (bErr) {
                for (int j = 0; j < nPos; j++) {
                    String strDel = (String) lstDel.get(j);
                    jAgent.update(strDel);
                    //System.out.println(strDel);
                }
            }

        } catch (Exception e) {
            //System.out.println(e.toString());

        }
    }

    public void runInsert() {

        doInsert();
    }

    @SuppressWarnings("unchecked")
    public void SetMyValidateInfo(String name, String value, String exp) {
        ValidateUtilImpl dCentral = new ValidateUtilImpl();//数据库格式验证
        if (exp == null) return;
        if (exp.equals("")) {
            if (value.trim().equals("")) {
                if (mapErr == null) mapErr = new ConcurrentHashMap();
                if (pMyValidateInfo == null) pMyValidateInfo = new MyValidateInfo();
                if (mapErr.get(name) == null) {
                    @SuppressWarnings("unused")
                    String errMsg = pMyValidateInfo.getValidate(exp);
                    mapErr.put(name, "必添数据");
                }
                return;
            }
        }
        boolean nOk = dCentral.matcher(exp, value);
        if (!nOk) {
            if (mapErr == null) mapErr = new ConcurrentHashMap();
            if (pMyValidateInfo == null) pMyValidateInfo = new MyValidateInfo();
            if (mapErr.get(name) == null) {
                String errMsg = pMyValidateInfo.getValidate(exp);
                mapErr.put(name, errMsg);
            }

        }

    }

    @SuppressWarnings("unchecked")
    public String getErrMessage(String key) {
        Map map = this.getMapErr();
        if (map != null) {
            String ss = (String) map.get(key);
            if (ss == null) {
                ss = "";
            }
            return ss;
        }
        return "";
    }

    //设置查询条件
    @SuppressWarnings("unchecked")
    public void setQuery(String name, String sql) {
        if (mapQuery == null) {
            mapQuery = new ConcurrentHashMap();
        }
        mapQuery.put(name, sql);


    }

    //=============新增部分结束====================
//===================修改改部分处理=============
    @SuppressWarnings("unchecked")
    public Map mapDBModifyInfo = null;//修改使用的信息
    @SuppressWarnings("unchecked")
    private List lstModifyScript = null;

    @SuppressWarnings("unchecked")
    public List getLstModifyScript() {
        return lstModifyScript;
    }

    @SuppressWarnings("unchecked")
    public void setLstModifyScript(List lstModifyScript) {
        this.lstModifyScript = lstModifyScript;
    }

    @SuppressWarnings("unchecked")
    public Map getMapDBModifyInfo() {
        return mapDBModifyInfo;
    }

    @SuppressWarnings("unchecked")
    public void setMapDBModifyInfo(ConcurrentHashMap mapDBModifyInfo) {
        this.mapDBModifyInfo = mapDBModifyInfo;
    }

    //设置修改的表名
    @SuppressWarnings("unchecked")
    public void setModiTable(String tablename) {
        if (mapDBModifyInfo == null) mapDBModifyInfo = new ConcurrentHashMap();
        Object obj = mapDBModifyInfo.get(tablename);
        if (obj == null) {
            ConcurrentHashMap mapFiled = new ConcurrentHashMap();//字段使用
            mapDBModifyInfo.put(tablename, mapFiled);
        }

    }

    @SuppressWarnings("unchecked")
    public void setModiTableWhere(String tablename, String myWhere) {
        //if (mapDBModifyInfo==null){
        setModiTable(tablename);
        //}
        //将查询条件放入固定的位置
        ConcurrentHashMap mapFiled = (ConcurrentHashMap) mapDBModifyInfo.get(tablename);//得到当前表的字段信息
        mapFiled.put("myWhere", myWhere);//设置当前修改使用的查询条件
    }

    //修改语句脚本
    @SuppressWarnings("unchecked")
    public void setModiInfo(String tablename, String insField, String[] values, String selValidate) {

        setModiTable(tablename);

        ConcurrentHashMap mapFiled = (ConcurrentHashMap) mapDBModifyInfo.get(tablename);//得到当前表的字段信息
        FieldValues pFieldValues = new FieldValues();

        pFieldValues.setSelValidate(selValidate);
        pFieldValues.setValuses(values);

        mapFiled.put(insField, pFieldValues);
    }

    //得到插入的脚本数组
    @SuppressWarnings({"unchecked", "static-access"})
    public List getModiScript() {
        List lstRtn = new ArrayList();
        String modiFrm = "update %s set ";
        DBValidateCentral dCentral = new DBValidateCentral();//数据库格式验证
        try {
            if (mapDBModifyInfo != null) {
                Iterator itSearch = mapDBModifyInfo.entrySet().iterator();
                while (itSearch.hasNext()) {
                    Map.Entry me = (Map.Entry) itSearch.next();
                    String mapKey = (String) me.getKey();
                    ConcurrentHashMap mapFiled = (ConcurrentHashMap) me.getValue();
                    String strModiSql = "";
                    strModiSql = String.format(modiFrm, mapKey);
                    //处理跟新使用的查询条件
                    String strWhere = (String) mapFiled.get("myWhere");

                    if (mapFiled != null) {//有字段
                        Iterator it = mapFiled.entrySet().iterator();
                        List listCol = new ArrayList();
                        int nValCnt = 0;
                        List ValidateField = new ArrayList();
                        List myListValiadte = new ArrayList();
                        List listColName = new ArrayList();

                        while (it.hasNext()) {
                            Map.Entry meField = (Map.Entry) it.next();
                            String strCurrField = (String) meField.getKey();
                            if (strCurrField.equals("myWhere")) {//查询条件字段
                                continue;
                            }
                            ValidateField.add(strCurrField);
                            //得到但前字段的信息
                            FieldValues pFieldValues = (FieldValues) meField.getValue();
                            String[] sV = (String[]) pFieldValues.getValuses();//得到数据置
                            if (sV != null) {
                                nValCnt = sV.length;//记录长度
                            }
                            listCol.add(sV);
                            listColName.add(strCurrField);
                            myListValiadte.add((String) pFieldValues.getSelValidate());//自定义判断表达士
                        }
                        //增加修改使用的语句
                        for (int i = 0; i < nValCnt; i++) {
                            String strValues = "";
                            for (int n = 0; n < listCol.size(); n++) {//按列增加
                                String strFieldName = (String) listColName.get(n);
                                String[] temp = (String[]) listCol.get(n);

                                String strVal = null;//得到当前列的第N行数据
                                if (temp != null) {
                                    if (i < temp.length)
                                        strVal = temp[i];
                                }
                                String valiadateFiled = mapKey + "." + (String) ValidateField.get(n);
                                String myValiadate = (String) myListValiadte.get(n);
                                int nErr = -1;
                                if (myValiadate != null) {//需要交验自定义格式
                                    if (!myValiadate.trim().equals("")) {
                                        nErr = dCentral.columnValidateMatcher(valiadateFiled, strVal, myValiadate);
                                        if (nErr > -1) {
                                            if (mapErr == null) mapErr = new ConcurrentHashMap();
                                            if (pMyValidateInfo == null) pMyValidateInfo = new MyValidateInfo();
                                            if (mapErr.get(valiadateFiled) == null) {
                                                String errMsg = pMyValidateInfo.getValidate(myValiadate);
                                                mapErr.put(valiadateFiled, errMsg);
                                            }

                                        }
                                    }
                                }
                                if (nErr == -1 && strVal != null) {
                                    nErr = dCentral.columnValidateData(valiadateFiled, strVal);
                                    if (nErr > -1) {
                                        if (mapErr == null) mapErr = new ConcurrentHashMap();
                                        if (pErrorMessage == null) pErrorMessage = new ErrorMessage();
                                        if (mapErr.get(valiadateFiled) == null) {
                                            String errMsg = pErrorMessage.getErrorMessage(nErr);
                                            mapErr.put(valiadateFiled, errMsg);
                                        }
                                    }
                                }

                                if (strVal == null) {
                                    strValues += strFieldName + "=" + strVal;
                                } else {
                                    strVal = strVal.replace("'", "''");
                                    strVal = "'" + strVal + "'";
                                    strValues += strFieldName + "=" + strVal;
                                }
                                if (n < listCol.size() - 1) strValues += ",";

                            }//for (int nCol=0;nCol<listCol.size();nCol++){
                            String sql = strModiSql + strValues;
                            if (strWhere != null) {
                                sql += " where " + strWhere;
                            }
                            lstRtn.add(sql);
                        }

                    }//if (mapFiled!=null)

                }//while   (itSearch.hasNext())

            }//if (mapDBModifyInfo!=null){


        } catch (Exception e) {
            //System.out.println(e.toString());

        }
        this.setLstModifyScript(lstRtn);
        return lstRtn;
    }

    @SuppressWarnings("unchecked")
    public void doModify() {
        try {

            getModiScript();//得到插入脚本
            if (mapErr != null) {//有错误不能进行插入
                Iterator itSearch = mapErr.entrySet().iterator();
                while (itSearch.hasNext()) {
                    Map.Entry me = (Map.Entry) itSearch.next();
                    @SuppressWarnings("unused")
                    String mapKey = (String) me.getKey();
                    //String strErr=(String)me.getValue();
                    //	System.out.println(strErr+"="+mapKey);
                }
                pEditInfo.setMapErr(mapErr);
                return;
            }

            JdbcAgent jAgent = new JdbcAgent();
            boolean bErrQuery = false;
            if (mapQuery != null) {
                Iterator itSearch = mapQuery.entrySet().iterator();
                while (itSearch.hasNext()) {
                    Map.Entry me = (Map.Entry) itSearch.next();
                    String mapKey = (String) me.getKey();
                    String sql = (String) me.getValue();
                    DBResult rs = jAgent.query(sql);
                    if (rs.getRowCount() > 0) {
                        String strCnt = rs.getString(0, 0);
                        if (strCnt == null) strCnt = "0";
                        try {
                            int n = Integer.parseInt(strCnt);
                            if (n > 0) {
                                bErrQuery = true;
                                if (mapErr == null) mapErr = new ConcurrentHashMap();
                                if (pMyValidateInfo == null) pMyValidateInfo = new MyValidateInfo();
                                mapErr.put(mapKey, "存在相同的记录");
                            }
                        } catch (Exception e) {
                        }
                        break;
                    }

                }
            }
            pEditInfo.setMapErr(mapErr);
            if (bErrQuery) return;//验证错误
            List lstModi = this.getLstModifyScript();

            //int nPos=0;
            //boolean bErr=false;
            for (int i = 0; i < lstModi.size(); i++) {
                //nPos=i;
                String strModi = (String) lstModi.get(i);

                int n = jAgent.update(strModi);
                if (n < 0) {//数据库修改失败
                    //bErr=true;
                    break;
                }
                if (n == 1) {
                    continue;
                }
                if (n == 0) {
                    //bErr=true;
                    break;
                }
            }

        } catch (Exception e) {
            //System.out.println(e.toString());

        }
    }

    public void runModify() {

        doModify();
    }

    //初始化信息列表
    @SuppressWarnings("unchecked")
    public void setInitMoidfyByTable(String sel, String from, String where) {
        ReqSearch pReqSearch = new ReqSearch(); //得到查询使用的类
        ReqSearchResult pReqSearchResult = new ReqSearchResult();//返回结果
        // pReqSearchResult.setNPageSize(1);
        QuerySqlBean pQuerySQLBean = pReqSearch.getQuerySQLBean();
        pQuerySQLBean.setSelectField(sel);//select 中的字段
        pQuerySQLBean.setQueryFrom(from);//from 的数据表名
        pQuerySQLBean.setQueryWhere(where);//查询的where 条件语句
        pReqSearchResult = pReqSearch.runReq();//执行
        //修改结果集合
        List rsList = pReqSearchResult.getRsList();
        try {
            if (rsList.size() > 0) {
                ConcurrentHashMap map = (ConcurrentHashMap) rsList.get(0);
                if (mapQueryTerm != null) {
                    Iterator itSearch = map.entrySet().iterator();
                    while (itSearch.hasNext()) {
                        Map.Entry me = (Map.Entry) itSearch.next();
                        String mapKey = (String) me.getKey();
                        String[] sVal = new String[rsList.size()];
                        for (int nRow = 0; nRow < rsList.size(); nRow++) {
                            sVal[nRow] = (String) me.getValue();

                        }
                        mapQueryTerm.put(mapKey, sVal);
                    }
                    pEditInfo.setMapQueryTerm(mapQueryTerm);
                }
            }


        } catch (Exception e) {
            //System.out.println(e.toString());

        }

    }

    public void setMyMap(String mapKey, String[] sVal) {
        if (this.mapQueryTerm == null) mapQueryTerm = new ConcurrentHashMap();
        mapQueryTerm.put(mapKey, sVal);
        pEditInfo.setMapQueryTerm(mapQueryTerm);
    }

    @SuppressWarnings("unchecked")
    public void setInitMoidfyByTable(String sel, String from, String where, int pagesize) {
        ReqSearch pReqSearch = new ReqSearch(); //得到查询使用的类
        ReqSearchResult pReqSearchResult = new ReqSearchResult();//返回结果
        pReqSearchResult.setNPageSize(pagesize);
        QuerySqlBean pQuerySQLBean = pReqSearch.getQuerySQLBean();
        pQuerySQLBean.setSelectField(sel);//select 中的字段
        pQuerySQLBean.setQueryFrom(from);//from 的数据表名
        pQuerySQLBean.setQueryWhere(where);//查询的where 条件语句
        pReqSearchResult = pReqSearch.runReq();//执行
        //修改结果集合
        List rsList = pReqSearchResult.getRsList();
        try {
            if (rsList.size() > 0) {
                ConcurrentHashMap map = (ConcurrentHashMap) rsList.get(0);
                if (mapQueryTerm != null) {
                    Iterator itSearch = map.entrySet().iterator();
                    while (itSearch.hasNext()) {
                        Map.Entry me = (Map.Entry) itSearch.next();
                        String mapKey = (String) me.getKey();
                        String[] sVal = new String[rsList.size()];
                        for (int nRow = 0; nRow < rsList.size(); nRow++) {
                            sVal[nRow] = (String) me.getValue();

                        }
                        mapQueryTerm.put(mapKey, sVal);
                    }
                    pEditInfo.setMapQueryTerm(mapQueryTerm);
                }
            }


        } catch (Exception e) {
            //System.out.println(e.toString());

        }

    }

    //===========修改完成==============================
    public void validateIsEmpty(String strName) {
        String s[] = getPageParamValues(strName);
        if (s != null) {
            if (s.length > 0) {
                SetMyValidateInfo(strName, s[0], "");//必添校验
            }
        } else {//jack.dong update
            SetMyValidateInfo(strName, "", "");//必添校验
        }
    }

    @SuppressWarnings("unchecked")
    public void setJsonMoidfyByMap(Map map) {
        try {
            if (map == null) return;
            if (mapQueryTerm == null) mapQueryTerm = new ConcurrentHashMap();
            if (mapQueryTerm != null) {
                Iterator itSearch = map.entrySet().iterator();
                while (itSearch.hasNext()) {
                    Map.Entry me = (Map.Entry) itSearch.next();
                    String mapKey = (String) me.getKey();
                    Object obj = me.getValue();
                    String strVal = "";
                    if (obj != null) strVal = (String) obj;
                    String[] sVal = new String[]{strVal};

                    mapQueryTerm.put(mapKey, sVal);
                }
                pEditInfo.setMapQueryTerm(mapQueryTerm);
            }

        } catch (Exception e) {
            //System.out.println(e.toString());

        }

    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        ReqEditForm pReqEditForm = new ReqEditForm();
        String[] factory_id = {"1", "2"};
        String[] factory_name = {"ssdfds", "sdfdsf"};
        pReqEditForm.setInsertInfo("factory_info", "factory_id", factory_id, null);
        pReqEditForm.setInsertInfo("factory_info", "factory_name", factory_name, null);
        List lst = pReqEditForm.getInsertScript();
        for (int i = 0; i < lst.size(); i++) {
            @SuppressWarnings("unused")
            String str = (String) lst.get(i);
            //System.out.println(str);
            //	String sdel=(String)pReqEditForm.getLstInsErrDel().get(i);
            //System.out.println(sdel);
        }

        //pReqSearch.setQueryTermName("b");
        //String ss=pReqSearch.getTermUrl();
        //System.out.print(ss);


    }


}
