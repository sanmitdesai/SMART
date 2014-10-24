package edu.SMART.ReadWriteFile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import edu.SMART.sentimentAnalyzer.EmotionAnalyzer;
import edu.SMART.stemmer.Cleaning;

public class ReadFile {
	
	 public void read(String fileName){
		 BufferedReader br = null;
		 Cleaning objCleaning = new Cleaning();
		 EmotionAnalyzer objEmotionAnalyzer = new EmotionAnalyzer();
		 
		 
			try {
	 
				String input;
	 
				br = new BufferedReader(new FileReader(fileName));
				int count =0;
				
//				while ((sCurrentLine = br.readLine()) != null) {
				while (count<200) {
					input = br.readLine();
					//remove username
					input = objCleaning.removeUserNameFromTweet(input);

					// remove RT symbol
					input = objCleaning.removeRTFromTweet(input);

					//removes # sign from hashtags
					input = objCleaning.removeHashTagFromTweet(input);
					
					input = objCleaning.removeUrl(input);
					
					System.out.println(input);
					System.out.println(objEmotionAnalyzer.analysis(input));
					
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

	 
		public static void main(String[] args) {
	 
			ReadFile objReadFile = new ReadFile();
			objReadFile.read("E:\\Dropbox\\workspace\\SMART\\data\\tweetListV1.txt");
	 
		}
	
}
