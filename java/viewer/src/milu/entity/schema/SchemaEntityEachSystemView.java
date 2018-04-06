package milu.entity.schema;

public class SchemaEntityEachSystemView extends SchemaEntity
{
	/*
	public SchemaEntityEachSystemView( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW );
		
		this.imageResourceName = "file:resources/images/systemview.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW;
		this.imageResourceName = "file:resources/images/systemview.png";
	}
}
