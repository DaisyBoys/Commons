package webtools.common.database;

import java.util.*;
import java.lang.reflect.*;

import org.apache.log4j.Logger;

public class JdbcAgentEx extends JdbcAgent {

	 private static Logger logger = Logger.getLogger(JdbcAgentEx.class);
	/**
	 * 
	 * */
	private Object getObjectValue(Object obj,String property)
	{
		Class clazz = obj.getClass();
		try{		
			Field f = clazz.getField(property);		
			return f.get(obj);
		}
		catch(Exception ex)
		{
			char chr = Character.toUpperCase(property.charAt(0));
			String func = "get" + String.valueOf(chr) + property.substring(1);
			try{
				Method m = clazz.getMethod(func, null);
				return m.invoke(obj, null);
			}
			catch(Exception sex)
			{
				//logger.error(sex.toString());
				return null;
			}	
		}
	}
	
	/**
	 * ���table_name Ϊ�գ��򷵻���������Ϊ����
	 * */
	private String real_table_name(Object obj,String table_name)
	{
		String className = obj.getClass().getName();
		int pt = className.lastIndexOf(".");
		if(pt != -1)
		{
			className = className.substring(pt + 1);
		}
		return (table_name != null && !table_name.equals(""))?table_name:className;
	}
	
	/**
	 * ����ݼ�����ָ���Ķ�����д���List������ÿ��Ԫ�ؾ���elem_class ָ������Ķ���ʵ��<br>
	 * ��¼��������Ӧ���ǰ����������������ͬ�ķ�ʽ����ӳ�䡣
	 * @author liyong
	 * @param res ��¼������
	 * @param elem_class ӳ���������
	 * @return List ���ذ�elem_classԪ�ص��б?
	 * */
	private List packData(DBResult res,Class elem_class) throws Exception
	{
		List<Object> ret = new ArrayList<Object>();
		for(int i = 0; i < res.getRowCount(); i++)
		{
			Object elem = elem_class.newInstance();
			for(int j = 0; j < res.getColCount(); j++)
			{
				ColumAttr colattr = res.getColumAttribute(j);
				try{
					String var_name = colattr.getColname();
					Field f = elem_class.getDeclaredField(var_name);
					String type = f.getType().getName();
					if(Modifier.isPublic(f.getModifiers()))
					{
						if(type.equals("boolean"))
						{
							f.setBoolean(elem, res.getBoolean(i, j));
						}
						else if(type.equals("int"))
						{
							f.setInt(elem, res.getInt(i, j));
						}
						else if(type.equals("float"))
						{
							f.setFloat(elem, res.getFloat(i, j));
						}
						else if(type.equals("double"))
						{
							f.setDouble(elem, res.getDouble(i, j));
						}
						else if(type.equals("long"))
						{
							f.setLong(elem, res.getLong(i, j));
						}
						else if(type.equals("short"))
						{
							f.setShort(elem, res.getShort(i, j));
						}
						else if(type.equals("byte"))
						{
							f.setByte(elem, res.getByte(i, j));
						}
						else if(type.equals("char"))
						{
							f.setChar(elem, res.getChar(i, j));
						}
						else if(type.equals("java.lang.String"))
						{
							f.set(elem, res.getString(i, j));
						}
						else{
							f.set(elem, res.getObject(i, j));
						}
					}
					else
					{
						char chr = Character.toUpperCase(var_name.charAt(0));
						String func = "set" + String.valueOf(chr) + var_name.substring(1);
						if(type.equals("boolean"))
						{
							Method mthd = elem_class.getMethod(func, boolean.class);
							if(mthd != null){
								mthd.invoke(elem, res.getBoolean(i, j));
							}
						}
						else if(type.equals("int"))
						{
							Method mthd = elem_class.getMethod(func, int.class);
							if(mthd != null){
								mthd.invoke(elem, res.getInt(i, j));
							}
						}
						else if(type.equals("float"))
						{
							Method mthd = elem_class.getMethod(func, float.class);
							if(mthd != null){
								mthd.invoke(elem, res.getFloat(i, j));
							}
						}
						else if(type.equals("double"))
						{
							Method mthd = elem_class.getMethod(func, double.class);
							if(mthd != null){
								mthd.invoke(elem, res.getDouble(i, j));
							}
						}
						else if(type.equals("long"))
						{
							Method mthd = elem_class.getMethod(func, long.class);
							if(mthd != null){
								mthd.invoke(elem, res.getLong(i, j));
							}
						}
						else if(type.equals("short"))
						{
							Method mthd = elem_class.getMethod(func, short.class);
							if(mthd != null){
								mthd.invoke(elem, res.getShort(i, j));
							}
						}
						else if(type.equals("byte"))
						{
							Method mthd = elem_class.getMethod(func, byte.class);
							if(mthd != null){
								mthd.invoke(elem, res.getByte(i, j));
							}
						}
						else if(type.equals("char"))
						{
							Method mthd = elem_class.getMethod(func, char.class);
							if(mthd != null){
								mthd.invoke(elem, res.getChar(i, j));
							}
						}
						else if(type.equals("java.lang.String"))
						{
							Method mthd = elem_class.getMethod(func, String.class);
							if(mthd != null){
								mthd.invoke(elem, res.getString(i, j));
							}
						}
						else{							
							Method mthd = elem_class.getMethod(func, f.getType());
							if(mthd != null){
								mthd.invoke(elem, res.getObject(i, j));
							}
						}
					}
				}
				catch(Exception ex)
				{
					//logger.error(ex.toString());
				}
			}
			ret.add(elem);
		}
		return ret;
	}
	
	/**
	 * ��ѯ��¼��������ָ������ʵ��Ķ��󼯺ϡ�
	 * @param sql        ��ѯ���
	 * @param elem_class ��������
	 * @return List ��ѯ�õ��Ķ��󼯺͡�
	 * */
	public List query(String sql,Class elem_class)
	{
		try
		{
			DBResult res = query(sql);
			if(res != null)
			{
				return packData(res,elem_class);
			}
		}
		catch(Exception ex)
		{
			logger.error("query error:" + ex.toString());
		}
		return null;
	}

	/**
	 * ��ҳ��ѯ��¼��������ָ������ʵ��Ķ��󼯺ϡ�
	 * @param sql   ��ѯ��䡣
	 * @param start ��ʼ���� ���ڵ���0
	 * @param end   ��ѯ������� > start��
	 * @param elem_class ��������
	 * @return List ��ѯ�õ��Ķ��󼯺͡�
	 * */
	public List query(String sql,int start,int end,Class elem_class)
	{
		try
		{
			DBResult res = query(sql,start,end);
			if(res != null)
			{
				return packData(res,elem_class);
			}
		}
		catch(Exception ex)
		{
			logger.error("query error:" + ex.toString());
		}
		return null;
	}
	
	/**
	 * ��ָ�����л�ȡָ������ֵ�ü�¼������¼��Ϊ������з��ء�
	 * 
	 * @param table �����
	 * @param pkval ����ֵ�������������صĽ���������ֵΪpkval.
	 * @param cls   ���صĶ������á�
	 * @return      ���ض���ʵ��
	 * */
	public Object getObject(String table,String pkval,Class cls)
	{
		try{
			String sql = sql_by_tablepk(table,pkval);
			List res = query(sql,cls);
			if(res != null && res.size() > 0)
			{
				return res.get(0);
			}
		}
		catch(Exception ex)
		{
			logger.error("getObject error:" + ex.toString());
		}
		return null;
	}
	
	/**
	 * ��ѯ�õ���¼���ܳ��ȡ�
	 * @param  sql ��ͨ�Ĳ�ѯ��䣨�����Ǵ���count()����䣩��
	 * @return int ��ݵ��ܳ���,�����?����-1��
	 * */
	public int getRecCount(String sql) 
	{
		try{
			String modul = sql.toLowerCase();
			String count_sql = "select count(*) ";
			int pt = modul.indexOf("from");
			count_sql += sql.substring(pt);
			return queryCount(count_sql);
		}
		catch(Exception ex)
		{
			logger.error("getRecCount error:" + ex.toString());
		}
		return -1;
	}
	
	/**
	 * ɾ��ָ����¼
	 * @param tablename Ҫɾ��ı���
	 * @param colname   �����ֶ�
	 * @param colval    ����ֵ
	 * @return   ɾ���Ƿ�ɹ���
	 * */
	public boolean delete(String tablename,String colname,String colval)
	{
		String sql = "delete from " + tablename + " where " + colname + "=?";
		List<Object> parlist = new ArrayList<Object>();
		parlist.add(colval);
		return update(sql,parlist) >= 0;
	}
	
	/**
	 * ȡ����������ֶ����
	 * @param tablename ����
	 * @return �����ֶ���
	 * */
	public String getPrimaryKeyName(String tablename)
	{
		try{
			DBResult res = list_primarykey(tablename);
			if(res != null && res.getRowCount() > 0)
			{
				return res.getString(0, "COLUMN_NAME");
			}
		}
		catch(Exception ex)
		{
			logger.error("getPrimaryKeyName error:" + ex.toString());
		}
		return null;
	}
	
	/**
	 * ���sql��ȡ�ض���ֵ
	 * @param sql  sql���
	 * @return �����ض���ֵ
	 * */
	public String getKeyString(String sql)
	{
		try
		{
			DBResult res = query(sql);
			if(res != null)
			{
				return res.getString(0, 0);
			}
		}
		catch(Exception ex)
		{
			logger.error("getKeyString error:" + ex.toString());
		}
		return null;
	}
	
	/**
	 * ��һ��Hashtable�е�����ֵ�����������Ӧ�ı��С�
	 * @param elem Ҫ������Hashtable����
	 * @param table_name �����ƣ�������Ϊ����Ĭ�Ͻ������������Ϊ����
	 * @return boolean �����Ƿ�ɹ�
	 * */
	public boolean insertHashtable(Map<String,Object> elem,String table_name)
	{
		DBResult res = list_columns(table_name);
		int count = res.getRowCount();
		try{
			String cols = "";
			String vals = "";
			List<Object> parlist = new ArrayList<Object>();
			for(int i = 0; i < count ; i++)
			{
				String colname = res.getString(i, "COLUMN_NAME");
				Object val = elem.get(colname);
				if(val != null && !val.equals(""))
				{
					cols += "," + colname;
					vals += ",?";
					parlist.add(val);
				}
			}
			if(!cols.equals("")) cols = cols.substring(1);
			if(!vals.equals("")) vals = vals.substring(1);
			String sql = "insert into " + table_name + "(" + cols + ") values(" + vals + ")";
			return update(sql, parlist) > 0;
		}
		catch(Exception ex)
		{
			logger.error("insertHashtable error:" + ex.toString());
			return false;
		}
	}
	
	/**
	 * ��һ�������е�����ֵ�����������Ӧ�ı��С�
	 * @param obj Ҫ�����Ķ���
	 * @param table_name �����ƣ�������Ϊ����Ĭ�Ͻ������������Ϊ����
	 * @return boolean �����Ƿ�ɹ�
	 * */
	public boolean insert(Object obj,String table_name)
	{	
		String table = real_table_name(obj,table_name);
		DBResult res = list_columns(table);
		int count = res.getRowCount();
		try{
			String cols = "";
			String vals = "";
			List<Object> parlist = new ArrayList<Object>();
			for(int i = 0; i < count ; i++)
			{
				String colname = res.getString(i, "COLUMN_NAME");
				Object val = getObjectValue(obj,colname);
				if(val != null && !val.equals(""))
				{
					cols += "," + colname;
					vals += ",?";
					parlist.add(val);
				}
			}
			if(!cols.equals("")) cols = cols.substring(1);
			if(!vals.equals("")) vals = vals.substring(1);
			String sql = "insert into " + table + "(" + cols + ") values(" + vals + ")";
			return update(sql, parlist) > 0;
		}
		catch(Exception ex)
		{
			logger.error("insert Object to table error:" + ex.toString());
			return false;
		}
	}
	
	/**
	 * �ж�ĳ�ֶ��Ƿ������ͬ��ֵ��paras�ﰴ���ֶ�����������ͶԱ�ֵ����ֵ���Ա�ֵͨ���colname�Ա��ֶ�
	 * ���бȽϣ��ж��Ƿ�����ͬ��ֵ����(��¼��������?���бȽϣ�ͨ�������������)���������ͬ��ֵ���ڣ���
	 * �����棬���򷵻�false
	 * @param paras �����ֶ�����������ͶԱ�ֵ����ֵ
	 * @param tablename �����
	 * @param colname   �����ֶ����
	 * @return �������ͬ��ֵ���ڣ��򷵻��棬���򷵻�false
	 * */
	public boolean isExist(Map<String,String> paras,String tablename,String colname)
	{
		String pk = this.getPrimaryKeyName(tablename);
		String pkval = paras.get(pk);	
		String colval = paras.get(colname);
		return this.isExist(colname,colval, tablename, pk + "!='" + pkval + "'");
	}
	
	/**
	 * �жϱ��е�ĳ�ֶ�ֵ�Ƿ��Ѿ�����
	 * @param varname �ֶ���
	 * @param value  �ֶ�ֵ
	 * @param table  Ҫ�жϵı���
	 * @param extterm ��չ��������
	 * */
	public boolean isExist(String varname,String value,String table,String extterm)
	{
		if(value != null && !value.equals(""))
		{
			String sql = "select count(*) from " + table + " where " + varname + " = '" + value + "'";
			if(extterm != null)
			{
				sql += " and " + extterm;
			}
			if(new JdbcAgent().queryCount(sql) > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ��һ��Hashtable��ֵ�޸ı��ж�Ӧ��¼
	 * @param elem Hashtable�б���ļ�¼
	 * @param table_name �����ƣ�������Ϊ����Ĭ�Ͻ������������Ϊ����
	 * @return boolean �޸��Ƿ�ɹ�
	 * */
	public boolean updateHashtable(Map<String,String> elem,String table_name)
	{
		try{
			String pk = list_primarykey(table_name).getString(0, 3);
			String pkval = elem.get(pk);
			if(pkval != null)
			{
				String sql = "select * from " + table_name + " where " + pk + "=?";
				List<Object> paras = new ArrayList<Object>();
				paras.add(pkval);
				DBResult res = query(sql,paras);
				if(res != null && res.getRowCount() > 0)
				{
					String sets = "";
					String where = pk + "=?";
					Object pkobj = pkval;
					
					List<Object> parlist = new ArrayList<Object>();
					for(int i = 0; i < res.getColCount() ; i++)
					{
						String colname = res.getColumAttribute(i).getColname();
						String oldval = res.getString(0, i);
						
						Object val = elem.get(colname);
						if(val != null && !val.equals(oldval))
						{			
							sets += "," + colname + "=?";
							parlist.add(val);
						}
					}
					if(pkobj != null) parlist.add(pkobj);
					if(!sets.equals("")){
						sets = sets.substring(1);
						sql = "update " + table_name + " set " + sets + " where " + where;
						return update(sql, parlist) > 0;
					}
					return true;
				}
			}	
		}
		catch(Exception ex)
		{
			logger.error("updateHashtable error:" + ex.toString());
		}
		return false;
	}
	
	/**
	 * ��һ�����������ֵ�޸ı��ж�Ӧ��¼
	 * @param obj Ҫ�޸ı�Ķ���
	 * @param table_name �����ƣ�������Ϊ����Ĭ�Ͻ������������Ϊ����
	 * @return boolean �޸��Ƿ�ɹ�
	 * */
	public boolean update(Object obj,String table_name)
	{
		String table = real_table_name(obj,table_name);
		DBResult res = list_columns(table);
		int count = res.getRowCount();
		try{
			String sets = "";
			String where = "";
			Object pkobj = null;
			String pk = list_primarykey(table).getString(0, 3);
			List<Object> parlist = new ArrayList<Object>();
			for(int i = 0; i < count ; i++)
			{
				String colname = res.getString(i, "COLUMN_NAME");
				Object val = getObjectValue(obj,colname);
				//if(val != null && !val.equals(""))
				/**
				 ** �˴�ȥ��!val.equals("")��Ϊ�˱�֤�ܹ�����ֵ���ֶ���Ϊ���ַ�Ҳ����
				 ** ����������Ϊnullʱ�������ֶβ��޸ģ��������Ϊ""ʱ�ֶ���ɿ��ַ�
				 **/
				if(val != null) 
				{
					if(colname.equalsIgnoreCase(pk))
					{
						where = pk + "=?";
						pkobj = val;
					}
					else
					{
						sets += "," + colname + "=?";
						parlist.add(val);
					}
				}
			}
			if(pkobj != null) parlist.add(pkobj);
			if(!sets.equals("")) sets = sets.substring(1);
			String sql = "update " + table + " set " + sets + " where " + where;
			return update(sql, parlist) > 0;
		}
		catch(Exception ex)
		{
			logger.error("modified object to table error:" + ex.toString());
			return false;
		}
	}
	
	/**
	 * ����ݿ��н�һ�������ָ���������ֵ�Ĳ�ѯ��䡣
	 * @param tablename ����
	 * @param pk_value  ����ֵ
	 * @return ���ر�׼sql��ѯ��䡣
	 * */
	public String sql_by_tablepk(String tablename,String pk_value)
	{
		try{
			String sql = "select";
			String flag = " ";
			DBResult res = list_columns(tablename);
			for(int i = 0; res != null && i < res.getRowCount(); i++)
			{
				String colname = res.getString(i, "COLUMN_NAME");
				sql += flag + colname;
				flag = ",";
			}
			sql += " from " + tablename;
			
			//��ʼƴ����������
			String pkname = getPrimaryKeyName(tablename);
			sql += " where " + pkname + "='" + pk_value + "'";
			return sql;
		}
		catch(Exception ex)
		{
			logger.error("sql_by_tablepk error:" + ex.toString());
			return "";
		}
	}
	
	/**
	 * ����ݿ��н�һ�������ָ�����������Ĳ�ѯ��䡣
	 * @param tablename ����
	 * @param term      ��չ������
	 * @return ���ر�׼sql��ѯ��䡣  
	 * */
	public String sql_by_tableterm(String tablename,String term,String extend)
	{
		try{
			String sql = "select";
			String flag = " ";
			DBResult res = list_columns(tablename);
			for(int i = 0; res != null && i < res.getRowCount(); i++)
			{
				String colname = res.getString(i, "COLUMN_NAME");
				sql += flag + colname;
				flag = ",";
			}
			sql += " from " + tablename;
			if(term != null && !term.equals(""))
			{
				sql += " where " + term;
			}
			if(extend != null && !extend.equals(""))
			{
				sql += " " + extend;
			}
			return sql;
		}
		catch(Exception ex)
		{
			logger.error("sql_by_tableterm error:" + ex.toString());
			return "";
		}
	}
}
