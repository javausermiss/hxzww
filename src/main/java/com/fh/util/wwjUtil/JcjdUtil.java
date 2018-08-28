package com.fh.util.wwjUtil;

import com.fh.entity.system.AppUser;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 聚彩H5游戏注册登录&兑换金豆
 */
public class JcjdUtil{

    private static final String jcexurl = "http://106.75.129.211/WebService/WwjRegister.asmx/Exchange";
    private static final String jcreurl = "http://106.75.129.211/WebService/WwjRegister.asmx/Register";

    public static String doExchangeBean(AppUser appUser ,Integer beanNum)throws Exception{
        String jd = "";

        //兑换
        CloseableHttpClient httpClient_c = HttpClients.createDefault();
        HttpPost httpPost_c = new HttpPost(jcexurl);
        httpPost_c.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        Map<String, String> map_c = new HashMap<>();
        map_c.put("jd", String.valueOf(beanNum));
        map_c.put("userid", appUser.getJCID());
        StringBuilder sb_c = new StringBuilder();

        for (Map.Entry<String, String> entry : map_c.entrySet()) {
            sb_c.append(entry.getKey()).append("=")
                    .append(entry.getValue()).append("&");
        }
        String content_c = sb_c.toString().substring(0, sb_c.length() - 1);
        StringEntity s_c = new StringEntity(content_c,Charset.forName("UTF-8"));
        s_c.setContentEncoding("UTF-8");
        s_c.setContentType("text/x-www-form-urlencoded");
        httpPost_c.setEntity(s_c);

        CloseableHttpResponse response_c = httpClient_c.execute(httpPost_c);
        String code_c = String.valueOf(response_c.getStatusLine().getStatusCode());
        if (code_c.equals("200")) {
            //读返回数据
            String conResult = EntityUtils.toString(response_c.getEntity());
            JSONObject object = JSONObject.fromObject(conResult);
            jd = object.getString("jd");
        }
        response_c.close();
        httpClient_c.close();
        return jd;

    }

    public static String doRegUser(AppUser appUser,String userimg)throws Exception{
        String jcid = "";

        //注册
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(jcreurl);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        Map<String, String> map = new HashMap<>();
        map.put("name", appUser.getUSER_ID());
        map.put("mobile", "18900000000");
        map.put("password", "987654321");
        map.put("nickname", appUser.getNICKNAME());
        map.put("userimg", userimg);
        map.put("userid", "null");

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=")
                    .append(entry.getValue()).append("&");
        }
        String content = sb.toString().substring(0, sb.length() - 1);

        StringEntity s = new StringEntity(content,Charset.forName("UTF-8"));
        System.out.println(s);
        s.setContentEncoding("utf-8");
        s.setContentType("text/x-www-form-urlencoded");
        httpPost.setEntity(s);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        String code = String.valueOf(response.getStatusLine().getStatusCode());
        System.out.println(code);
        if (code.equals("200")) {
            //读返回数据
            String conResult = EntityUtils.toString(response.getEntity());
            JSONObject object = JSONObject.fromObject(conResult);
            jcid = object.getString("uid");
        }
        response.close();
        httpClient.close();
        return jcid;

    }

    public static String doLogin(AppUser appUser,String userimg)throws Exception{
        String jcid = "";

        //注册
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(jcreurl);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        Map<String, String> map = new HashMap<>();
        map.put("name", appUser.getUSER_ID());
        map.put("mobile", "18900000000");
        map.put("password", "987654321");
        map.put("nickname", appUser.getNICKNAME());
        map.put("userimg", userimg);
        map.put("userid", appUser.getJCID());

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=")
                    .append(entry.getValue()).append("&");
        }
        String content = sb.toString().substring(0, sb.length() - 1);
        System.out.println(content);

        StringEntity s = new StringEntity(content,Charset.forName("UTF-8"));
        System.out.println(s);
        s.setContentEncoding("UTF-8");
        s.setContentType("text/x-www-form-urlencoded");
        httpPost.setEntity(s);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        String code = String.valueOf(response.getStatusLine().getStatusCode());
        System.out.println(code);
        if (code.equals("200")) {
            //读返回数据
            String conResult = EntityUtils.toString(response.getEntity());

            JSONObject object = JSONObject.fromObject(conResult);
            jcid = object.getString("uid");
        }
        response.close();
        httpClient.close();
        return jcid;
    }

    /**
     * 查询用户当前的金豆数量
     * @param appUser
     * @return
     * @throws Exception
     */
    public static String getUserBean(AppUser appUser)throws Exception{
        String jdnum= "";
        //注册
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet("http://106.75.129.211/WebService/WwjRegister.asmx/GetJD?uid="+appUser.getJCID());
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String code = String.valueOf(response.getStatusLine().getStatusCode());
        System.out.println(code);
        if (code.equals("200")) {
            //读返回数据
            String conResult = EntityUtils.toString(response.getEntity());
            JSONObject object = JSONObject.fromObject(conResult);
            jdnum = object.getString("msg");
        }
        response.close();
        httpClient.close();
        return jdnum;

    }

}
