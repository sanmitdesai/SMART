import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jfree.chart.urls.URLUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.SMART.stemmer.Cleaning;

public class NetClientGet {

	private static final Map JSON_DATA = null;

	public HashSet<String> querySpotLight(String data){
		try {

			String temp = URLUtilities.encode(data, "ASCII");

			URL url = new URL("http://spotlight.dbpedia.org/rest/annotate?text="+temp+"&confidence=0.2&support=0&types=Person,Place,Organization");
//						System.out.println(url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));



			String output="",line;
			
			
			while (( line = br.readLine()) != null) {
//				
//				System.out.println(output);
//				
				output+=line;

			}
			
//			System.out.println(output);
			conn.disconnect();
			return this.readJSON(output);



		} catch (MalformedURLException e) {

			e.printStackTrace();
			return null;

		} catch (IOException e) {

			e.printStackTrace();
			return null;

		}



	}

	public HashSet<String> readJSON(String input){
		HashSet<String> output = new HashSet<String>();
		JSONParser parser=new JSONParser();

		try{
			Object line = parser.parse(input);
//			Object line = parser.parse(new InputStreamReader(new FileInputStream(input)));
			JSONObject jsonObj = (JSONObject) line;
//			JSONObject annotationJsonObject = (JSONObject) jsonObj.get("annotation");
			JSONArray entityArray = (JSONArray) jsonObj.get("Resources");
			String uri="";
			for(int i=0;i<entityArray.size();i++){
				JSONObject entity = (JSONObject) entityArray.get(i);
				
					
//					JSONObject resourceJSON = (JSONObject) entity.get("@URI");
				uri=entity.get("@URI").toString();
				if(uri!=null){
					if(this.checkForRelevance(entity)==true){
						output.add(uri);
					}
				}
					
					
						
					

				}
			
		}catch(ParseException pe){
			System.out.println(input);
			pe.printStackTrace();
		}
		return output;

	}


	public boolean checkForRelevance(JSONObject entity) {
		
		String type = entity.get("@types").toString();
		type=type.toLowerCase();
		if(type.contains("country")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("city")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("state")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("government")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("officeholder")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("politic")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("military")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("arms")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("town")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("religion")){
//			System.out.println(type);
			return true;
		}
		else if(type.contains("ethnic")){
			
//			System.out.println(type);
			return true;
		}
		else if(type.contains("economic")){
			
//			System.out.println(type);
			return true;
		}
		else{
			return false;
		}
		
	}

	public void multipleRequests(String data){
//		String data = "President Obama called Wednesday on Congress to extend a tax break for students included in last year's economic stimulus package, arguing that the policy provides more generous assistance.";//"President%20Obama%20called%20Wednesday%20on%20Congress%20to%20extend%20a%20tax%20break%20for%20students%20included%20in%20last%20year%27s%20economic%20stimulus%20package,%20arguing%20that%20the%20policy%20provides%20more%20generous%20assistance.";
		long startTime = System.nanoTime();
		System.out.println(">>>>>"+this.querySpotLight(data));
		long stopTime = System.nanoTime();
		System.err.println(" ...["+TimeUnit.NANOSECONDS.toSeconds(stopTime - startTime)+" sec].");
	}
	
	public static void main(String[] args) {

		NetClientGet obj = new NetClientGet();
		Cleaning objCleaning = new Cleaning();
		
//		System.out.println(obj.readJSON("jsonexample.txt"));
	ArrayList<String> tweets = new ArrayList<String>();
		tweets.add("In Prague, and I heard aweful need about people being killed with Snipers from the buildings in Ukraine #ukraineprotests");
		tweets.add("Praying for Ukraine...");
		tweets.add("(Marine Corps Times) Obama threatens consequences for Ukraine violence: President Obama on Wednesday... http://t.co/d2KHTKcBwP #Military");
		tweets.add("RT @redostoneage: Obama draws ‘Red Line’ in #Ukraine. That means Ukrainian people are screwed…see Syria…");
		tweets.add("RT @CNN: Thailand, Ukraine, Venezuela. Unrest in three continents. We break down the basic facts: http://t.co/eamysyQB9Z http://t.co/4Ojwwo…");
		tweets.add("Just waking up to what's going on in the Ukraine? Here's a good place to start: BBC News - Why is Ukraine in turmoil? http://t.co/xLdvbcLr0q");
		tweets.add("Tyranny must not prevail in Ukraine support the people in their quest for freedom");
		tweets.add("RT @DamonMacWilson: As Obama concludes call with Merkel, hearing that Berlin announces US-German-Russian coordinated approach to defuse Ukr…");
		
		for(String input:tweets){
			//remove username
			input = objCleaning.removeUserNameFromTweet(input);
			
			// remove RT symbol
			input = objCleaning.removeRTFromTweet(input);
			
			//removes # sign from hashtags
			input = objCleaning.removeHashTagFromTweet(input);
			System.out.println(input);
			obj.multipleRequests(input);
		}
		

	}

}