package webtools.common;

import java.util.*;

public class PageGroup {
	private Pagehref prev_group = null;
	private List<Pagehref> pages = new ArrayList<Pagehref>();
	private Pagehref next_group = null;
	private int groupsize = 0;
	public int getGroupsize() {
		return groupsize;
	}
	public void setGroupsize(int groupsize) {
		this.groupsize = groupsize;
	}
	public Pagehref getNext_group() {
		return next_group;
	}
	public void setNext_group(Pagehref next_group) {
		this.next_group = next_group;
	}
	public Pagehref getPrev_group() {
		return prev_group;
	}
	public void setPrev_group(Pagehref prev_group) {
		this.prev_group = prev_group;
	}
	public int get_pagesize()
	{
		return pages.size();
	}
	public Pagehref getPage(int index)
	{
		return pages.get(index);
	}
	public void addPage(Pagehref page)
	{
		pages.add(page);
	}
}
