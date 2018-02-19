package milu.entity.schema;

public class SchemaEntityEachTable extends SchemaEntity
{
	public SchemaEntityEachTable( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.TABLE );
		
		this.imageResourceName = "file:resources/images/table.png";
	}
}
