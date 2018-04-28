package webtools.com.utils;



import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import it.sauronsoftware.jave.AudioAttributes;
//import it.sauronsoftware.jave.Encoder;
//import it.sauronsoftware.jave.EncodingAttributes;


public class CommonUtil {
	private  Logger log = LoggerFactory.getLogger(CommonUtil.class);

	/**
	 * 获取文件后缀
	 * @param name 文件名称
	 * @return 后缀名称
	 *
	 * @author 唐德龙
	 * 2014年12月20日
	 */
	public  String getImageSuffix(String name){
		String suffix = "";
		int index = name.lastIndexOf(".");
		if(index<0){
			name+=".jpg";
			index = name.lastIndexOf(".");
		}
		suffix = name.substring(index);
		return suffix;
	}
	
	/**
	 * 去除后缀
	 * @param src 源字符串
	 * @param suffix 后缀名
	 * @return
	 */
	public  String removeSuffix(String src,String suffix){
		String temp = src;
		if(StringUtils.isNotBlank(src) && StringUtils.isNotBlank(suffix)){
			try{
				int index = src.lastIndexOf(suffix);
				temp = src.substring(0, index);
			}catch(Exception e){
				temp = src;
			}
		}
		return temp;
	}
	
	
	/**
	 * 获取当前时间日期
	 * @param pattern 日期格式   如:yyyy-MM-dd HH:mm:ss
	 * @return 日期字符串
	 * 
	 * @author 唐德龙
	 * 2014年12月23日
	 */
	public  String getCurrentDate(String pattern){
		return getDateStr(new Date(),pattern);
	}
	
	/**
	 * 根据date和时间格式，返回时间字符串
	 * @param date Date类型日期
	 * @param pattern 日期格式   如:yyyy-MM-dd HH:mm:ss
	 * @return 返回时间字符串
	 *
	 * @author 唐德龙
	 * 2014年12月23日
	 */
	public  String getDateStr(Date date,String pattern){
		SimpleDateFormat sdf = null;
		if(pattern != null){
			String temp = pattern;
			try{
				sdf = new SimpleDateFormat(temp);
			}catch(IllegalArgumentException e){
				temp = "yyyy-MM-dd HH:mm:ss";
				sdf = new SimpleDateFormat(temp);
			}
		}
		return sdf.format(date);
	}
	
	
	
	/**
	 * 根据date获取该日期的周一和周日日期
	 * @return 日期字符串
	 *
	 * @author 唐德龙
	 * 2014年12月23日
	 */
	public  String[] getWeek(Date date){
		String monday = "";
		String sunday = "";
		if(date != null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
			cal.add(Calendar.DATE, -day_of_week);
			monday = getDateStr(cal.getTime(), "yyyy-MM-dd");
			cal.add(Calendar.DATE, 6);
			sunday = getDateStr(cal.getTime(), "yyyy-MM-dd");
		}
		return new String[]{monday,sunday};
	}
	
	
	/**
	 * 根据date获取该日期的1号和月末日期
	 * @return 日期字符串
	 *
	 * @author 唐德龙
	 * 2014年12月23日
	 */
	public  String[] getMonth(Date date){
		String start = "";
		String end = "";
		if(date != null){
			Calendar calendar = Calendar.getInstance(); 
			calendar.setTime(date);
		    calendar.set(Calendar.DAY_OF_MONTH, calendar   
		                .getActualMinimum(Calendar.DAY_OF_MONTH)); 
		    start = getDateStr(calendar.getTime(), "yyyy-MM-dd");
		    calendar.set(Calendar.DAY_OF_MONTH, calendar   
	                .getActualMaximum(Calendar.DAY_OF_MONTH));
		    end = getDateStr(calendar.getTime(), "yyyy-MM-dd");
		}
		return new String[]{start,end};
	}
	
	/**
	 * 根据日期转换timestamp ，根式为yyyy-MM-dd
	 * @param data 日期字符串
	 * @return Timestamp
	 *
	 * @author 唐德龙
	 * 2015年1月7日
	 */
	public  Timestamp getTimestap(String data){
        return getTimestap(data,"yyyy-MM-dd");
	}
	
	
	/**
	 * 根据日期转换timestamp 
	 * @param data 日期字符串
	 * @param pattern 日期格式：如yyyy-MM-dd
	 * @return Timestamp
	 *
	 * @author 唐德龙
	 * 2015年1月7日
	 */
	public  Timestamp getTimestap(String data,String pattern){
		Date date = getDateByStr(data,pattern);
        Timestamp ts = new Timestamp(date.getTime());
        return ts;
	}
	
	
	/**
	 * 根据指定的字符串获取Date时间
	 * @param data 如：2011-10-10
	 * @param pattern 日期格式 如：yyyy-MM-dd
	 * 
	 * @author 唐德龙
	 * 2015年1月9日
	 */
	public  Date getDateByStr(String data,String pattern){
		Date date = new Date();  
        //注意format的格式要与日期String的格式相匹配  
        DateFormat sdf = new SimpleDateFormat(pattern);
        try {
			date = sdf.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return date;
	}
	
	
	/**
	 * 获取源日期加指定天数后的日期字符串
	 * @param srcDate 源日期
	 * @param day 加多少天
	 * @param pattern 日期格式 yyyy-MM-dd
	 *
	 * @author 唐德龙
	 * 2015年1月9日
	 */
	public  String getDateByAdd(String srcDate,int day,String pattern){
		Date date = getDateByStr(srcDate,"yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(Calendar.DAY_OF_MONTH, day);
		String str = getDateStr(cd.getTime(),"yyyy-MM-dd");
		return str;
	}
	
	/**
	 * 媒体格式改变为MP3格式
	 * @param sourcePath 源路径
	 * @param targetPath 目标路径
	 *
	 * @author 唐德龙
	 * 2015年2月4日
	 */
	public  void changeToMp3(String sourcePath, String targetPath) {  
//        File source = new File(sourcePath);  
//        File target = new File(targetPath);  
//        AudioAttributes audio = new AudioAttributes();  
//        audio.setCodec("libmp3lame");  
//        EncodingAttributes attrs = new EncodingAttributes();  
//        attrs.setFormat("mp3");  
//        attrs.setAudioAttributes(audio);
//  
//        Encoder encoder = new Encoder();  
//        try {  
//            encoder.encode(source, target, attrs);  
//        } catch (Exception e) {  
//        	log.warn("音频转换为MP3格式:"+e.toString());  
//        }finally{
//        	source.delete();
//        }
    }
	
	
	/**
	 * 数组去重
	 * @param str 数组
	 * @return 结果
	 * @return
	 */
	public  String[] ArraysReplayDelRepeat(String[] str){
		List<String> list = new LinkedList<String>();  
	    for(int i = 0; i < str.length; i++) {  
	        if(!list.contains(str[i])) {  
	            list.add(str[i]);  
	        }  
	    } 
	    return (String[])list.toArray(new String[list.size()]);  
	}
}
