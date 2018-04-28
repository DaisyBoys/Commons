package webtools.common.database;

import java.util.*;

public final class DataResult implements DBResult 
{
	/*
	@SuppressWarnings("unchecked")
	protected  void finalize() throws Throwable{             
        super.finalize();
        try {
        	if (data!=null){
        		for (int i=0;i<data.size();i++){
        			List lst=data.get(i);
        			if (lst!=null){
        				for (int k=0;k<lst.size();k++){
        					@SuppressWarnings("unused")
							Object obj=lst.get(k);
        					obj=null;
        				}
        				lst.clear();
        				lst=null;
        			}
        		}
        		data.clear();
        		data=null;
        	}
        	if (col_attrs!=null){
        		col_attrs=null;
        		
        		
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
      
          
    } */
    int col_count = 0;
    ColumAttr[]  col_attrs = null;
    List<List> data = new ArrayList<List>();

    public DataResult()
    {
    	
    }
    
    public DataResult(final int colcount)
    {
    	col_count = colcount;
        col_attrs = new ColumAttr[col_count];
    }
    
    /**
	 * �õ��ֶ�������Ϣ

	 * @param col ָ���д�0 ��ʼ��
	 * @return ColumAttr �ֶε���Ϣ����
	 * */
    public final ColumAttr getColumAttribute(final int col)
    {
   	 try{
	    	 if(col >=0 && col < col_count)
	    	 {
	    		 return col_attrs[col];
	    	 }
   	 }
   	 catch(Exception ex)
   	 {
   		 
   	 }
   	 return null;
    }
    
    /**
	 * �õ���¼��������

	 * @return int ��¼��������
	 * */
    public int getRowCount(){
        return data.size();
    }

    /**
	 * �õ���¼��������

	 * @return int ��¼��������
	 * */
    public int getColCount(){
        return col_count;
    }

    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return String ��¼ֵ��
	 * */
    public final String getString(final int row,final int col) throws Exception{
    	final Object val = getObject(row,col);
        if(val == null) return "";
        return val.toString();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param colname �����
	 * @return String ��¼ֵ��
	 * */
    public final String getString(final int row,final String colname) throws Exception{
    	final Object val = getObject(row,colname);
        if(val == null) return "";
        return val.toString();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return int ��¼ֵ��
	 * */
    public int    getInt(final int row,final int col) throws Exception{
        Integer val = (Integer)getObject(row,col);
        if(val == null) return 0;
        return val.intValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param colname �����
	 * @return int ��¼ֵ��
	 * */
    public int    getInt(final int row,final String colname) throws Exception{
        Integer val = (Integer)getObject(row,colname);
        if(val == null) return 0;
        return val.intValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return double ��¼ֵ��
	 * */
    public double getDouble(int row,int col) throws Exception
    {
   	 Double val = (Double)getObject(row,col);
   	 if(val == null) return 0.0;
   	 return val.doubleValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param colname �����
	 * @return double ��¼ֵ��
	 * */
    public double getDouble(int row,String colname) throws Exception
    {
   	 Double val = (Double)getObject(row,colname);
   	 if(val == null) return 0.0;
   	 return val.doubleValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return float ��¼ֵ��
	 * */
    public float getFloat(int row,int col) throws Exception
    {
   	 Float val = (Float)getObject(row,col);
   	 if(val == null) return 0;
   	 return val.floatValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param colname �����
	 * @return float ��¼ֵ��
	 * */
    public float getFloat(int row,String colname) throws Exception
    {
   	 Float val = (Float)getObject(row,colname);
   	 if(val == null) return 0;
   	 return val.floatValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return boolean ��¼ֵ��
	 * */
    public boolean getBoolean(int row,int col) throws Exception
    {
   	 Boolean val = (Boolean)getObject(row,col);
   	 if(val == null) return false;
   	 return val.booleanValue();
    }
    
    /**
 	 * �õ���¼����ĳ��ĳ��ֵ��
 
 	 * @param row ������
 	 * @param colname �����
 	 * @return boolean ��¼ֵ��
 	 * */
    public boolean getBoolean(int row,String colname) throws Exception
    {
   	 Boolean val = (Boolean)getObject(row,colname);
   	 if(val == null) return false;
   	 return val.booleanValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return char ��¼ֵ��
	 * */
    public char    getChar(int row,int col) throws Exception
    {
   	 Character val = (Character)getObject(row,col);
   	 if(val == null) return ' ';
   	 return val.charValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param colname �����
	 * @return char ��¼ֵ��
	 * */
    public char    getChar(int row,String colname) throws Exception
    {
   	 Character val = (Character)getObject(row,colname);
   	 if(val == null) return ' ';
   	 return val.charValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return byte ��¼ֵ��
	 * */
    public byte    getByte(int row,int col) throws Exception
    {
   	 Byte val = (Byte)getObject(row,col);
   	 if(val == null) return ' ';
   	 return val.byteValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param colname �����
	 * @return byte ��¼ֵ��
	 * */
    public byte    getByte(int row,String colname) throws Exception
    {
   	 Byte val = (Byte)getObject(row,colname);
   	 if(val == null) return ' ';
   	 return val.byteValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return long ��¼ֵ��
	 * */
    public long    getLong(int row,int col) throws Exception
    {
   	 Long val = (Long)getObject(row,col);
   	 if(val == null) return 0;
   	 return val.longValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param colname �����
	 * @return long ��¼ֵ��
	 * */
    public long    getLong(int row,String colname) throws Exception
    {
   	 Long val = (Long)getObject(row,colname);
   	 if(val == null) return 0;
   	 return val.longValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return short ��¼ֵ��
	 * */
    public short   getShort(int row,int col) throws Exception
    {
   	 Short val = (Short)getObject(row,col);
   	 if(val == null) return 0;
   	 return val.shortValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param colname �����
	 * @return short ��¼ֵ��
	 * */
    public short   getShort(int row,String colname) throws Exception
    {
   	 Short val = (Short)getObject(row,colname);
   	 if(val == null) return 0;
   	 return val.shortValue();
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param col ������
	 * @return Object ��¼ֵ��
	 * */
    public Object getObject(int row,int col) throws Exception{
        if(row < 0 || row >= getRowCount()) return null;
        if(col < 0 || col >= col_count) return null;
        return getRow(row).get(col);
    }
    
    /**
	 * �õ���¼����ĳ��ĳ��ֵ��

	 * @param row ������
	 * @param colname �����
	 * @return Object ��¼ֵ��
	 * */
    public Object getObject(int row,String colname) throws Exception{
        if(row < 0 || row >= getRowCount()) return null;
        int col = -1;
        for(int i = 0; i < col_count; i++){
            if(col_attrs[i].getColname().equalsIgnoreCase(colname)){
                col = i;
                break;
            }
        }
        if(col < 0 || col >= col_count) return null;
        return getRow(row).get(col);
    }
    
    /**
	 * �õ�ĳ�е�ֵ���ϡ�
	 * @param row ������
	 * @return List �м�¼ֵ���ϡ�
	 * */
    public List getRow(int row) throws Exception{
        return (List)data.get(row);
    }
    
    public void addRow(List row)
    {
    	data.add(row);
    }

    public void setColumnCount(int colcount)
    {
    	col_count = colcount;
        col_attrs = new ColumAttr[col_count];
    }
    
    public void setColumnAttribute(int colidx,ColumAttr attr)
    {
    	if(colidx >= 0 && colidx < col_count)
    	{
    		col_attrs[colidx] = attr;
    	}
    }
}
