package com.fh.util.wwjUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fh.util.FastDFSClient;
import org.apache.log4j.Logger;

public class FaceImageUtil {
    private static Logger logger = Logger.getLogger(FaceImageUtil.class);
   
	/**
	 * 下载用户注册的头像，上传到文件服务器
	 * @param url
	 * @return
	 * @throws IOException
	 */
    public static String downloadImage(String url) throws IOException {
        String decodeUrl =  URLDecoder.decode(url,"utf-8");
        logger.info("解码后的URL----------------------------->"+decodeUrl);
        String faceName="";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet();
        CloseableHttpResponse response=null;
        
        try{
        	//请求远程图片
        	httpget.setURI(URI.create(decodeUrl));
            response = httpClient.execute(httpget);

            
            //获取文件流
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                byte[] byteArray = EntityUtils.toByteArray(entity);
                faceName=FastDFSClient.uploadFile(byteArray, "default.png"); //上传到文件服务器
            }
        }catch(Exception ex){
        	ex.printStackTrace();
        }finally{
        	   response.close();
               httpClient.close();
        }
        logger.info("返回的faceName----------------------------->"+faceName);
        return faceName;
    }


    public static void main(String[] a)throws Exception{

        FaceImageUtil.downloadImage("http://imgtu.5011.net/uploads/content/20170209/4934501486627131.jpg");


    }
}
