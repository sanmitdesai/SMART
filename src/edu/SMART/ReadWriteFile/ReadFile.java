package edu.SMART.ReadWriteFile;
//import SummaryPrep;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import edu.SMART.Main.NetClientGet;
import edu.SMART.Main.SummaryPrep;
import edu.SMART.sentimentAnalyzer.EmotionAnalyzer;
import edu.SMART.stemmer.Cleaning;

public class ReadFile {
	
	 public void read(String fileName){
		 BufferedReader br = null;
		 Cleaning objCleaning = new Cleaning();
		 EmotionAnalyzer objEmotionAnalyzer = new EmotionAnalyzer();
		 SummaryPrep objSummaryPrep = new SummaryPrep();
		 NetClientGet objNetClientGet = new NetClientGet();
		 
			try {
	 
				String input;
	 
				br = new BufferedReader(new FileReader(fileName));
				int count =0;
				
//				while ((sCurrentLine = br.readLine()) != null) {
				while (count<3) {
					long startTime = System.nanoTime();
					input = br.readLine();
					
					//remove username
					input = objCleaning.removeUserNameFromTweet(input);

					// remove RT symbol
					input = objCleaning.removeRTFromTweet(input);

					//removes # sign from hashtags
					input = objCleaning.removeHashTagFromTweet(input);
					
					input = objCleaning.removeUrl(input);
					

					System.out.print(objSummaryPrep.getEmotionsPriority(objEmotionAnalyzer.analysis(input),10,-10)+"\n");
//					System.out.print(objSummaryPrep.returnAnnotations(objNetClientGet.querySpotLight(input))+"\n");
					long endTime = System.nanoTime();
					long duration = (endTime - startTime)/1000000;
					System.err.println(duration);
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
			objReadFile.read("E:\\Dropbox\\workspace\\SMART\\data\\israel.txt");
//			objReadFile.read("F:\\isr_out_2.txt");
	 
		}
	
}
