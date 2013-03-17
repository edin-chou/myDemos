package cn.edu.gdufs.jsjx.service.httpclient;

import java.io.IOException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientGetHtml {

	/**
	 * 爬取整个页面的内内容
	 * @param url
	 * @return
	 */
    public static String gethtml(String url) {
        String htmlContent="";
        //构造HttpClient的实例        
        HttpClient httpClient = new HttpClient();
        //创建GET方法的实例       
        GetMethod getMethod = new GetMethod(url);
        //设置页面编码
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF8");
        //设置访问源（即客户端）为浏览器    
        httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:8.0.1) Gecko/20100101 Firefox/8.0.2");
        //使用系统提供的默认的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
            //执行getMethod
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("获取失败: "
                        + getMethod.getStatusLine());
            }
            //读取内容
            byte[] responseBody = getMethod.getResponseBody();
            //处理内容
            //System.out.println(new String(responseBody));
            htmlContent=new String(responseBody,"UTF-8");
        } catch (HttpException e) {
            //发生致命的异常，可能是协议不对或者返回的内容有问题          
        	System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            //发生网络异常
            e.printStackTrace();
        } finally {
            //释放连接
            getMethod.releaseConnection();
        }
        return htmlContent;
    }
}
