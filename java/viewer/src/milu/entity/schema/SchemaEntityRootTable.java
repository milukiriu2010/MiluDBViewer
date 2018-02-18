package milu.entity.schema;

public class SchemaEntityRootTable extends SchemaEntity
{
	public SchemaEntityRootTable()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
		
		this.imageResourceName = "file:resources/images/table_root.png";
	}
}
