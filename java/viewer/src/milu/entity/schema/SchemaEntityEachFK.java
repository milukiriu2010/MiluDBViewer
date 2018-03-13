package milu.entity.schema;

public class SchemaEntityEachFK extends SchemaEntity 
{
	public SchemaEntityEachFK( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.FOREIGN_KEY );
		
		this.imageResourceName = "file:resources/images/index_i.png";
	}
}
