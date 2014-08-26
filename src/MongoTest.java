import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class MongoTest {

	public void findRepeats(){
		try{   
			 // To connect to mongodb server
	         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	         // Now connect to your databases
	         DB db = mongoClient.getDB( "test" );
			 System.out.println("Connect to database successfully");
	         //boolean auth = db.authenticate(myUserName, myPassword);
			 //System.out.println("Authentication: "+auth);
			 
			 // get a collection object to work with
		        DBCollection coll = db.getCollection("ukraine_full");
		        		        
//		        DBCursor myDoc = coll.find().limit(100);
		        
//		        System.out.println(myDoc);
		        
		        BasicDBObject query = new BasicDBObject("lang", "en");
//		        DBObject query = new BasicDBObject("text:", new BasicDBObject("lang", "en"));
		        DBCursor cursor = coll.find(query, null, 0, 500);
//		        List dist = coll.distinct("id", query);
//		        DBCursor cursor = coll.find(query);
		       int count=0,repeat=0;
		       HashMap<String, String> tweets = new HashMap<String, String>();
		        try {
		           while(cursor.hasNext()) {
		        	   if(count==500000)
		        		   break;
		        	   DBObject row = cursor.next();
		        	   String temp = row.toString();
		        	   if(!tweets.containsKey(row.get("text"))){
		        		   tweets.put(row.get("text").toString(), "0");
		        	   }
		        	   else{
//		        		   System.out.println("repeat");
		        		   repeat++;
		        	   }
//		        	   temp=(String) temp.subSequence(51, temp.length());
//		               System.out.println(repeat);
		               count++;
		           }//while
		           System.out.println(repeat);
		        } finally {
		           cursor.close();
		        }
		        /*int count=0;
		        while(count<dist.size()){
		        	System.out.println(dist.get(count));
		        	
		        }*/
	      }catch(Exception e){
	    	  e.printStackTrace();
//		     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		  }
	}
	public static void main(String[] args) {
		
	   }

	

}
