package webtools.common.database;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

import webtools.common.database.pagination.*;

/**
 * Created by IntelliJ IDEA.
 * User: kfly
 * Date: 2006-12-7
 * Time: 11:19:12
 * To change this template use File | Settings | File Templates.
 */
public class JdbcAgent
{
	 private Connection con = null;
	    private static Logger logger = Logger.getLogger(JdbcAgent.class);
	    private String errMsg = "";
	  
	    public int queryCount(final String sql)
	    {
	    	System.out.println(sql);

	        boolean mycon = false;
	        ResultSet rs = null;
	        Statement ps = null;
	        try{
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	          
	           ps = con.createStatement();
	           rs = ps.executeQuery(DBManager.getCharset().code2_2_code1(sql));
	           rs.next();
	           return rs.getInt(1);
	        }
	        catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("function query [" + sql + "]:" + ex.toString());
	            return -1;
	        }
	        finally{
	            try{
	            	if(rs != null) rs.close();
	                if(ps != null) ps.close();
	              //
	              if(mycon){ con.close(); con = null;}
	            }
	            catch(Exception ex){
	              errMsg = ex.toString();
	              logger.error("function query [" + sql + "]:" + ex.toString());
	            }
	        }
	    }

	   
	    public int queryCount(final String sql,final List<Object> paras){
	        ResultSet rs = null;
	        PreparedStatement ps = null;
	        boolean mycon = false;
	        try{
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           ps = con.prepareStatement(sql);
	           for(int i = 0; i < paras.size(); i++){
	        	   final Object obj = paras.get(i);
	 			  if (obj instanceof String) {
	 				 ps.setObject(i + 1, DBManager.getCharset().code2_2_code1((String)obj));
	 			  }
	               else {
	 				 ps.setObject(i + 1, obj);
	 			  }
	           }
	           rs = ps.executeQuery();
	           rs.next();
	           return rs.getInt(1);
	        }
	        catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("function query [" + sql + "]:" + ex.toString());
	            return -1;
	        }
	        finally{
	            try{
	            	if(rs != null) rs.close();
	                if(ps != null) ps.close();
	             
	               if(mycon){ con.close(); con = null;}
	            }
	            catch(Exception ex){
	               errMsg = ex.toString();
	               logger.error("function query [" + sql + "]:" + ex.toString());
	            }
	        }
	     }
	    
	  
	    public final DBResult query(final String sql){
	    	System.out.println(sql);
	    	final JdbcResult res = new JdbcResult();
		       ResultSet rs = null;
		       Statement ps = null;
		       boolean mycon = false;
		       try{
		          if(con == null){
		              con = DBManager.getConnection();
		              mycon = true;
		          }
		          try {
			        	//Thread.sleep(1);
					} catch (Exception e) {}
				  if(!con.isClosed()){
			          ps = con.createStatement();
			          rs = ps.executeQuery(DBManager.getCharset().code2_2_code1(sql));
			          res.extractdata(rs);
				  }
			          return res;
				  
		       }
		       catch(Exception ex){
		           errMsg = ex.toString();
		           logger.error("function query [" + sql + "]:" + ex.toString());
		           return null;
		       }
		       finally{
		           try{
		              if(rs != null) rs.close();
		              if(ps != null) ps.close();
		             
		              if(mycon){ 
		            	  if(!con.isClosed()) con.close(); con = null;}
		           }
		           catch(Exception ex){
		              errMsg = ex.toString();
		              logger.error("function query [" + sql + "]:" + ex.toString());
		           }
		       }
	    }

	  
	    public final DBResult query(final String sql,final int start,final int end){
	    	System.out.println(sql);
	    	System.out.println("start:"+start+"   end:"+end);

	    	final JdbcResult res = new JdbcResult();
	       ResultSet rs = null;
	       Statement ps = null;
	       boolean mycon = false;
	       try{
	          if(con == null){
	              con = DBManager.getConnection();
	              mycon = true;
	          }
	          
	       	if(con.isClosed()){
					try {
			        	Thread.sleep(100);
					} catch (Exception e) {}
					con = DBManager.getConnection();
				}
				 if(!con.isClosed()){	
			          ps = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			          
			          String query_sql = sql;
			          final Pagination p = Pagination.create_pagination();
			          
			          if(p != null){
			        	  query_sql = p.parse_sql(sql, start, end);
			          }
			          rs = ps.executeQuery(DBManager.getCharset().code2_2_code1(query_sql));
			          if(p != null)
			          {
			        	  res.extractdata(rs);
			          }
			          else{
			        	  res.extractdata(rs,start,end);
			          }
				 }
	          return res;
	       }
	       catch(Exception ex){
	           errMsg = ex.toString();
	           logger.error("function query [" + sql + "]:" + ex.toString());
	           return null;
	       }
	       finally{
	           try{
	        	   if(rs != null) rs.close();
	               if(ps != null) ps.close();
	             
	              if(mycon){ if(!con.isClosed()) con.close(); con = null;}
	           }
	           catch(Exception ex){
	              errMsg = ex.toString();
	              logger.error("function query [" + sql + "]:" + ex.toString());
	           }
	       }
	    }

	  
	    public DBResult query(final String sql,final List<Object> paras){
	    	System.out.println(sql);

	    	final JdbcResult res = new JdbcResult();
	       ResultSet rs = null;
	       PreparedStatement ps = null;
	       boolean mycon = false;
	       try{
	          if(con == null){
	              con = DBManager.getConnection();
	              mycon = true;
	          }
	          ps = con.prepareStatement(sql);
	          for(int i = 0; i < paras.size(); i++){
	              Object obj = paras.get(i);
				  if (obj instanceof String) {
					 ps.setObject(i + 1, DBManager.getCharset().code2_2_code1((String)obj));
				  }
	              else {
					 ps.setObject(i + 1, obj);
				  }
	          }
	          rs = ps.executeQuery();
	          res.extractdata(rs);
	          return res;
	       }
	       catch(Exception ex){
	           errMsg = ex.toString();
	           logger.error("function query [" + sql + "]:" + ex.toString());
	           return null;
	       }
	       finally{
	           try{
	        	   if(rs != null) rs.close();
	               if(ps != null) ps.close();
	             
	              if(mycon){ con.close(); con = null;}
	           }
	           catch(Exception ex){
	              errMsg = ex.toString();
	              logger.error("function query [" + sql + "]:" + ex.toString());
	           }
	       }
	    }

	   
	    public DBResult query(final String sql,final List<Object> paras,final int start,final int end){
	    	System.out.println(sql);

	    	final JdbcResult res = new JdbcResult();
	       ResultSet rs = null;
	       PreparedStatement ps = null;
	       boolean mycon = false;
	       try{
	          if(con == null){
	              con = DBManager.getConnection();
	              mycon = true;
	          }
	          try {
		        	Thread.sleep(1);
				} catch (Exception e) {}
	          String query_sql = sql;
	          Pagination p = Pagination.create_pagination();
	          if(p != null)
	          {
	        	  query_sql = p.parse_sql(sql, start, end);
	          }
	          ps = con.prepareStatement(query_sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	          for(int i = 0; i < paras.size(); i++){
	              Object obj = paras.get(i);
				  if (obj instanceof String) {
					 ps.setObject(i + 1, DBManager.getCharset().code2_2_code1((String)obj));
				  }
	              else {
					 ps.setObject(i + 1, obj);
				  }
	          }
	          rs = ps.executeQuery();
	          if( p != null)
	          {
	        	  res.extractdata(rs);
	          }
	          else{
	        	  res.extractdata(rs, start, end);
	          }
	          return res;
	       }
	       catch(Exception ex){
	           errMsg = ex.toString();
	           logger.error("function query [" + sql + "]:" + ex.toString());
	           return null;
	       }
	       finally{
	           try{
	        	   if(rs != null) rs.close();
	               if(ps != null) ps.close();
	            
	              if(mycon){ con.close(); con = null;}
	           }
	           catch(Exception ex){
	              errMsg = ex.toString();
	              logger.error("function query [" + sql + "]:" + ex.toString());
	           }
	       }
	    }

	  
	    public int update(final String sql,final List<Object> paras){
	    	System.out.println(sql);

	        boolean mycon = false;
	        PreparedStatement ps = null;
	        int updateCount = -1;
	        try
	        {
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           try {
		        	Thread.sleep(1);
				} catch (Exception e) {}
	           ps = con.prepareStatement(sql);
	           for(int i = 0; paras != null && i < paras.size(); i++){
	              Object para = paras.get(i);
	              if(para instanceof String){
	                  ps.setString( i + 1 , DBManager.getCharset().code2_2_code1((String)para));
	              }
	              else{
	                  ps.setObject( i + 1 , para );
	              }
	           }
	           updateCount = ps.executeUpdate();
	        }
	        catch(Exception ex){
	           updateCount = -1;
	           errMsg = ex.toString();
	           logger.error("function update [" + sql + "]:" + ex.toString());
	        }
	        finally{
	           try{
	               if(ps != null) ps.close();
	               //������Լ����������ӣ������ص������򲻹ر�����
	               if(mycon){ con.close(); con = null;}
	           }
	           catch(Exception ex)
	           {
	        	   errMsg = ex.toString();
	               logger.error("function update [" + sql + "]:" + errMsg);
	           }
	        }
	        return updateCount;
	    }

	   
	    public int update(String sql){
	    	System.out.println(sql);

	        boolean mycon = false;
	        Statement ps = null;
	        int updateCount = -1;
	        
	        try
	        {
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           try {
		        	Thread.sleep(1);
				} catch (Exception e) {}
				if(!con.isClosed()){
		           ps = con.createStatement();
		           sql = DBManager.getCharset().code2_2_code1(sql);
		           
		           
		           
		           updateCount = ps.executeUpdate(sql);
				}else{
					updateCount = -1;
				}
	           //updateCount = ps.executeUpdate(sql);
	        }
	        catch(Exception ex){
	           updateCount = -1;
	           errMsg = ex.toString();
	           logger.error("function update [" + sql + "]:" + ex.toString());
	        }
	        finally{
	           try{
	        	   if(ps != null) ps.close();
	               //
	               if(mycon){ if(!con.isClosed()) con.close(); con = null;}
	           }
	           catch(Exception ex){
	        	   errMsg = ex.toString();
	               logger.error("function update [" + sql + "]:" + errMsg);
	           }
	        }
	        return updateCount;
	    }

	    public List<Integer> m_ListErrBatchNum;//错误信息
	    public boolean batchupdate(final String[] sqls){
	        boolean ret = true;
	        boolean mycon = false;
	        Statement ps = null;
	        m_ListErrBatchNum=new ArrayList<Integer>();
	        try
	        {
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           ps = con.createStatement();
	           for(int i = 0; i < sqls.length; i++)
	           {
	        	   sqls[i] = DBManager.getCharset().code2_2_code1(sqls[i]);
	        	   try {
	        		   ps.executeUpdate(sqls[i]);
					} catch (Exception e) {
					   ret = false;
			           errMsg = e.toString();
			           logger.error("batch update :" + e.toString());
			           m_ListErrBatchNum.add(i);
					}
	              
	           }
	          
	        }
	        catch(Exception ex){
	           ret = false;
	           errMsg = ex.toString();
	           logger.error("batch update :" + ex.toString());
	        }
	        finally{
	           try{
	        	   if(ps != null) ps.close();
	               //������Լ����������ӣ������ص������򲻹ر�����
	               if(mycon){ con.close(); con = null;}
	           }
	           catch(Exception ex){
	        	   errMsg = ex.toString();
	               logger.error("batch update :" + errMsg);
	           }
	        }
	        return ret;
	    }
	    

	  
	    @SuppressWarnings("unchecked")
		public boolean batchupdate(final String sql,final List<Object> paralist){
	    	System.out.println(sql);

	        boolean mycon = false;
	        PreparedStatement ps = null;
	        boolean ret = true;
	        try
	        {
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           ps = con.prepareStatement(sql);
	           for(int i = 0; paralist != null && i < paralist.size(); i++){
	              List<Object> paras =(List<Object>) paralist.get(i);
	              for(int j = 0; j < paras.size(); j++){
	                  Object para = paras.get(j);
	                   if(para instanceof String){
	                      ps.setString( j + 1 , DBManager.getCharset().code2_2_code1((String)para) );
	                   }
	                   else{
	                       ps.setObject( j + 1 , para );
	                   }
	              }
	              ps.executeUpdate();
	           }          
	        }
	        catch(Exception ex){
	           ret = false;
	           errMsg = ex.toString();
	           logger.error("function batchupdate [" + sql + "]:" + ex.toString());
	        }
	        finally{
	           try{
	        	   if(ps != null) ps.close();
	               
	               if(mycon){ con.close(); con = null;}
	           }
	           catch(Exception ex){
	        	   errMsg = ex.toString();
	               logger.error("batch update :" + errMsg);
	           }
	        }
	        return ret;
	    }

	   
	    public boolean beginTransaction(){
	         try{
	            if(con != null){
	                errMsg = "can not begin transaction is connecting is living";
	                logger.error("beginTransaction:" + errMsg);
	                return false;
	            }
	            con = DBManager.getConnection();
	            con.setAutoCommit(false);
	         }
	         catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("beginTransaction:" + ex.toString());
	            return false;
	         }
	         return true;
	    }

	   
	    public void commit(){
	         try{
	            if(con != null){
	                con.commit();
	                con.setAutoCommit(true);
	            }
	         }
	         catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("commit:" + ex.toString());
	         }
	         finally{
	             try{
	                con.close();
	                con = null;
	             }
	             catch(Exception ex){
	                 errMsg = ex.toString();
	                 logger.error("commit:" + ex.toString());
	             }
	         }
	    }

	    /**
	     * �ع�һ����ݿ�����
	     * */
	    public void Rollback(){
	        try{
	            if(con != null){
	                con.rollback();
	                con.setAutoCommit(true);
	            }
	         }
	         catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("commit:" + ex.toString());
	         }
	         finally{
	             try{
	                con.close();
	                con = null;
	             }
	             catch(Exception ex){
	                 errMsg = ex.toString();
	                 logger.error("commit:" + ex.toString());
	             }
	         }
	    }

	  
	    public DBResult list_tables()
	    {
	    	final JdbcResult res = new JdbcResult();
	        ResultSet rs = null;
	        DatabaseMetaData  data;
	        boolean mycon = false;
	        try{
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           data = con.getMetaData();
	           rs = data.getTables(null, null, null, null);
	           res.extractdata(rs);
	           return res;
	        }
	        catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("function list_tables:" + ex.toString());
	            return null;
	        }
	        finally{
	            try{
	               if(rs != null) rs.close();
	               //������Լ����������ӣ������ص������򲻹ر�����
	               if(mycon){ con.close(); con = null;}
	            }
	            catch(Exception ex){
	               errMsg = ex.toString();
	               logger.error("function list_tables:" + ex.toString());
	            }
	        }
	    }
	    
	    /**
	     * �о���ݿ�ı���Ϣ��<br>
	     * @param namepattern  �ο��ı�������(������ģ���ֶ�)
	     * @param types  ������
	     * */
	    public DBResult list_tables(final String namepattern,final String[] types)
	    {
	    	final JdbcResult res = new JdbcResult();
	        ResultSet rs = null;
	        DatabaseMetaData  data;
	        boolean mycon = false;
	        try{
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           data = con.getMetaData();
	           rs = data.getTables(null, null, namepattern, types);
	           res.extractdata(rs);
	           return res;
	        }
	        catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("function list_tables:" + ex.toString());
	            return null;
	        }
	        finally{
	            try{
	            	if(rs != null) rs.close();
	               //������Լ����������ӣ������ص������򲻹ر�����
	               if(mycon){ con.close(); con = null;}
	            }
	            catch(Exception ex){
	               errMsg = ex.toString();
	               logger.error("function list_tables:" + ex.toString());
	            }
	        }
	    }
	    
	    /**
	     * �о���ݿ���������Ϣ��<br>
	     *  TABLE_CAT String => ����𣨿�Ϊ null�� <br>
			TABLE_SCHEM String => ��ģʽ����Ϊ null�� <br>
			TABLE_NAME String => ����� <br>
			COLUMN_NAME String => ����� <br>
			KEY_SEQ short => �����е����кţ�ֵ 1 ��ʾ�����еĵ�һ�У�ֵ 2 ��ʾ�����еĵڶ��У��� <br>
			PK_NAME String => �������ƣ���Ϊ null�� <br>
		 * @param tablename �����
	     * @return DBResult �Լ�¼������ʽ�г����б���Ϣ��null�������쳣��
	     * */
	    public DBResult list_primarykey(String tablename)
	    {
	    	final JdbcResult res = new JdbcResult();
	        ResultSet rs = null;
	        DatabaseMetaData  data;
	        boolean mycon = false;
	        try{
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           data = con.getMetaData();
	           rs = data.getPrimaryKeys(null, null, tablename);
	           res.extractdata(rs);
	           return res;
	        }
	        catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("function list_tables:" + ex.toString());
	            return null;
	        }
	        finally{
	            try{
	            	if(rs != null) rs.close();
	               //������Լ����������ӣ������ص������򲻹ر�����
	               if(mycon){ con.close(); con = null;}
	            }
	            catch(Exception ex){
	               errMsg = ex.toString();
	               logger.error("function list_tables:" + ex.toString());
	            }
	        }
	    }
	    
	    /**
	     * ���е�ָ���ֶ��Ƿ�Ϊunique
	     * @param column  ָ�����ֶ�
	     * @param tablename ָ���ı���
	     * @return ���Ψһ���ͣ�����true�����򷵻�false
	     * */
	    public boolean is_unique(String column,String tablename)
	    {
	        ResultSet rs = null;
	        DatabaseMetaData  data;
	        boolean mycon = false;
	        try{
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           data = con.getMetaData();
	           rs = data.getIndexInfo(null, null, tablename,true,false);
	           while(rs.next())
	           {
	        	   String pk = rs.getString("COLUMN_NAME");
	        	   if(pk.equals(column))
	        	   {
	        		   return true;
	        	   }
	           }
	           return false;
	        }
	        catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("function is_primary:" + ex.toString());
	            return false;
	        }
	        finally{
	            try{
	            	if(rs != null) rs.close();
	               //������Լ����������ӣ������ص������򲻹ر�����
	               if(mycon){ con.close(); con = null;}
	            }
	            catch(Exception ex){
	               errMsg = ex.toString();
	               logger.error("function is_primary:" + ex.toString());
	            }
	        }
	    }
	    
	    /**
	     * ���е�ָ���ֶ��Ƿ�Ϊprimary key
	     * @param column  �ֶ���
	     * @param tablename  ����
	     * @return ���Ϊ�����򷵻�true�����򷵻�false��
	     * */
	    public boolean is_primary(String column,String tablename)
	    {
	        ResultSet rs = null;
	        DatabaseMetaData  data;
	        boolean mycon = false;
	        try{
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           data = con.getMetaData();
	           rs = data.getPrimaryKeys(null, null, tablename); //getIndexInfo
	           while(rs.next())
	           {
	        	   String pk = rs.getString("COLUMN_NAME");
	        	   if(pk.equals(column))
	        	   {
	        		   return true;
	        	   }
	           }
	           return false;
	        }
	        catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("function is_primary:" + ex.toString());
	            return false;
	        }
	        finally{
	            try{
	            	if(rs != null) rs.close();
	               //������Լ����������ӣ������ص������򲻹ر�����
	               if(mycon){ con.close(); con = null;}
	            }
	            catch(Exception ex){
	               errMsg = ex.toString();
	               logger.error("function is_primary:" + ex.toString());
	            }
	        }
	    }
	    
	    /**
	     *  �г�ָ����������ֶ���Ϣ<br>
	     *  TABLE_CAT String => ����𣨿�Ϊ null�� <br>
			TABLE_SCHEM String => ��ģʽ����Ϊ null�� <br>
			TABLE_NAME String => ����� <br>
			COLUMN_NAME String => ����� <br>
			DATA_TYPE int => ���� java.sql.Types �� SQL ���� <br>
			TYPE_NAME String => ���Դ������������ƣ����� UDT���������������ȫ�޶��� <br>
			COLUMN_SIZE int => �еĴ�С�� <br>
			BUFFER_LENGTH δ��ʹ�á� <br>
			DECIMAL_DIGITS int => С��ֵ�λ����� DECIMAL_DIGITS �����õ�������ͣ��򷵻� Null�� <br>
			NUM_PREC_RADIX int => ����ͨ��Ϊ 10 �� 2�� <br>
			NULLABLE int => �Ƿ�����ʹ�� NULL�� <br>
			columnNoNulls - ���ܲ�����ʹ�� NULL ֵ <br>
			columnNullable - ��ȷ����ʹ�� NULL ֵ <br>
			columnNullableUnknown - ��֪���Ƿ��ʹ�� null <br>
			REMARKS String => �����е�ע�ͣ���Ϊ null�� <br>
			COLUMN_DEF String => ���е�Ĭ��ֵ����ֵ�ڵ������ʱӦ������Ϊһ���ַ���Ϊ null�� <br>
			SQL_DATA_TYPE int => δʹ�� <br>
			SQL_DATETIME_SUB int => δʹ�� <br>
			CHAR_OCTET_LENGTH int => ���� char ���ͣ��ó��������е�����ֽ��� <br>
			ORDINAL_POSITION int => ���е��е������ 1 ��ʼ�� <br>
			IS_NULLABLE String => ISO ��������ȷ�����Ƿ���� null�� <br>
			YES --- ��������԰��� NULL <br>
			NO --- ��������԰��� NULL <br>
			���ַ� --- ���֪�������Ƿ���԰��� null <br>
			SCOPE_CATLOG String => �����������������Ե���������� DATA_TYPE ���� REF����Ϊ null�� <br>
			SCOPE_SCHEMA String => ���ģʽ�������������Ե���������� DATA_TYPE ���� REF����Ϊ null�� <br>
			SCOPE_TABLE String => ����ƣ������������Ե���������� DATA_TYPE ���� REF����Ϊ null�� <br>
			SOURCE_DATA_TYPE short => ��ͬ���ͻ��û���� Ref ���͡����� java.sql.Types �� SQL ���͵�Դ���ͣ���� DATA_TYPE ���� DISTINCT ���û���ɵ� REF����Ϊ null��<br> 
			IS_AUTOINCREMENT String => ָʾ�����Ƿ��Զ����� <br>
			YES --- �������Զ����� <br>
			NO --- �����в��Զ����� <br>
			���ַ� --- �����ȷ�������Ƿ����Զ����Ӳ���<br>
		*@param  tablename  ϣ���г��ֶ���Ϣ�ı���ơ�
		*@return DBResult   ����ݼ�����ʽ�г����ֶ���Ϣ��null�������쳣��
	 * */
	    public DBResult list_columns(String tablename)
	    {
	    	final JdbcResult res = new JdbcResult();
	        ResultSet rs = null;
	        DatabaseMetaData  data;
	        boolean mycon = false;
	        try{
	           if(con == null){
	               con = DBManager.getConnection();
	               mycon = true;
	           }
	           data = con.getMetaData();
	           rs = data.getColumns(null, null, tablename, null);
	           res.extractdata(rs);
	           return res;
	        }
	        catch(Exception ex){
	            errMsg = ex.toString();
	            logger.error("function list_tables:" + ex.toString());
	            return null;
	        }
	        finally{
	            try{
	            	if(rs != null) rs.close();
	               //������Լ����������ӣ������ص������򲻹ر�����
	               if(mycon){ con.close(); con = null;}
	            }
	            catch(Exception ex){
	               errMsg = ex.toString();
	               logger.error("function list_tables:" + ex.toString());
	            }
	        }
	    }
	    
	  
	   public DBResult list_procedure_paras(String proc_name)
	   {
		   final JdbcResult res = new JdbcResult();
	       ResultSet rs = null;
	       DatabaseMetaData  data;
	       boolean mycon = false;
	       try{
	          if(con == null){
	              con = DBManager.getConnection();
	              mycon = true;
	          }
	          data = con.getMetaData();
	          rs = data.getProcedureColumns(null, null, proc_name, null);
	          res.extractdata(rs);
	          return res;
	       }
	       catch(Exception ex){
	           errMsg = ex.toString();
	           logger.error("function list_tables:" + ex.toString());
	           return null;
	       }
	       finally{
	           try{
	        	   if(rs != null) rs.close();
	             
	              if(mycon){ con.close(); con = null;}
	           }
	           catch(Exception ex){
	              errMsg = ex.toString();
	              logger.error("function list_tables:" + ex.toString());
	           }
	       }
	   }
	   
	    
	   
	    public String getErrMsg() {
	        return errMsg;
	    }
}
