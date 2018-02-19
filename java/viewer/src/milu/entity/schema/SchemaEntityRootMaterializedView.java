package milu.entity.schema;

public class SchemaEntityRootMaterializedView extends SchemaEntity
{
	public SchemaEntityRootMaterializedView()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
		
		this.name = "MATERIALIZED VIEW";
		
		this.imageResourceName = "file:resources/images/materialized_view_root.png";
	}
}
