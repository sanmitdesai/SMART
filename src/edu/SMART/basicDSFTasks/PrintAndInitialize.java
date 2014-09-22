package edu.SMART.basicDSFTasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class PrintAndInitialize {
	/********************************
	 * Initializes an ArrayList of any size with all zero's
	 * @param input
	 * @return
	 */
		public ArrayList<String> initializeArrayList(ArrayList<String> input, int size){
			for(int i=0;i<size-1;i++){
				input.add("0");
			}
			return input;
		}
		
		/**************************
		 * print all elements for a given HashMap
		 * @param mp
		 */
		
		public static void printMap(HashMap<String, ArrayList<String>> mp) {
			Collection<?> keys = mp.keySet();
			for(Object key: keys){
			    System.out.print("Key " + key);
			    System.out.println(" Value " + mp.get(key));
			}
		}
		
		public void displayNestedArraylist(ArrayList<ArrayList<String>> input){
			
			
			for(ArrayList<String> nest : input){
				System.out.println(nest);
			}


		}
}
