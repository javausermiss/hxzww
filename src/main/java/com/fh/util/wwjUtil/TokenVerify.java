package com.fh.util.wwjUtil;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import com.fh.util.MD5;
import net.sf.json.JSONObject;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.MessageDigest;

/**
 * 微信登录token效验
 */

public class TokenVerify {
    private final static String ckey = "rcWhucD6efT=";
    private final static String cid = "aed34f22d80e430a868c083da0e4de07";


    public static String verify(String acctoken) {
        String code = null;
        try {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            System.out.println(timestamp);
            String signatrue = md5(TokenVerify.getSignature(timestamp, ckey, cid, acctoken));
            System.out.println(signatrue);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://test.api.hxwolf.com:7002/userVisit?"+"cid=" + cid +
                    "&timestamp=" + timestamp+ "&access_token=" + acctoken + "&signatrue=" + signatrue );
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
            HttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                //读返回数据
                String conResult = EntityUtils.toString(response.getEntity());
                JSONObject object = new JSONObject();
                object = object.fromObject(conResult);//将字符串转化为json对象
                code = String.valueOf(object.get("msg"));
                System.out.println("right!!!!!!!!!!!!!!");
            } else {
                System.out.println("error!!!!!!!!!!!!!!");
                return RespStatus.fail().toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;

    }

    public static String getSignature(String timestamp, String secret, String cid, String access_token) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        HashMap<String, String> h = new HashMap<>();
        h.put("cid", cid);
        h.put("timestamp", timestamp);
        h.put("access_token", access_token);
        Map<String, String> sortedParams = new TreeMap<String, String>(h);
        Set<Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        for (Entry<String, String> param : entrys) {
            basestring.append(param.getKey()).append('=').append(param.getValue()).append('&');
        }
        basestring.delete(basestring.length() - 1, basestring.length()).append(secret);
        System.out.println("====================" + basestring + "=======================");
        return basestring.toString();

    }

    public static String md5(String text) {
        try {


            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(text.getBytes("UTF8"));
            return hex(m.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String hex(byte[] bits) {
        StringBuffer sb = new StringBuffer();
        for (byte bt : bits) {
            sb.append(Integer.toHexString((bt & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static void main(String[] a) throws IOException {

       // String s = TokenVerify.md5(getSignature("20171214145904274", "rcWhucD6efT=", "aed34f22d80e430a868c083da0e4de07", "a1ad355608497f73f0bc31831197e103235b5b28c95967a76680ba7cb9cca23f949ebc1d236ce4b7e95e1d4e9dd88da01dcbf0e7571db6031af35f379932d0237d91e33b90ce235bd10382ec9b684191c5aaeec4"));

        String s1 = TokenVerify.verify("64aa295a37af4df8b3075c8ed302294c");
        System.out.println(s1);
    }


}
