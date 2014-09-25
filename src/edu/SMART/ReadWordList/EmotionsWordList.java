package edu.SMART.ReadWordList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.SMART.basicDSFTasks.PrintAndInitialize;
import edu.stanford.nlp.util.HashableCoreMap;

public class EmotionsWordList {
	public static PrintAndInitialize objAndInitialize = new PrintAndInitialize();
	/**************************************
	 * Creates ArrayList for emotions word list
	 * @param input
	 * @return
	 */
	public ArrayList<String> makeArrayListEmotions(ArrayList<String> input){

		ArrayList<String> output = new ArrayList<String>();
		
		output = objAndInitialize.initializeArrayList(output,12);
		String[] temp = input.get(0).split(",");
		output.set(0, temp[0].toLowerCase());

		for(String line: input){
			String[] element = line.split(",");
			if(element[0].toLowerCase().equals(output.get(0))){


				if(element[1].equals("negative")){
					if(!(element[2].contains("0")))
						output.set(1, "-"+element[2]);
				}
				else if(element[1].equals("positive")){
					if(!output.get(1).contains("-")){
						output.set(1, element[2]);
					}
					else{
						if(element[2].contains("1")){
//							System.err.println("Both Positive and negative "+ output.get(0));
						}
							
					}

				}
				else if(element[1].equals("joy")){
					output.set(2, element[2]);
				}
				else if(element[1].equals("trust")){
					output.set(3, element[2]);
				}
				else if(element[1].equals("fear")){
					output.set(4, element[2]);
				}
				else if(element[1].equals("surprise")){
					output.set(5, element[2]);
				}
				else if(element[1].equals("sadness")){
					output.set(6, element[2]);
				}
				else if(element[1].equals("disgust")){
					output.set(7, element[2]);
				}
				else if(element[1].equals("anger")){
					output.set(8, element[2]);
				}
				else if(element[1].equals("anticipation")){
					output.set(9, element[2]);
				}
				else{
					System.err.println("Unknow emotion "+ element[1]);
				}
			}//if word check
			else{
				System.err.println("Unknown word "+element[0]);
			}//else word check



		}//for

		return output;
	}

	/*****************************
	 * Reads emotions word list and creats a HashMap
	 * @param csvFile
	 * @return
	 */
	public HashMap<String, ArrayList<String>> readEmotionWordList(String csvFile) {

		//		String csvFile = "/Users/mkyong/Downloads/GeoIPCountryWhois.csv";

		HashMap<String, ArrayList<String>> output = new HashMap<String, ArrayList<String>>();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int counter = 0;
		ArrayList<String> lines = new ArrayList<String>();
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {


				// use comma as separator

				lines.add(line);
				counter++;
				if(counter%10==0){
					//					System.out.println(lines);
					//					System.out.println("--------------------------------");
					//					System.out.println(this.organizeList(lines));
					
//					lines = this.organizeList(lines);
					
					ArrayList<String> tempArrayList = this.makeArrayListEmotions(lines);
					
					output.put(tempArrayList.get(0), tempArrayList);
//					System.out.println(output.get(lines.get(0)));
					lines.clear();

				}




			}//while

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}

		
		return output;
	}

	
	
	/*************************************
	 * Creates ArrayList for SMART word list
	 * @param element
	 * @return
	 */
	
	public ArrayList<String> makeArrayListSMART(String[] element){
		ArrayList<String> output = new ArrayList<String>();
		
		output = objAndInitialize.initializeArrayList(output,12);
		
		output.set(0, element[0].toLowerCase());
		output.set(1, element[1]);
		
		for(int i=2;i<element.length;i++){
			if(element[i].equals("joy")){
				output.set(2, element[1]);
			}
			else if(element[i].equals("trust")){
				output.set(3, element[1]);
			}
			else if(element[i].equals("fear")){
				output.set(4, element[1]);
			}
			else if(element[i].equals("surprise")){
				output.set(5, element[1]);
			}
			else if(element[i].equals("sadness")){
				output.set(6, element[1]);
			}
			else if(element[i].equals("disgust")){
				output.set(7, element[1]);
			}
			else if(element[i].equals("anger")){
				output.set(8, element[1]);
			}
			else if(element[i].equals("anticipation")){
				output.set(9, element[1]);
			}
			else if(element[i].equals("none")){
				
			}
			else{
				System.err.println("In SMART Unknow emotion "+ element[i]+" for word "+element[0]);
			}
		}
		
		return output;
	}
	
	/**************************************
	 * Reads SMART word list and create HashMap for the same
	 * @param csvFile
	 */
	
	public HashMap<String, ArrayList<String>> readSMARTWordList(String csvFile, HashMap<String, ArrayList<String>> output) {
		 
//		String csvFile = "/Users/mkyong/Downloads/GeoIPCountryWhois.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] element = line.split(cvsSplitBy);
				
//				System.out.println(this.makeArrayListSMART(element));
				ArrayList<String> tempArrayList = this.makeArrayListSMART(element);
				
				output.put(tempArrayList.get(0), tempArrayList);
				
	 
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
	 
return output;
	  }
	
	/******************
	 * creates combined word list for both emotions and SMART
	 * @param emotionsFilePath
	 * @param SMARTFilePath
	 * @return
	 */
	
	public HashMap<String, ArrayList<String>> makeWordList (String emotionsFilePath, String SMARTFilePath){
		HashMap<String, ArrayList<String>> output = new HashMap<String, ArrayList<String>>();
		
		long startTime = System.nanoTime();
		output=this.readEmotionWordList(emotionsFilePath);
		long stopTime = System.nanoTime();
		System.err.println("Word list loaded Emotions "+TimeUnit.NANOSECONDS.toSeconds(stopTime - startTime)+" sec");
		startTime = System.nanoTime();
		output = this.readSMARTWordList(SMARTFilePath, output);
		stopTime = System.nanoTime();
		System.err.println("Word list loaded Emotions2 "+TimeUnit.NANOSECONDS.toSeconds(stopTime - startTime)+" sec");
		return output;
	}
	
	public static void main(String[] args) {
		EmotionsWordList objEmotionsWordList = new EmotionsWordList();
//		printMap(objEmotionsWordList.readEmotionWordList("wordlist/emotions.csv"));
//		objEmotionsWordList.readSMARTWordList("wordlist\\MergeLists v2.csv");
		
		System.out.println(objEmotionsWordList.makeWordList("wordlist/emotions.csv", "wordlist\\MergeLists v2.csv"));
		
	}
}
