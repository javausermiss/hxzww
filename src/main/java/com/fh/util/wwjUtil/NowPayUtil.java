package com.fh.util.wwjUtil;


import com.fh.util.Logger;
import com.fh.util.PropertiesUtils;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.conn.ssl.TrustStrategy;

/**
 * 现在支付工具
 */
public class NowPayUtil {
    private static Logger logger = Logger.getLogger(NowPayUtil.class);

    public static String doPost(String parameters) throws Exception {
        String code = "";
        String conResult = "";
        CloseableHttpClient httpClient = buildSSLCloseableHttpClient();
        HttpPost httpPost = new HttpPost(PropertiesUtils.getCurrProperty("nowpay.nowPayServer.url") + parameters);
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        code = String.valueOf(response.getStatusLine().getStatusCode());
        if (code.equals("200")) {
            //读返回数据
            conResult = EntityUtils.toString(response.getEntity());
            System.out.print(conResult);

        }
        response.close();
        httpClient.close();
        logger.info("Code---------------" + code);
        return conResult;
    }

    private static CloseableHttpClient buildSSLCloseableHttpClient() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            //信任所有
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        //ALLOW_ALL_HOSTNAME_VERIFIER:这个主机名验证器基本上是关闭主机名验证的,实现的是一个空操作，并且不会抛出javax.net.ssl.SSLException异常。
        // 只允许使用TLSv1.1协议
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.1"}, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }


    /**
     * 创建SSL连接
     *
     * @return
     */
    private static CloseableHttpClient createSSLClientDefault() {
        try {
            @SuppressWarnings("deprecation")
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

}
