package milu.entity.schema;

public class SchemaEntityEachTable extends SchemaEntity
{
	@Override
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.TABLE;
		this.imageResourceName = "file:resources/images/table.png";
	}
}
