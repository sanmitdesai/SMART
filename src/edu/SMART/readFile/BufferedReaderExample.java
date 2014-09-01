package edu.SMART.readFile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
 
public class BufferedReaderExample {
 
	public static void main(String[] args) {
 
		BufferedReader br = null;
 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("F:\\ukraine_full.txt"));
			int count=0;
			while ((sCurrentLine = br.readLine()) != null) {
				if(count==10){
					break;
				}
				System.out.println(sCurrentLine);
				count++;
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 
	}
}