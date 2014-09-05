package edu.SMART.mongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class RemoveRepeat {
	public void findRepeats(String dbName,String collectionNameSrc,String collectionNameDest,int startAt, int batchSize){


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
		RemoveRepeat obj = new RemoveRepeat();
		obj.findRepeats("test","ukraine_no_repeat","ukraine_v2",0,50000);
	}
}
