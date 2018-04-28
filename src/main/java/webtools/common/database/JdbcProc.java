package webtools.common.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

public final class JdbcProc {
   
	private Connection con = null;
	private CallableStatement ps = null;	
	{
		try{
			con = DBManager.getConnection();			
		}
		catch(Exception ex)
		{
			con = null;
		}
	}
	
	public boolean call(String sql,List paras,Integer rettype)
	{
		try{
			if(con != null)
			{
				ps = con.prepareCall(sql);
				if(rettype != null){
					ps.registerOutParameter(1, rettype);
				}
	            for(int i = 0; i < paras.size(); i++)
	            {
	        	   Object para = paras.get(i);
	        	   if(para instanceof Integer)
	        	   {
	        		   ps.setInt(i + 2, (Integer)paras.get(i));
	        	   }
	        	   else if(para instanceof Long)
	        	   {
	        		   ps.setLong(i + 2, (Long)paras.get(i));
	        	   }
	        	   else if(para instanceof Short)
	        	   {
	        		   ps.setShort(i + 2, (Short)paras.get(i));
	        	   }
	        	   else if(para instanceof java.sql.Date)
	        	   {
	        		   ps.setDate(i + 2, (java.sql.Date)paras.get(i));
	        	   }
	        	   else if(para instanceof Double)
	        	   {
	        		   ps.setDouble(i + 2, (Double)paras.get(i));
	        	   }
	        	   else if(para instanceof Float)
	        	   {
	        		   ps.setFloat(i + 2, (Float)paras.get(i));
	        	   }
	        	   else if(para instanceof String)
	        	   {
	        		   ps.setString(i + 2, (String)paras.get(i));
	        	   }
	        	   else
	        	   {
	        		   ps.setObject(i + 2, paras.get(i));
	        	   }
	            }
				return ps.execute();
			}
		}
		catch(Exception ex)
		{
			
		}
		return false;
	}
	
	public DBResult getResult(int i)
	{
		try{
			JdbcResult res = new JdbcResult();
			res.extractdata((ResultSet)ps.getObject(i));
			return res;
		}
		catch(Exception ex)
		{
			
		}
		return null;
	}
	
	public Object getObject(int i)
	{
		try{
			return ps.getObject(i);
		}
		catch(Exception ex)
		{
			
		}
		return null;
	}
	
	public String getString(int i)
	{
		try{
			return ps.getString(i);
		}
		catch(Exception ex)
		{
			
		}
		return null;
	}
	
	public java.sql.Date getDate(int i)
	{
		try{
			return ps.getDate(i);
		}
		catch(Exception ex)
		{
			
		}
		return null;
	}
	
	public int getInt(int i) throws Exception
	{
		return ps.getInt(i);
	}
	public long getLong(int i) throws Exception
	{
		return ps.getLong(i);
	}
	public short getShort(int i) throws Exception
	{
		return ps.getShort(i);
	}
	public double getDouble(int i) throws Exception
	{
		return ps.getDouble(i);
	}
	public float getFloat(int i) throws Exception
	{
		return ps.getFloat(i);
	}
}
