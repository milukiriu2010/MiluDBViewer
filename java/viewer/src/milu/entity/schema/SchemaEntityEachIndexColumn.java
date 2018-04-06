package milu.entity.schema;

public class SchemaEntityEachIndexColumn extends SchemaEntity 
{
	/*
	public SchemaEntityEachIndexColumn( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.INDEX_COLUMN );
		
		this.imageResourceName = "file:resources/images/column.png";
	}
	*/
	
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.INDEX_COLUMN;
		this.imageResourceName = "file:resources/images/column.png";
	}
}
