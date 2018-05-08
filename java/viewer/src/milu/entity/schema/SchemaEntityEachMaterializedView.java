package milu.entity.schema;

public class SchemaEntityEachMaterializedView extends SchemaEntity
{
	void init()
	{
		this.type = SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW;
		this.imageResourceName = "file:resources/images/materialized_view.png";
	}
}
