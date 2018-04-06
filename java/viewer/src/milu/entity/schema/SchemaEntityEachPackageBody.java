package milu.entity.schema;

public class SchemaEntityEachPackageBody extends SchemaEntity 
{
	/*
	public SchemaEntityEachPackageBody( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.PACKAGE_BODY );
		
		this.imageResourceName = "file:resources/images/package_body.png";
	}
	*/

	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.PACKAGE_BODY;
		this.imageResourceName = "file:resources/images/package_body.png";
	}
}
