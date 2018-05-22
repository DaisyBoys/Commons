package commons.util;

import com.alibaba.fastjson.JSON;

/**
 * 
 *<pre><b><font color="blue">JSONUtil</font></b></pre>
 *
 *<pre><b>JSON工具类</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   JSONUtil obj = new JSONUtil();
 *   obj.method();
 * </pre>
 * @author <b>wugaoshang</b>
 */
public class JSONUtil {
	/**
	 * 把对象转化为json字符串
	 * @param obj
	 * @return
	 * @author:wugaoshang
	 */
	public static String toJSONString(Object obj) {
		return JSON.toJSONString(obj);
	}
	/**
	 * 把json字符串转化为相应的实体对象
	 * @param json
	 * @param clazz
	 * @return
	 * @author:wugaoshang
	 */
	public static <T> T parseObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}
}
