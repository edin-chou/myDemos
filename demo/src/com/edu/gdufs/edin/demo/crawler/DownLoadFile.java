package com.edu.gdufs.edin.demo.crawler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class DownLoadFile {
	
	/**
	 * 根据URl和网页类型生成需要保存的网页的文件名，去除URL中的非文件名字符
	 * @param url
	 * @param contentType
	 * @return
	 */
	public static String getFileNameByUrl(String url,String contentType){
		//移除http
		url=url.substring(7);
		//text/html类型
		if(contentType.indexOf("html")!=-1){
			url = url.replaceAll("[\\?/:*|<>\"]","_")+".html";
			return url;
		}else{
			return url.replaceAll("[\\?/:*|<>\"]", "_")+"."+contentType.substring(contentType.lastIndexOf("/")+1);
		}
	}
	
	/**
	 * 保存网页字节数组到本地文件，filePath为要保存的文件的相对地址
	 * @param data
	 * @param filePath
	 * @throws IOException
	 */
	public static void saveToLocal(InputStream inStream,String filePath){
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while((bytesRead=inStream.read(buffer, 0, 1024))!=-1){
				out.write(buffer,0,bytesRead);
			}
			out.flush();
			out.close();
			inStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 保存网页字节数组到本地文件，filePath 为要保存的文件的相对地址
	 * @param url
	 * @return
	 */
	public static String downloadFile(String url){
		
		String filePath = null;
		
		try {
			
			HttpClient httpclient = new DefaultHttpClient();
			//get方法请求对象

			System.out.println(">");
			System.out.println(url);
			System.out.println("<");
			
			HttpGet httpget = new HttpGet(url);
			//执行请求得到响应
			HttpResponse response = httpclient.execute(httpget);
			//验证响应状态
			System.out.println(response.getStatusLine());
			//若没有响应返回空
			if(response.getStatusLine().getStatusCode()!=200){
				return null;
			}
			//得到响应实体
			HttpEntity entity = response.getEntity();
			
			filePath = "e:\\news\\sohu\\"
					+ getFileNameByUrl(url, response.getLastHeader(
					"Content-Type").getValue());
			
			saveToLocal(entity.getContent(), filePath);
			
			System.out.println("save path:"+filePath);
		
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return filePath;
	}
	
	
	
	

}
