package webtools.common.database.pagination;

import webtools.common.database.*;

public abstract class Pagination {
    public static Pagination create_pagination() {
        String db_type = DBManager.getDBType();
        if (db_type.equals("mysql")) {
            return new MysqlPagination();
        } else if (db_type.equals("oracle")) {
            return new OraclePagination();
        } else if (db_type.equals("mssql")) {
            return new MssqlPagination();
        }
        return null;
    }

    public abstract String parse_sql(String sql, int start_row, int end_row);
}
