package edu.SMART.ReadWordList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
 /**********************************************
  * reads a csv word list and returns a HashMap
  * @author sanmit
  *
  */
public class ReadCVS {
 
  public static void main(String[] args) {
 
	ReadCVS obj = new ReadCVS();
	System.out.println(obj.wordListGeneratorAFINN("wordlist/AFINN.csv"));
 
  }
 
  /**********************************
   * show contents of a HashMap
   * @param hashMap
   */
  public void displayMap(HashMap<String, ArrayList<Double>> hashMap){
	//loop map
			for (Map.Entry<String, ArrayList<Double>> entry : hashMap.entrySet()) {
	 
				System.out.println("Key= " + entry.getKey() + " , value= "
					+ entry.getValue());
	 
			}
  }
  
  /*******************************************************
   * reads the file and inserts into a HashMap for probabilistic word list
   * @param csvFile
   */
  public HashMap<String, ArrayList<Double>> wordListGenerator(String csvFile) {
 
//	String csvFile = "wordlist/twitter_sentiment_list.csv";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
	HashMap<String, ArrayList<Double>> sentiWordListMap = new HashMap<String, ArrayList<Double>>();
 
	try {
 
		
 
		br = new BufferedReader(new FileReader(csvFile));
		line = br.readLine();
		while ((line = br.readLine()) != null) {
 
			// use comma as separator
			String[] sentiWordList = line.split(cvsSplitBy);
			ArrayList<Double> sentiScore = new ArrayList<>();
			sentiScore.add(Double.parseDouble(sentiWordList[1]));
			sentiScore.add(Double.parseDouble(sentiWordList[2]));
			sentiWordListMap.put(sentiWordList[0].toUpperCase(), sentiScore);
 
		}
 
		
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
 
	System.out.println("WordList read and loaded from : "+csvFile);
	return sentiWordListMap;
	
  }
 
  /****************************************************************
   * reads the file and inserts into a HashMap for score wise word list
   * @param csvFile
   * @return
   */
  public HashMap<String, Integer> wordListGeneratorAFINN(String csvFile) {
	  
//		String csvFile = "wordlist/twitter_sentiment_list.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		HashMap<String, Integer> sentiWordListMap = new HashMap<String, Integer>();
	 
		try {
	 
			
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
				// use comma as separator
				String[] sentiWordList = line.split(cvsSplitBy);
				sentiWordListMap.put(sentiWordList[0].toUpperCase(), Integer.parseInt(sentiWordList[1]));
	 
			}
	 
			
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	 
		System.out.println("WordList read and loaded from : "+csvFile);
		return sentiWordListMap;
		
	  }
}