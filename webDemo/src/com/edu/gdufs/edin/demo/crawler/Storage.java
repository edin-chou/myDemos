package com.edu.gdufs.edin.demo.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.gdufs.edin.demo.crawler.cobweb.Cobweb;
import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.News;
import com.edu.gdufs.edin.demo.model.NewsCounter;

public class Storage {
	
	final  Logger logger  =  LoggerFactory.getLogger(Storage.class );
	public static final int SAVESRATE_TIMEOUT = -1;
	public static final int SAVESRATE_UNSAVED = 0;
	public static final int SAVESRATE_SAVED = 1;
	
	private Session _session;
	private Transaction _transaction;
	private int _bufferCount;
	private String _url;
	private Cobweb _cobweb;
	private NewsCounter _newsCounter;
	
	public Storage(){
		_session = HibernateUtil.currentSession();
		/*_transaction = _session.beginTransaction();*/
		_bufferCount=0;
	}
	
	public void close(){
		HibernateUtil.closeSession();
	}
	
	/**
	 * 保存网页字节数组到本地文件，filePath 为要保存的文件的相对地址
	 * @param url
	 * @return
	 */
	public int downloadFile(String url,Cobweb cobweb){
		
		int returnFlag = SAVESRATE_TIMEOUT;
		
		try {
			
			HttpClient httpclient = new DefaultHttpClient();
			
			_url = url;
			
			logger.warn("DownLoad url:"+_url);

			//get方法请求对象
			HttpGet httpget = new HttpGet(_url);
			//执行请求得到响应
			HttpResponse response = httpclient.execute(httpget);
			//验证响应状态
			logger.warn("Status:"+response.getStatusLine().toString());
			//若没有响应返回空
			if(response.getStatusLine().getStatusCode()!=200){
				return SAVESRATE_TIMEOUT;
			}
			//得到响应实体
			HttpEntity entity = response.getEntity();

			_cobweb = cobweb;
			
			returnFlag = saveToDataBase(entity.getContent());
			
		
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return returnFlag;
		}
	}
	
	
	@SuppressWarnings({ "finally", "deprecation" })
	public int saveToDataBase(InputStream inStream){
		int returnFlag = SAVESRATE_UNSAVED;
		try {
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(inStream,_cobweb.getCharSet()));
			String tmp = br.readLine();
			while(tmp!=null){
				sb.append(tmp);
				tmp = br.readLine();
			}
			inStream.close();
			String html = sb.toString();
			Parser parser = Parser.createParser(html,_cobweb.getCharSet());
			NodeList nodes = parser.parse(_cobweb.getNodeFilter());
			int num = nodes.size();
			if(num>=4){//实际命中有这么多个标签才
				News news = new News();
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
				news.setSource(_url);
				news.setFrom(_cobweb.getFrom());
				_session.save(news);
				++_bufferCount;
				returnFlag=SAVESRATE_SAVED;
				_cobweb.idList.add(news.getId());
				if(_bufferCount%20==0){
					_session.flush();
					_session.clear();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (HibernateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
/*			HibernateUtil.closeSession();
			_session = HibernateUtil.currentSession();
			_transaction = _session.beginTransaction();*/
		} finally{
			return returnFlag;
		}
	}

}
