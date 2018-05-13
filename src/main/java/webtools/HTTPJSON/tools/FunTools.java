package webtools.HTTPJSON.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


//通用公斤
public final class FunTools {

    public String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public String getMonth() {
        String strRtn = "";
        try {
            Date d = new Date();
            //SimpleDateFormat dNow=new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat dNow = new SimpleDateFormat("MM");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    public String getNowDate() {
        Date d = new Date();
        SimpleDateFormat dNow = new SimpleDateFormat("yyyy-MM-dd");
        return dNow.format(d);

    }

    public String getNow() {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(d);

    }

    public int compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                // System.out.println("dt1 在d后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            // exception.printStackTrace();
        }
        return 0;
    }

    public String getMonth(String date) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date d = df.parse(date);


            SimpleDateFormat dNow = new SimpleDateFormat("MM");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    public String getYear(String date) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date d = df.parse(date);


            SimpleDateFormat dNow = new SimpleDateFormat("yyyy");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    public String getDay(String time) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d = df.parse(time);
            SimpleDateFormat dNow = new SimpleDateFormat("yyyy-MM-dd");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    /**
     * 小时
     */
    public String getHour(String time) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d = df.parse(time);


            SimpleDateFormat dNow = new SimpleDateFormat("HH");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    public String getMinute(String time) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d = df.parse(time);

            SimpleDateFormat dNow = new SimpleDateFormat("mm");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    public List<String> getListMonth(String day1, String day2) {
        List<String> listRtn = new ArrayList<String>();
        try {
            if ("".equals(day1) || "".equals(day2)) return listRtn;
            String year1 = this.getYear(day1);
            String year2 = this.getYear(day2);
            String month1 = this.getMonth(day1);
            String month2 = this.getMonth(day2);
            int n_year1 = this.convertInt(year1);
            int n_year2 = this.convertInt(year2);
            int n_month1 = this.convertInt(month1);
            int n_month2 = this.convertInt(month2);
            if (n_year1 == n_year2) {
                for (int i = n_month1; i <= n_month2; i++) {
                    String str = year1 + "-" + format_tk_no(i + "", "2");
                    listRtn.add(str);
                }
            } else {
                for (int i = n_month1; i <= 12; i++) {
                    String str = year1 + "-" + format_tk_no(i + "", "2");
                    listRtn.add(str);
                }
                n_year1++;
                for (int i = n_year1; i < n_year2; i++) {
                    for (int k = 1; k <= 12; k++) {
                        String str = format_tk_no(i + "", "2") + "-" + format_tk_no(k + "", "2");
                        listRtn.add(str);
                    }
                }
                for (int i = 1; i <= n_month2; i++) {
                    String str = year2 + "-" + format_tk_no(i + "", "2");
                    listRtn.add(str);
                }
                //for(int i=0;i<listRtn.size();i++){
                //	System.out.println(listRtn.get(i));
                //}
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return listRtn;

    }

    //得到当前年
    public String getNowYear() {
        Date d = new Date();
        SimpleDateFormat dNow = new SimpleDateFormat("yyyy");
        return dNow.format(d);

    }

    //得到当前月
    public String getNowMonth() {
        Date d = new Date();
        SimpleDateFormat dNow = new SimpleDateFormat("MM");
        return dNow.format(d);

    }

    //得到当前第几周
    public int getYearWeekNum(String today) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(date);
            return (calendar.get(Calendar.WEEK_OF_YEAR));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;

    }

    //得到当月的周
    public int getMonthWeekNum(String today) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(date);
            return (calendar.get(Calendar.WEEK_OF_MONTH));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;

    }

    //根据日期得到周几
    public String getWeekOfDate(String today) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(today);
            String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            return weekDaysCode[intWeek];

        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }


    public int getMonthAllWeek(String today) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(date);
            return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

            // System.out.println("Actual周数：" + calendar.getActualMaximum(Calendar.WEEK_OF_MONTH));
            // return (calendar.get(Calendar.WEEK_OF_YEAR));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    //得到本月的所有日期
    public String[][] getMonthAllWeekDay(String currYearMonth) {
        int nSize = getMonthAllWeek(currYearMonth + "-" + "01");
        String[][] strMonthWeekDay = new String[nSize][7];
        try {

        } catch (Exception e) {
            // TODO: handle exception
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf1.setLenient(false);
        //  SimpleDateFormat sdf2 = new SimpleDateFormat("EEE");


        for (int i = 1; i < 32; i++) {
            try {
                Date date = sdf1.parse(currYearMonth + "-" + i);
                String strTmpDay = sdf1.format(date);
                int n = getMonthWeekNum(strTmpDay) - 1;

                int nW = Integer.parseInt(getWeekOfDate(strTmpDay));
                strMonthWeekDay[n][nW] = sdf1.format(date);
                // System.out.println(sdf1.format(date) + " : " + sdf2.format(date));
            } catch (ParseException e) {
                //do nothing
            }
        }
        return strMonthWeekDay;

    }

    //更加周数字得到日期信息
    public String[] getWeekDay(String[] weekDays) {
        String[] sList = new String[7];
        try {
            for (int i = 0; i < sList.length; i++) {
                sList[i] = "";
            }
            for (int i = 0; i < weekDays.length; i++) {
                String strDay = weekDays[i];
                if (strDay == null) continue;
                int n = this.convertInt(this.getWeekOfDate(strDay));

                sList[n] = getDayInfo(strDay)[2] + "";

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return sList;
    }

    public String getDayByMonthAllWeekDay(String[][] slist, int nWeek, int nWeekNum) {
        return slist[nWeek - 1][nWeekNum];
    }

    public int[] getDayInfo(String today) {
        int[] nList = new int[3];
        try {
            String[] strList = today.split("-");
            if (strList != null) {
                nList[0] = convertInt(strList[0]);
                nList[1] = convertInt(strList[1]);
                nList[2] = convertInt(strList[2]);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return nList;
    }

    public int convertInt(String str) {
        try {
            if (str == null || "".equals(str)) {
                str = "0";
            }
            return Integer.parseInt(str);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    public String getNowYear(String today) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date d = df.parse(today);


            SimpleDateFormat dNow = new SimpleDateFormat("yyyy");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;

    }

    public String getNowMonth(String today) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date d = df.parse(today);


            SimpleDateFormat dNow = new SimpleDateFormat("MM");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;

    }

    public String getNowDay(String today) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date d = df.parse(today);


            SimpleDateFormat dNow = new SimpleDateFormat("dd");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;

    }

    public String getDateTimeToDate(String today) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date d = df.parse(today);


            SimpleDateFormat dNow = new SimpleDateFormat("yyyy-MM-dd");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;

    }

    public String getDateTimeToHHMM(String today) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d = df.parse(today);


            SimpleDateFormat dNow = new SimpleDateFormat("HH:mm");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;

    }

    public String getCNDateTimeHHMM(String today) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d = df.parse(today);

            SimpleDateFormat dNow = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;

    }

    public String getDateTimeHHMM(String today) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d = df.parse(today);

            SimpleDateFormat dNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;

    }

    public String getDateTimeToHHMMSS(String today) {
        String strRtn = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d = df.parse(today);


            SimpleDateFormat dNow = new SimpleDateFormat("HH:mm:ss");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;

    }

    public String getNowDay() {
        Date d = new Date();
        SimpleDateFormat dNow = new SimpleDateFormat("dd");
        return dNow.format(d);

    }

    //时段处理
    public String get_time_intervalByCurrTime() {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        return df.format(d);
    }

    /**
     * 指定日期加一个时间段后产生的新日期。
     */
    public String dateAdd(int num, String strInitDate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            // Date d = df.parse(strInitDate);

            Calendar myCal = Calendar.getInstance();
            Date initDate = df.parse(strInitDate);
            myCal.setTime(initDate);
            myCal.add(Calendar.DATE, num);
            return (new SimpleDateFormat("yyyy-MM-dd")).format(myCal.getTime());
        } catch (Exception e) {
            // TODO: handle exception
        }


        return "";

    }

    public String dateAdd(String interval, int num, String strInitDate) {
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

    public String commonTime(Date mydate) {
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

    public Date parseDate(String strDate) {
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

    //月份加减
    public String dateMonthAdd(int num, String strInitDate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            // Date d = df.parse(strInitDate);

            Calendar myCal = Calendar.getInstance();
            Date initDate = df.parse(strInitDate);
            myCal.setTime(initDate);
            myCal.add(Calendar.MONTH, num);
            return (new SimpleDateFormat("yyyy-MM-dd")).format(myCal.getTime());
        } catch (Exception e) {
            // TODO: handle exception
        }


        return "";

    }

    //季度
    public int getYearQuarter(String month) {
        try {
            int n_month = this.convertInt(month);
            if (n_month >= 1 && n_month <= 3) {
                return 1;
            }
            if (n_month >= 4 && n_month <= 6) {
                return 2;
            }
            if (n_month >= 7 && n_month <= 9) {
                return 3;
            }
            if (n_month >= 10 && n_month <= 12) {
                return 4;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 1;
    }

    public String getTkID(String tk_id, String barcoder) {
        try {
            int nLen = tk_id.length();
            return barcoder.substring(0, nLen);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    public String getTkID_NO(String tk_id, String barcoder) {
        try {
            int nLen = tk_id.length();
            return barcoder.substring(nLen);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    //得到天数
    public long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
            if (day >= 0) day++;
            else day--;
            //System.out.println("相隔的天数="+day);
        } catch (ParseException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return day;
    }

    //时间比对
    public int compare_time(String time1, String time2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(time1);
            Date dt2 = df.parse(time2);
            if (dt1.getTime() > dt2.getTime()) {
                // System.out.println("dt1 在d后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            // exception.printStackTrace();
        }
        return 0;
    }

    private String format_tk_no(String tk_no, String tc_no_len) {
        String strRtn = tk_no;
        try {
            int n_len = this.convertInt(tc_no_len);
            int n_tk_no_len = tk_no.length();
            if (n_tk_no_len < n_len) {
                //处理补位
                int nL = n_len - n_tk_no_len;
                String str = "";
                for (int i = 0; i < nL; i++) {
                    str += "0";
                }
                strRtn = str + tk_no;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    public String getWeekName(String date) {
        try {
            String str = getWeekOfDate(date);
            return weekDaysName[this.convertInt(str)];
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    //上月传入2015-01 返回2014-12
    public String preMonth(String month) {
        String strRtn = "";
        try {
            String startDate = month + "-01";
            String report_year = this.getYear(startDate);
            String report_month = this.getNowMonth(startDate);
            String start_date = report_year + "-" + report_month + "-01";//开始日期
            int nYear = this.convertInt(report_year);
            int nMonth = this.convertInt(report_month) + 1;
            if (nMonth > 12) {
                nMonth = 1;
                nYear++;
            }
            //String tempDate=this.format_tk_no(nYear+"", "4")+"-"+this.format_tk_no(nMonth+"", "2")+"-01";
            //String end_date=this.dateAdd(-1, tempDate);//结束日期
            String date = this.dateAdd(-1, start_date);


            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date d = df.parse(date);


            SimpleDateFormat dNow = new SimpleDateFormat("yyyy-MM");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    //下月
    public String nextMonth(String month) {
        String strRtn = "";
        try {
            String startDate = month + "-01";
            String report_year = this.getYear(startDate);
            String report_month = this.getNowMonth(startDate);
            String start_date = report_year + "-" + report_month + "-01";//开始日期
            int nYear = this.convertInt(report_year);
            int nMonth = this.convertInt(report_month) + 1;
            if (nMonth > 12) {
                nMonth = 1;
                nYear++;
            }
            String tempDate = this.format_tk_no(nYear + "", "4") + "-" + this.format_tk_no(nMonth + "", "2") + "-01";
            String end_date = this.dateAdd(-1, tempDate);//结束日期
            String date = this.dateAdd(1, end_date);


            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date d = df.parse(date);


            SimpleDateFormat dNow = new SimpleDateFormat("yyyy-MM");
            strRtn = dNow.format(d);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    //得到最后一天的日期
    public String getMonthLastDay(String month) {
        String strRtn = "";
        try {
            String startDate = month + "-01";
            String report_year = this.getYear(startDate);
            String report_month = this.getNowMonth(startDate);
            String start_date = report_year + "-" + report_month + "-01";//开始日期
            int nYear = this.convertInt(report_year);
            int nMonth = this.convertInt(report_month) + 1;
            if (nMonth > 12) {
                nMonth = 1;
                nYear++;
            }
            String tempDate = this.format_tk_no(nYear + "", "4") + "-" + this.format_tk_no(nMonth + "", "2") + "-01";
            strRtn = this.dateAdd(-1, tempDate);//结束日期

        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;
    }

    /**
     * @param date1 <String>
     * @param date2 <String>
     * @return int
     * @throws ParseException
     */
    public int getMonthSpace(String date1, String date2) {

        int result = 0;
        try {
            result = getDiffMonth(date1, date2);
            /*
        	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

             Calendar c1 = Calendar.getInstance();
             Calendar c2 = Calendar.getInstance();

             c1.setTime(sdf.parse(date1));
             c2.setTime(sdf.parse(date2));

             result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
             if(result<0){
            	 
             }
             */
        } catch (Exception e) {
            // TODO: handle exception
        }


        // return result == 0 ? 1 : Math.abs(result);
        return result;

    }

    public String getCNDay(String date) {
        String strRtn = "";
        try {
            String[] ss = date.split("-");
            if (ss.length >= 2) {
                strRtn += ss[0] + "年" + ss[1] + "月" + ss[2] + "日";
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return strRtn;

    }

    public int getDiffMonth(String date1, String date2) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date beginDate = df.parse(date1);
            Date endDate = df.parse(date2);
            Calendar calbegin = Calendar.getInstance();
            Calendar calend = Calendar.getInstance();
            calbegin.setTime(beginDate);
            calend.setTime(endDate);
            int m_begin = calbegin.get(Calendar.MONTH) + 1; //获得合同开始日期月份
            int m_end = calend.get(Calendar.MONTH) + 1;  //获得合同结束日期月份
            int checkmonth = m_end - m_begin + (calend.get(Calendar.YEAR) - calbegin.get(Calendar.YEAR)) * 12;//获得合同结束日期于开始的相差月份
            return checkmonth + 1;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    public int isCurrDate(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getDate() > dt2.getDate()) {
                // System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getDate() < dt2.getDate()) {
                //System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            // exception.printStackTrace();
        }
        return 0;
    }

    public int getMonthAllDays(String year, String month) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            cal.set(Calendar.DATE, 1);
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DATE, -1);
            int j = cal.get(Calendar.DAY_OF_MONTH);
            return j;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return 31;
    }

    public static void main(String[] args) {
        FunTools pFunTools = new FunTools();

        double dd = 14353501.67;
        System.out.println(pFunTools.getDateTimeToHHMM("2015-12-12 00:00:01"));
        //int n=pFunTools.getMonthSpace("2015-11-01", "2015-12-1");
        StringBuffer sb = new StringBuffer();
        sb.append("sff");
        sb.append("==sff");
        System.out.println(sb);
        //pFunTools.compare_time("2015-02-13 12:00:00", "2015-02-13 13:00:00");
        //pFunTools.getMonthLastDay("2014-12");
		/*int n=pFunTools.getMonthSpace("2015-02-01","2015-04-30");
		System.out.println(n);
		
		DayMgr pDayMgr =new DayMgr();
		String start_date="2015-02-15";
		String end_date="2015-07-14";
		pDayMgr.doInit(start_date,end_date);
		pDayMgr.getListPayInfo(start_date, end_date, "1");
		
	//	String month=pFunTools.getMonth();
	//	pFunTools.getTableNameByDate(month,MapTablesDef.bill_consume_type);
	//	System.out.println(pFunTools.test("2014-05"));
		*/
    }
}
