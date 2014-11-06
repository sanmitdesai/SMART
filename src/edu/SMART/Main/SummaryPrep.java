package edu.SMART.Main;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class SummaryPrep {

	/**********************************
	 * For a given number returns its respective emotion
	 * @param num
	 * @return
	 */
	public static String returnEmotion(int num){

		if(num == 1){
			return "Joy";
		}
		else if(num == 2){
			return "Trust";
		}
		else if(num == 3){
			return "Fear";
		}
		else if(num == 4){
			return "Surprise";
		}
		else if(num == 5){
			return "Sadness";
		}
		else if(num == 6){
			return "Disgust";
		}
		else if(num == 7){
			return "Anger";
		}
		else if(num == 8){
			return "Anticipation";
		}
		else{
			return ""+num;
		}
	}

	/********************************
	 * Converts String ArrayList to Integer ArrayList 
	 * @param stringArray
	 * @return
	 */
	static ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		if(stringArray==null){
			return null;
		}
		else{
		for(String stringValue : stringArray) {
			try {
				//Convert String to Integer, and store it into integer array list.
				result.add(Integer.parseInt(stringValue));
			} catch(NumberFormatException nfe) {
				System.err.println("Could not parse " + nfe);

			} 
		}       
		return result;
		}
	}

	/*************************
	 * Gets maximum number from a given ArrayList
	 * @param list
	 * @return
	 */
	public static int getMax(ArrayList<Integer> list){
		int max = Integer.MIN_VALUE;
		int pos = 0;
		for(int i=1; i<list.size(); i++){
			if(list.get(i) > max){
				max = list.get(i);
				pos = i;
			}
		}
		return pos;
	}

	/*****************
	 * Takes an ArrayList as an input returns String with 3 most prominent emotion and sentiment as output
	 * @param input
	 * @return
	 */
	public String getEmotionsPriority(ArrayList<String> input,double upperLimit, double lowerLimit){
		String output = "";
		ArrayList<Integer> intInput = getIntegerArray(input);
		int sentiScore = intInput.get(0);
		intInput.set(0, 0);

		int emotion1 = getMax(intInput);
		intInput.set(emotion1, 0);

		int emotion2 = getMax(intInput);
		intInput.set(emotion2, 0);

		int emotion3 = getMax(intInput);
		//		intInput.set(emotion3, 0);

		output+=this.returnSentiState(upperLimit, lowerLimit, sentiScore)+",";
		output+=returnEmotion(emotion1)+",";
		output+=returnEmotion(emotion2)+",";
		output+=returnEmotion(emotion3);


		return output;
	}

	/**********************
	 * Get sentiment state for a given range and a value
	 * @param upperLimit
	 * @param lowerLimit
	 * @param sentiScore
	 * @return
	 */
	public String returnSentiState(double upperLimit, double lowerLimit, int sentiScore){
		double fractPos = upperLimit/3, fractNeg = lowerLimit/3;
		String output="Neutral";

		double posLim1=Math.round(fractPos), posLim2=fractPos*2;
		posLim2=Math.round(posLim2);

		double negLim1=Math.round(fractNeg), negLim2=fractNeg*2;
		negLim2=Math.round(negLim2);

		if((sentiScore>0)&&(sentiScore<posLim1)){
			output="Slightly Positive";
		}
		else if((sentiScore>=posLim1)&&(sentiScore<= posLim2)){
			output="Positive";
		}
		else if(sentiScore > posLim2){
			output="Very Positive";
		}
		else if((sentiScore<0)&&(sentiScore>negLim1)){
			output="Slightly Negative";
		}
		else if((sentiScore<=negLim1)&&(sentiScore>=negLim2)){
			output="Negative";
		}
		else if(sentiScore<negLim2){
			output="Very Negative";
		}


		return output;
	}
	
	/*************
	 * Concatenates the entries of a given HashSet<String> to return String
	 * @param input
	 * @return
	 */
	public String returnAnnotations(HashSet<String> input){
		String output="";
		if(input!=null){
		for (String s : input) {
			output+=s.subSequence(s.lastIndexOf('/')+1, s.length())+" | ";
		}
		if(output.length()>3){
		CharSequence finalOutput = output.subSequence(0, output.length()-3);
		return ""+finalOutput;}
		else{
			return output;
		}
		}
		else{
			return "no annotations";
		}
	}

	/***********************************
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> output = new ArrayList<String>();
		output.add("-2");
		output.add("5");
		output.add("6");
		output.add("2");
		output.add("5");
		output.add("0");
		output.add("0");
		output.add("5");

		SummaryPrep obj = new SummaryPrep();
//		System.out.println(output);
		System.out.println(obj.getEmotionsPriority(output,10,-10));
		
		HashSet<String> input = new HashSet<String>();
		input.add("http://dbpedia.org/resource/Ukraine");
		input.add("http://dbpedia.org/resource/Angela_Merkel");
		input.add("http://dbpedia.org/resource/Barack_Obama");
		input.add("http://dbpedia.org/resource/Berlin");
		
		System.out.println(obj.returnAnnotations(input));

	}

}
