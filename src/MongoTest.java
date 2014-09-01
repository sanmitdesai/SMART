import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class MongoTest {

	
	public void writeToFile(String fileName){
		try {
			 
			String content = "This is the content to write into file";
 
			File file = new File(fileName);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
//			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void findRepeats(String dbName,String collectionName,String fileName,int startAt){
		
		
		try{   
			File file = new File(fileName);
			 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			else{
				file.delete();
				System.out.println("file deleted");
				file.createNewFile();
			}
			
			Writer fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8")); // new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			
			 // To connect to mongodb server
	         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	         // Now connect to your databases
	         DB db = mongoClient.getDB( dbName );
			 System.out.println("Connect to database successfully");
			 
			 // get a collection object to work with
		        DBCollection coll = db.getCollection(collectionName);
		        
		        BasicDBObject query = new BasicDBObject("lang", "en");

		        DBCursor cursor = coll.find(query, null, startAt, 500);
		       int count=0,repeat=0;
		       HashMap<String, String> tweets = new HashMap<String, String>();
		        try {

		           while(cursor.hasNext()) {
		        	   if(count==500000)
		        		   break;
		        	   DBObject row = cursor.next();
		        	   String singleTweet = row.toString();
		        	   if(!tweets.containsKey(row.get("text"))){
		        		   tweets.put(row.get("text").toString(), "0");
			        	   singleTweet="{"+ singleTweet.subSequence(51, singleTweet.length())+"\n";
//			               System.out.println(temp);
			               bw.write(singleTweet);
		        	   }
		        	   else{

		        		   repeat++;
		        	   }

		               count++;
		           }//while
		           System.out.println(repeat);
		        } finally {
		           cursor.close();
		        }

		        bw.close();
		        System.out.println("Written to "+fileName + " sucessfully");
	      }catch(Exception e){
	    	  e.printStackTrace();
//		     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		  }
		
		
		
		
	}
	
public void findRepeats2(String dbName,String collectionNameSrc,String collectionNameDest,int startAt, int batchSize){
		
		
		try{   
			
			 // To connect to mongodb server
	         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	         // Now connect to your databases
	         DB db = mongoClient.getDB( dbName );
			 System.out.println("Connect to database successfully");
			 
			 // get a collection object to work with
		        DBCollection coll = db.getCollection(collectionNameSrc);
		        
		        BasicDBObject query = new BasicDBObject("lang", "en");

		        DBCursor cursor = coll.find(query, null, startAt, 500);
		       
				 // get a collection object to work with
			        DBCollection collDest = db.getCollection(collectionNameDest);
			        

			        
		      int count = 0,repeat=0;
		        try {
		        	while(cursor.hasNext()){
		        		if(count>batchSize){
		        			break;
		        		}
		        		DBObject temp = cursor.next();
		        		
//		        		System.out.println(temp);
		        		BasicDBObject queryRepeat = new BasicDBObject("text",temp.get("text").toString());
		        		DBCursor cursorRepeat = collDest.find(queryRepeat);
		        		if(cursorRepeat.count()>0){
		        			repeat++;
		        		}
		        		else{
		        			collDest.insert(temp);
		        			
		        			
		        		}
		        		count++;
		        	}
		           System.out.println(repeat+" repeated tweets");
		        } finally {
		           cursor.close();
		           
		        }

		        
//		        System.out.println("Written to "+fileName + " sucessfully");
	      }catch(Exception e){
	    	  e.printStackTrace();
//		     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		  }
		
		
		
		
	}
	public static void main(String[] args) {
		MongoTest obj = new MongoTest();
//		obj.findRepeats("test","ukraine_no_repeat","E://ukraine_no_repeatv2.txt",0);
		obj.findRepeats2("test","ukraine_no_repeat","ukraine_v1",0,1093931);
	   }
	

	

}
