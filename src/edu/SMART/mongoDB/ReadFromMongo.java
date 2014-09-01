package edu.SMART.mongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ReadFromMongo {
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
	public static void main(String[] args) {
		ReadFromMongo obj = new ReadFromMongo();
		obj.readMongo("test", "ukraine_v1", 0, 50000);
	}
}