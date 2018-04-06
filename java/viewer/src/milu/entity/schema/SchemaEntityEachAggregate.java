package milu.entity.schema;

public class SchemaEntityEachAggregate extends SchemaEntity 
{
	/*
	public SchemaEntityEachAggregate( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.AGGREGATE );
		
		this.imageResourceName = "file:resources/images/aggregate.png";
	}
	*/

	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.AGGREGATE;
		this.imageResourceName = "file:resources/images/aggregate.png";
	}
}
