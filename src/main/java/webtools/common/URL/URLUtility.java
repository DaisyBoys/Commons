/**
 * @author ��Ӿ
 */
package webtools.common.URL;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @deprecated
 * */
public class URLUtility {
    public static Hashtable HttpServletRequest2Hashtable(HttpServletRequest request) {
        Hashtable<String, String> table = new Hashtable<String, String>();
        Enumeration para_names = request.getParameterNames();
        URLConnection con = new URLConnection(request, null);
        while (para_names.hasMoreElements()) {
            String para_name = (String) para_names.nextElement();
            String para_val = con.getSafeParameter(para_name, "");
            table.put(para_name, para_val);
        }
        return table;
    }

    public static Hashtable UploadRequest2Hashtable(webtools.common.upload.Upload upload) {
        Hashtable<String, String> table = new Hashtable<String, String>();
        Enumeration para_names = upload.getParameterNames();
        UploadConnection con = new UploadConnection(upload, null);
        while (para_names.hasMoreElements()) {
            String para_name = (String) para_names.nextElement();
            String para_val = con.getSafeParameter(para_name, "");
            table.put(para_name, para_val);
        }
        return table;
    }
}
