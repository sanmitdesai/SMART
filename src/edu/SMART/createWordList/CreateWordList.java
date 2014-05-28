package edu.SMART.createWordList;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;
public class CreateWordList {
	private CSVReader objectForCSVReaderLibrary;
	
	/*****************************************************************
	 * 
	 * @param fileName
	 * @return
	 */
	public HashMap<String, Integer> readCSV(String fileName){
		try {
			HashMap<String, Integer> wordListHashMap = new HashMap<>();
			objectForCSVReaderLibrary = new CSVReader(new FileReader(fileName));
			java.util.List<String[]> wordList = objectForCSVReaderLibrary.readAll();
			for(String[] IteratorOnWordList:wordList){
				
					wordListHashMap.put(IteratorOnWordList[0], 0);
				
				
			}
			return wordListHashMap;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

	}
	
	
/*******************************************************
 * 
 * @param args
 */
	public static void main(String[] args) {
		CreateWordList obj = new CreateWordList();
		//System.out.println(obj.readCSV("wordlist\\LoughranMcDonald_Positive.csv"));
		System.out.println(obj.readCSV("wordlist\\LoughranMcDonald_Negative.csv"));
	}
	
}
