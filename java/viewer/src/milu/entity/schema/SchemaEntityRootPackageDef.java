package milu.entity.schema;

public class SchemaEntityRootPackageDef extends SchemaEntity
{
	public SchemaEntityRootPackageDef()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF );
		
		this.name = "PACKAGE";
		
		this.imageResourceName = "file:resources/images/package_def_root.png";
	}
}
