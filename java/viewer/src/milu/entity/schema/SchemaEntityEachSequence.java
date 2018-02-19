package milu.entity.schema;

public class SchemaEntityEachSequence extends SchemaEntity 
{
	public SchemaEntityEachSequence( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.SEQUENCE );
		
		this.imageResourceName = "file:resources/images/seq.png";
	}
}
