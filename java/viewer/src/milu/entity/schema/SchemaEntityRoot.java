package milu.entity.schema;

public class SchemaEntityRoot extends SchemaEntity
{
	public SchemaEntityRoot( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.ROOT );
		
		this.imageResourceName = "file:resources/images/url.png";
	}
}
