package com.edu.gdufs.edin.demo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientTest {

	
	public static void main(String args[]) throws ClientProtocolException, IOException{
		//HttpClient test
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpGet httpget = new HttpGet("http://news.ifeng.com/mainland/special/2013lianghui/renshi/detail_2013_03/14/23099811_0.shtml");
		
		HttpResponse response = httpclient.execute(httpget);
		
		System.out.println(response.getStatusLine());
		
		HttpEntity entity = response.getEntity();
		
		if(entity != null){
/*			InputStream instream = entity.getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(instream));
			String tmp = reader.readLine();
			while(tmp!=null){
				System.out.println(tmp);
				tmp=reader.readLine();
			}*/
			long l = entity.getContentLength();
			System.out.println(l);
		}
		
		
		System.out.println("=============================================================");
		
		httpget = new HttpGet("http://www.hao123.com/");
		
		response = httpclient.execute(httpget);
		
		System.out.println(response.getStatusLine());
		
		entity = response.getEntity();
		
		if(entity != null){
			
/*			InputStream instream = entity.getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(instream));
			String tmp = reader.readLine();
			while(tmp!=null){
				System.out.println(tmp);
				tmp=reader.readLine();
			}*/

			System.out.println(entity.getContentLength());
		}
	}
	
}
