package edu.SMART.stemmer;

public class Cleaning {
	/**************
	 * removes everything between "RT @" and ":" 
	 * @param input
	 * @return
	 */
	public String removeRT(String input){
		return input.replaceAll("RT @.*: ", "");
	}
	
	/***********************
	 * remove "#" from a given word
	 * @param input
	 * @return
	 */
	public String removeHashTag(String input){
//		input=input.toLowerCase();
		if(input.startsWith("#")){
//			System.out.println(input);
			input = input.substring(1);
//			System.out.println(input);
		}
		return input;
	}
	
	/*************************
	 * remove "#" from a sentence
	 * @param sampleString
	 * @return
	 */
	public String removeHashTagFromTweet(String sampleString){
		return sampleString.replaceAll("#","");
	}
	/*************************
	 * remove "@+any string followed by a space" from a sentence
	 * @param sampleString
	 * @return
	 */
	public String removeUserNameFromTweet(String sampleString){
		return sampleString.replaceAll("@[A-Za-z]+","");
	}
	
	/*************************
	 * remove "RT  : " from a sentence
	 * @param sampleString
	 * @return
	 */
	public String removeRTFromTweet(String sampleString){
		return sampleString.replaceAll("RT : ","");
	}
	
	/***************
	 * removes POS tags from a sentence
	 * @param input
	 * @return
	 */
	public String removeTags(String input){
		return input.substring(0, input.lastIndexOf('/'));
	}
	
	public static void main(String[] args) {
		Cleaning objCleaning = new Cleaning();
//		System.out.println(objCleaning.removeHashTagFromTweet(objCleaning.removeRT("RT @richardbranson: So sad to see violence ruling over democracy in #Ukraine &amp; #Venezuela. We all need to stand up for true democracy http:…'")));
//		System.out.println(objCleaning.removeRTFromTweet(objCleaning.removeRT("RT @richardbranson: So sad to see violence ruling over democracy in #Ukraine &amp; #Venezuela. We all need to stand up for true democracy http:…'")));
		System.out.println(objCleaning.removeUserNameFromTweet("RT @richardbranson: So sad to see violence ruling over democracy in #Ukraine &amp; #Venezuela. We all need to stand up for true democracy http:…'"));
	}
}
