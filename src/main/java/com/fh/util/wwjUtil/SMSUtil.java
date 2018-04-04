package com.fh.util.wwjUtil;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.fh.util.Logger;
/**
 * 短信发送工具类
 */
public class SMSUtil {
	
	private static Logger logger = Logger.getLogger(SMSUtil.class);
	   
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	logger.error("短信发送失败：", e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void veriCode1(String phone, String Code) {
        try{
            String url = "http://jk.106api.cn/smsUTF8.aspx?";
            String username = "38580643";
            String password = "E142620406F41F48B7311561F8B96CD3";
            String gwid = "85ab540";
            String content = URLEncoder.encode("【炎亿网络】您的验证码是","UTF-8") + Code;
            String postData = "type=send&username=" + username + "&password=" + password + "&gwid=" + gwid + "&mobile=" + phone + "&message=" + content + "";
            //SMSUtil.sendPost(url, postData);
            logger.info("短信发送内容："+content);
            String smsReturn=SMSUtil.sendPost(url, postData);
            logger.info("短信发送返回："+smsReturn);

        } catch (UnsupportedEncodingException e) {
        	logger.error("veriCode1：", e);
        }
    }
}
