import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import edu.SMART.Main.Main;
import edu.SMART.ReadWordList.ReadCVS;


public class MergeLists {
	public static void main(String[] args) throws IOException {
		ReadCVS obj = new ReadCVS();
//		HashMap<String, ArrayList<Double>> probWordList = obj.wordListGenerator("wordlist/twitter_sentiment_list.csv");

		HashMap<String, Integer> scoreWordList = obj.wordListGeneratorAFINN("wordlist/test.csv");


		System.out.println(scoreWordList.size());
		MergeLists objList = new MergeLists();
//		objList.merge(scoreWordList, "wordlist/SMART.csv");
//		TreeMap<String, ArrayList<Double>> temp = new TreeMap<>(objList.merge(scoreWordList, probWordList, "wordlist/data.csv"));
//		System.out.println(temp);

	}
	
	public static void merge(HashMap scoreWordList,String sFileName) throws IOException {
	    Iterator it = scoreWordList.entrySet().iterator();
	    FileWriter writer = new FileWriter(sFileName);
	    HashMap<String, ArrayList<Double>> output = new HashMap<>();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();

	        	
	        	writer.append((CharSequence) pairs.getKey());
	        	   writer.append(',');
	        	   writer.append(""+ pairs.getValue());
	        	   writer.append('\n');
	        	
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		
	}
	
	
	
	public void generateCsvFile(String sFileName)
	   {
		
		
		try
		{
		    FileWriter writer = new FileWriter(sFileName);
	 
		    writer.append("DisplayName");
		    writer.append(',');
		    writer.append("Age");
		    writer.append('\n');
	 
		    writer.append("MKYONG");
		    writer.append(',');
		    writer.append("26");
	            writer.append('\n');
	 
		    writer.append("YOUR NAME");
		    writer.append(',');
		    writer.append("29");
		    writer.append('\n');
	 
		    //generate whatever data you want
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	    }
}
