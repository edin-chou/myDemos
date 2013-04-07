package com.edu.gdufs.edin.demo.lucene;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

import com.edu.gdufs.edin.demo.model.News;

public class LuceneSearcher {
	
	private final int MAX_SEARCH_NUM = 10000;
	private final String INDEX_STORE_PATH = "e:/排序/Index";
	
	public List getTitlesByWords(String searchWords){
		String[] words = searchWords.trim().split("\\s");
		List TitleList = new LinkedList(); 
		FSDirectory fsDir;
		try {
			fsDir = SimpleFSDirectory.open(new File(INDEX_STORE_PATH));
			IndexReader ir = IndexReader.open(fsDir);  
            IndexSearcher search = new IndexSearcher(ir);
            BooleanQuery bQuery=new BooleanQuery();
            List termList = new LinkedList();
            for(String word:words){
            	char chars[] = word.toCharArray();
            	PhraseQuery query=new PhraseQuery();
            	for(char c:chars){
                	query.add(new Term("content",(new Character(c)).toString()));
            	}
            	bQuery.add(query, BooleanClause.Occur.MUST);
            }
            Sort sort = new Sort();
            SortField sf = new SortField("date", SortField.LONG, true);
            sort.setSort(sf);
            ScoreDoc[] docs = search.search(bQuery, MAX_SEARCH_NUM,sort).scoreDocs;
            for(ScoreDoc sd:docs){
            	News n = new News();
            	n.setId(Integer.parseInt(search.doc(sd.doc).get("id")));
            	n.setTitle(search.doc(sd.doc).get("title"));
            	n.setDate(new Date(Long.parseLong(search.doc(sd.doc).get("date"))));
            	TitleList.add(n);
            }
            return TitleList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] srgs){
		LuceneSearcher ls = new LuceneSearcher();
		List l = ls.getTitlesByWords("驾驶");
		Iterator i = l.iterator();
		while(i.hasNext()){
			News n = (News)i.next();
			System.out.println("id:"+n.getId()+"\ntitle:"+n.getTitle()+"\ndate:"+n.getDate());
		}
	}

}
