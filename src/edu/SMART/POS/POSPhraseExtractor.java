package edu.SMART.POS;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.io.IOUtils;

import edu.SMART.ReadWriteFile.SMARTFileWriter;
import edu.SMART.stemmer.Cleaning;
import edu.SMART.stemmer.Stemmer;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSPhraseExtractor {
	public HashMap<String, Integer> biGrams = new HashMap<String, Integer>();
	/***************
	 * 
	 * @param inputFileName
	 * @param outputFileName
	 * @throws FileNotFoundException 
	 */
	private POSPhraseExtractor(String inputFileName, String outputFileName) throws FileNotFoundException {
		MaxentTagger tagger = new MaxentTagger("lib\\models\\english-caseless-left3words-distsim.tagger");
		List<List<HasWord>> sentences;
					sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(inputFileName)));
					InputStream is = new ByteArrayInputStream(inputFileName.getBytes());
					 
					// read it with BufferedReader
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					
					sentences = MaxentTagger.tokenizeText(br);
		//			System.out.println(sentences);
				for (List<HasWord> sentence : sentences) {
					List<TaggedWord> tSentence = tagger.tagSentence(sentence);
					
					this.iterateWords(tSentence);
					System.out.println(tSentence);
//						      System.out.println(this.SentiWordNetTags(tSentence));
				}//for
				System.out.println(this.biGrams);
				SMARTFileWriter fileWrite = new SMARTFileWriter();
				fileWrite.writeHashMapToCSV(this.biGrams, outputFileName);
	}
	
	/*************************+
	 * checks for bigram combinations in a sentence provided as a string array
	 * @param input
	 * @return
	 */
	public String[] findBigrams(String[] input){
		// JJ + JJ + not NN or NNS
	if(input[0].endsWith("JJ")&&input[1].endsWith("JJ")&&!(input[2].endsWith("NN")||input[2].endsWith("NNS"))){

		return input;
	}
	// RB or RBR or RBS + JJ + not NN or NNS
	else if((input[0].endsWith("RB")||input[0].endsWith("RBR")||input[0].endsWith("RBS"))&&input[1].endsWith("JJ")&&!(input[2].endsWith("NN")||input[2].endsWith("NNS"))){
		return input;
	}
	// NN or NNS + JJ + not NN or NNS
	else if((input[0].endsWith("NN")||input[0].endsWith("NNS"))&&input[1].endsWith("JJ")&&!(input[2].endsWith("NN")||input[2].endsWith("NNS"))){
		return input;
	}
	// JJ + NN or NNS + anything
	else if(input[0].endsWith("JJ")&&(input[1].endsWith("NN")||input[1].endsWith("NNS"))){
		return input;
	}
	// RB or RBR or RBS + VB or VBD or VBN or VBG
	else if((input[0].endsWith("RB")||input[0].endsWith("RBR")||input[0].endsWith("RBS"))&&(input[1].endsWith("VB")||input[1].endsWith("VBD")||input[1].endsWith("VBN")||input[1].endsWith("VBG"))){
		return input;
	}
	else{
		return null;
	}
	}
	
	
	/***************
	 * removes POS tags from a sentence
	 * @param input
	 * @return
	 */
	public String removeTags(String input){
		return input.substring(0, input.lastIndexOf('/'));
	}
	
	/**********************
	 * given a sentence iterate through it word by word
	 * @param tokenizeInput
	 */
	public void iterateWords(List<TaggedWord> tokenizeInput){
		Cleaning objCleaning = new Cleaning();
//		Stemmer objStemmer = new Stemmer();
		for(int i=0;i<tokenizeInput.size();i++){
			if(i+2>=tokenizeInput.size()){
				break;
			}
			else{
//				System.out.print(tSentence.get(i)+","+tSentence.get(i+1)+" ");
				String[] output = {objCleaning.removeHashTag(tokenizeInput.get(i).toString()),objCleaning.removeHashTag(tokenizeInput.get(i+1).toString()),objCleaning.removeHashTag(tokenizeInput.get(i+2).toString())};
				if(this.findBigrams(output)!=null){
					
//					String key = objStemmer.callStemmer(this.removeTags(output[0]))+" "+objStemmer.callStemmer(this.removeTags(output[1]).toLowerCase());
					String key = this.removeTags(output[0]).toLowerCase()+" "+this.removeTags(output[1]).toLowerCase();
					if(biGrams.containsKey(key)){
						
						biGrams.put(key, biGrams.get(key)+1);
					}
					else{
						biGrams.put(key, 1);
					}
				}
				
				
			}


		}
//		System.out.println();
	}
	
	public ArrayList<String> SentiWordNetTags(List<TaggedWord> input){
		String word = null, temp = null;
		Cleaning objCleaning = new Cleaning();
		ArrayList<String> output = new ArrayList<String>();
		for(int i=0;i<input.size();i++){
			word=input.get(i).toString();
			temp = word.substring(word.lastIndexOf("/")+1, word.length());
			if(temp.startsWith("N")){
//				System.out.println("n");
				output.add(objCleaning.removeTags(word)+"#n");
			}
			else if(temp.startsWith("V")){
				output.add(objCleaning.removeTags(word)+"#v");
//				System.out.println("v");
			}
			else if(temp.startsWith("R")){
				output.add(objCleaning.removeTags(word)+"#r");
//				System.out.println("r");
			}
			else if(temp.startsWith("J")){
				output.add(objCleaning.removeTags(word)+"#a");
//				System.out.println("a");
			}
			else{
//				output.add(word);
			}
			
			
		}
		
		
		return output;
	}
	
	public static void main(String[] args) throws Exception {

		POSPhraseExtractor obj = new POSPhraseExtractor("obama is a pussy and a quacker","");
		
		
	}
}

