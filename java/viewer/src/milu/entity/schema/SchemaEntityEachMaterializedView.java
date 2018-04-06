package milu.entity.schema;

public class SchemaEntityEachMaterializedView extends SchemaEntity
{
	/*
	public SchemaEntityEachMaterializedView( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW );
		
		this.imageResourceName = "file:resources/images/materialized_view.png";
	}
	*/
	
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW;
		this.imageResourceName = "file:resources/images/materialized_view.png";
	}
}
