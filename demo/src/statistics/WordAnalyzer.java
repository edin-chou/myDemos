package statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WordAnalyzer {

	private File _inputFile;
	
	private String _outputPath;
	
	public WordAnalyzer(File InputFile,String OutputPath){
		this._inputFile = InputFile;
		this._outputPath = OutputPath;
	}
	
	public String generateDOCAndRDOF() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(this._inputFile));
		
		for(String s = br.readLine();s!=null&&s!="";s=br.readLine()){
			int len = s.length();
			
		}
		return _outputPath;
	}
	
	public String generateLDOF(){
		
		return _outputPath;
		
	}
}
