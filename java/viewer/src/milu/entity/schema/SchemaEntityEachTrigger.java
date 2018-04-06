package milu.entity.schema;

public class SchemaEntityEachTrigger extends SchemaEntity 
{
	/*
	public SchemaEntityEachTrigger( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.TRIGGER );
		
		this.imageResourceName = "file:resources/images/trigger.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.TRIGGER;
		this.imageResourceName = "file:resources/images/trigger.png";
	}
}
