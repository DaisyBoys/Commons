package webtools.Lucene.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GsonMapMgr {
    /**
     * 生产JSON需要的MAP对象数组
     */
    public Object[] getMapObjS(final List<Map<String, Object>> list) {
        try {
            if (list == null) return new Object[]{};
            if (list.size() == 0) return new Object[]{};
            Object[] sList_Obj = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> pMap = list.get(i);
                sList_Obj[i] = pMap;
            }
            return sList_Obj;
        } catch (Exception e) {
            // TODO: handle exception
        }


        return new Object[]{};
    }

    /*
     * 得到对象数组
     * */
    public Object[] doLoadFun(List<Map<String, Object>> listMap) {
        Object[] rtn = null;

        try {
            rtn = this.getMapObjS(listMap);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return rtn;
    }

    /**
     * 得到字符传
     */
    public String getString(final Map<String, Object> map, final String key) {
        String str = "";
        try {
            if (map == null) return "";
            Object obj = map.get(key);
            if (obj == null) return "";
            if (obj instanceof String) {
                str = obj.toString();
                //str=URLDecoder.decode(str,"utf-8");
                return str;
            } else {
                return obj.toString();

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }

    public String getString01(final Map<String, String> map, final String key) {
        String str = "";
        try {
            if (map == null) return "";
            Object obj = map.get(key);
            if (obj == null) return "";
            if (obj instanceof String) {
                str = obj.toString();
                //str=URLDecoder.decode(str,"utf-8");
                return str;
            } else {
                return obj.toString();

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }

    public Map<String, Object> getMapObj(final Map<String, Object> map, final String key) {
        try {
            if (map == null) return null;
            Object obj = map.get(key);
            if (obj == null) return null;
            if (obj instanceof Map) {

                return (Map<String, Object>) obj;
            } else {
                return null;

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<String> getListStr(final Map<String, Object> map, final String key) {
        List<String> listRtn = new ArrayList<String>();
        try {
            if (map == null) return listRtn;
            Object obj = map.get(key);
            if (obj == null) return listRtn;
            if (obj instanceof ArrayList) {
                return (List<String>) obj;
            } else {
                return listRtn;

            }
        } catch (Exception e) {

        }
        return listRtn;
    }

    /**
     * 得到LIST
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getListMapObj(final Map<String, Object> map, final String key) {
        List<Map<String, Object>> listRtn = new ArrayList<Map<String, Object>>();
        Gson gson = new Gson();
        try {
            Object obj = map.get(key);
            if (obj == null) return listRtn;
            if (obj instanceof String) {
                String str = (String) map.get(key);


                List<Map<String, Object>> list = gson.fromJson(str,
                        new TypeToken<List<Map<String, Object>>>() {
                        }.getType());

                if (list != null) {
                    listRtn = list;
                }
            } else {
                List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(key);
                if (list != null) {
                    listRtn = list;
                }
            }


        } catch (Exception e) {

        }
        return listRtn;
    }

    /**
     * 格式化MAP对象
     */
    public Map<String, Object> gsonFormatMapObj(final String jsonStr) {
        Map<String, Object> map = new ConcurrentHashMap<String, Object>();
        try {
            Gson gson = new Gson();
            Map<String, Object> mapTemp = gson.fromJson(jsonStr,
                    new TypeToken<Map<String, Object>>() {
                    }.getType());

            if (mapTemp != null) map = mapTemp;

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return map;
    }

    public List<Map<String, Object>> gsonFormatListMapObj(final String jsonStr) {
        List<Map<String, Object>> listRtn = new ArrayList<Map<String, Object>>();
        try {
            Gson gson = new Gson();
            List<Map<String, Object>> mapTemp = gson.fromJson(jsonStr,
                    new TypeToken<List<Map<String, Object>>>() {
                    }.getType());

            if (mapTemp != null) listRtn = mapTemp;

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return listRtn;
    }

    public Map<String, String> gsonFormatMapObjString(final String jsonStr) {
        Map<String, String> map = new ConcurrentHashMap<String, String>();
        try {
            Gson gson = new Gson();
            Map<String, String> mapTemp = gson.fromJson(jsonStr,
                    new TypeToken<Map<String, String>>() {
                    }.getType());

            if (mapTemp != null) map = mapTemp;

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return map;
    }

    /*
     * 格式化
     * **/
    public String fomatJson(final Map<String, Object> map) {
        Gson gson = new Gson();

        return gson.toJson(map);
    }

    public String fomatJsonDataList(final List<Map<String, Object>> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    public String[] getStringList(final Map<String, Object> map, final String key) {
        String[] strList = null;
        try {
            if (map == null) return null;
            Object objs = map.get(key);
            if (objs == null) return null;
            String s = objs.toString();
            int n1 = s.indexOf("[");
            int n2 = s.indexOf("]");
            if (n1 >= 0 && n2 >= 0) {
                String str = s.substring(n1 + 1, n2);
                strList = str.split(",");
                if (strList != null) {
                    for (int i = 0; i < strList.length; i++) {
                        String tmp = strList[i];
                        if (tmp != null) {
                            strList[i] = tmp.trim();
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return strList;
    }

    public Integer[] getInts(Map<String, Object> map, String key) {
        String[] strs = this.getStringList(map, key);
        try {
            if (strs != null) {
                Integer[] ints = new Integer[strs.length];
                for (int i = 0; i < strs.length; i++) {
                    ints[i] = convertInt(strs[i]);
                }
                return ints;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    public Double[] getDoubles(Map<String, Object> map, String key, int nLen) {
        String[] strs = this.getStringList(map, key);
        try {
            if (strs != null) {
                Double[] ints = new Double[strs.length];
                for (int i = 0; i < strs.length; i++) {
                    ints[i] = convertDouble(strs[i], nLen);
                    if ((i + 1) % 100 == 0) {
                        Thread.sleep(3);
                    }
                }
                return ints;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    /**
     * @param nLen 是10的倍数，如1 ，10， 100，1000，小数点后多少位用10代表1位，100代表2位等
     */
    public double convertDouble(String str, long nLen) {
        try {
            if (str == null || "".equals(str)) {
                str = "0";
            }

            double dd = (double) Math.round(Double.parseDouble(str) * nLen) / nLen;
            return dd;
        } catch (Exception e) {

        }
        return 0;

    }

    private int convertInt(String str) {
        try {
            if (str == null || "".equals(str)) {
                str = "0";
            }
            return Integer.parseInt(str);
        } catch (Exception e) {

        }
        return 0;
    }
}
