package com.edu.gdufs.edin.demo.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class Analyzer4LDOF extends Analyzer {
	
	public Analyzer4LDOF(File inputFile,String OutputPath){
		super(inputFile,OutputPath);
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		if(checkFile()){
			HeadCharNode hcn = new HeadCharNode();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(_inputFile)));
			String ss = br.readLine();
			while(ss!=null){
				hcn.addWords(new StringBuffer(ss));
				ss = br.readLine();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out)); 
			hcn.writeCount(bw);
			bw.close();
		}else{
			throw new Exception("Bad inputFile for Analyzer,please chose a \".txt\" file for it.");
		}
		return null;
	}

}
