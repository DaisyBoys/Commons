package webtools.config;

import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;
import java.io.*;
import java.util.*;

public class SysConfig {
    private static final Properties db = new Properties();
    private static final Map<String, Object> vars = new HashMap<String, Object>();

    static {
        // 读取db.properties
        InputStream inputStream = null;
        try {
            String path = SysConfig.class.getResource("/").toURI().getPath();
            path = new File(path).getParentFile().getParentFile().getCanonicalPath();
            inputStream = new FileInputStream(path + "/WEB-INF/config/db.properties");
            db.load(new InputStreamReader(inputStream, "UTF-8"));
            Enumeration ite = db.keys();
            while (ite.hasMoreElements()) {
                String key = (String) ite.nextElement(), value = db.getProperty(key);
                Object var = vars.get(key);
                if (var == null) {
                    vars.put(key, value);
                } else if (var instanceof List) {
                    ((List) var).add(value);
                } else {
                    List list = new ArrayList();
                    list.add(value);
                    list.add(var);
                    vars.put(key, list);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading properties file.", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 初始化sys_config
        String sysid = db.getProperty("sysid");
        String devMode = db.getProperty("devMode");
        List<Map<String, Object>> listSysConfig = new ArrayList<Map<String, Object>>();
        try {
            String select = " select ";
            String field = " t1.config_key" +
                    ",t1.config_value" +
                    "";
            String from = " from  sys_config t1 ";
            String where = " where " +
                    " t1.is_act='" + 1 + "'" +
                    " and t1.sysid='" + sysid + "'" +
                    " and t1.devMode='" + devMode + "'" +
                    "";

            String sql = select + field + from + where + " ";
            //==查询处理
            JdbcAgent jAgent = new JdbcAgent();
            DBResult rs = jAgent.query(sql);
            if (rs != null) {
                for (int i = 0; i < rs.getRowCount(); i++) {
                    //创建通用返回结果集
                    String key = rs.getString(i, "config_key"), value = rs.getString(i, "config_value");
                    Object var = vars.get(key);
                    if (var == null) {
                        vars.put(key, value);
                    } else if (var instanceof List) {
                        ((List) var).add(value);
                    } else {
                        List list = new ArrayList();
                        list.add(value);
                        list.add(var);
                        vars.put(key, list);
                    }
                }
            }
        } catch (Exception e) {
        }


    }


    public static <T> T get(String key) {
        return (T) vars.get(key);
    }

    public static void main(String[] ags){





    }
}
