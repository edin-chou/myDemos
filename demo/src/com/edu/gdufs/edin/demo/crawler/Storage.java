package com.edu.gdufs.edin.demo.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.gdufs.edin.demo.crawler.nodefilters.SinaFilter;
import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.News;

public class Storage {
	
	final  Logger logger  =  LoggerFactory.getLogger(MyCrawler.class );
	private Session _session;
	private Transaction _transaction;
	private int _bufferCount;
	
	public Storage(){
		_session = HibernateUtil.currentSession();
		_transaction = _session.beginTransaction();
		_bufferCount=0;
	}
	
	public void close(){
		_transaction.commit();
		HibernateUtil.closeSession();
	}
	
	/**
	 * 保存网页字节数组到本地文件，filePath 为要保存的文件的相对地址
	 * @param url
	 * @return
	 */
	public String downloadFile(String url,NodeFilter nodeFilter,String charSet){
		
		try {
			
			HttpClient httpclient = new DefaultHttpClient();

			logger.warn("DownLoad url:"+url);

			//get方法请求对象
			HttpGet httpget = new HttpGet(url);
			//执行请求得到响应
			HttpResponse response = httpclient.execute(httpget);
			//验证响应状态
			logger.warn("Status:"+response.getStatusLine().toString());
			//若没有响应返回空
			if(response.getStatusLine().getStatusCode()!=200){
				return null;
			}
			//得到响应实体
			HttpEntity entity = response.getEntity();
			
			saveToDataBase(entity.getContent(), url,nodeFilter , charSet);
		
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return url;
	}
	
	
	@SuppressWarnings("finally")
	public String saveToDataBase(InputStream inStream,String source,NodeFilter nodeFilter,String charSet){
		try {
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(inStream,charSet));
			String tmp = br.readLine();
			while(tmp!=null){
				sb.append(tmp);
				tmp = br.readLine();
			}
			inStream.close();
			String html = sb.toString();
			Parser parser = Parser.createParser(html,charSet);
			News news = new News();
			NodeList nodes = parser.parse(nodeFilter);
			int num = nodes.size();
			if(num>=4){//实际命中有这么多个标签才
				for (int i = 0; i < num; i ++) {
				    TagNode node = (TagNode)nodes.elementAt(i);
				    String tagName = node.getTagName();
				    String tagId = node.getAttribute("id");
				    if(tagId!=null){
				    	if(tagId.equalsIgnoreCase("media_name")){
				    		news.setMediaid(node.getAttribute("content").toString());
				    	}else if(tagId.equalsIgnoreCase("pub_date")){
				    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
				    		String tmpStr = node.toPlainTextString();
				    		news.setDate(new Date(sdf.parse(tmpStr.substring(0, tmpStr.lastIndexOf('日')+1)).getTime()));
				    	}else if(tagId.equalsIgnoreCase("body_content")){
				    		news.setContent(node.toHtml());
				    	}
				    }else if(tagName.equalsIgnoreCase("title")){
				    	news.setTitle(node.toPlainTextString());
				    }
				}
				news.setSource(source);
				_session.save(news);
				if(++_bufferCount%20==0){
					_transaction.commit();
					_session.flush();
					_session.clear();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  catch (HibernateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			HibernateUtil.closeSession();
			_session = HibernateUtil.currentSession();
			_transaction = _session.beginTransaction();
			return null;
		} finally{
			return source;
		}
	}

}
