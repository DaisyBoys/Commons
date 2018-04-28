package webtools.common.database;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-12-7
 * Time: 11:51:30
 * To change this template use File | Settings | File Templates.
 */
public interface DBResult {
	
	public ColumAttr getColumAttribute(final int col);
	    public int getRowCount(); 
    
        public int getColCount();
    
        public String getString(final int row,final int col) throws Exception;
        public String getString(final int row,final String colname) throws Exception;
    
        public int    getInt(int row,int col) throws Exception;
        public int    getInt(int row,String colname) throws Exception;
    
        public double getDouble(int row,int col) throws Exception;
    
        public double getDouble(int row,String colname) throws Exception;
    
        public float getFloat(int row,int col) throws Exception;
    
        public float getFloat(int row,String colname) throws Exception;
    
        public boolean getBoolean(int row,int col) throws Exception;
        public boolean getBoolean(int row,String colname) throws Exception;
    
        public char    getChar(int row,int col) throws Exception;
        public char    getChar(int row,String colname) throws Exception;
        public byte    getByte(int row,int col) throws Exception;
        public byte    getByte(int row,String colname) throws Exception;
    
        public long    getLong(int row,int col) throws Exception;
    
        public long    getLong(int row,String colname) throws Exception;
    
        public short   getShort(int row,int col) throws Exception;
    
        public short   getShort(int row,String colname) throws Exception;
    
        public Object getObject(int row,int col) throws Exception;
    
        public Object getObject(int row,String colname) throws Exception;
    
        public List getRow(int row) throws Exception;
}
