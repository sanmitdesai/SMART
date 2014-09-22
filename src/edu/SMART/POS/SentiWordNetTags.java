package edu.SMART.POS;

public class SentiWordNetTags {

	public String returnSentiWordNetTags(String temp){
//		String temp = word.substring(word.lastIndexOf("/")+1, word.length());
		if(temp.startsWith("N")){
			
			return "n";
		}
		else if(temp.startsWith("V")){
			
			return "v";
		}
		else if(temp.startsWith("R")){
			
			return "r";
		}
		else if(temp.startsWith("J")){
		
			return "a";
		}
		else{
			return null;
		}
	}

}
