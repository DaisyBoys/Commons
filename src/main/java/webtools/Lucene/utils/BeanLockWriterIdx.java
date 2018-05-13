package webtools.Lucene.utils;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * 索引锁
 * */
public class BeanLockWriterIdx {
	public ReadWriteLock lock =null; //锁
	public String id="";
}
