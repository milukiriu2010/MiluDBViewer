package milu.entity.schema;

public class SchemaEntityEachFunc extends SchemaEntity
{
	public SchemaEntityEachFunc( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.FUNC );
		
		this.imageResourceName = "file:resources/images/func.png";
	}
}
