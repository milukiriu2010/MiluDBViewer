package milu.entity.schema;

public class SchemaEntityEachSchema extends SchemaEntity 
{
	/*
	public SchemaEntityEachSchema( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.SCHEMA );
		
		this.imageResourceName = "file:resources/images/schema.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.SCHEMA;
		this.imageResourceName = "file:resources/images/schema.png";
	}
}
