package db.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;



import java.util.Arrays;

public class MongoCon 
{

	public static void main(String[] args) 
	{
		// http://mongodb.github.io/mongo-java-driver/2.13/getting-started/quick-tour/
		/**/
		MongoCredential credential = 
			MongoCredential.createCredential( "mongo", "admin", "mongo".toCharArray() );
		
		@SuppressWarnings("deprecation")
		MongoClient mongoClient = 
			new MongoClient
			(
				Arrays.asList(new ServerAddress("localhost", 27017)),
                Arrays.asList(credential)
			);
		
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB( "sample" );
		
		DBCollection coll = db.getCollection("sample_coll");
		
		System.out.println( "document count=" + coll.getCount() );
		DBCursor cursor = coll.find();
		try
		{
			while ( cursor.hasNext() )
			{
				System.out.println( "item:" + cursor.next() );
			}
		}
		finally
		{
			cursor.close();
			mongoClient.close();
		}
		/**/
		
		//https://www.mkyong.com/mongodb/java-authentication-access-to-mongodb/
		/*
	    try {
	    	Mongo mongo = new Mongo("localhost", 27017);
	    	DB db = mongo.getDB("sample");

	    	boolean auth = db.authenticate("milu", "password".toCharArray());
	    	if (auth) 
	    	{

	    		DBCollection table = db.getCollection("sample_coll");

	    		
	    		//BasicDBObject document = new BasicDBObject();
	    		//document.put("name", "mkyong");
	    		//table.insert(document);
	    		

	    		System.out.println("Login is successful!");
	    	} 
	    	else 
	    	{
	    		System.out.println("Login is failed!");
	    	}
	    	System.out.println("Done");

	    } 
	    catch (UnknownHostException e) 
	    {
	    	e.printStackTrace();
	    } 
	    catch (MongoException e) 
	    {
	    	e.printStackTrace();
	    }
	    */
	}

}
