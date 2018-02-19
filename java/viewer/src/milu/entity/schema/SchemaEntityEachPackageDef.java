package milu.entity.schema;

public class SchemaEntityEachPackageDef extends SchemaEntity 
{
	public SchemaEntityEachPackageDef( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.PACKAGE_DEF );
		
		this.imageResourceName = "file:resources/images/package_def.png";
	}
}
