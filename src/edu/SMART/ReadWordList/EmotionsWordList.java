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

import edu.stanford.nlp.util.HashableCoreMap;

public class EmotionsWordList {

	public ArrayList<String> initializeArrayList(ArrayList<String> input){
		for(int i=0;i<10;i++){
			input.add("");
		}
		return input;
	}

	public ArrayList<String> organizeList(ArrayList<String> input){

		ArrayList<String> output = new ArrayList<String>();
		output = this.initializeArrayList(output);
		String[] temp = input.get(0).split(",");
		output.set(0, temp[0]);

		for(String line: input){
			String[] element = line.split(",");
			if(element[0].equals(output.get(0))){


				if(element[1].equals("negative")){
					if(!(element[2].contains("0")))
						output.set(1, "-"+element[2]);
				}
				else if(element[1].equals("positive")){
					if(!output.get(1).contains("-")){
						output.set(1, element[2]);
					}
					else{
						if(element[2].contains("1"))
							System.err.println("Both Positive and negative "+ output.get(0));
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
					output.put(this.organizeList(lines).get(0), this.organizeList(lines));
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

	public static void printMap(HashMap<String, ArrayList<String>> mp) {
		Collection<?> keys = mp.keySet();
		for(Object key: keys){
		    System.out.print("Key " + key);
		    System.out.println(" Value " + mp.get(key));
		}
	}
	public void run(String csvFile) {
		 
//		String csvFile = "/Users/mkyong/Downloads/GeoIPCountryWhois.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
//				String[] country = line.split(cvsSplitBy);
				System.out.println(line);
				
	 
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
	 
//		System.out.println("Done");
	  }
	
	public static void main(String[] args) {
		EmotionsWordList objEmotionsWordList = new EmotionsWordList();
//		printMap(objEmotionsWordList.readEmotionWordList("wordlist/emotions.csv"));
		objEmotionsWordList.run("wordlist\\MergeLists v2.csv");
		
	}
}
