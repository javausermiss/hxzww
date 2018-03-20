package com.fh.util;

import java.math.BigDecimal;

public class NumberUtils {
	
   public static int parseInt(String str, int defaultNum){
    	int num = 0;
    	try{
    		if(str==null || "".equals(str)){
    			return defaultNum;
    		}
    		num = Integer.parseInt(str);
    	}catch(Exception e){
    		num = defaultNum;
    	}
    	return num;
   }
   
   public static int parseInt(String str){
    	return parseInt(str, 0);
   }

   public static String parseString(String str) {
        if (str == null)
            return "";
        return str;
    }

   public static long parseLong(String str)
    {
        if (str == null)
            return 0;
        try {
            return Long.parseLong(str);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

   public static double parseDouble(String str) {
        return parseDouble(str, 0.0d);
    }

   public static double parseDouble(String str, double defaultNum)
   {
        try {
            return Double.parseDouble(str);
        }
        catch (Exception e)
        {
            return defaultNum;
        }
    }
   public static double round(double value){
	   return round(value, 2);
   }
   
   public static String format(String str, int scale){
	   String mat = "%." + scale + "f";
	   //"%.4f"
	   return String.format(mat, str);
   }
   

   
	public static double round(double value, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal v = BigDecimal.valueOf(value);
		return v.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 两个数据相减
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static String sub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).toString();
	}
	
	/**
	 * 两个数据相加
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static String add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).toString();
	}
	
	/**  
	* 提供精确的乘法运算。  
	* @param v1 被乘数  
	* @param v2 乘数  
	* @return 两个参数的积  
	*/  
	public static int mul(String v1,String v2){   
		BigDecimal b1 = new BigDecimal(v1);   
		BigDecimal b2 = new BigDecimal(v2);   
		return b1.multiply(b2).intValue();   
	}   
	
	/**  
	* 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指  
	* 定精度，以后的数字四舍五入。  
	* @param v1 被除数  
	* @param v2 除数  
	* @param scale 表示表示需要精确到小数点以后几位。  
	* @return 两个参数的商  
	*/  
	public static double div(double v1,double v2,int scale){   
		if(scale<0){   
		throw new IllegalArgumentException(   
				"The scale must be a positive integer or zero");   
		}   
		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();   
	}   
	
	/**
	 * 人民币 元转分
	 * @param money
	 * @return
	 */
	public static String RMBYuanToCent(String money){
		 BigDecimal b1 = new BigDecimal(money);
		 BigDecimal b2 = new BigDecimal(100);
		return b1.multiply(b2).setScale(0,BigDecimal.ROUND_DOWN).toString();
	}
	/**
	 * 人民币 分转元
	 * @param money
	 * @return
	 */
	public static String RMBCentToYuan(String money){
		 BigDecimal b1 = new BigDecimal(money);
		 BigDecimal b2 = new BigDecimal(100);
		return b1.divide(b2).setScale(2,BigDecimal.ROUND_DOWN).toString();
	}
	
	/**
	 * 人民币 分转元
	 * @param money
	 * @return
	 */
	public static String RMBCentToYuan(int money){
		 BigDecimal b1 = new BigDecimal(money);
		 BigDecimal b2 = new BigDecimal(100);
		return b1.divide(b2).setScale(2,BigDecimal.ROUND_DOWN).toString();
	}
	
	public static String addMoney(String money1,String money2){
		 BigDecimal b1 = new BigDecimal(money1);
		 BigDecimal b2 = new BigDecimal(money2);
		return b1.add(b2).toString();
	}

	/**
	 * 获取16位随机数字
	 * @return
	 */
	public static String get16RandomNum() {
		return "" + (long) (Math.random() * 10000000000000000l);
	}
	
	public static void main(String[] args) {
		System.out.println(RMBYuanToCent("0.02"));
	}
}