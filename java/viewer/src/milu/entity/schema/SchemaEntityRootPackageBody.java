package milu.entity.schema;

public class SchemaEntityRootPackageBody extends SchemaEntity
{
	public SchemaEntityRootPackageBody()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY );
		
		this.name = "PACKAGE BODY";
		
		this.imageResourceName = "file:resources/images/package_body_root.png";
	}
}
