package milu.entity.schema;

public class SchemaEntityEachView extends SchemaEntity
{
	public SchemaEntityEachView( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.VIEW );
		
		this.imageResourceName = "file:resources/images/view.png";
	}
}
