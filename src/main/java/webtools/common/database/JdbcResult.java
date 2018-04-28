package webtools.common.database;

import java.util.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: kfly
 * Date: 2006-12-7
 * Time: 11:26:19
 * To change this template use File | Settings | File Templates.
 */
public final class JdbcResult implements DBResult{
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
        		/*
        		for (int i=0;i<col_attrs.length;i++){
        			ColumAttr pColumAttr=col_attrs[i];
        			pColumAttr=null;
        			
        		}
        		*/
        		
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
      
          
    } 
     int col_count = 0;
     ColumAttr[]  col_attrs = null;
     List<List> data = new ArrayList<List>();

     /**
 	 * 
 	 * @param col ָ���д�0 ��ʼ��
 	 * @return ColumAttr �ֶε���Ϣ����
 	 * */
     public ColumAttr getColumAttribute(int col)
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
     public String getString(int row,int col) throws Exception{
         Object val = getObject(row,col);
         if(val == null) return "";
         return DBManager.getCharset().code1_2_code2(val.toString());
     }
     
     /**
 	 * �õ���¼����ĳ��ĳ��ֵ��
 	 * @param row ������
 	 * @param colname �����
 	 * @return String ��¼ֵ��
 	 * */
     public String getString(int row,String colname) throws Exception{
         Object val = getObject(row,colname);
         if(val == null) return "";
         return DBManager.getCharset().code1_2_code2(val.toString());
     }
     
     /**
 	 * �õ���¼����ĳ��ĳ��ֵ��
 	 * @param row ������
 	 * @param col ������
 	 * @return int ��¼ֵ��
 	 * */
     public int    getInt(int row,int col) throws Exception{
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
     public int    getInt(int row,String colname) throws Exception{
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
     
     public void extractdata(ResultSet res) throws Exception{
         getcolsattr(res);
         while(res.next()){
           data.add( getrowdata( res ) );
         }
     }
     /*
     public void extractdata(ResultSet res,int start,int end) throws Exception{
         if(end < start) {
                throw new Exception("end rows is less than start row.");
         }
         if(start < 0) {
                throw new Exception("start row must be more than zeor.");
         }
         int count = 0;
         getcolsattr(res);
         if(res.absolute(start + 1))
         {
	         do{
	           data.add( getrowdata( res ) );
	           count++;
	         }while(count < end - start + 1 && res.next());
         }
     }
     */
     public void extractdata(ResultSet res,int start,int end) throws Exception{
    	 int count = 0;
    	 getcolsattr(res);
    	 boolean state;
    	 if(start >= 0)
    	 {
    		 state = res.absolute(start + 1);
    	 }
    	 else{
    		 state = res.next();
    	 }
    	 if(state)
    	 {
    		 do{
  	           data.add( getrowdata( res ) );
  	           count++;
  	         }while((end < 0 || count < end - start + 1) && res.next());
    	 }
     }
     
     private List getrowdata(ResultSet res){
         List<Object> row = new ArrayList<Object>();       
    	 for(int i = 0; i < col_count; i++)
    	 {
    		 String type=col_attrs[i].type;
    		
    		 if (type.equalsIgnoreCase("DOUBLE")||type.equalsIgnoreCase("FLOAT")){
    			 try{
    				// double d=(double)res.getBigDecimal(i+1);
    				// System.out.print(col_attrs[i].getColname()+"=="+res.getBigDecimal(i+1));
	    			 row.add(i,(Object)res.getBigDecimal(i+1)); 
	    		 }
	    		 catch(Exception ex){
	                 row.add(i,null);
	             }
    		 }else{
	    		// res.getDouble(columnIndex)
	    		 try{
	    			 row.add(i,res.getObject(i + 1)); 
	    		 }
	    		 catch(Exception ex){
	                 row.add(i,null);
	             }
    	 	}
    	 }      
         return row;
     }

     private void getcolsattr(ResultSet res) throws Exception{
         ResultSetMetaData rsmd = res.getMetaData();
         
         col_count = rsmd.getColumnCount();
         col_attrs = new ColumAttr[col_count];
         for(int i = 0; i < col_count; i++){
             col_attrs[i] = new ColumAttr();
           //  String colname = DBManager.getCharset().code1_2_code2(rsmd.getColumnName(i + 1));
             
             String colname = DBManager.getCharset().code1_2_code2(rsmd.getColumnLabel(i + 1));
             col_attrs[i].setColname(colname);
             
             int n1=rsmd.getPrecision(i+1);
             //System.out.println(colname+"=="+n1);
             col_attrs[i].setType(rsmd.getColumnTypeName(i + 1));
             
             //col_attrs[i].setColsize(rsmd.getColumnDisplaySize(i + 1));
             col_attrs[i].setColsize(n1+1);
         }
     }
}
