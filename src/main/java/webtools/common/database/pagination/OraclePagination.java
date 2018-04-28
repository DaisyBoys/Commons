package webtools.common.database.pagination;

public class OraclePagination extends Pagination {

	@Override
	public String parse_sql(String sql, int start_row, int end_row) {
		// TODO Auto-generated method stub
		if(start_row >= 0 && end_row >= start_row)
		{
			String p_sql = "SELECT * FROM(SELECT A.*, ROWNUM RN " + 
						   " FROM (" + sql + ") A WHERE ROWNUM <= " + String.valueOf(start_row) +
			") WHERE RN >= " + String.valueOf(end_row);
			return p_sql;
		}
		return sql;
	}

}
