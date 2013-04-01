package com.edu.gdufs.edin.demo.analysis;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.Nletter;
import com.edu.gdufs.edin.demo.model.Nword;
import com.edu.gdufs.edin.demo.model.NwordsCounter;

public class WordWriter4DOCAndLDOF extends WordWriter {

	public WordWriter4DOCAndLDOF(String outputFileName,NwordsCounter nwordsCounter) throws IOException {
		super(outputFileName, nwordsCounter);
	}

	@Override
	public void write(CharNode root) throws IOException {
		double entropy = getEntropy(root);
		//System.out.println(root._character+"\t"+entropy+"\t"+ root.getCount()+"\t");
		if(entropy>=ENTROPY_THRESHOLD&&root._count>=COUNT_THRESHOLD){
			//_writer.write(root._character+"\t"+entropy+"\t"+root._count+"\n");
		}
		Set<Entry<Character, CharNode>> se = root._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),root._character.toString());
		}
		_writer.flush();
	}
	
	private void writeCountRecursion(CharNode charNode,String prefixString) throws IOException{
		String tmp = charNode.getCharacter()+prefixString;
		double entropy = getEntropy(charNode);
		//System.out.println(tmp+"\t"+tRate+"\t"+entropy+"\t"+ charNode.getCount()+"\t"+_totalCount+"\t"+tmp.length()+"\t"+Math.pow(_totalCount, tmp.length()));
		if(entropy>=ENTROPY_THRESHOLD&&charNode._count>=COUNT_THRESHOLD){
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createQuery("from Nword nw where nw.word=:word and nw.nwordsCountId=:nwcid")
					.setString("word", tmp)
					.setInteger("nwcid", _nwordsCounter.getId());
			Nword nword = (Nword)query.uniqueResult();
			if(nword!=null){
				nword.setLentropy(entropy);
				Double mi = getMutualInformation(nword);
				nword.setMutualInfo(mi);
				session.save(nword);
				tx.commit();
			}
			System.out.println(tmp);
			//_writer.write(tmp+"\t"+entropy+"\t"+charNode._count+"\n");
		}
		Set<Entry<Character, CharNode>> se = charNode._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),tmp);
		}
	}
	
	private Double getMutualInformation(Nword nword){
		char[] charArr =nword.getWord().toCharArray();
		int para1 = 0;
		for(int i=0;i<charArr.length-1;i++){
			para1+=Math.log(_nwordsCounter.getNlettersCount())/Math.log(2);
		}
		
		StringBuffer queryString = new StringBuffer();
		queryString.append("from Nletter nl where (");
		for(int i=0;i<charArr.length;i++){
			queryString.append(" nl.nletter='"+charArr[i]+"' or ");
		};
		String tmpStr =queryString.substring(0, queryString.lastIndexOf("or"))+") and nl.nwordsCountId='"+_nwordsCounter.getId()+"'";
		
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery(tmpStr);
		List list = query.list();
		Iterator i = list.iterator();
		int para2 = 0;
		while(i.hasNext()){
			Nletter nletter = (Nletter)i.next();
			para2+=Math.log(nletter.getCount())/Math.log(2);
		}
		double result = ((double)para1+Math.log(nword.getNwordCount())/Math.log(2))-((double)para2);
		return result;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		_writer.flush();
		_writer.close();
		super.finalize();
	}
	
	
}
