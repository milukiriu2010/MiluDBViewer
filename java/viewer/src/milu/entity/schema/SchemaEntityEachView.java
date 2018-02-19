package milu.entity.schema;

public class SchemaEntityEachView extends SchemaEntity
{
	public SchemaEntityEachView( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.FUNC );
		
		this.imageResourceName = "file:resources/images/func.png";
	}
}
