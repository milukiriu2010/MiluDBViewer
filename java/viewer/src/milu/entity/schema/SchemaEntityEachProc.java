package milu.entity.schema;

public class SchemaEntityEachProc extends SchemaEntity
{
	/*
	public SchemaEntityEachProc( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.PROC );
		
		this.imageResourceName = "file:resources/images/proc.png";
	}
	*/

	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.PROC;
		this.imageResourceName = "file:resources/images/proc.png";
	}
}
