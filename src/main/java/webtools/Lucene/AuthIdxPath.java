package webtools.Lucene;

import webtools.Lucene.utils.GsonMapMgr;
import webtools.common.FileMgr;

import java.util.Map;

/**
 * 索引目录认证
 */
public class AuthIdxPath {

    private final FileMgr pFileMgr = new FileMgr();

    private final GsonMapMgr pGsonMapMgr = new GsonMapMgr();

    public boolean Auth(String jsonStr, String urlToken, String businessNo, Map<String, Object> calbackMap) {
        try {

            //this.getClass().get
            jsonStr = this.unicode2String(jsonStr);
            Map<String, Object> map = this.pGsonMapMgr.gsonFormatMapObj(jsonStr);
            if (map == null) return false;
            String app_id = this.pGsonMapMgr.getString(map, "app_id");
            String index_path = this.pGsonMapMgr.getString(map, "index_path");
            String url_token = this.pGsonMapMgr.getString(map, "url_token");
            String business_no = this.pGsonMapMgr.getString(map, "business_no");
            if (!url_token.equals(urlToken) || !business_no.equals(businessNo)) {
                return false;
            }
            if (!this.pFileMgr.isFileExist(index_path)) {
                this.pFileMgr.mkdir(index_path);
            }
            calbackMap.put("index_path", index_path);
            calbackMap.put("business_no", business_no);
            calbackMap.put("app_id", app_id);

            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    private String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * unicode 转字符串
     */
    private String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }
}
