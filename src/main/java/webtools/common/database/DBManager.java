package webtools.common.database;
 
import java.sql.*;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import webtools.common.*;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;



public final class DBManager {
		
        private static PoolingDataSource dataSource=null;
        private static  CharExchange  charset = null;
        private static  String db_type = "unknown";
        public static  int m_MaxActive=30;
        public static void init(final String driver, final String connectURI,final String userName, final String password) throws Exception  {
                try {
                        Class.forName(driver);
                } catch (ClassNotFoundException e) {
                        throw new Exception("Could not find your database driver:" + driver);
                }

                //
                // First, we'll need a ObjectPool that serves as the
                // actual pool of connections.
                //
                // We'll use a GenericObjectPool instance, although
                // any ObjectPool implementation will suffice.
                //
                final GenericObjectPool connectionPool = new GenericObjectPool(null);
                if (m_MaxActive>30){
                	connectionPool.setMaxActive(m_MaxActive-10);
                }else{
                	connectionPool.setMaxActive(30-10);
                }
              
                connectionPool.setMaxWait(30000L);
                
                connectionPool.setTimeBetweenEvictionRunsMillis(-1);
                connectionPool.setTestOnBorrow(true);
                connectionPool.setMaxIdle(10);
                
                //connectionPool.setMinEvictableIdleTimeMillis(5000L);
                connectionPool.setMinEvictableIdleTimeMillis(30000L);
               // connectionPool.setMaxWait(60000L);
                
               //==新改
                 //connectionPool.setMaxIdle(maxActive);    //实际中可以设小一些, 如30
                 connectionPool.setTestOnBorrow(true);
                 connectionPool.setTestOnReturn(false);
                 connectionPool.setTestWhileIdle(false);
                            
               //  connectionPool.setTestWhileIdle(arg0)
                
                //
                // Next, we'll create a ConnectionFactory that the
                // pool will use to create Connections.
                // We'll use the DriverManagerConnectionFactory,
                // using the connect string passed in the command line
                // arguments.
                //
                //ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI,null);
                 final ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, userName, password);

                //
                // Now we'll create the PoolableConnectionFactory, which wraps
                // the "real" Connections created by the ConnectionFactory with
                // the classes that implement the pooling functionality.
                //
                //PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,"select 1",false,true);
                 final  PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
                new PoolableConnectionFactory(connectionFactory,connectionPool,null,"select 1",false,true);
               
               
                //poolableConnectionFactory.setValidationQueryTimeout(1);
                //
                // Finally, we create the PoolingDriver itself,
                // passing in the object pool we created.
                //
                dataSource = new PoolingDataSource(connectionPool);
                
                
        }

     
		public static Connection getConnection() throws SQLException 
		{
			Connection con = dataSource.getConnection();
			return con;
	    }
		
		public static CharExchange getCharset()
		{
			return charset;
		}
		
		public static String getDBType()
		{
			return db_type.toLowerCase();
		}
		
        public static void InitialConnection(final String PropFile)throws Exception
        {
            String dbPropFile = PropFile;
            FileInputStream fin = new FileInputStream(dbPropFile);
            Properties p = new Properties();
            p.load(fin);
            fin.close();

            String dbDriver  = p.getProperty("database.driver");
            String dbUrl = p.getProperty("database.url");
            String userName = p.getProperty("database.user");
            String password = p.getProperty("database.password");
            String dbchar = p.getProperty("database.charset");
            String appchar = p.getProperty("application.charset");
            db_type = p.getProperty("database.type");
            String strMaxActive=p.getProperty("pool.active");
            int nMaxActive=0;
            if (strMaxActive!=null){
            	if ("".equals(strMaxActive)){
            		try {
            			nMaxActive=Integer.parseInt(strMaxActive);
            			m_MaxActive=nMaxActive;
					} catch (Exception e) {
						// TODO: handle exception
					}
            	}
            }
 
        	charset = new CharExchange();
        	charset.setCode1(dbchar);
        	charset.setCode2(appchar);
        	//Utility.set_app_charcode(appchar, "iso8859-1");
        	
            init(dbDriver, dbUrl, userName, password);
        }
        
        //need delete
     
        public static void initConnection(final Map connectionInfo)throws Exception
        {
        	
            String dbDriver  = (String)connectionInfo.get("driver");
            String dbUrl = (String)connectionInfo.get("url");
            String userName = (String)connectionInfo.get("user");
            String password = (String)connectionInfo.get("password");
            String dbchar = (String)connectionInfo.get("database.charset");
            String appchar = (String)connectionInfo.get("application.charset");
            db_type = (String)connectionInfo.get("database.type");
 
        	charset = new CharExchange();
        	charset.setCode1(dbchar);
        	charset.setCode2(appchar);
        	        	
            init(dbDriver, dbUrl, userName, password);
            
        }
        
}
