package webtools.common;

public class Pagehref {
	private int pageShow = 0;
	private String pageurl = "";
	private boolean current = false;
	public boolean isCurrent() {
		return current;
	}
	public void setCurrent(boolean current) {
		this.current = current;
	}
	public int getPageShow() {
		return pageShow;
	}
	public void setPageShow(int pageShow) {
		this.pageShow = pageShow;
	}
	public String getPageurl() {
		return pageurl;
	}
	public void setPageurl(String pageurl) {
		this.pageurl = pageurl;
	}
	
}
