package milu.entity.schema;

public class SchemaEntityEachType extends SchemaEntity
{
	public SchemaEntityEachType( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.TYPE );
		
		this.imageResourceName = "file:resources/images/type.png";
	}
}
