package webtools.reg.page.bean.search;

public final class QuerySqlBean {
	private String selectField="*";
	private String QueryFrom="";
	public String getQueryFrom() {
		return QueryFrom;
	}
	public void setQueryFrom(String queryFrom) {
		QueryFrom = queryFrom;
	}

	private String QueryWhere="";//where 
	private String QueryOrderBy="";
	private String QueryGroupBy="";
	
	public String getSelectField() {
		return selectField;
	}
	public void setSelectField(final String selectField) {
		this.selectField = selectField;
	}

	public String getQueryWhere() {
		return QueryWhere;
	}
	
	public void setQueryWhere(final String queryWhere) {
		QueryWhere = queryWhere;
	}
	public String getQueryOrderBy() {
		return QueryOrderBy;
	}

	public void setQueryOrderBy(final String queryOrderBy) {
		QueryOrderBy = queryOrderBy;
	}
	public String getQueryGroupBy() {
		return QueryGroupBy;
	}


	public void setQueryGroupBy(final String queryGroupBy) {
		QueryGroupBy = queryGroupBy;
	}

}
