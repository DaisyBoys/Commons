package webtools.common.database.pagination;

public class MssqlPagination extends Pagination {

    @Override
    public String parse_sql(String sql, int start_row, int end_row) {
        // TODO Auto-generated method stub
        if (start_row >= 0 && end_row >= start_row) {
            String cols = get_cols(sql);
            String orders = get_order(sql);
            if (orders == null || orders.equals("")) {
                orders = get_first_col(cols);
            }
            String p_sql = "select * from {select " + cols +
                    ",ROW_NUMBER() over(order by " + orders + ") as rowNum from " +
                    get_table_conditions(sql) + " ) where rowNum >=" + start_row +
                    " and rowNum <=" + end_row;
            return p_sql;
        }
        return sql;
    }

    private String get_cols(String sql) {
        String s = sql.toLowerCase();
        int st_pt = s.indexOf("select") + 6;
        int ed_pt = s.indexOf("from");
        return sql.substring(st_pt, ed_pt).trim();
    }

    private String get_order(String sql) {
        String s = sql.toLowerCase();
        int pt = s.lastIndexOf("order");
        if (pt >= 0) {
            String tail = sql.substring(pt + 5).trim().substring(2);
            return tail;
        }
        return null;
    }

    private String get_first_col(String cols) {
        int pt = cols.indexOf(',');
        if (pt > 0) {
            return cols.substring(0, pt - 1);
        }
        return cols;
    }

    private String get_table_conditions(String sql) {
        String s = sql.toLowerCase();
        int st_pt = s.indexOf("from") + 4;
        int ed_pt = s.lastIndexOf("order");
        if (ed_pt > 0) {
            return sql.substring(st_pt, ed_pt - 1);
        }
        return sql.substring(st_pt);
    }
}
