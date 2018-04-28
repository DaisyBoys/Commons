package webtools.common.database;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @deprecated
 * */
public class SQLManager {
        //��ݿ���ر���
        private Connection conn = null;
        private PreparedStatement ps = null;
        private ResultSet rs = null;
        //�����ѯ���
        private ArrayList resultList = null;
        //�����ѯ�ֶ�
        private ArrayList fieldList = null;
        //��־
        private static Logger logger = Logger.getLogger(SQLManager.class);
        //������Ϣ
        private String errMsg = "";
        //�����ݿ����ӷ�ʽ�ı�ʶ��0����ʾ������ڲ���ã�1����ʶ���ⲿ���
        private int connFlag = 0;

        public void executeQuery(String sql, ArrayList paraValueList) {
                try {
                        if(resultList == null) {
                                resultList = new ArrayList();
                        } else {
                                resultList.clear();
                        }

                        //�õ��ֶ����
                        getFieldsName(sql);

                        if(conn == null) {
                                getConnection();
                        }

                        ps = conn.prepareStatement(sql);
                        ArrayList paraInfoList = null;
                        for(int i = 0; paraValueList == null || i < paraValueList.size(); i++) {
                                paraInfoList = (ArrayList)paraValueList.get(i);
                                for(int j = 0; j < paraInfoList.size(); j++) {
                                        if(paraInfoList.get(j) instanceof Integer) {
                                                ps.setInt(j + 1, ((Integer)paraInfoList.get(j)).intValue());
                                        } else if(paraInfoList.get(j) instanceof String) {
                                                ps.setString(j + 1, ((String)paraInfoList.get(j)));
                                        } else if(paraInfoList.get(j) instanceof Float) {
                                                ps.setFloat(j + 1, Float.parseFloat(((String)paraInfoList.get(j))));
                                        }
                                }
                                rs = ps.executeQuery();
                                
                                getQueryResult();
                        }
                } catch(SQLException e) {
                        resultList = null;
                        fieldList = null;
                        errMsg = e.toString();
                        logger.error("1.Error: " + e.toString());
                } catch(Exception e) {
                        fieldList = null;
                        resultList = null;
                        errMsg = "syntax error." + e.toString();
                        logger.error("4.Syntax error:" + sql);
                } finally {
                        try { rs.close(); } catch(SQLException e) {}
                        try { ps.close(); } catch(SQLException e) {}
                        try { if(connFlag == 0) { conn.close(); }} catch(SQLException e) {}
                }
        }

        /**
         * ִ��һ����ѯ���
         * @param sql                   select���
         */
        public void executeQuery(String sql) {
                try {
                        if(resultList == null) {
                                resultList = new ArrayList();
                        } else {
                                resultList.clear();
                        }

                        //�õ��ֶ����
                        getFieldsName(sql);

                        if(conn == null) {
                                getConnection();
                        }
                        ps = conn.prepareStatement(sql);
                        rs = ps.executeQuery();
                        getQueryResult();
                } catch(SQLException e) {
                        resultList = null;
                        fieldList = null;
                        errMsg = e.toString();
                        logger.error("1.Error: " + e.toString());
                } catch(Exception e) {
                        fieldList = null;
                        resultList = null;
                        errMsg = "syntax error." + e.toString();
                        logger.error("4.Syntax error:" + sql);
                } finally {
                        try { rs.close(); } catch(SQLException e) {}
                        try { ps.close(); } catch(SQLException e) {}
                        try { if(connFlag == 0) { conn.close(); }} catch(SQLException e) {}
                }
        }

        /**
         * ��ҳ��ѯ
         * @param strSQL                ��ѯ���
         * @param start                    ��ʼ����
         * @param end                     ��ֹ����
         */
        public void executeQuery(String strSQL, int start, int end) {
                try {
                        if(resultList == null) {
                                resultList = new ArrayList();
                        } else {
                                resultList.clear();
                        }

                        //�õ��ֶ����
                        getFieldsName(strSQL);

                        if(conn == null) {
                                getConnection();
                        }
                        ps = conn.prepareStatement(strSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        rs = ps.executeQuery();
                        getQueryResult(start, end);
                } catch(SQLException e) {
                        resultList = null;
                        fieldList = null;
                        errMsg = e.toString();
                        logger.error("1.Error: " + e.toString());
                } catch(Exception e) {
                        fieldList = null;
                        resultList = null;
                        errMsg = "syntax error." + e.toString();
                        logger.error("4.Syntax error:" + strSQL);
                } finally {
                        try { rs.close(); } catch(SQLException e) { e.printStackTrace(); }
                        try { ps.close(); } catch(SQLException e) { e.printStackTrace(); }
                        try { if(connFlag == 0) { conn.close(); }} catch(SQLException e) { e.printStackTrace(); }
                }
        }

        /**
         * ִ������select count�Ĳ�ѯ���
         * @param strSQL                        ��ѯ���
         * @return int                                 ��ѯ���
         */
        public int executeQueryCount(String strSQL) {
                int count = 0;
                try {
                        if(conn == null) {
                                getConnection();
                        }
                        ps = conn.prepareStatement(strSQL);
                        rs = ps.executeQuery();
                        rs.next();
                        count = rs.getInt(1);
                } catch(SQLException e) {
                        count = -1;
                        errMsg = e.toString();
                } finally {
                        try { rs.close(); } catch(SQLException e) { e.printStackTrace(); }
                        try { ps.close(); } catch(SQLException e) { e.printStackTrace(); }
                        try { if(connFlag == 0) { conn.close(); }} catch(SQLException e) { e.printStackTrace(); }
                }
                return count;
        }

        /**
         * ִ��һ��insert��update��delete���
         * @param sql                           insert��update��delete���
         * @return                                  ִ���Ƿ�ɹ���true��ʾ�ɹ���false��ʾʧ��
         */
        public boolean executeUpdate(String sql) {
                boolean success = true;
                try {
                        if(conn == null) {
                                getConnection();
                        }
                        ps = conn.prepareStatement(sql);
                        ps.executeUpdate();
                } catch(SQLException e) {
                        success = false;
                        errMsg = e.toString();
                        logger.error("3.Error: " + e.toString());
                } finally {
                        try { ps.close(); } catch(SQLException e) { e.printStackTrace(); }
                        try { if(connFlag == 0) { conn.close(); }} catch(SQLException e) { e.printStackTrace(); }
                }
                return success;
        }

        /**
         * ����ִ�д�����insert��update��delete��䣬���磺delete tablename where fieldname = ?��Ҫ�����ֵ�����sql����еĲ������Ӧ
         * @param strSQL                                sql���
         * @param paraValueList                   ����ֵ�б?ÿ��Ԫ�ػ���arraylist����
         * @return  boolean                             ִ���Ƿ�ɹ���true��ʾ�ɹ���false��ʾʧ��
         */
        public boolean executeUpdate(String strSQL, ArrayList paraValueList) {
                boolean retValue = true;
                //��֤sql���Ͳ���ֵ
//                if(!isTrueSyntax(strSQL, paraValueList)) {
//                        errMsg = "parameter number is wrong.";
//                        return false;
//                }

                try {
                        if(conn == null) {
                                getConnection();
                        }
                        ps = conn.prepareStatement(strSQL);
                        ArrayList paraInfoList = null;
                        for(int i = 0; paraValueList == null || i < paraValueList.size(); i++) {
                                paraInfoList = (ArrayList)paraValueList.get(i);
                                for(int j = 0; j < paraInfoList.size(); j++) {
                                        if(paraInfoList.get(j) instanceof Integer) {
                                                ps.setInt(j + 1, ((Integer)paraInfoList.get(j)).intValue());
                                        } else if(paraInfoList.get(j) instanceof String) {
                                                ps.setString(j + 1, ((String)paraInfoList.get(j)));
                                        } else if(paraInfoList.get(j) instanceof Float) {
                                                ps.setFloat(j + 1, Float.parseFloat(((String)paraInfoList.get(j))));
                                        }
                                }
                                ps.executeUpdate();
                        }
                } catch(SQLException e) {
                        retValue = false;
                        errMsg = e.toString();
                        logger.error("Error Message:" + e.toString());
                } finally {
                        try { ps.close(); } catch(SQLException e) { e.printStackTrace(); }
                        try { if(connFlag == 0) { conn.close(); }} catch(SQLException e) { e.printStackTrace(); }
                }
                return retValue;
        }

        /**
         * �����ݿ�����
         * @throws SQLException
         */
        private void getConnection() throws SQLException {
                try {
                        conn = DBManager.getConnection();
                } catch(SQLException e) {
                        throw new SQLException(e.toString());
                }
        }
        /*
        private ArrayList getQueryResult(int fieldNum) {
                ArrayList resultList = new ArrayList();

                try {
                        while(rs.next()) {
                                ConcurrentHashMap fieldsInfo = new ConcurrentHashMap();
                                for(int i = 0; i < fieldNum; i++) {
                                        fieldsInfo.put(new Integer(i), rs.getString(i + 1));
                                }
                                resultList.add(fieldsInfo);
                        }
                } catch(SQLException e) {
                        resultList = null;
                        errMsg = e.toString();
                        logger.error("2.Error: " + e.toString());
                }
                return resultList;
        }
        */
        /**
         * �õ���ѯ�Ľ��
         */
        private void getQueryResult() {
                try {
                        while(rs.next()) {
                                ConcurrentHashMap fieldsInfo = new ConcurrentHashMap();
                                for(int i = 0; i < fieldList.size(); i++) {
                                        fieldsInfo.put((String)fieldList.get(i), rs.getString(i + 1) == null ? "" : rs.getString(i + 1));
                                }
                                resultList.add(fieldsInfo);
                        }
                } catch(SQLException e) {
                        resultList = null;
                        errMsg = e.toString();
                        logger.error("5.Unknown Error." + e.toString());
                }
        }

        private void getQueryResult(int start, int end) throws SQLException {
                if(end < start) {
                        throw new SQLException("end rows is less than start row.");
                }
                if(start < 1) {
                        throw new SQLException("start row must be more than zeor.");
                }

//                if(resultList == null) {
//                        resultList = new ArrayList();
//                } else {
//                        resultList.clear();
//                }
                try {
                        int count = 0;
                        rs.absolute(start);
                        do {
                                ConcurrentHashMap fieldsInfo = new ConcurrentHashMap();
                                for(int i = 0; i < fieldList.size(); i++) {
                                        fieldsInfo.put((String)fieldList.get(i), rs.getString(i + 1) == null ? "" : rs.getString(i + 1));
                                }
                                resultList.add(fieldsInfo);
                                count++;
                        } while(count < end - start && rs.next());
                } catch(SQLException e) {
                        throw e;
                }
        }

        /**
         * �õ���ѯ���ļ�¼��
         * @return int                  ���ļ�¼��
         */
        public int getSize() {
                return resultList == null ? 0 : resultList.size();
        }

        /**
         * ��select����н������ֶε����
         * @param strSQL                        select���
         */
        private void getFieldsName(String strSQL) throws Exception {
                String searchStr = strSQL.toLowerCase();
                String selectStr = "select ";
                int pos = searchStr.indexOf(selectStr);
                if(pos < 0) {
                        //������ǰ��select����﷨����
                        throw new Exception("Syntax error.");
                }

                searchStr = searchStr.substring(pos + selectStr.length());
                selectStr = " from";
                pos = searchStr.indexOf(selectStr);
                if(pos < 0) {
                        //������ǰ��select����﷨����
                        throw new Exception("Syntax error.");
                        //return;
                }

                fieldList = new ArrayList();
                searchStr = searchStr.substring(0, pos);
                String[] fields = searchStr.split(",");
                String field = null;
                for(int i = 0; i < fields.length; i++) {
                        field = fields[i].trim();
                        if(field.lastIndexOf(' ') > 0) {
                                //�����ֶδ��ڱ�������
                                fieldList.add(field.substring(field.lastIndexOf(' ')).trim());
                        } else {
                                //�ж��ֶ������Ƿ��б���
                                if(field.indexOf('.') > 0) {                //ָ��select a.id,b.name�����
                                        fieldList.add(field.substring(field.indexOf('.') + 1));
                                } else {
                                        fieldList.add(field.trim());
                                }
                        }
                }
        }

        /**
         * ��ȡ��n����¼��ĳ���ֶε�ֵ
         * @param line                          ��n����¼
         * @param fieldName             �ֶ����
         * @return                                  �ֶε�ֵ�����۸��ֶξ����������ʲôȫ�������ַ�����<p>=null��ʾ������ָ�����ֶ�
         */
        public String getFieldValue(int line, String fieldName) throws Exception {
                if(resultList == null) {
                        throw new Exception("result set can not empty.");
                }

                if(line < 0 || (resultList != null && line > resultList.size())) {
                        throw new Exception("parameter is over ");
                }

                ConcurrentHashMap fieldInfo = (ConcurrentHashMap)resultList.get(line);
                return (String)fieldInfo.get(fieldName);
        }

        /**
         * �õ�������Ϣ
         * @return  String              ������Ϣ������
         */
        public String getErrorMsg() {
                return errMsg;
        }

        /**
         * ��֤sql���Ͳ���ֵ�Ƿ���һһ��Ӧ��
         * @param strSQL                                sql���
         * @param paraValueList                   ����ֵ����
         * @return boolean                              ��֤���true���Ϸ���false���Ƿ�
         */
        private boolean isTrueSyntax(String strSQL, ArrayList paraValueList) {
                boolean retValue = false;
                int count = 0;
                int pos = 0;
                String searchString = strSQL;
                while(searchString.indexOf("?", pos) > 0) {
                        count++;
                        pos = searchString.indexOf("?", pos) + 1;
                }
                if(count > 0) {
                        if(paraValueList != null && paraValueList.size() == count) {
                                retValue = true;
                        }
                } else {
                        if(paraValueList == null || paraValueList.size() == 0) {
                                retValue = true;
                        }
                }
                return retValue;
        }

        /**
         * ����һ����ݿ����ӣ���һ��ҵ���߼�����Ҫ���ö����ݿ����ʱʹ�õ�
         * @param connect
         */
        public void setConnection(Connection connect) {
                conn = connect;
                connFlag = 1;
        }

        public static void main(String[] argc) {
                SQLManager sqlMgr = new SQLManager();
//                try {
//                String str = "select uinfo.i_usr_id  i_usr_id,uinfo.nc_nick_name  nc_nick_name,uinfo.dt_create  createdt," +
//                                     "uinfo.nc_province  nc_province," +
//                                     "count(w.i_work_id)  work_count " +
////                                     "count(wacc.i_work_id)  wacc_count   " +
////                                     "from t_user_info uinfo " +
//                                     "from t_user_info uinfo left join t_work w on uinfo.i_usr_id = w.i_usr_id    " +
////                                     "left join t_work_access_list wacc on uinfo.i_usr_id = wacc.i_usr_id    " +
////                                     "left join t_user_category cat on uinfo.i_usr_id = cat.i_usr_id    " +
////                                     "left join t_podcast_category catname on cat.i_category_id = catname.i_category_id,    " +
////                                     "t_user usr, t_user_home_page homepage    " +
//                                     "where uinfo.nc_province = '" + new String((new String("����").getBytes("GB2312")),"ISO8859-1") + "' " +
//                                     "group by i_usr_id,nc_nick_name,createdt,nc_province";
////                String str = "select a.i_column_id,a.nc_column_name,count(b.i_work_id) num " +
////                                             "from t_column a left join t_work b on a.i_column_id = b.i_column_id " +
////                                             "where a.i_usr_id = '20060116111245611' " +
////                                             "group by a.i_column_id,a.nc_column_name " +
////                                             "order by a.i_column_id";
//                sqlMgr.executeQuery(str);
//                try {
//                        for(int i = 0; i < sqlMgr.getSize(); i++) {
//                                System.out.println(sqlMgr.getFieldValue(i, "i_usr_id"));
//                        }
//                } catch(Exception e) {
//                        System.out.println(e.toString());
//                }
//                } catch(UnsupportedEncodingException e) {
//
//                }

                //����������ѯ������executeQuery
                try {
                        ArrayList paraList = new ArrayList(1);
                        ArrayList a = new ArrayList(1);
                        a.add("20060308003719656");
                        paraList.add(a);
                        String sql = "select title from t_bbs_category where category_id=?";
                        sqlMgr.executeQuery(sql, paraList);
                        for(int i = 0; i < sqlMgr.getSize(); i++) {
                                System.out.println("-----" + sqlMgr.getFieldValue(i, "title") + "-------");
                        }
                } catch(Exception e) {
                        System.out.println("error");
                }

                //����executeUpdate(String, ArrayList)����
//                ArrayList paraList = new ArrayList(1);
//                String[] a = new String[2];
//                a[0] = "Int";
//                a[1] = "1";
//                paraList.add(a);
//                String sql = "update t_autoplay_list set i_no = 2 where i_file_id = ?";
//                boolean retValue = sqlMgr.executeUpdate(sql, paraList);
//                System.out.println(retValue + " " + sqlMgr.getErrorMsg());

                //���Բ������
//                ArrayList a = new ArrayList();
//                ArrayList b = new ArrayList();
//                b.add("12345");
//                b.add("4567");
//                b.add(new Integer(1));
//                a.add(b);
//
//                ArrayList c = new ArrayList();
//                c.add("55555");
//                c.add("6565");
//                c.add(new Integer(2));
//                a.add(c);
//
//                sqlMgr.executeUpdate("insert into t_autoplay_list(i_file_id,i_usr_id,i_no) values(?,?,?)", a);
//                System.out.println(sqlMgr.getErrorMsg());
        }
}
