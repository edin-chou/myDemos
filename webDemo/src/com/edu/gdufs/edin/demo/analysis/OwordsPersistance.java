package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.Oword;

public class OwordsPersistance {
	
	public void readToOwordsTable(String path) throws IOException{
		
		File f = new File(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf-8"));
		String s = br.readLine();
		Session sess = HibernateUtil.currentSession();
		while(s!=null){
			Oword oword = new Oword();
			oword.setWord(s.trim());
			try{
				System.out.println("save:"+oword.getWord());
				sess.save(oword);
			}catch(HibernateException he){
				he.printStackTrace();
			}finally{
				s = br.readLine();
				continue;
			}
		}
		return ;
	}
	
	public static void main(String[] args) throws IOException{
		/*		OwordsPersistance owp = new OwordsPersistance();
		owp.readToOwordsTable("E:/排序/123.txt");*/
		OwordsPersistance owp = new OwordsPersistance();
		owp.readToOwordsTable("E:/排序/owords/result1365261001239.txt");
	}

}
