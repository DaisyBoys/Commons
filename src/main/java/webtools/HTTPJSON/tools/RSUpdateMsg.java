package webtools.HTTPJSON.tools;

import webtools.common.database.JdbcAgent;
import webtools.req.edit.ReqEditForm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class RSUpdateMsg {
    private JdbcAgent jAgent = new JdbcAgent();

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getListErrMap(final ReqEditForm pReqEditForm) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Map<String, String> mapErr = pReqEditForm.getMapErr();
            if (mapErr == null) return list;
            Map<String, Object> mapData = new ConcurrentHashMap<String, Object>();
            Iterator<Entry<String, String>> itSearch = mapErr.entrySet().iterator();
            while (itSearch.hasNext()) {
                Entry<String, String> me = itSearch.next();
                String key = me.getKey();
                String value = me.getValue();
                if (value != null) {
                    mapData.put(key, value);

                }

            }
            list.add(mapData);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getListErrMap(final ReqEditForm pReqEditForm, final String tableName, final String tableAS) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Map<String, String> mapErr = pReqEditForm.getMapErr();
            if (mapErr == null) return list;
            Map<String, Object> mapData = new ConcurrentHashMap<String, Object>();
            Iterator<Entry<String, String>> itSearch = mapErr.entrySet().iterator();
            while (itSearch.hasNext()) {
                Entry<String, String> me = itSearch.next();
                String key = me.getKey();
                key = key.replaceAll(tableName + "\\.", tableAS + "_Err_");
                String value = me.getValue();
                if (value != null) {
                    mapData.put(key, value);

                }

            }
            list.add(mapData);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    /**
     * 新增处理
     */
    public int doInsertByMap(final Map<String, String> map, final String tablename) {
        int nRet = 0;
        try {
            String sql = "insert into " + tablename;
            //sb.delete(arg0, arg1)
            //String
            StringBuffer sbField = new StringBuffer();
            StringBuffer sbVal = new StringBuffer();
            Iterator<Entry<String, String>> itSearch = map.entrySet().iterator();
            while (itSearch.hasNext()) {
                Entry<String, String> me = itSearch.next();
                String key = me.getKey();
                String value = me.getValue();
                if (value != null) {
                    sbField.append(key);
                    sbField.append(",");
                    sbVal.append("'" + value + "'");
                    sbVal.append(",");
                }
            }
            sbField.delete(sbField.lastIndexOf(","), sbField.length());
            sbVal.delete(sbVal.lastIndexOf(","), sbVal.length());
            sql += "(" + sbField.toString() + ")values(" + sbVal.toString() + ")";
            nRet = this.jAgent.update(sql);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return nRet;
    }

    /**
     * 删除
     */
    public int doDeleteByMap(final Map<String, String> map, final String tablename) {
        int nRet = 0;
        try {
            String sql = "delete from  " + tablename + " ";
            StringBuffer sb = new StringBuffer();
            Iterator<Entry<String, String>> itSearch = map.entrySet().iterator();
            while (itSearch.hasNext()) {
                Entry<String, String> me = itSearch.next();
                String key = me.getKey();
                String value = me.getValue();
                String str = key + "='" + value + "'";
                sb.append(str);
                sb.append(" and ");

            }
            sb.delete(sb.lastIndexOf("and"), sb.length());
            String term = sb.toString().trim();
            if (!"".equals(term)) {
                sql += " where " + term;
            }
            nRet = this.jAgent.update(sql);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return nRet;
    }

    /**
     * 修改
     */
    public int doModifyByMap(final Map<String, String> map, final Map<String, String> mapWhere, final String tablename) {
        int nRet = 0;
        try {
            String sql = "update  " + tablename + " set ";
            StringBuffer sb = new StringBuffer();
            Iterator<Entry<String, String>> itSearch = map.entrySet().iterator();
            while (itSearch.hasNext()) {
                Entry<String, String> me = itSearch.next();
                String key = me.getKey();
                String value = me.getValue();
                String str = key + "='" + value + "'";
                sb.append(str);
                sb.append(",");

            }
            sb.delete(sb.lastIndexOf(","), sb.length());
            String setVal = sb.toString().trim();
            if (!"".equals(setVal)) {
                sql += "  " + setVal;
            }
            //==
            String where = "";
            StringBuffer sbWhere = new StringBuffer();
            Iterator<Entry<String, String>> itSearchWhere = mapWhere.entrySet().iterator();
            while (itSearchWhere.hasNext()) {
                Entry<String, String> me = itSearchWhere.next();
                String key = me.getKey();
                String value = me.getValue();
                String str = key + "='" + value + "'";
                sbWhere.append(str);
                sbWhere.append(" and ");

            }
            sbWhere.delete(sbWhere.lastIndexOf(" and "), sbWhere.length());
            where = sbWhere.toString();
            if (!"".equals(sbWhere)) {
                sql += " where " + where;
                nRet = this.jAgent.update(sql);
            }


        } catch (Exception e) {
            // TODO: handle exception
        }
        return nRet;
    }

    public int doModifyByMap(final Map<String, String> map, final Map<String, String> mapWhere, Map<String, String> mapWhereTerm, final String tablename) {
        int nRet = 0;
        try {
            String sql = "update  " + tablename + " set ";
            StringBuffer sb = new StringBuffer();
            Iterator<Entry<String, String>> itSearch = map.entrySet().iterator();
            while (itSearch.hasNext()) {
                Entry<String, String> me = itSearch.next();
                String key = me.getKey();
                String value = me.getValue();
                String str = key + "='" + value + "'";
                sb.append(str);
                sb.append(",");

            }
            sb.delete(sb.lastIndexOf(","), sb.length());
            String setVal = sb.toString().trim();
            if (!"".equals(setVal)) {
                sql += "  " + setVal;
            }
            //==
            String where = "";
            StringBuffer sbWhere = new StringBuffer();
            Iterator<Entry<String, String>> itSearchWhere = mapWhere.entrySet().iterator();
            while (itSearchWhere.hasNext()) {
                Entry<String, String> me = itSearchWhere.next();
                String key = me.getKey();
                String value = me.getValue();
                String term = mapWhereTerm.get(key);
                if (term == null) {
                    String str = key + "='" + value + "'";
                    sbWhere.append(str);
                    sbWhere.append(" and ");
                } else {
                    String str = key + term + "'" + value + "'";
                    sbWhere.append(str);
                    sbWhere.append(" and ");
                }


            }
            sbWhere.delete(sbWhere.lastIndexOf(" and "), sbWhere.length());
            where = sbWhere.toString();
            if (!"".equals(sbWhere)) {
                sql += " where " + where;
                nRet = this.jAgent.update(sql);
            }


        } catch (Exception e) {
            // TODO: handle exception
        }
        return nRet;
    }

    public int doModifyByMapInWhere(final Map<String, String> map, final Map<String, String> mapWhere, final String tablename) {
        int nRet = 0;
        try {
            String sql = "update  " + tablename + " set ";
            StringBuffer sb = new StringBuffer();
            Iterator<Entry<String, String>> itSearch = map.entrySet().iterator();
            while (itSearch.hasNext()) {
                Entry<String, String> me = itSearch.next();
                String key = me.getKey();
                String value = me.getValue();
                String str = key + "='" + value + "'";
                sb.append(str);
                sb.append(",");

            }
            sb.delete(sb.lastIndexOf(","), sb.length());
            String setVal = sb.toString().trim();
            if (!"".equals(setVal)) {
                sql += "  " + setVal;
            }
            //==
            String where = "";
            StringBuffer sbWhere = new StringBuffer();
            Iterator<Entry<String, String>> itSearchWhere = mapWhere.entrySet().iterator();
            while (itSearchWhere.hasNext()) {
                Entry<String, String> me = itSearchWhere.next();
                String key = me.getKey();
                String value = me.getValue();
                String str = value;
                sbWhere.append(str);
                sbWhere.append(" and ");

            }
            sbWhere.delete(sbWhere.lastIndexOf(" and "), sbWhere.length());
            where = sbWhere.toString();
            if (!"".equals(sbWhere)) {
                sql += " where " + where;
                nRet = this.jAgent.update(sql);
            }


        } catch (Exception e) {
            // TODO: handle exception
        }
        return nRet;
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        sb.append("sss");
        sb.append("bb");
        sb.append("cc");
        System.out.printf(sb.toString() + sb.length());
        sb.delete(sb.lastIndexOf("c"), sb.length());
        System.out.printf(sb.toString());

    }
}
