package edu.SMART.POS;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

import edu.SMART.stemmer.Stemmer;
import edu.SMART.writeFile.SMARTFileWriter;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSPhraseExtractor {
	public HashMap<String, Integer> biGrams = new HashMap<String, Integer>();
	private POSPhraseExtractor(String inputFileName, String outputFileName) {
		MaxentTagger tagger = new MaxentTagger("lib\\models\\english-caseless-left3words-distsim.tagger");
		List<List<HasWord>> sentences;
		try {
			sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(inputFileName)));
		
		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			
			this.iterateWords(tSentence);
//				      System.out.println(Sentence.listToString(tSentence, false));
		}//for
//		System.out.println(obj.biGrams);
		SMARTFileWriter fileWrite = new SMARTFileWriter();
		fileWrite.writeHashMapToCSV(this.biGrams, outputFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
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
	
	public String cleaning(String input){
//		input=input.toLowerCase();
		if(input.startsWith("#")){
//			System.out.println(input);
			input = input.substring(1);
//			System.out.println(input);
		}
		return input;
	}
	
	public String removeTags(String input){
		return input.substring(0, input.lastIndexOf('/'));
	}
	
	public void iterateWords(List<TaggedWord> tSentence){
//		Stemmer objStemmer = new Stemmer();
		for(int i=0;i<tSentence.size();i++){
			if(i+2>=tSentence.size()){
				break;
			}
			else{
//				System.out.print(tSentence.get(i)+","+tSentence.get(i+1)+" ");
				String[] output = {this.cleaning(tSentence.get(i).toString()),this.cleaning(tSentence.get(i+1).toString()),this.cleaning(tSentence.get(i+2).toString())};
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
	public static void main(String[] args) throws Exception {

		POSPhraseExtractor obj = new POSPhraseExtractor("data\\tweetListV1.txt","data\\bigrams.csv");
		
	}
}

