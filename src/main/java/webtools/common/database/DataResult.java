package webtools.common.database;

import java.util.*;

public final class DataResult implements DBResult {
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
    ColumAttr[] col_attrs = null;
    List<List> data = new ArrayList<List>();

    public DataResult() {

    }

    public DataResult(final int colcount) {
        col_count = colcount;
        col_attrs = new ColumAttr[col_count];
    }


    @Override
    public final ColumAttr getColumAttribute(final int col) {
        try {
            if (col >= 0 && col < col_count) {
                return col_attrs[col];
            }
        } catch (Exception ex) {

        }
        return null;
    }


    @Override
    public int getRowCount() {
        return data.size();
    }


    @Override
    public int getColCount() {
        return col_count;
    }


    @Override
    public final String getString(final int row, final int col) throws Exception {
        final Object val = getObject(row, col);
        if (val == null) return "";
        return val.toString();
    }


    @Override
    public final String getString(final int row, final String colname) throws Exception {
        final Object val = getObject(row, colname);
        if (val == null) return "";
        return val.toString();
    }


    @Override
    public int getInt(final int row, final int col) throws Exception {
        Integer val = (Integer) getObject(row, col);
        if (val == null) return 0;
        return val.intValue();
    }


    @Override
    public int getInt(final int row, final String colname) throws Exception {
        Integer val = (Integer) getObject(row, colname);
        if (val == null) return 0;
        return val.intValue();
    }


    @Override
    public double getDouble(int row, int col) throws Exception {
        Double val = (Double) getObject(row, col);
        if (val == null) return 0.0;
        return val.doubleValue();
    }


    @Override
    public double getDouble(int row, String colname) throws Exception {
        Double val = (Double) getObject(row, colname);
        if (val == null) return 0.0;
        return val.doubleValue();
    }


    @Override
    public float getFloat(int row, int col) throws Exception {
        Float val = (Float) getObject(row, col);
        if (val == null) return 0;
        return val.floatValue();
    }


    @Override
    public float getFloat(int row, String colname) throws Exception {
        Float val = (Float) getObject(row, colname);
        if (val == null) return 0;
        return val.floatValue();
    }


    @Override
    public boolean getBoolean(int row, int col) throws Exception {
        Boolean val = (Boolean) getObject(row, col);
        if (val == null) return false;
        return val.booleanValue();
    }


    @Override
    public boolean getBoolean(int row, String colname) throws Exception {
        Boolean val = (Boolean) getObject(row, colname);
        if (val == null) return false;
        return val.booleanValue();
    }


    @Override
    public char getChar(int row, int col) throws Exception {
        Character val = (Character) getObject(row, col);
        if (val == null) return ' ';
        return val.charValue();
    }


    @Override
    public char getChar(int row, String colname) throws Exception {
        Character val = (Character) getObject(row, colname);
        if (val == null) return ' ';
        return val.charValue();
    }


    @Override
    public byte getByte(int row, int col) throws Exception {
        Byte val = (Byte) getObject(row, col);
        if (val == null) return ' ';
        return val.byteValue();
    }


    @Override
    public byte getByte(int row, String colname) throws Exception {
        Byte val = (Byte) getObject(row, colname);
        if (val == null) return ' ';
        return val.byteValue();
    }


    @Override
    public long getLong(int row, int col) throws Exception {
        Long val = (Long) getObject(row, col);
        if (val == null) return 0;
        return val.longValue();
    }


    @Override
    public long getLong(int row, String colname) throws Exception {
        Long val = (Long) getObject(row, colname);
        if (val == null) return 0;
        return val.longValue();
    }


    @Override
    public short getShort(int row, int col) throws Exception {
        Short val = (Short) getObject(row, col);
        if (val == null) return 0;
        return val.shortValue();
    }


    @Override
    public short getShort(int row, String colname) throws Exception {
        Short val = (Short) getObject(row, colname);
        if (val == null) return 0;
        return val.shortValue();
    }


    @Override
    public Object getObject(int row, int col) throws Exception {
        if (row < 0 || row >= getRowCount()) return null;
        if (col < 0 || col >= col_count) return null;
        return getRow(row).get(col);
    }


    @Override
    public Object getObject(int row, String colname) throws Exception {
        if (row < 0 || row >= getRowCount()) return null;
        int col = -1;
        for (int i = 0; i < col_count; i++) {
            if (col_attrs[i].getColname().equalsIgnoreCase(colname)) {
                col = i;
                break;
            }
        }
        if (col < 0 || col >= col_count) return null;
        return getRow(row).get(col);
    }


    @Override
    public List getRow(int row) throws Exception {
        return (List) data.get(row);
    }

    public void addRow(List row) {
        data.add(row);
    }

    public void setColumnCount(int colcount) {
        col_count = colcount;
        col_attrs = new ColumAttr[col_count];
    }

    public void setColumnAttribute(int colidx, ColumAttr attr) {
        if (colidx >= 0 && colidx < col_count) {
            col_attrs[colidx] = attr;
        }
    }
}
