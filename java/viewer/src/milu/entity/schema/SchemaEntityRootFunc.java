package milu.entity.schema;

public class SchemaEntityRootFunc extends SchemaEntity
{
	public SchemaEntityRootFunc()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_FUNC );
		
		this.name = "FUNCTION";
		
		this.imageResourceName = "file:resources/images/func_root.png";
	}
}
