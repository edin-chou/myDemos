package com.edu.gdufs.edin.demo.analysis;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.Nletter;
import com.edu.gdufs.edin.demo.model.Nword;
import com.edu.gdufs.edin.demo.model.NwordsCounter;

public class WordWriter4DOCAndRDOF extends WordWriter {

	public WordWriter4DOCAndRDOF(String outputFileName,NwordsCounter nwordsCounter) throws IOException {
		super(outputFileName,nwordsCounter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void write(CharNode root) throws IOException {
		double entropy = getEntropy(root);
		//System.out.println(root._character+"\t"+entropy+"\t"+ root.getCount()+"\t");
		if(entropy>=ENTROPY_THRESHOLD&&root._count>=COUNT_THRESHOLD){
			Nletter nletter = new Nletter();
			nletter.setNletter(root.getCharacter().toString());
			nletter.setCount(root.getCount());
			nletter.setNwordsCountId(_nwordsCounter.getId());
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			session.save(nletter);
			tx.commit();
			_nwordsCounter.setNlettersCount(_nwordsCounter.getNlettersCount()+root.getCount());
			_writer.write(root._character+"\t"+entropy+"\t"+root._count+"\n");
		}
		Set<Entry<Character, CharNode>> se = root._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),root._character.toString());
		}
		_writer.flush();
	}
	
	private void writeCountRecursion(CharNode charNode,String prefixString) throws IOException{
		String tmp = prefixString+charNode.getCharacter();
		double entropy = getEntropy(charNode);
		//System.out.println(tmp+"\t"+tRate+"\t"+entropy+"\t"+ charNode.getCount()+"\t"+_totalCount+"\t"+tmp.length()+"\t"+Math.pow(_totalCount, tmp.length()));
		if(entropy>=ENTROPY_THRESHOLD&&charNode._count>=COUNT_THRESHOLD){
			Nword nword = new Nword();
			nword.setWord(tmp);
			nword.setNwordCount(charNode._count);
			nword.setNwordsCountId(_nwordsCounter.getId());
			nword.setRentropy(entropy);
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			session.save(nword);
			tx.commit();
			_writer.write(tmp+"\t"+entropy+"\t"+charNode._count+"\n");
		}
		Set<Entry<Character, CharNode>> se = charNode._charNodeMap.entrySet();
		for(Iterator i = se.iterator();i.hasNext();){
			Entry<Character,CharNode> e = (Entry<Character,CharNode>)i.next();
			writeCountRecursion(e.getValue(),tmp);
		}
	}
	

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		session.update(_nwordsCounter);
		tx.commit();
		_writer.flush();
		_writer.close();
		super.finalize();
	}

}
