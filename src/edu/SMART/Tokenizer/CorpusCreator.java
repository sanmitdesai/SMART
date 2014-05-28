package edu.SMART.Tokenizer;
import java.awt.image.CropImageFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class CorpusCreator {
	/***************************************************************
	 * 
	 * @param srcFile
	 * @return
	 */
	public String readFile(String srcFile){
		BufferedReader br = null;
String output="";
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(srcFile));

			while ((sCurrentLine = br.readLine()) != null) {
				if(!sCurrentLine.equals(""
						+ ""))
				output+=this.extractText(sCurrentLine)+"\n";
				//break;
			}

		} catch (IOException e) {
			e.printStackTrace();
			output = e.toString();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				output = ex.toString();
			}
		}
		return output;
	}
/*******************************************************************
 * 
 * @param content
 * @param destFile
 */
	public void writeToFile(String content,String destFile){
		try {
			 
			//String content = "This is the content to write into file";
 
			File file = new File(destFile);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
			//System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*******************************************************
	 * 
	 * @param text
	 * @return
	 */
	public String extractText(String text){
		try{
			String output = text.substring(text.indexOf("text")+7, text.indexOf("source")-3);
		return output;
		}catch(Exception e){
			System.out.println("input : "+text);
			return null;
		}
	}
	/******************************************************************
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CorpusCreator obj = new CorpusCreator();
		//String example = obj.readFile("twitDB.txt");
		obj.writeToFile(obj.readFile("twitDB.txt"),"tweets.txt");
		//System.out.println(obj.extractText(example));
	}
}
