package db.mongo;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;

import com.mongodb.MongoClientOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

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
		
		MongoCollection<Document> coll = mongoDB.getCollection( "sample_coll" );
		System.out.println( "document count=" + coll.count() );
		
		FindIterable<?> findIterable = coll.find();
		MongoCursor<?> iterator = findIterable.iterator();
		try
		{
			while ( iterator.hasNext() )
			{
				System.out.println( "item:" + iterator.next() );
			}
		}
		finally
		{
			iterator.close();
		}
		
		mongoClient.close();
	}

}
