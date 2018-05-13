package webtools.org.common.pfcy.util;

import java.util.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-7-5
 * Time: 7:05:05
 * To change this template use File | Settings | File Templates.
 */
public class MyDataFormat {
    public static long dateDiff(String interval, String bDate) {
        Date dateBegin = parseDate(bDate);
        return dateDiff(interval, dateBegin, new Date());
    }

    public static long dateDiff(String interval, Date bDate, Date eDate) {
        long ret = eDate.getTime() - bDate.getTime();
        if (interval.equals("s")) ret = ret / 1000;
        if (interval.equals("m")) ret = ret / (1000 * 60);
        if (interval.equals("h")) ret = ret / (1000 * 60 * 60);
        if (interval.equals("d")) ret = ret / (1000 * 60 * 60 * 24);
        return ret;
    }

    public static long dateDiff(String interval, String bDate, String eDate) {
        Date dateEnd = parseDate(eDate);
        Date dateBegin = parseDate(bDate);
        return dateDiff(interval, dateBegin, dateEnd);
    }

    public static int getDayOfWeek(String strDate) {
        Date date = parseDate(strDate);
        Calendar dCal = Calendar.getInstance();
        int n = dCal.getFirstDayOfWeek();
        //	dCal.setTime(date );
        //	int n=dCal.getFirstDayOfWeek()  ;

        return n;
    }

    /**
     * 将指定的字符串转换为日期类型。
     *
     * @param strDate
     * @return
     */
    public static Date parseDate(String strDate) {
        long retMill = 0;
        try {
            StringTokenizer token = new StringTokenizer(strDate, " ");

            String myDate = token.nextToken();
            String myTime = token.nextToken();
            StringTokenizer tkTime = new StringTokenizer(myTime, ":");
            Date newDate = java.sql.Date.valueOf(myDate);
            retMill = newDate.getTime();
            retMill += Integer.parseInt(tkTime.nextToken()) * 60 * 60 * 1000;
            retMill += Integer.parseInt(tkTime.nextToken()) * 60 * 1000;
            retMill += Integer.parseInt(tkTime.nextToken()) * 1000;
        } catch (Exception ex) {
            return new Date();
        }
        return new Date(retMill);
    }

    /**
     * 指定日期加一个时间段后产生的新日期。
     */
    public static String dateAdd(String interval, int num, String strInitDate) {
        Calendar myCal = Calendar.getInstance();
        Date initDate = parseDate(strInitDate);
        myCal.setTime(initDate);
        if (interval.equals("s")) myCal.add(Calendar.SECOND, num);
        if (interval.equals("mm")) myCal.add(Calendar.MINUTE, num);
        if (interval.equals("h")) myCal.add(Calendar.HOUR, num);
        if (interval.equals("d")) myCal.add(Calendar.DAY_OF_MONTH, num);
        if (interval.equals("w")) myCal.add(Calendar.WEEK_OF_YEAR, num);
        if (interval.equals("m")) myCal.add(Calendar.MONTH, num);
        if (interval.equals("y")) myCal.add(Calendar.YEAR, num);

        return commonTime(myCal.getTime());
    }

    /**
     * 按指定格式得到当前时间的字符串。type=0为完整时间，1为日期，2为时间
     *
     * @return
     */
    public static String commonTime() {
        return commonTime(new Date(), 0);
    }

    public static String commonTime(int type) {
        return commonTime(new Date(), type);
    }

    public static String commonTime(long mills) {
        return commonTime(new Date(mills), 0);
    }

    public static String commonTime(long mills, int type) {
        return commonTime(new Date(mills), type);
    }

    public static String commonTime(Date mydate) {
        return commonTime(mydate, 0);
    }

    public static String commonTime(Date mydate, int type) {
        String strRet = "";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            strRet = formater.format(mydate);
        } catch (Exception ex) {
            //strRet=formater.format(new Date());
        }

        StringTokenizer token = new StringTokenizer(strRet, " ");
        String myDate = token.nextToken();
        String myTime = token.nextToken();

        if (type == 1) strRet = myDate;
        if (type == 2) strRet = myTime;
        return strRet;
    }

    public static String commonTime(String strDate, int type) {
        String ret = strDate;
        StringTokenizer token = new StringTokenizer(strDate, " ");
        String myDate = token.nextToken();
        String myTime = token.nextToken();

        if (type == 1) ret = myDate;
        if (type == 2) ret = myTime;
        return ret;
    }

    public static String currentDate(String strDate) {
        String ret = "";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ret = formater.format(parseDate(strDate));
        } catch (Exception ex) {
            //ret=formater.format(new Date());
        }
        return ret;
    }

    public static String currentDateMonthDay(String strDate) {
        String ret = "";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-01");
        try {
            ret = formater.format(parseDate(strDate));
        } catch (Exception ex) {
            ret = formater.format(new Date());
        }
        return ret;
    }

    public static String currentDate() {
        return currentDate(commonTime());
    }

    public static String currentTime(String strDate) {
        String ret = "";
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
        try {
            ret = formater.format(parseDate(strDate));
        } catch (Exception ex) {
            ret = formater.format(new Date());
        }
        return ret;
    }

    public static String currentTime() {
        return currentTime(commonTime());
    }

    public static void main(String args[]) {
        System.out.println(currentTime());
    }
}
