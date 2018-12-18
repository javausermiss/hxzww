package com.fh.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** 
 * 说明：日期处理
 * 创建人：FH Q313596790
 * 修改时间：2015年11月24日
 * @version
 */
public class DateUtil {
	
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat sdfTimes = new SimpleDateFormat("yyyyMMddHHmmss");
	private final static SimpleDateFormat sdfTimesss = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private final static SimpleDateFormat sdf_hhmmss = new SimpleDateFormat("HHmmss");

	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getSdfTimes() {
		return sdfTimes.format(new Date());
	}
	
	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}
	
	/**
	 * 获取YYYYMMDD格式
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss:SSS格式
	 * @return
	 */
	public static String getTimeSSS() {
		return sdfTimesss.format(new Date());
	}
	
	/**
	 * 获取HHmmss格式
	 * @return
	 */
	public static String getTimeHHmmss() {
		return sdf_hhmmss.format(new Date());
	}

	/**
	* @Title: compareDate
	* @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean  
	* @throws
	* @author fh
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//long aa=0;
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}
	 
	/**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;
        
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
      
        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);
        
        return dateStr;
    }
    
    /**
     * 获取当前日期，月变化的时间字符串
     * @param months
     * @return
     */
    public static String getCalendarByMonths(int months){
    	Calendar canlendar = Calendar.getInstance(); // java.util包
    	canlendar.setTime(new Date());
    	canlendar.add(Calendar.MONTH, -1);
    	Date m = canlendar.getTime();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	String mon = format.format(m);
    	return mon;
    }
    
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        return dateStr;
    }

	public  static String getNumOrder(String num,String ddtime){
		//订单号拼接格式2018011500000897   16位
		int ii=num.length()+8;
		StringBuffer sBuffer = new StringBuffer();
		for(int i=0;i<(16-ii);i++){
			sBuffer.append("0");
		}
		return getTimeNum(ddtime)+sBuffer+num;
	}

	private static String getTimeNum(String time){
		return time.substring(0,10).replace("-","");
	}


	public static String getMinMonthDate(String repeatDate){
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			if(StringUtils.isNotBlank(repeatDate) && !"null".equals(repeatDate)){
				calendar.setTime(dft.parse(repeatDate));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

		StringBuilder sb = new StringBuilder(dft.format(calendar.getTime()));
		sb.append(" 00:00:00");
		return sb.toString();
    }


	public static String getMaxMonthDate(String repeatDate) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			if (StringUtils.isNotBlank(repeatDate) && !"null".equals(repeatDate)) {
				calendar.setTime(dft.parse(repeatDate));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		StringBuilder sb = new StringBuilder(dft.format(calendar.getTime()));
		sb.append(" 23:59:59");
		return sb.toString();
	}






		public static void main(String[] args) {
    	System.out.println(getDays());
    	System.out.println(getAfterDayWeek("3"));

    }

}
