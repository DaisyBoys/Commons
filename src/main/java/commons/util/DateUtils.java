package commons.util;

import org.apache.http.util.TextUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期处理工具类
 * Created by wugaoshang on 2018/5/14.
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils{

    public static final String DATETIME_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_TIME_ZONE_FORMAT="yyyy-MM-dd HH:mm:ssZZ";
    public static final String DATE_FORMAT="yyyy-MM-dd";


    public static void main(String[] args) {

        String num="1.515146889E12";
        String str =plainToString(num);
        System.out.println(str);

        String timeStamp2Date = TimeStampDate(str,"");
        System.out.println(timeStamp2Date);

    }


    /**
     * 字符日期格式化
     * @param timeStr
     * @param formats
     * @return
     * @throws NumberFormatException
     */
    public static String TimetoDateStr(String timeStr, String formats){
        if (TextUtils.isEmpty(formats)) {
            formats = DATETIME_FORMAT;
        }
        if (StringUtils.isBlank(timeStr)) {
            return timeStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formats);
        Date date = null;
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            System.out.println("日期转换出错！");
            e.printStackTrace();
            return timeStr;
        }
        return sdf.format(date);
    }


    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     * @param timestampString 时间戳 如："1515146889000";
     * @param formats 要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     *
     * @return 返回结果 如："2018-01-05 18:08:09";
     */
    public static String TimeStampDate(String timestampString, String formats)
            throws NumberFormatException{
            if (TextUtils.isEmpty(formats)) {
                formats = DATETIME_FORMAT;
            }
            if (StringUtils.isBlank(timestampString)) {
                return timestampString;
            }
            Long timestamp = Long.parseLong(timestampString);
            String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
            return date;

    }

    /**
     * 科学计数法转String
     * 如：1.515146889E12
     * @return
     */
    public static String plainToString(String value) {
        if(StringUtils.isBlank(value)) {
            return value;
        }
        if(value.indexOf("E")==-1) {
            return value;
        }
        BigDecimal db = new BigDecimal(value);
        return db.toPlainString();

    }

}
