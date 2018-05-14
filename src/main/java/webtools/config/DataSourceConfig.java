package webtools.config;

import webtools.HTTPJSON.GsonMapMgr;
import webtools.HTTPJSON.tools.RSPageMgr;
import webtools.common.database.DBManager;
import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wugaoshang on 2018/5/14.
 */
public class DataSourceConfig {


    private static JdbcAgent jAgent = new JdbcAgent();
    private static RSPageMgr rsPageMgr = new RSPageMgr();
    private final static GsonMapMgr pGsonMapMgr = new GsonMapMgr();
    private static final Properties db = new Properties();

    static {
        initMysql("application.properties");
    }

    /**
     * 初始化mySql数据库
     * 本配置在resources下
     */
    private static void initMysql(String resourceFile) {
        try {
            InputStream inputStream = DataSourceConfig.class.getClassLoader().getResourceAsStream(resourceFile);
            db.load(inputStream);
            final String driverClass =(String) db.get("spring.datasource.driver-class-name");
            final String jdbcUrl = (String) db.get("spring.datasource.url");
            final String user = (String) db.get("spring.datasource.username");
            final String password = (String) db.get("spring.datasource.password");
            DBManager.init(driverClass, jdbcUrl, user, password);
            Map map = new ConcurrentHashMap();
            map.put("driver", driverClass);
            map.put("url", jdbcUrl);
            map.put("user", user);
            map.put("password", password);
            map.put("database.charset", "UTF-8");
            map.put("application.charset", "UTF-8");
            map.put("database.type", "MySQL");
            DBManager.initConnection(map);
        } catch (IOException e) {
            System.out.println("读取配置文件失败！");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        }

    }

    /**
     * 传入查询SQL，返回结果集
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> selectList(String sql) {
        List<Map<String, Object>> list = null;
        DBResult rs = jAgent.query(sql);
        if (rs != null) {
            list = new ArrayList<>();
            for (int i = 0; i < rs.getRowCount(); i++) {
                // 创建通用返回结果集
                Map<String, Object> pData = rsPageMgr.createRSMapData(rs, i);
                list.add(pData);
            }
        }
        return list;
    }


    /**
     * 示例1
     *  select  t1.id as id,t1.title as title from  news t1  where 1=1 order by  t1.id desc
     * 结果：
     *
     * id>>>>:11title>>>>TITLE{10}
     * id>>>>:10title>>>>TITLE{9}
     * id>>>>:9title>>>>TITLE{8}
     * id>>>>:8title>>>>TITLE{7}
     * id>>>>:7title>>>>TITLE{6}
     * id>>>>:6title>>>>TITLE{5}
     * id>>>>:5title>>>>TITLE{4}
     * id>>>>:4title>>>>TITLE{3}
     * id>>>>:3title>>>>TITLE{2}
     * id>>>>:2title>>>>TITLE{1}
     * id>>>>:1title>>>>TITLE{0}
     *
     */
    public static void findList() {
        String select = " select ";

        String field = " t1.id as id" +
                ",t1.title as title" +


                "";
        String from = " from  news t1 ";

        String where = "" +
                " where" +
                " 1=1" +
                "";

        String orderBy = " order by " +
                " t1.id" +
                " desc";
        String sql = select + field + from + where + orderBy;
        List<Map<String, Object>> maps = DataSourceConfig.selectList(sql);
        System.out.println(maps.size());
        for (Map<String, Object> map : maps) {
            String id = pGsonMapMgr.getString(map, "id");
            String title = pGsonMapMgr.getString(map, "title");
            System.out.println("id>>>>:" + id+"title>>>>"+title);

        }


    }

    public static void main(String[] ags) {
        DataSourceConfig.findList();
    }


}
