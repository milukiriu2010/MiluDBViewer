package milu.entity.schema;

public class SchemaEntityRootTable extends SchemaEntity
{
	public SchemaEntityRootTable( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
		
		this.imageResourceName = "file:resources/images/table_root.png";
	}
}
