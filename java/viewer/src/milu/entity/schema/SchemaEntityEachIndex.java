package milu.entity.schema;

public class SchemaEntityEachIndex extends SchemaEntity 
{
	/*
	public SchemaEntityEachIndex( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.INDEX );
		
		this.imageResourceName = "file:resources/images/index_i.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.INDEX;
		this.imageResourceName = "file:resources/images/index_i.png";
	}
}
