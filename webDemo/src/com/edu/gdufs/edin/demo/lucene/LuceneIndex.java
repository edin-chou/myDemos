package com.edu.gdufs.edin.demo.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.News;

public class LuceneIndex {
	
	private final int MAX_SEARCH_NUM = 6;
	private final String INDEX_STORE_PATH = "e:/排序/Index";
	
	
	public void IndexAllNews(){
		
		try {
			//索引
			FSDirectory fsDir = SimpleFSDirectory.open(new File(INDEX_STORE_PATH));
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,  
	                new StandardAnalyzer(Version.LUCENE_36));  
			conf.setOpenMode(OpenMode.CREATE);
			IndexWriter writer = new IndexWriter(fsDir, conf);  
	        //数据库
			Session sess = HibernateUtil.currentSession();
			Query query = sess.createQuery("from News");
			ScrollableResults news = query.scroll(ScrollMode.FORWARD_ONLY);
			int count = 0;
			//便利新闻建立索引
			while(news.next()){
				News n = (News)news.get(0);
				Field fid = new Field("id",n.getId().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED);
				Field fcontent = new Field("content",n.getContent(),Field.Store.NO,Field.Index.ANALYZED);
				Field ftitle = new Field("title",n.getTitle(),Field.Store.YES,Field.Index.ANALYZED);
				Field fdate = new Field("date",n.getDate().getTime()+"",Field.Store.YES,Field.Index.NOT_ANALYZED);
				Document doc = new Document();
				doc.add(fid);
				doc.add(fcontent);
				doc.add(ftitle);
				doc.add(fdate);
				System.out.println(ftitle);
				writer.addDocument(doc);
				if(++count%500==0){
					System.out.println("commit");
					writer.commit();
				}
			}
			System.out.println(count);
			writer.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void IndexIncreaseNews(List idList){
		try {
			FSDirectory fsDir = SimpleFSDirectory.open(new File(INDEX_STORE_PATH));
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,  
		            new StandardAnalyzer(Version.LUCENE_36));  
			conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
			IndexWriter writer = new IndexWriter(fsDir, conf);
			int count = 0;
				Session sess = HibernateUtil.currentSession();
				Iterator i = idList.iterator();
				while(i.hasNext()){
					int id = (Integer)i.next();
					News n = (News)sess.load(News.class, id);
					Field fid = new Field("id",n.getId().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED);
					Field fcontent = new Field("content",n.getContent(),Field.Store.NO,Field.Index.ANALYZED);
					Field ftitle = new Field("title",n.getTitle(),Field.Store.YES,Field.Index.ANALYZED);
					Field fdate = new Field("date",n.getDate().getTime()+"",Field.Store.YES,Field.Index.NOT_ANALYZED);
					Document doc = new Document();
					doc.add(fid);
					doc.add(fcontent);
					doc.add(ftitle);
					doc.add(fdate);
					System.out.println(ftitle);
					writer.addDocument(doc);
					if(++count%500==0){
						System.out.println("commit");
						writer.commit();
					}
				}
				System.out.println(count);
				writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void closeWriter() {
		FSDirectory fsDir;
		try {
			fsDir = SimpleFSDirectory.open(new File(INDEX_STORE_PATH));
			
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,  
	                new StandardAnalyzer(Version.LUCENE_36));  
	        IndexWriter writer = new IndexWriter(fsDir, conf);  
	        writer.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] srgs){
		LuceneIndex li = new LuceneIndex();
		li.IndexAllNews();
	}

}
