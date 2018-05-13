package webtools.Lucene.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LuceneConfig {

    public static String INDEX_PATH = "C:/data/lucene_idx";
    public static ReadWriteLock lock = new ReentrantReadWriteLock();
    private static Map<String, BeanLockWriterIdx> mapINDEX_PATH = new ConcurrentHashMap<String, BeanLockWriterIdx>();

    //得到今日场次所
    public static ReadWriteLock getIdxLock(String idxPath) {
        ReadWriteLock lock = null;
        try {
            //	lock=getLock( idxPath);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return lock;
    }

    public static BeanLockWriterIdx getLock(String idxPath) {
        try {
            if (mapINDEX_PATH == null) mapINDEX_PATH = new ConcurrentHashMap<String, BeanLockWriterIdx>();
            BeanLockWriterIdx pBeanLockWriterIdx = mapINDEX_PATH.get(idxPath);
            if (pBeanLockWriterIdx == null) {
                pBeanLockWriterIdx = new BeanLockWriterIdx();
                pBeanLockWriterIdx.id = idxPath;
                pBeanLockWriterIdx.lock = new ReentrantReadWriteLock();
                mapINDEX_PATH.put(idxPath, pBeanLockWriterIdx);
            }
            if (pBeanLockWriterIdx != null) {
                return pBeanLockWriterIdx;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
