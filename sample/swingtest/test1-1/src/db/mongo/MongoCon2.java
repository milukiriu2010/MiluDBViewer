package db.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernError;
import com.mongodb.client.MongoDatabase;

import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Arrays;

public class MongoCon2 
{

	public static void main(String[] args) 
	{
		// http://mongodb.github.io/mongo-java-driver/2.13/getting-started/quick-tour/
		MongoCredential credential = 
			MongoCredential.createCredential( "mongo", "admin", "mongo".toCharArray() );
		
		WriteConcern l_concern = new WriteConcern( 1, 1000 ).withJournal( true );
		
		MongoClientOptions l_opts =
			MongoClientOptions
				.builder()
				.writeConcern( l_concern )
				.build();
		
		MongoClient mongoClient = 
			new MongoClient
			(
				new ServerAddress("localhost", 27017),
                credential,
                l_opts
			);
		
		MongoDatabase mongoDB = mongoClient.getDatabase( "sample" );
		
		/*
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
		}
		*/
	}

}
