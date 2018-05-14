package commons.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * map集合工具类
 * Created by wugaoshang on 2018/5/14.
 */
public class MapUtils {

    /**
     * map集合值转成utf-8格式
     *
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     */
    public static <T, V> Map<T, V> mapToUtf8(Map<T, V> map)
            throws UnsupportedEncodingException {
        Map<T, V> result = new HashMap<T, V>();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            T key = (T) entry.getKey();
            V val = (V) entry.getValue();
            //反射处理String和Object对象
            if (val.getClass().getName().equals("java.lang.String") || val.getClass().getName().equals("java.lang.Object")) {
                result.put(key, (V) CharacterCodingUtils.toUTF8(val));
            }
        }
        return result;
    }

}
