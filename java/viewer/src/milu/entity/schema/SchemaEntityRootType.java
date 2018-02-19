package milu.entity.schema;

public class SchemaEntityRootType extends SchemaEntity
{
	public SchemaEntityRootType()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_TYPE );
		
		this.name = "TYPE";
		
		this.imageResourceName = "file:resources/images/type_root.png";
	}
}
