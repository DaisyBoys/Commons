package webtools.common.databaseex;

import webtools.common.URL.RequestOper;
import webtools.common.URL.URLConnection;
import webtools.common.database.ColumAttr;
import webtools.common.database.DBResult;
import webtools.common.database.JdbcAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @deprecated
 */
public class DBOper {
    String base_query;
    int total_rec = -1;
    JdbcAgent agent = new JdbcAgent();


    public JdbcAgent getAgent() {
        return agent;
    }


    public String getBase_query() {
        return base_query;
    }


    public void parse_query(String sql, URLConnection con) {
        RequestOper creator = new RequestOper();
        this.base_query = creator.query_create(sql, con);
        this.total_rec = -1;
    }


    public void parse_table_query(String tablename, String pk_value) {
        try {
            base_query = "select";
            String flag = " ";
            DBResult res = agent.list_columns(tablename);
            for (int i = 0; res != null && i < res.getRowCount(); i++) {
                String colname = res.getString(i, "COLUMN_NAME");
                base_query += flag + colname;
                flag = ",";
            }
            base_query += " from " + tablename;


            String pkname = getPrimaryKeyName(tablename);
            base_query += " where " + pkname + "='" + pk_value + "'";
        } catch (Exception ex) {
            this.base_query = "";
        }
        this.total_rec = -1;
    }


    public void parse_table_query1(String tablename, String term) {
        try {
            base_query = "select";
            String flag = " ";
            DBResult res = agent.list_columns(tablename);
            for (int i = 0; res != null && i < res.getRowCount(); i++) {
                String colname = res.getString(i, "COLUMN_NAME");
                base_query += flag + colname;
                flag = ",";
            }
            base_query += " from " + tablename;
            if (term != null && !term.equals("")) {
                base_query += " where " + term;
            }
        } catch (Exception ex) {
            this.base_query = "";
        }
        this.total_rec = -1;
    }


    public String getPrimaryKeyName(String tablename) {
        try {
            DBResult res = agent.list_primarykey(tablename);
            if (res != null && res.getRowCount() > 0) {
                return res.getString(0, "COLUMN_NAME");
            }
        } catch (Exception ex) {

        }
        return null;
    }


    public void setBase_query(String base_query) {
        this.base_query = base_query;
        this.total_rec = -1;
    }


    public int query_count() {
        if (total_rec < 0)
            caculate_total_rec();
        return total_rec;
    }


    public DBResult query() {
        return agent.query(base_query);
    }


    public DBResult query(int start, int end) {
        return agent.query(base_query, start, end);
    }


    private ArrayList packData(DBResult res, Class elem_class) throws Exception {
        ArrayList ret = new ArrayList();
        int row_count = res.getRowCount();
        int col_count = res.getColCount();
        for (int i = 0; i < res.getRowCount(); i++) {
            Object elem = elem_class.newInstance();
            for (int j = 0; j < res.getColCount(); j++) {
                ColumAttr colattr = res.getColumAttribute(j);
                try {
                    String var_name = colattr.getColname();
                    Field f = elem_class.getDeclaredField(var_name);
                    String type = f.getType().getName();
                    if (Modifier.isPublic(f.getModifiers())) {
                        if (type.equals("boolean")) {
                            f.setBoolean(elem, res.getBoolean(i, j));
                        } else if (type.equals("int")) {
                            f.setInt(elem, res.getInt(i, j));
                        } else if (type.equals("float")) {
                            f.setFloat(elem, res.getFloat(i, j));
                        } else if (type.equals("double")) {
                            f.setDouble(elem, res.getDouble(i, j));
                        } else if (type.equals("long")) {
                            f.setLong(elem, res.getLong(i, j));
                        } else if (type.equals("short")) {
                            f.setShort(elem, res.getShort(i, j));
                        } else if (type.equals("byte")) {
                            f.setByte(elem, res.getByte(i, j));
                        } else if (type.equals("char")) {
                            f.setChar(elem, res.getChar(i, j));
                        } else if (type.equals("java.lang.String")) {
                            f.set(elem, res.getString(i, j));
                        } else {
                            f.set(elem, res.getObject(i, j));
                        }
                    } else {
                        char chr = Character.toUpperCase(var_name.charAt(0));
                        String func = "set" + String.valueOf(chr) + var_name.substring(1);
                        if (type.equals("boolean")) {
                            Method mthd = elem_class.getMethod(func, boolean.class);
                            if (mthd != null) {
                                mthd.invoke(elem, res.getBoolean(i, j));
                            }
                        } else if (type.equals("int")) {
                            Method mthd = elem_class.getMethod(func, int.class);
                            if (mthd != null) {
                                mthd.invoke(elem, res.getInt(i, j));
                            }
                        } else if (type.equals("float")) {
                            Method mthd = elem_class.getMethod(func, float.class);
                            if (mthd != null) {
                                mthd.invoke(elem, res.getFloat(i, j));
                            }
                        } else if (type.equals("double")) {
                            Method mthd = elem_class.getMethod(func, double.class);
                            if (mthd != null) {
                                mthd.invoke(elem, res.getDouble(i, j));
                            }
                        } else if (type.equals("long")) {
                            Method mthd = elem_class.getMethod(func, long.class);
                            if (mthd != null) {
                                mthd.invoke(elem, res.getLong(i, j));
                            }
                        } else if (type.equals("short")) {
                            Method mthd = elem_class.getMethod(func, short.class);
                            if (mthd != null) {
                                mthd.invoke(elem, res.getShort(i, j));
                            }
                        } else if (type.equals("byte")) {
                            Method mthd = elem_class.getMethod(func, byte.class);
                            if (mthd != null) {
                                mthd.invoke(elem, res.getByte(i, j));
                            }
                        } else if (type.equals("char")) {
                            Method mthd = elem_class.getMethod(func, char.class);
                            if (mthd != null) {
                                mthd.invoke(elem, res.getChar(i, j));
                            }
                        } else if (type.equals("java.lang.String")) {
                            Method mthd = elem_class.getMethod(func, String.class);
                            if (mthd != null) {
                                mthd.invoke(elem, res.getString(i, j));
                            }
                        } else {
                            Method mthd = elem_class.getMethod(func, f.getType());
                            if (mthd != null) {
                                mthd.invoke(elem, res.getObject(i, j));
                            }
                        }
                    }
                } catch (Exception ex) {

                }
            }
            ret.add(elem);
        }
        return ret;
    }


    public ArrayList query(Class elem_class) {
        try {
            DBResult res = agent.query(base_query);
            if (res != null) {
                return packData(res, elem_class);
            }
        } catch (Exception ex) {
            System.out.println("error:" + ex.getMessage());
        }
        return null;
    }


    public ArrayList query(int start, int end, Class elem_class) {
        try {
            DBResult res = agent.query(base_query, start, end);
            if (res != null) {
                return packData(res, elem_class);
            }
        } catch (Exception ex) {
            System.out.println("error:" + ex.getMessage());
        }
        return null;
    }

    public boolean isExist(Hashtable paras, String tablename, String colname) {
        String pk = this.getPrimaryKeyName(tablename);
        String pkval = (String) paras.get(pk);
        String colval = (String) paras.get(colname);
        return this.isExist(colval, colname, tablename, pk + "!='" + pkval + "'");
    }

    public boolean isExist(String value, String varname, String table, String extterm) {
        if (value != null && !value.equals("")) {
            String sql = "select count(*) from " + table + " where " + varname + " = '" + value + "'";
            if (extterm != null) {
                sql += " and " + extterm;
            }
            if (new JdbcAgent().queryCount(sql) > 0) {
                return true;
            }
        }
        return false;
    }


    private void caculate_total_rec() {
        try {
            String modul = base_query.toLowerCase();
            String count_sql = "select count(*) ";
            int pt = modul.indexOf("from");
            count_sql += base_query.substring(pt);
            total_rec = agent.queryCount(count_sql);
        } catch (Exception ex) {

        }
    }

    public boolean beginTransaction() {
        return agent.beginTransaction();
    }


    public void commit() {
        agent.commit();
    }


    public void rollback() {
        agent.Rollback();
    }


    private String real_table_name(Object obj, String table_name) {
        String className = obj.getClass().getName();
        int pt = className.lastIndexOf(".");
        if (pt != -1) {
            className = className.substring(pt + 1);
        }
        return (table_name != null && !table_name.equals("")) ? table_name : className;
    }


    private Object getObjectValue(Object obj, String property) {
        Object ret = null;
        Class clazz = obj.getClass();
        try {
            Field f = clazz.getField(property);
            return f.get(obj);
        } catch (Exception ex) {
            char chr = Character.toUpperCase(property.charAt(0));
            String func = "get" + String.valueOf(chr) + property.substring(1);
            try {
                Method m = clazz.getMethod(func, null);
                return m.invoke(obj, null);
            } catch (Exception sex) {
                return null;
            }
        }
    }

    public boolean insert_table2(Hashtable elem, String table_name) {
        DBResult res = agent.list_columns(table_name);
        int count = res.getRowCount();
        try {
            String cols = "";
            String vals = "";
            ArrayList parlist = new ArrayList();
            for (int i = 0; i < count; i++) {
                String colname = res.getString(i, "COLUMN_NAME");
                //String val = con.getSafeParameter(colname, "");
                Object val = elem.get(colname);
                if (val != null && !val.equals("")) {
                    cols += "," + colname;
                    vals += ",?";
                    parlist.add(val);
                }
            }
            if (!cols.equals("")) cols = cols.substring(1);
            if (!vals.equals("")) vals = vals.substring(1);
            String sql = "insert into " + table_name + "(" + cols + ") values(" + vals + ")";
            return agent.update(sql, parlist) > 0;
        } catch (Exception ex) {
            return false;
        }
    }


    public boolean insert_table(Object obj, String table_name) {
        String table = real_table_name(obj, table_name);
        //JdbcAgent agent = new JdbcAgent();
        DBResult res = agent.list_columns(table);
        int count = res.getRowCount();
        try {
            String cols = "";
            String vals = "";
            ArrayList parlist = new ArrayList();
            for (int i = 0; i < count; i++) {
                String colname = res.getString(i, "COLUMN_NAME");
                Object val = getObjectValue(obj, colname);
                if (val != null && !val.equals("")) {
                    cols += "," + colname;
                    vals += ",?";
                    parlist.add(val);
                }
            }
            if (!cols.equals("")) cols = cols.substring(1);
            if (!vals.equals("")) vals = vals.substring(1);
            String sql = "insert into " + table + "(" + cols + ") values(" + vals + ")";
            return agent.update(sql, parlist) > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean delete_table(String tablename, String colname, String colval) {
        String sql = "delete from " + tablename + " where " + colname + "=?";
        ArrayList parlist = new ArrayList();
        parlist.add(colval);
        return agent.update(sql, parlist) >= 0;
    }

	/*
	public boolean update_table2(Hashtable elem,String table_name)
	{
		DBResult res = agent.list_columns(table_name);
		int count = res.getRowCount();
		try{
			String sets = "";
			String where = "";
			Object pkobj = null;
			String pk = agent.list_primarykey(table_name).getString(0, 3);
			ArrayList parlist = new ArrayList();
			for(int i = 0; i < count ; i++)
			{
				String colname = res.getString(i, "COLUMN_NAME");
				//String val = con.getSafeParameter(colname,"");
				Object val = elem.get(colname);
				if(val != null && !val.equals(""))
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
			String sql = "update " + table_name + " set " + sets + " where " + where;
			return agent.update(sql, parlist) > 0;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	*/

    public boolean update_table2(Hashtable elem, String table_name) {
        try {
            String pk = agent.list_primarykey(table_name).getString(0, 3);
            String pkval = (String) elem.get(pk);
            if (pkval != null) {
                String sql = "select * from " + table_name + " where " + pk + "=?";
                ArrayList paras = new ArrayList();
                paras.add(pkval);
                DBResult res = agent.query(sql, paras);
                if (res != null && res.getRowCount() > 0) {
                    String sets = "";
                    String where = pk + "=?";
                    Object pkobj = pkval;

                    ArrayList parlist = new ArrayList();
                    for (int i = 0; i < res.getColCount(); i++) {
                        String colname = res.getColumAttribute(i).getColname();
                        String oldval = res.getString(0, i);

                        Object val = elem.get(colname);
                        if (val != null && !val.equals(oldval)) {
                            sets += "," + colname + "=?";
                            parlist.add(val);
                        }
                    }
                    if (pkobj != null) parlist.add(pkobj);
                    if (!sets.equals("")) {
                        sets = sets.substring(1);
                        sql = "update " + table_name + " set " + sets + " where " + where;
                        return agent.update(sql, parlist) > 0;
                    }
                    return true;
                }
            }
        } catch (Exception ex) {

        }
        return false;
    }

    public boolean update_table(Object obj, String table_name) {
        String table = real_table_name(obj, table_name);
        //JdbcAgent agent = new JdbcAgent();
        DBResult res = agent.list_columns(table);
        int count = res.getRowCount();
        try {
            String sets = "";
            String where = "";
            Object pkobj = null;
            String pk = agent.list_primarykey(table).getString(0, 3);
            ArrayList parlist = new ArrayList();
            for (int i = 0; i < count; i++) {
                String colname = res.getString(i, "COLUMN_NAME");
                Object val = getObjectValue(obj, colname);
                if (val != null && !val.equals("")) {
                    if (colname.equalsIgnoreCase(pk)) {
                        where = pk + "=?";
                        pkobj = val;
                    } else {
                        sets += "," + colname + "=?";
                        parlist.add(val);
                    }
                }
            }
            if (pkobj != null) parlist.add(pkobj);
            if (!sets.equals("")) sets = sets.substring(1);
            String sql = "update " + table + " set " + sets + " where " + where;
            return agent.update(sql, parlist) > 0;
        } catch (Exception ex) {
            return false;
        }
    }
}
