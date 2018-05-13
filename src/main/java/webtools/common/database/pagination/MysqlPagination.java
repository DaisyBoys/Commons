package webtools.common.database.pagination;

public class MysqlPagination extends Pagination {

    @Override
    public final String parse_sql(final String sql, final int start_row, final int end_row) {
        if (start_row >= 0 && end_row >= start_row) {
            String p_sql = sql + " LIMIT " + String.valueOf(start_row) + "," +
                    String.valueOf(end_row - start_row + 1);
            return p_sql;
        }
        return sql;
    }

}
