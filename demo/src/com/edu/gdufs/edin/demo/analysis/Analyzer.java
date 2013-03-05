package com.edu.gdufs.edin.demo.analysis;

import java.io.File;

public abstract class Analyzer {

	protected File _inputFile;
	
	protected String _outputPath;
	
	public Analyzer(File inputFile,String OutputPath){
		_inputFile = inputFile;
		_outputPath = OutputPath;
	}

	public void setInputFile(File inputFile) {
		// TODO Auto-generated method stub
		_inputFile = inputFile;
	}

	public void setOutputPath(String OutputPath){
		// TODO Auto-generated method stub
		_outputPath = OutputPath;
	}

	public abstract String execute() throws Exception;
	
	protected boolean checkFile(){
		String s = _inputFile.getName();
		if(s.substring(s.indexOf("."), s.length()).equals(".txt")){
			return true;
		}else{
			return false;
		}
	}
	
}
