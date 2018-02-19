package milu.entity.schema;

public class SchemaEntityRootIndex extends SchemaEntity 
{
	public SchemaEntityRootIndex()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_INDEX );
		
		this.name = "INDEX";
		
		this.imageResourceName = "file:resources/images/index_root.png";
	}
}
