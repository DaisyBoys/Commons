package webtools.common;

public class PaginationOper {

	private int current_page = 0;
	private int pagesize = 0;
	private int total_records = 0;
	private String url = "";
	
	public PaginationOper()
	{
	}
	
	public PaginationOper(int currentpage,int psize,int reccount,String url)
	{
		current_page = currentpage;
		pagesize = psize;
		total_records = reccount;
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}

	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getTotal_records() {
		return total_records;
	}
	public void setTotal_records(int total_records) {
		this.total_records = total_records;
	}
	public int getPageCount()
	{
		if(total_records == 0 || pagesize == 0)
    	{
    		return 1;
    	}
    	return (int)Math.ceil((double)total_records/pagesize);
	}
	
	public int getBeginRec()
	{
		int begin_rec = current_page * pagesize;
		return begin_rec;
	}
	public int getEndRec()
	{
		int end_rec = (current_page + 1) * pagesize - 1;
		return Math.min(end_rec,total_records);
	}
	
	/**
	 * ��ȡ��һҳ��url���ӵ�ַ
	 * @return �������ӵ�ַ(����""˵����ǰ���ǵ�һҳ)��
	 * */
	public String getFirstPage()
	{
		String strurl = "";
        if(current_page > 0){
            strurl = url + String.valueOf(0);;
        }
        return strurl;
	}
	
	/**
	 * ��ȡ��ǰҳ����һҳҳ��url���ӵ�ַ
	 * @return �������ӵ�ַ(����""˵����ǰ�����һҳ)��
	 * */
	public String getNextPage()
	{
	   	String strurl = "";
    	int  page_count = getPageCount();
        if(current_page < page_count - 1){
        	strurl = url + String.valueOf(current_page + 1);
        }
        return strurl;
	}
	
	/**
	 * ��ȡǰһҳ��url���ӵ�ַ
	 * @return �������ӵ�ַ(����""˵����ǰ�ǵ�һҳ)��
	 * */
	public String getPrevPage()
	{
		String strurl = "";
        if(current_page > 0){
            strurl = url + String.valueOf(current_page - 1);
        }
        return strurl;
	}
	
	/**
	 * ��ȡ���ҳ��url���ӵ�ַ
	 * @return ����ָ��Ҳ�����ӵ�ַ(����""˵����ǰ�����һҳ)��
	 * */
	public String getLastPage()
	{
	   	String strurl = "";
    	int  page_count = getPageCount();
        if(current_page < page_count - 1){
        	strurl = url + String.valueOf(page_count - 1);
        }
        return strurl;
	}
	
	/**
	 * ��ȡָ��ҳ��url���ӵ�ַ
	 * @param pageindex ָ��ҳ�루���ڵ��� 0��
	 * @return ����ָ��Ҳ�����ӵ�ַ��
	 * */
	public String getPage(int pageindex)
	{
		int  page_count = getPageCount();
		if(pageindex >= 0 && pageindex < page_count)
		{
			return url + String.valueOf(pageindex);
		}
		return "";
	}
	/**
	 * �Ե�ǰҳΪ��׼������һ�鷭ҳ���ӡ����磺<br>
	 * << [1] [2] [3] .... [10] >> <br>
	 * ��groupsize����һ�鷭ҳ���ҳ����������һ��ҳ����������groupsizeʱ����ʵ��ҳ��Ϊ׼ <br>
	 * �����һ������һ��,������ʾ << ���� >> <br>
	 * @param groupsize һ�鷭ҳ��������
	 * @return ���ذ�Pagehref�����arraylist���顣
	 * */	
	public PageGroup getPageGroup(int groupsize)
	{
		int  page_count = getPageCount();
		int group_no = current_page / groupsize;
		int begin = group_no * groupsize;
		
		PageGroup group = new PageGroup();
		group.setGroupsize(groupsize);	
		
		if(group_no > 0)
		{
			Pagehref prev = new Pagehref();
			prev.setPageurl(getPage(begin - 1));
			group.setPrev_group(prev);
		}

		int i = 0;
		for(; i < groupsize && i + begin <= page_count - 1; i++ )
		{
			int idx = i + begin;
			Pagehref page = new Pagehref();
			page.setPageShow(idx + 1);
			page.setPageurl(getPage(idx));
			page.setCurrent(idx == current_page);
			group.addPage(page); 
		}
		
		if(i + begin <= page_count - 1)
		{
			Pagehref next = new Pagehref();
			next.setPageurl(getPage(i + begin));
			group.setNext_group(next);
		}		
		return group;
	}
}
