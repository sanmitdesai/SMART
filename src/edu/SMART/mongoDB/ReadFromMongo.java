package edu.SMART.mongoDB;

import javax.sql.rowset.serial.SerialArray;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.SMART.sentimentAnalyzer.SentimentAnalyzer;
import edu.SMART.stemmer.Cleaning;

public class ReadFromMongo {
	/****************
	 * read from mongoDB and post each tweet
	 * @param dbName
	 * @param collectionNameSrc
	 * @param startAt
	 * @param batchSize
	 */
	public void readMongo(String dbName,String collectionNameSrc,int startAt, int batchSize){
		try{   
			// To connect to mongodb server
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			// Now connect to your databases
			DB db = mongoClient.getDB( dbName );
//			System.out.println("Connect to database successfully");

			// get a collection object to work with
			DBCollection coll = db.getCollection(collectionNameSrc);

			BasicDBObject query = new BasicDBObject();

			DBCursor cursor = coll.find(query, null, startAt, 500);
			int count = 0;
			try {
				while(cursor.hasNext()){
					if(count>batchSize){
						break;
					}
					DBObject temp = cursor.next();
//					String input = temp.get("sentiment").toString();
					Cleaning objCleaning = new Cleaning();
					
					//remove username
//					input = objCleaning.removeUserNameFromTweet(input);
//					
//					// remove RT symbol
//					input = objCleaning.removeRTFromTweet(input);
					
					System.out.println(temp.get("text"));
					
					count++;
				}
				
			} finally {
				cursor.close();

			}
			//		        System.out.println("Written to "+fileName + " sucessfully");
		}catch(Exception e){
			e.printStackTrace();
			//		     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	/********************
	 * read tweets from mongoDB and send them for sentiment analysis
	 * @param dbName
	 * @param collectionNameSrc
	 * @param startAt
	 * @param batchSize
	 * @param csvFile
	 */
	public void readMongoSentiAnalysis(String dbName,String collectionNameSrc,int startAt, int batchSize, String csvFile){
		try{ 
			SentimentAnalyzer objSentimentAnalyzer = new SentimentAnalyzer(csvFile);
			
			// To connect to mongodb server
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			// Now connect to your databases
			DB db = mongoClient.getDB( dbName );
//			System.out.println("Connect to database successfully");

			// get a collection object to work with
			DBCollection coll = db.getCollection(collectionNameSrc);

			BasicDBObject query = new BasicDBObject();
			
			DBCursor cursor = coll.find(query, null, startAt, 500);
			int count = 0;
			try {
				while(cursor.hasNext()){
					if(count>batchSize){
						break;
					}
					
					DBObject temp = cursor.next();

					String tweet = ""+temp.get("text");
					int sentiScore = objSentimentAnalyzer.analyzer(tweet);
					
					System.out.println(tweet+">>"+sentiScore);
					
					
					
					BasicDBObject where = new BasicDBObject("id",temp.get("id"));
					
					System.out.print(" "+temp.get("id"));
					temp.put("sentiment", sentiScore);
					
					System.out.println(coll.update(where, temp));
					
					count++;
				}
				
			} finally {
				cursor.close();

			}
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		ReadFromMongo obj = new ReadFromMongo();
		obj.readMongo("test", "ukraine_v2", 0, 10);
//		obj.readMongoSentiAnalysis("test", "ukraine_v2", 0 ,10, "wordlist/SMART.csv");
	}
}
