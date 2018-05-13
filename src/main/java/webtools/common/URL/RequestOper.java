package webtools.common.URL;


import webtools.common.databaseex.BasePhrase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

public class RequestOper {

    public String query_create(String query, RequestConnection con) {
        String modul = query.toLowerCase();
        int pt = modul.indexOf("where");
        if (pt > 0) {
            String head = query.substring(0, pt);
            int tail1 = query.lastIndexOf("order");
            if (tail1 == -1) tail1 = query.length();
            int tail2 = query.lastIndexOf("group");
            if (tail2 == -1) tail2 = query.length();
            int tail = (tail1 > tail2) ? tail2 : tail1;
            modul = query.substring(pt + 5, tail);
            String strtail = query.substring(tail);
            return head + parse(modul, con) + " " + strtail;
        }
        return query;
    }


    private String parse(String condition, RequestConnection con) {
        String strret = "";
        String tmp[] = new String[2];
        tmp[0] = condition;
        String conj = " where ";
        while (true) {
            BasePhrase phrase = BasePhrase.parse_phrase(tmp);
            if (phrase == null) {
                break;
            }
            String str = phrase.getPhrase(con);
            if (!str.equals("")) {
                strret += conj + str;
                conj = " " + tmp[1] + " ";
            }
        }

        return strret;
    }

    public Map<String, String> Request2Hashtable(RequestConnection request) {
        Map<String, String> table = new Hashtable<String, String>();
        Enumeration para_names = request.getParameterNames();
        while (para_names.hasMoreElements()) {
            String para_name = (String) para_names.nextElement();
            String para_val = request.getSafeParameter(para_name, "");
            table.put(para_name, para_val);
        }
        return table;
    }


    public Object Request2Object(Class objclass, RequestConnection request) {
        try {
            Object ret_obj = objclass.newInstance();
            Field f[] = objclass.getDeclaredFields();
            for (int i = 0; i < f.length; i++) {
                String type = f[i].getType().getName();
                String var_name = f[i].getName();
                String var_value = (String) request.getSafeParameter(var_name, null);
                if (var_value != null) {
                    if (Modifier.isPublic(f[i].getModifiers())) {
                        if (type.equals("boolean")) {
                            f[i].setBoolean(ret_obj, Boolean.parseBoolean(var_value));
                        } else if (type.equals("int")) {
                            f[i].setInt(ret_obj, Integer.parseInt(var_value));
                        } else if (type.equals("float")) {
                            f[i].setFloat(ret_obj, Float.parseFloat(var_value));
                        } else if (type.equals("double")) {
                            f[i].setDouble(ret_obj, Double.parseDouble(var_value));
                        } else if (type.equals("long")) {
                            f[i].setLong(ret_obj, Long.parseLong(var_value));
                        } else if (type.equals("short")) {
                            f[i].setShort(ret_obj, Short.parseShort(var_value));
                        } else if (type.equals("byte")) {
                            f[i].setByte(ret_obj, Byte.parseByte(var_value));
                        } else if (type.equals("char")) {
                            f[i].setChar(ret_obj, var_value.charAt(0));
                        } else if (type.equals("java.lang.String")) {
                            f[i].set(ret_obj, var_value);
                        }
                    } else {
                        char chr = Character.toUpperCase(var_name.charAt(0));
                        String func = "set" + String.valueOf(chr) + var_name.substring(1);
                        if (type.equals("boolean")) {
                            Method mthd = objclass.getDeclaredMethod(func, boolean.class);
                            if (mthd != null) {
                                mthd.invoke(ret_obj, Boolean.parseBoolean(var_value));
                            }
                        } else if (type.equals("int")) {
                            Method mthd = objclass.getDeclaredMethod(func, int.class);
                            if (mthd != null) {
                                mthd.invoke(ret_obj, Integer.parseInt(var_value));
                            }
                        } else if (type.equals("float")) {
                            Method mthd = objclass.getDeclaredMethod(func, float.class);
                            if (mthd != null) {
                                mthd.invoke(ret_obj, Float.parseFloat(var_value));
                            }
                        } else if (type.equals("double")) {
                            Method mthd = objclass.getDeclaredMethod(func, double.class);
                            if (mthd != null) {
                                mthd.invoke(ret_obj, Double.parseDouble(var_value));
                            }
                        } else if (type.equals("long")) {
                            Method mthd = objclass.getDeclaredMethod(func, long.class);
                            if (mthd != null) {
                                mthd.invoke(ret_obj, Long.parseLong(var_value));
                            }
                        } else if (type.equals("short")) {
                            Method mthd = objclass.getDeclaredMethod(func, short.class);
                            if (mthd != null) {
                                mthd.invoke(ret_obj, Short.parseShort(var_value));
                            }
                        } else if (type.equals("byte")) {
                            Method mthd = objclass.getDeclaredMethod(func, byte.class);
                            if (mthd != null) {
                                mthd.invoke(ret_obj, Byte.parseByte(var_value));
                            }
                        } else if (type.equals("char")) {
                            Method mthd = objclass.getDeclaredMethod(func, char.class);
                            if (mthd != null) {
                                mthd.invoke(ret_obj, var_value.charAt(0));
                            }
                        } else if (type.equals("java.lang.String")) {
                            Method mthd = objclass.getDeclaredMethod(func, String.class);
                            if (mthd != null) {
                                mthd.invoke(ret_obj, var_value);
                            }
                        }
                    }
                }
            }
            return ret_obj;
        } catch (Exception ex) {
            System.out.println("error:" + ex.toString());
            return null;
        }
    }


}
