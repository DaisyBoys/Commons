package webtools.common.database;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-12-7
 * Time: 16:20:46
 * To change this template use File | Settings | File Templates.
 */
public final class ColumAttr {
    String colname = "";
    String type = "";
    int    colsize = 0;

    public ColumAttr()
    {
    	
    }
    
    public ColumAttr(final String colname,final String type,final int colsize)
    {
    	this.colname = colname;
    	this.type = type;
    	this.colsize = colsize;
    }
    
	public String getColname() {
        return colname;
    }

    public void setColname(String colname) {
        this.colname = colname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public int getColsize() {
		return colsize;
	}

	public void setColsize(int colsize) {
		this.colsize = colsize;
	}
    
}
